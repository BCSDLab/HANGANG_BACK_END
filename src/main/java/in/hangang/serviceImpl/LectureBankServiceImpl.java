package in.hangang.serviceImpl;

import in.hangang.domain.*;
import in.hangang.domain.scrap.Scrap;
import in.hangang.domain.scrap.ScrapLectureBank;
import in.hangang.enums.BankCategory;
import in.hangang.enums.ErrorMessage;
import in.hangang.enums.FIleType;
import in.hangang.enums.Point;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureBankMapper;
import in.hangang.mapper.UserMapper;
import in.hangang.response.BaseResponse;
import in.hangang.service.LectureBankService;
import in.hangang.service.UserService;
import in.hangang.util.S3Util;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Transactional
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
        return lectureBankMapper.findLectureBankByKeyword(lectureBankCriteria, userService.getLoginUser());
    }

    @Override
    public LectureBank getLectureBank(Long id) throws Exception{
        LectureBank lectureBank = lectureBankMapper.getLectureBankAll(id);
        if(lectureBank == null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        //checkHit 추가됨
        User logineduser = userService.getLoginUser();
        if(logineduser!=null){
            Long hits = lectureBankMapper.checkHits(logineduser.getId(), id);
            if(hits!=null)
                lectureBank.setIs_hit(true);
        }
        return lectureBank;
    }

    @Override
    public Lecture getLecture(Long id){
        Lecture lecture = lectureBankMapper.getLectureInfo(id);
        if(lecture == null) throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        return lecture;
    }


    // url의 확장자에 따라 default 썸네일을 반환해준다.
    private String getThumbnailUrl(String url){
        String ext = lectureBankMapper.getExt(url);
        // 해당 url이 우리 file 서버에 없는 경우
        if (ext == null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        
        if ( ext.equals(FIleType.HANSHOW.getType()))
            return FIleType.HANSHOW.getUrL();
        else if ( ext.equals(FIleType.JPG.getType()))
            return FIleType.JPG.getUrL();
        else if ( ext.equals(FIleType.HWP.getType()))
            return FIleType.HWP.getUrL();
        else if ( ext.equals(FIleType.PNG.getType()))
            return FIleType.PNG.getUrL();
        else if ( ext.equals(FIleType.PDF.getType()))
            return FIleType.PDF.getUrL();
        else if ( ext.equals(FIleType.TXT.getType()))
            return FIleType.TXT.getUrL();
        else if ( ext.equals(FIleType.ZIP.getType()))
            return FIleType.ZIP.getUrL();
        else if ( ext.equals(FIleType.EXCEL.getType()))
            return FIleType.EXCEL.getUrL();
        else if ( ext.equals(FIleType.PPT.getType()))
            return FIleType.PPT.getUrL();
        else if ( ext.equals(FIleType.HANCELL.getType()))
            return FIleType.HANCELL.getUrL();
        else if ( ext.equals(FIleType.WORD.getType()))
            return FIleType.WORD.getUrL();
        else
            return FIleType.DEFAULT.getUrL();
    }
    @Override
    public String fileUpload(MultipartFile file) throws Exception{
        if(file == null)
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
        String url = s3Util.privateUpload(file);
        lectureBankMapper.insertS3Url(url, file.getOriginalFilename(),file.getContentType(), FilenameUtils.getExtension(file.getOriginalFilename()));
        return url;
    }

    @Override
    public BaseResponse postLectureBank(LectureBank lectureBank) throws Exception {
        // 로그인한 유저의 ID값 삽입
        lectureBank.setUser_id(userService.getLoginUser().getId());
        // 강의자료 포인트값은 100원으로 고정
        lectureBank.setPoint_price(Point.LECTURE_BANK.getPoint());
        // 첫 URL의 확장자에 대한 섬네일 구성
        lectureBank.setThumbnail(this.getThumbnailUrl(lectureBank.getFiles().get(0)));
        // 해당 Lecture 가 실제로 존재하는지 확인
        if (getLecture(lectureBank.getId()) == null){
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        }
        // category값 검증
        for (int i =0; i< lectureBank.getCategory().size(); i++){
            boolean check = false;
            for ( BankCategory c : BankCategory.values() ){
                if ( lectureBank.getCategory().get(i).equals(String.valueOf(c))){
                    check = true;
                    break;
                }
            }
            if ( !check ){
                throw new RequestInputException(ErrorMessage.CATEGORY_INVALID);
            }
        }


        //1개의 강의자료 삽입
        //n개의 s3_url posted = 1로 update

            Long id = lectureBankMapper.postLectureBank(lectureBank);

        //n개의 category 삽입
        //n개의 upload_file url 삽입
        try {
            lectureBank.setId(id);
            lectureBankMapper.insertCategoryAndFiles(lectureBank);
        }catch (Throwable e){
            //e.printStackTrace();
            throw new RequestInputException(ErrorMessage.URL_NOT_UNIQUE);
        }

        return new BaseResponse("강의자료가 업로드되었습니다.", HttpStatus.CREATED);
    }

    @Override
    public void deleteLectureBank(Long id) throws Exception{
        //delete LectureBank - soft
        //TODO scrap 수정
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
                ,lectureBank.getUser_id(),Point.LECTURE_BANK.getTypeId(),Point.LECTURE_BANK.getTypeId());

    }


    //hits------------------------------------------------------------------------------------
    @Override
    public void pushHit(Long lecture_bank_id) throws Exception{
        Long userID = userService.getLoginUser().getId();

        Long hitID = lectureBankMapper.checkHitExist(userID, lecture_bank_id);
        Integer deleted = lectureBankMapper.checkHitIsdeleted(hitID);

        if(hitID !=null){
            if(deleted==1){
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

    @Override
    public LectureBank pushHitLectureBank(Long lecture_bank_id) throws Exception{
        pushHit(lecture_bank_id);
        Long userID = userService.getLoginUser().getId();
        LectureBank lectureBank = lectureBankMapper.getLectureBank(lecture_bank_id);

        //is_hit 추가 - mapper에서도 추가 가능할듯 (추후에 추가해보기)
        Long hits = lectureBankMapper.checkHits(userID, lectureBank.getId());
        if(hits !=null)
            lectureBank.setIs_hit(true);

        return lectureBank;
    }


    //file------------------------------------------------------------------------------------

    //UPLOAD====================================================================================


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
    public String getThumbnailURL() throws Exception{
        //TODO 확장자별 URL - enum 만들기
        String default_url = "https://static.hangang.in/lecture_bank_default_image.png";
        return default_url;
    }

    //TODO SCRAP TEST
    @Override
    public void createScrap(Long lecture_bank_id) throws Exception{
        Long user_id = userService.getLoginUser().getId();

        if(lecture_bank_id != null){
            Boolean check = lectureBankMapper.checkScrapDeleted(user_id,lecture_bank_id);
            System.out.println(check);
            if(check == null){
                lectureBankMapper.createScrap(user_id, lecture_bank_id);
            }else if(check){
                lectureBankMapper.unDeleteScrap(user_id,lecture_bank_id);
            }else{
                throw new RequestInputException(ErrorMessage.SCRAP_ALREADY_EXISTS);
            }
        }
        else
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
    }

    @Override
    public void deleteScrap(ArrayList<Long> idList) throws Exception{
        if(idList!= null && idList.size() > 0){
            //mybatis foreach 사용시 null 이 아닌 empty가 된다??? - 찾아보기
            List<Scrap> scList = new ArrayList<>();
            for(Long id : idList){
                scList.add(lectureBankMapper.checkScrap(id));
            }

            Long userID = userService.getLoginUser().getId();
            for(Scrap ch : scList){
                if(ch==null){
                    throw new RequestInputException(ErrorMessage.SCRAP_DOES_NOT_EXIST);
                }else if(ch.getIs_deleted()){
                    throw new RequestInputException(ErrorMessage.ALREADY_DELETED_SCRAP);
                }else{
                    if(!ch.getUser_id().equals(userID))
                        throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
                }
            }
            lectureBankMapper.deleteScrapList(idList);
        }else{
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
        }

    }

    @Override
    public List<ScrapLectureBank> getScrapList() throws Exception{
        Long user_id = userService.getLoginUser().getId();
        List<ScrapLectureBank> scrapLectureBank = lectureBankMapper.getScrapLectureBankList(user_id);
        return scrapLectureBank;
    }


}
