package in.hangang.serviceImpl;

import in.hangang.domain.*;
import in.hangang.enums.ErrorMessage;
import in.hangang.enums.Point;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureBankMapper;
import in.hangang.mapper.UserMapper;
import in.hangang.service.LectureBankService;
import in.hangang.service.UserService;
import in.hangang.util.S3Util;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service("LectureBankServiceImpl")
public class LectureBankServiceImpl implements LectureBankService {

    @Autowired
    protected LectureBankMapper lectureBankMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    private S3Util s3Util;

    //Main------------------------------------------------------------------------------------

    @Override
    public List<LectureBank> searchLectureBanks(LectureBankCriteria lectureBankCriteria) {

        List<LectureBank> result =  lectureBankMapper.findLectureBankByKeyword(lectureBankCriteria.getCursor(),
                lectureBankCriteria.getLimit(),
                lectureBankCriteria.getOrder(), lectureBankCriteria.getCategory(),
                lectureBankCriteria.getKeyword(), lectureBankCriteria.getDepartment());

        //TODO MYBATIS 의 COLLCETION 과 ASSOCIATION을 사용하면 개선할 수 있을것 같다는 느낌이 듭니다.
        for(int i=0; i<result.size(); i++){
            List<LectureBankCategory> categories = lectureBankMapper.getCategoryList(result.get(i).getId());
            ArrayList<String> categoryList = new ArrayList<>();
            for(int j=0; j<categories.size(); j++){
                categoryList.add(categories.get(j).getCategory());
            }
            result.get(i).setCategory(categoryList);
        }
        //강의자료의 카테고리를 lecture_bank_category테이블에서 가져와 넣어준다.
        return result;
    }

    @Override
    public LectureBank getLectureBank(Long id) throws Exception{
        LectureBank lectureBank = lectureBankMapper.getLectureBank(id);
        if(lectureBank == null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        User user = userMapper.getMe(lectureBank.getUser_id());
        lectureBank.setUser(user);

        Lecture lecture = getLecture(lectureBank.getLecture_id());
        lectureBank.setLecture(lecture);

        return lectureBank;
    }

    @Override
    public Lecture getLecture(Long id){
        Lecture lecture = lectureBankMapper.getLectureInfo(id);
        if(lecture == null) throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        return lecture;
    }

    @Override
    public Long createLectureBank() throws Exception{
        User user = userService.getLoginUser();

        //TODO 멀티쿼리르 사용해보시는 것은 어떨까요?
        lectureBankMapper.createLectureBank(user.getId());
        return lectureBankMapper.getLectureBankId(user.getId());
    }

    @Override
    @Transactional
    public void setLectureBank(LectureBank lectureBank) throws Exception{
        if(lectureBank.getId() == null) throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        if(checkWriter(lectureBank.getUser_id())) throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        LectureBank lb = lectureBankMapper.getLectureBank(lectureBank.getId());

        String date;
        if(lectureBank.getSemester_date() != null)  date = lectureBank.getSemester_date();
        else if(lb.getSemester_date() != null) date = lb.getSemester_date();
        else date = lectureBankMapper.getLatestSemester();
        Long semester_id = lectureBankMapper.getSemesterID(date);

        lectureBankMapper.setLectureBank(lectureBank.getId(), lectureBank.getLecture_id(), lectureBank.getTitle(),
                lectureBank.getContent(), lectureBank.getPoint_price(), semester_id);

        List<String> categoryList = lectureBank.getCategory();
        if(categoryList != null){
            //lectueMapper add to lecturebank _ Category : category list while empty
            List<Long> idList = lectureBankMapper.getCategoryIdList(lectureBank.getId());
            if(idList.size() != 0)
                lectureBankMapper.deleteMultiCategory((ArrayList<Long>) idList);
            lectureBankMapper.addMultiCategory(lectureBank.getId(),(ArrayList<String>) categoryList);
        }
    }

    @Override
    @Transactional
    public void submitLectureBank(LectureBank lectureBank) throws Exception{
        // TODO 섬네일 생성 및 등록하기
        setLectureBank(lectureBank);
        lectureBankMapper.setLectureBankAvailable(lectureBank.getId());

        List<Long> list = lectureBankMapper.getFileIdList(lectureBank.getId());
        System.out.println("File sizes"+list.size());
        for(Long i : list){
            System.out.print(i + " ");
        }
        System.out.println();
        if(list.size() != 0)
            lectureBankMapper.setMultiFileAvailable_0((ArrayList<Long>) list,1);

        User user = userService.getLoginUser();
        lectureBankMapper.setPoint(user.getId(), Point.LECTURE_UPLOAD.getPoint());
        lectureBankMapper.addPointHistory(user.getId(),Point.LECTURE_UPLOAD.getPoint(),Point.LECTURE_UPLOAD.getTypeId());
    }

    @Override
    @Transactional
    public void deleteLectureBank(Long id) throws Exception{
        //delete LectureBank - soft
        Long userId = userService.getLoginUser().getId();
        if(checkWriter(id)){
            lectureBankMapper.deleteLectureBank(id, userId);
            //TODO MULTI QUERY랑 CHOOSE WHEN 구문을 사용해 볼 수 있지 않을까요?
            //delete Comment : soft
            List<Long> commentIdList= lectureBankMapper.getCommentIdList(id);
            if(commentIdList.size() != 0)
                lectureBankMapper.deleteMultiComment((ArrayList<Long>)commentIdList);

            //delete File : soft -> hard => scheduler available 2
            List<Long> fileIdList = lectureBankMapper.getFileId(id);
            if(fileIdList.size() != 0)
                lectureBankMapper.deleteMultiFile((ArrayList<Long>) fileIdList,2);

            //delete Category : hard
            List<Long> categoryList = lectureBankMapper.getCategoryIdList(id);
            if(categoryList.size() != 0)
                lectureBankMapper.deleteMultiCategory((ArrayList<Long>)categoryList);

            //delete Hit : soft
            List<Long> hitIdList = lectureBankMapper.getHitId(id);
            if(hitIdList.size() != 0)
                lectureBankMapper.deleteMultiHit((ArrayList<Long>)hitIdList);
            //delete Purchase : soft
            List<Long> purchaseId = lectureBankMapper.getPurchaseId(id);
            if(purchaseId.size() != 0)
            lectureBankMapper.deleteMultiPurchase((ArrayList<Long>)purchaseId);
        }
        else throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

    }

    @Override
    @Transactional
    public void cancelLectureBank(Long id) throws Exception{
        //delete LectureBank - soft
        Long userId = userService.getLoginUser().getId();
        if(checkWriter(id)){
            lectureBankMapper.deleteLectureBank(id, userId);

            //delete File : soft -> hard => scheduler available 2
            List<Long> fileIdList = lectureBankMapper.getFileId(id);
            if(fileIdList.size() != 0)
                lectureBankMapper.deleteMultiFile_UN((ArrayList<Long>) fileIdList,2);
        }
        else throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

    }

    @Override
    public Boolean checkWriter(Long lecture_bank_id) throws Exception{
        Long userId = userService.getLoginUser().getId();
        Long writerId = lectureBankMapper.getWriterId(lecture_bank_id);
        System.out.println("ID:---------------" +userId +" " +writerId);
        return userId.equals(writerId);
    }

    //comments------------------------------------------------------------------------------------
    @Override
    public List<LectureBankComment> getComments(Long lecture_bank_id){
        return lectureBankMapper.getComments(lecture_bank_id);
    }

    @Override
    public void addComment(Long lecture_bank_id, String comments) throws Exception{
        // lecture_bank의 is_deleted, reported 검사?
        // TODO 만약 없는 강의자료에 커멘트를 다는 경우가 처리되어야할 것 같습니다.
        // TODO  comments 유효성 검사를 해줘야 할 것 같습니다 null, size... 등등..
        lectureBankMapper.addComment(userService.getLoginUser().getId(), lecture_bank_id, comments);
    }

    @Override
    public void setComment(Long lecture_bank_comment_id, String comments) throws Exception{
        // TODO 만약 없는 강의자료에 커멘트를 다는 경우가 처리되어야할 것 같습니다.
        // TODO  comments 유효성 검사를 해줘야 할 것 같습니다 null, size... 등등..
        if(checkCommentWriter(lecture_bank_comment_id))
            lectureBankMapper.setComment(lecture_bank_comment_id, comments);
        else
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
    }

    @Override
    public void deleteComment(Long lecture_bank_comment_id) throws Exception{
        // TODO 만약 없는 강의자료에 커멘트를 다는 경우가 처리되어야할 것 같습니다.
        if(checkCommentWriter(lecture_bank_comment_id))
            lectureBankMapper.deleteComment(lecture_bank_comment_id);
        else
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
    }

    @Override
    public Boolean checkCommentWriter(Long lecture_bank_comment_id)throws Exception{
        Long userID = userService.getLoginUser().getId();
        Long writerID = lectureBankMapper.getCommentWriterId(lecture_bank_comment_id);

        return userID.equals(writerID);
    }

    //purchase------------------------------------------------------------------------------------
    @Override
    public Boolean checkPurchase(Long lecture_bank_id) throws Exception {
        User user = userService.getLoginUser();
        Long purchase = lectureBankMapper.checkPurchased(user.getId(), lecture_bank_id);
        return purchase != null;
    }

    @Override
    @Transactional
    public void purchase(Long lecture_bank_id) throws Exception{
        if(checkPurchase(lecture_bank_id)) throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        Long userID = userService.getLoginUser().getId();
        LectureBank lectureBank = getLectureBank(lecture_bank_id);
        Integer point_price = lectureBank.getPoint_price();
        Integer user_point = lectureBankMapper.getUserPoint(userID);

        if(checkWriter(lecture_bank_id)) throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        if(user_point < point_price) throw new RequestInputException(ErrorMessage.NOT_ENOUGH_POINT);


        //TODO 자바에서 5번 다녀오는 것 보다 한번에 할 수 있도록 멀티쿼리를 사용하면 좋을 것 같습니다
        lectureBankMapper.purchaseInsert(userID, lecture_bank_id);
        // 구매한 유저 포인트-- 자료주인은 포인트++
        lectureBankMapper.setPoint(userID, -point_price);
        lectureBankMapper.addPointHistory(userID,-point_price, Point.LECTURE_PURCHASE.getTypeId());
        lectureBankMapper.setPoint(lectureBank.getUser_id(), point_price);
        lectureBankMapper.addPointHistory(lectureBank.getUser_id(),point_price,Point.LECTURE_SELL.getTypeId());


    }


    //hits------------------------------------------------------------------------------------
    @Override
    public Boolean checkHits(Long lecture_bank_id) throws  Exception {

        Long userID = userService.getLoginUser().getId();
        Long hits = lectureBankMapper.checkHits(userID, lecture_bank_id);
        return hits != null;
    }

    @Override
    @Transactional
    public void pushHit(Long lecture_bank_id) throws Exception{
        Long userID = userService.getLoginUser().getId();

        Long hits = lectureBankMapper.checkHits(userID, lecture_bank_id);

        if(hits == null){ // TODO 안누른경우
            lectureBankMapper.hitInsert(userID, lecture_bank_id);
            lectureBankMapper.addHit_lecture_bank(lecture_bank_id);
        }else if(hits.intValue()==1){ // TODO 누른 경우
            lectureBankMapper.subHit(userID, lecture_bank_id);
            lectureBankMapper.subHit_lecture_bank(lecture_bank_id);
        }else{ // TODO ???
            lectureBankMapper.addHit(userID, lecture_bank_id);
            lectureBankMapper.addHit_lecture_bank(lecture_bank_id);
        }
    }


    //file------------------------------------------------------------------------------------

    //UPLOAD====================================================================================
    @Override
    public List<Long> LectureBankFilesUpload(List<MultipartFile> fileList, Long lecture_bank_id) throws Exception {
        List<Long> result = new ArrayList<>();
        if(!fileList.isEmpty()){
            for(MultipartFile file : fileList){
                String uploadUrl = s3Util.privateUpload(file);
                String fileName = file.getOriginalFilename();
                int index = fileName.lastIndexOf(".");
                String fileExt = fileName.substring(index+1);
                lectureBankMapper.insertUpload_file(lecture_bank_id, uploadUrl,fileName, fileExt);
                result.add(lectureBankMapper.getUploadFileId(lecture_bank_id));
            }
        }
        return result;
    }

    @Override
    public Long fileUpload(MultipartFile file, Long lecture_bank_id) throws Exception{
        String uploadUrl = s3Util.privateUpload(file);
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String fileExt = fileName.substring(index+1);
        lectureBankMapper.insertUpload_file(lecture_bank_id, uploadUrl,fileName, fileExt);
        return lectureBankMapper.getUploadFileId(lecture_bank_id);
    }

    @Override
    public void cancelUpload(Long id) throws Exception{
        Long lectureBankID = lectureBankMapper.getLectureBankIDFile(id);
        if(checkWriter(lectureBankID))
            lectureBankMapper.setFileAvailable(id,2);
        else
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
    }

    @Override
    // TODO 사용하지 않는 파일들을 전부 지우기 위한거 같군요
    public void hardDeleteFile() throws Exception{

        List<String> objectKeys = lectureBankMapper.getDelObjectList();
        //delete on S3
        for(String key : objectKeys){
            s3Util.deleteObjectbyKey(key);
        }
        List<Long> id_list = lectureBankMapper.getDelIDList();
        lectureBankMapper.hardDeleteMultiFile((ArrayList<Long>) id_list);


    }

    //DOWNLOAD====================================================================================

    @Override
    public List<UploadFile> getFileList(Long lecture_bank_id) throws Exception{
        return lectureBankMapper.getFileList(lecture_bank_id);
    }

    /*
    @Override
    public org.springframework.core.io.Resource getprivateObject(Long id) throws Exception{
        String objectKey = lectureBankMapper.getUrl(id);
        URL url = s3Util.getPrivateObjectURL(objectKey);
        org.springframework.core.io.Resource resource = new UrlResource(url);
        return resource;

    }




     */

    @Override
    public String getObjectUrl(Long id) throws Exception{
        Long user_id = userService.getLoginUser().getId();
        Long lecturebank_id = lectureBankMapper.getLectureBankId_file(id);
        Long writer = lectureBankMapper.getWriterId(lecturebank_id);

        if(checkPurchase(lecturebank_id) || (writer.equals(user_id))){
            String objectKey = lectureBankMapper.getUrl(id);
            URL url = s3Util.getPrivateObjectURL(objectKey);
            return url.toString();
        }else{
            return null;
        }

    }

    //Thumbnail------------------------------------------------------------------------------------
    @Override
    public String makeThumbnail(MultipartFile multipartFile) throws Exception{
        return "test_thumbnail_url_path";
    }


    //REPORT------------------------------------------------------------------------------------
    @Override
    @Transactional
    public void reportLectureBank(Long lecture_bank_id, Long report_id) throws Exception{
        //TODO 신고기능은 SLACK 노티를 보내는 것이 좋을것 같습니다
        lectureBankMapper.reportLectureBank(lecture_bank_id, report_id);
        lectureBankMapper.makeLectureBankReported(lecture_bank_id);
    }

    @Override
    @Transactional
    public void reportLectureBankComment(Long lecture_bank_comment_id, Long report_id) throws Exception{
        //TODO 신고기능은 SLACK 노티를 보내는 것이 좋을것 같습니다
        lectureBankMapper.reportLectureBankComment(lecture_bank_comment_id, report_id);
        lectureBankMapper.makeLectureBankCommentReported(lecture_bank_comment_id);
    }


    //TODO 썸네일 테스트 --  정수현
    //파일이 n개라면 가장 첫 파일을 기준으로 썸네일을 만든다.
    @Override
    @Transactional
    public String tngusTest(MultipartFile multipartFile) throws Exception{
        /* TODO
            jpeg, png,
            txt,
            hwp, pdf, docs, ppt
            xlsx
         */

        String tmp;
        tmp = multipartFile.getContentType();

        // 이미지인 경우
        if( multipartFile.getContentType().equals("image/jpeg") || multipartFile.getContentType().equals("image/png")) {
            return s3Util.uploadObject(multipartFile);  // 그대로 업로드하여 return한다.
        }

        //txt 파일인 경우
        if ( multipartFile.getContentType().equals("text/plain")){
            return tmp;
        }
        
        //pdf 파일인 경우
        if ( multipartFile.getContentType().equals("application/pdf")) {
            try {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString()+".jpg";

                InputStream is = multipartFile.getInputStream();
                PDDocument pdfDoc = PDDocument.load(is );
                is.close();

                PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
                BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim,  fileName, 300); // 이미지 생성
                bim.flush();
                pdfDoc.close();

                File file = new File(fileName);
                FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
                try {
                    OutputStream os = fileItem.getOutputStream();
                    IOUtils.copy(new FileInputStream(file), os);
                    os.close();
                } catch (Exception e) {
                   e.printStackTrace();
                }
                MultipartFile thumbnail = new CommonsMultipartFile(fileItem);
                fileItem.delete();
                System.gc();
                if ( file.delete()){
                    System.out.println("성공");
                }else{
                    throw new Exception();
                }

                return s3Util.uploadObject(thumbnail);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }



        return tmp;
    }

}
