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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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
    public List<LectureBank> searchLectureBanks(LectureBankCriteria lectureBankCriteria) throws Exception{

        List<LectureBank> result =  lectureBankMapper.findLectureBankByKeyword(lectureBankCriteria.getCursor(),
                lectureBankCriteria.getLimit(), lectureBankCriteria.getOrder(),
                lectureBankCriteria.getCategory(), lectureBankCriteria.getKeyword(),
                lectureBankCriteria.getDepartment());



        //TODO MYBATIS 의 COLLCETION 과 ASSOCIATION을 사용하면 개선할 수 있을것 같다는 느낌이 듭니다.
        //강의자료의 카테고리를 lecture_bank_category 테이블에서 가져와 넣어준다.
        for(int i=0; i<result.size(); i++){
            List<LectureBankCategory> categories = lectureBankMapper.getCategoryList(result.get(i).getId());
            ArrayList<String> categoryList = new ArrayList<>();
            for(int j=0; j<categories.size(); j++){
                categoryList.add(categories.get(j).getCategory());
            }
            result.get(i).setCategory(categoryList);

            //checkHit 추가됨
            User user = userService.getLoginUser();
            if(user != null){
                Long hits = lectureBankMapper.checkHits(user.getId(), result.get(i).getId());
                //System.out.println("checkHit: "+hits+" "+user.getId() + " " + result.get(i).getId());
                if(hits !=null)
                    result.get(i).setIs_hit(true);
                else
                    result.get(i).setIs_hit(false);
            }else{
                //System.out.println("user is null..?!");
                result.get(i).setIs_hit(false);
            }

            //thumbnail Ext 추가
            String thExt = lectureBankMapper.getFileExtofOne(result.get(i).getId());
            result.get(i).setThumbnail_ext(thExt);

        }

        return result;
    }

    @Override
    public LectureBank getLectureBank(Long id) throws Exception{
        LectureBank lectureBank = lectureBankMapper.getLectureBank(id);
        if(lectureBank == null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        //TODO 멀티쿼리이용?
        User user = userMapper.getMe(lectureBank.getUser_id());
        lectureBank.setUser(user);

        Lecture lecture = getLecture(lectureBank.getLecture_id());
        lectureBank.setLecture(lecture);

        //checkHit 추가됨
        User logineduser = userService.getLoginUser();
        if(logineduser!=null){
            Long hits = lectureBankMapper.checkHits(logineduser.getId(), id);
            if(hits!=null)
                lectureBank.setIs_hit(true);
        }

        //thumbnail
        String thExt = lectureBankMapper.getFileExtofOne(id);
        lectureBank.setThumbnail_ext(thExt);


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
        //System.out.println("ID:---------------" +userId +" " +writerId);
        return userId.equals(writerId);
    }

    @Override
    public Boolean checkLectureBankAvailable(Long lecture_bank_id) throws Exception{
        LectureBank lectureBank = lectureBankMapper.getLectureBank(lecture_bank_id);
        if(lectureBank!=null){
            //reported, is_deleted
            if(lectureBank.getIs_deleted())
                throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        }else{
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        }
        return true;
    }

    //comments------------------------------------------------------------------------------------
    @Override
    public List<LectureBankComment> getComments(Long lecture_bank_id){
        return lectureBankMapper.getComments(lecture_bank_id);
    }

    @Override
    public void addComment(Long lecture_bank_id, String comments) throws Exception{
        if(comments == null || comments.length() <= 0){
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
        }else{
            // lecture_bank의 is_deleted, reported 검사
            if(checkLectureBankAvailable(lecture_bank_id))
                lectureBankMapper.addComment(userService.getLoginUser().getId(), lecture_bank_id, comments);
        }

    }

    @Override
    public void setComment(Long lecture_bank_comment_id, String comments) throws Exception{
        if(comments == null || comments.length() <= 0){
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
        }else{
            LectureBankComment comment = lectureBankMapper.getComment(lecture_bank_comment_id);
            if (comment != null) {
                if(checkLectureBankAvailable(comment.getLecture_bank_id())){
                    if(checkCommentWriter(lecture_bank_comment_id))
                        lectureBankMapper.setComment(lecture_bank_comment_id, comments);
                    else
                        throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
                }
            }

        }

    }

    @Override
    public void deleteComment(Long lecture_bank_comment_id) throws Exception{
        LectureBankComment comment = lectureBankMapper.getComment(lecture_bank_comment_id);
        if(comment!= null){
            if(checkCommentWriter(lecture_bank_comment_id))
                lectureBankMapper.deleteComment(lecture_bank_comment_id);
            else
                throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        }else{
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
        }

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
        if(checkPurchase(lecture_bank_id)) throw new RequestInputException(ErrorMessage.ALREADY_PURCHASED);

        Long userID = userService.getLoginUser().getId();
        LectureBank lectureBank = getLectureBank(lecture_bank_id);
        Integer point_price = lectureBank.getPoint_price();
        Integer user_point = lectureBankMapper.getUserPoint(userID);

        if(checkWriter(lecture_bank_id)) throw new RequestInputException(ErrorMessage.PURCHASE_EXCEPTION);
        if(user_point < point_price) throw new RequestInputException(ErrorMessage.NOT_ENOUGH_POINT);


        //TODO 자바에서 5번 다녀오는 것 보다 한번에 할 수 있도록 멀티쿼리를 사용하면 좋을 것 같습니다
        /*
        lectureBankMapper.purchaseInsert(userID, lecture_bank_id);
        // 구매한 유저 포인트-- 자료주인은 포인트++
        //user_id, lecture_bank_id, point_price_purchase, writer_id, point_price_sell
        lectureBankMapper.setPoint(userID, -point_price);
        lectureBankMapper.addPointHistory(userID,-point_price, Point.LECTURE_PURCHASE.getTypeId());
        lectureBankMapper.setPoint(lectureBank.getUser_id(), point_price);
        lectureBankMapper.addPointHistory(lectureBank.getUser_id(),point_price,Point.LECTURE_SELL.getTypeId());
         */
        lectureBankMapper.purchase(userID,lecture_bank_id,point_price
                ,lectureBank.getUser_id(),Point.LECTURE_PURCHASE.getTypeId(),Point.LECTURE_SELL.getTypeId());

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

        Long hitID = lectureBankMapper.checkHits(userID, lecture_bank_id);
        Boolean deleted = lectureBankMapper.checkHitIsdeleted(hitID);

        if(hitID !=null){
            if(deleted){
                //취소 후 다시 누른 경우
                lectureBankMapper.addHit(userID, lecture_bank_id);
                lectureBankMapper.addHit_lecture_bank(lecture_bank_id);
            }else{
                //누른 경우
                lectureBankMapper.subHit(userID, lecture_bank_id);
                lectureBankMapper.subHit_lecture_bank(lecture_bank_id);
            }
        }else{
            //안누른경우
            lectureBankMapper.hitInsert(userID, lecture_bank_id);
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
        String fileExt = file.getContentType();
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
        if(objectKeys != null && objectKeys.size() > 0){
            //delete on S3
            for(String key : objectKeys){
                s3Util.deleteObjectbyKey(key);
            }
            List<Long> id_list = lectureBankMapper.getDelIDList();
            if(id_list != null && id_list.size() > 0)
                lectureBankMapper.hardDeleteMultiFile((ArrayList<Long>) id_list);
        }
    }

    @Override
    public void deleteFile(Long id) throws Exception{
        Long lectureBankID = lectureBankMapper.getLectureBankIDFile(id);
        if(checkWriter(lectureBankID))
            lectureBankMapper.setFileAvailable(id,2);
        else
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
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
            throw new RequestInputException(ErrorMessage.DIDNT_PURCHASED);
        }

    }

    //Thumbnail------------------------------------------------------------------------------------
    @Override
    public String makeThumbnail(MultipartFile multipartFile) throws Exception{
        //multipartFile.getContentType();

        return "test_thumbnail_url_path";
    }


}
