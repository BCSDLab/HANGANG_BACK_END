package in.hangang.serviceImpl;

import com.amazonaws.services.xray.model.Http;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("LectureBankServiceImpl")
public class LectureBankServiceImpl implements LectureBankService {

    @Autowired
    protected LectureBankMapper lectureBankMapper;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    private S3Util s3Util;

    //Main------------------------------------------------------------------------------------

    /** 강의 페이지네이션 조회 메소드  - 정수현 */
    @Override
    public  Map<String, Object> searchLectureBanks(LectureBankCriteria lectureBankCriteria) throws Exception{
        // 카테고리 검증
        if ( lectureBankCriteria.getCategory() != null ) {
            this.is_acceptableCategory(lectureBankCriteria.getCategory());
        }
        if ( lectureBankCriteria.getOrder().equals("id") || lectureBankCriteria.getOrder().equals("hits")) {
            Map<String, Object> map = new HashMap<>();
            map.put("count", lectureBankMapper.getCount(lectureBankCriteria).size()); // 검색결과 총 갯수 
            map.put("result",lectureBankMapper.findLectureBankByKeyword(lectureBankCriteria, userService.getLoginUser())); // 페이지네이션 결과
            return map;
        }
        else
            throw new RequestInputException(ErrorMessage.KEYWORD_INVALID);
    }

    /**강의 단일조회 메소드 - 정수현 */
    @Override
    public LectureBank getLectureBank(Long id) throws Exception{
        LectureBank lectureBank = lectureBankMapper.getLectureBankAll(id ,userService.getLoginUser() );
        if(lectureBank == null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        else
            return lectureBank;

    }

    /** 해당 강의가 존재하는지 확인하는 메소드 -정수현 */
    private Lecture getLecture(Long id){
        Lecture lecture = lectureBankMapper.getLectureInfo(id);
        if(lecture == null) throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        return lecture;
    }


    /** url의 확장자에 따라 default 썸네일을 반환해주는 메소드  - 정수현 */
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
    /** 유저가 파일을 업로드 시 사용하는 메소드
     * s3 url과 file name, 확장자등의 이력을 관리한다.
     * s3 업로드된 url은 private한 url이다. - 정수현
     * */
    @Override
    public List<String> fileUpload(MultipartFile[] files) throws Exception{
        List<String> urls = new ArrayList<>();
        List<UploadFile> uploadFiles = new ArrayList<>();
        Long user_id = userService.getLoginUser().getId();
        // file이 없는 경우
        if(files == null || files.length == 0 )
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
        // 파일의 정보를 순서대로 java collection 에 저장한다.
        for ( MultipartFile file : files){
            String url = s3Util.privateUpload(file);
            UploadFile uploadFile = new UploadFile(url,file.getOriginalFilename(), file.getContentType(), FilenameUtils.getExtension(file.getOriginalFilename()), user_id,file.getSize() );
            uploadFiles.add(uploadFile);
            urls.add(url);
        }
        lectureBankMapper.insertS3Url(uploadFiles); // 파일 이력 db저장
        return urls; // url 정보반환 
    }

    /**
     * 강의자료를 업로드하는 메소드
     */
    @Override
    public BaseResponse postLectureBank(LectureBank lectureBank) throws Exception {
        // 로그인한 유저의 ID값 삽입
        lectureBank.setUser_id(userService.getLoginUser().getId());
        // 강의자료 포인트값은 100원으로 고정
        lectureBank.setPoint_price(Point.LECTURE_BANK.getPoint());
        // url validation 확인
        for ( int i=0;i < lectureBank.getFiles().size(); i++){
            if ( !lectureBank.getFiles().get(i).startsWith("https://static.hangang.in/") ){
                throw new RequestInputException(ErrorMessage.URL_INVALID);
            }
        }

        // 첫 URL의 확장자에 대한 섬네일 구성
        lectureBank.setThumbnail(this.getThumbnailUrl(lectureBank.getFiles().get(0)));
        // 해당 Lecture 가 실제로 존재하는지 확인
        this.getLecture(lectureBank.getLecture_id());
        // category 값 검증
        this.is_acceptableCategory(lectureBank.getCategory());
        //1개의 강의자료 삽입
        //n개의 s3_url posted = 1로 update
        Long id = lectureBankMapper.postLectureBank(lectureBank);
        //n개의 category 삽입
        //n개의 upload_file url 삽입
        // 강의자료 업로드시 유저의 포인트 ++ , point_history insert
        try {
            lectureBank.setId(id);
            lectureBankMapper.insertCategoryAndFiles(lectureBank, Point.LECTURE_UPLOAD.getPoint(), Point.LECTURE_UPLOAD.getTypeId());
        }catch (Throwable e){
            e.printStackTrace();
            throw new RequestInputException(ErrorMessage.URL_NOT_UNIQUE);
        }

        return new BaseResponse("강의자료가 업로드되었습니다.", HttpStatus.CREATED);
    }

    /** 카테고리가 올바른 값으로 들어왔는지 검증하는 메소드  - 정수현  */
    private boolean is_acceptableCategory(List<String> category){
        for (int i =0; i< category.size(); i++){
            boolean check = false;
            for ( BankCategory c : BankCategory.values() ){
                if ( category.get(i).equals(String.valueOf(c))){
                    check = true;
                    break;
                }
            }
            if ( !check ){
                throw new RequestInputException(ErrorMessage.CATEGORY_INVALID);
            }
        }
        return true;
    }
    /** 강의자료를 수정하는 메소드 - 정수현 */
    @Override
    public BaseResponse updateLectureBank(LectureBank lectureBank, Long id) throws Exception{
        lectureBank.setId(id);
        this.getLectureBank(lectureBank.getId());// 해당 강의자료가 실제로 존재하는지?
        this.getLecture(lectureBank.getLecture_id()); // 해당 타겟 강의자료가 실존하는지?
        for ( int i=0;i < lectureBank.getFiles().size(); i++){
            if ( !lectureBank.getFiles().get(i).startsWith("https://static.hangang.in/") ){
                throw new RequestInputException(ErrorMessage.URL_INVALID);
            }
        }
        if( this.is_writer(lectureBank.getId() )== false){
            throw new RequestInputException(ErrorMessage.FORBIDDEN_EXCEPTION);
        } // 저자가 맞는지?
        lectureBank.setThumbnail(this.getThumbnailUrl(lectureBank.getFiles().get(0))); // 첫 URL의 확장자에 대한 섬네일 구성
        this.is_acceptableCategory(lectureBank.getCategory()); // category 값 검증
        // 현재 사용중인 file의 url들을 사용안함 처리로 바꾼다. -> s3_url 테이블
        // upload_table의 내용을 지운다.
        /// --> 즉 강의자료 작성전, 파일만 업로드된 상태로 되돌린다.
        // 카테고리도 모두 삭제한다.
        lectureBankMapper.initLectureBank(lectureBankMapper.getFiles(lectureBank.getId()) , lectureBank.getId());
        //강의자료를 업데이트한다
        //파일을 다시 입력한다. (upload_file table)
        //파일을 다시 사용처리한다. ( s3_url table)
        //카테고리를 다시 입력한다.
        try {
            lectureBankMapper.updateLectureBank(lectureBank);
            return new BaseResponse("강의자료가 수정되었습니다.", HttpStatus.OK);
        }catch (Exception e){
            throw new RequestInputException(ErrorMessage.URL_NOT_UNIQUE);
        }
    }

    public void deleteLectureBank(Long id) throws Exception{
        //delete LectureBank - soft
        //TODO scrap 수정
        Long userId = userService.getLoginUser().getId();
        if(is_writer(id)){
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
        else throw new RequestInputException(ErrorMessage.FORBIDDEN_EXCEPTION);
    }

    private Boolean is_writer(Long LectureBankId) throws Exception{
        if ( lectureBankMapper.is_writer(LectureBankId, userService.getLoginUser().getId() ) == null){
            return false;
        }
        return true;
    }

    //comments------------------------------------------------------------------------------------
    @Override
    public List<LectureBankComment> getComments(Long lecture_bank_id){
        return lectureBankMapper.getComments(lecture_bank_id);
    }

    @Override
    public BaseResponse addComment(Long id, String comments) throws Exception{
        this.getLectureBank(id); // 해당 강의자료가 존재하는가?
        lectureBankMapper.addComment(userService.getLoginUser().getId(),id,comments);
        return new BaseResponse("댓글이 작성되었습니다", HttpStatus.CREATED);
    }

    @Override
    public BaseResponse setComment(Long id,Long commentId, String comments) throws Exception{
        // 해당강의자료가 존재하는가?
        this.getLectureBank(id);

        // 해당 댓글이 존재하는가 ?
        Long commentWriterId = lectureBankMapper.getCommentWriterId(commentId);
        if ( commentWriterId == null){
            throw new RequestInputException(ErrorMessage.COMMENT_NOT_EXIST);
        }
        // 저자인가?
        if  ( commentWriterId != userService.getLoginUser().getId() ){
            throw new RequestInputException(ErrorMessage.FORBIDDEN_EXCEPTION);
        }
        lectureBankMapper.setComment(commentId,comments);
        return new BaseResponse("댓글이 수정되었습니다", HttpStatus.OK);
    }

    @Override
    public BaseResponse deleteComment(Long id, Long commentId) throws Exception{
        // 해당강의자료가 존재하는가?
        this.getLectureBank(id);

        Long userId = lectureBankMapper.getCommentWriterId(commentId);
        // 해당 댓글이 존재하는가?
        if (userId == null){
            throw new RequestInputException(ErrorMessage.COMMENT_NOT_EXIST);
        }
        // 저자인가?
        if ( userId != userService.getLoginUser().getId() ){
            throw new RequestInputException(ErrorMessage.FORBIDDEN_EXCEPTION);
        }
        lectureBankMapper.deleteComment(commentId);
        return new BaseResponse("댓글이 삭제되었습니다.", HttpStatus.OK);
    }





    //purchase------------------------------------------------------------------------------------
    @Override
    public Boolean checkPurchase(Long lecture_bank_id) throws Exception {
        User user = userService.getLoginUser();
        // 구매이력에 해당 유저가 존재하는가?
        Long purchase = lectureBankMapper.checkPurchased(user.getId(), lecture_bank_id);
        if ( purchase == null){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void purchase(Long lecture_bank_id) throws Exception{
        // 이미 구입하였다면
        if(checkPurchase(lecture_bank_id)) throw new RequestInputException(ErrorMessage.ALREADY_PURCHASED);

        Long userID = userService.getLoginUser().getId();
        LectureBank lectureBank = getLectureBank(lecture_bank_id);
        Integer user_point = lectureBankMapper.getUserPoint(userID);
        // 저자가 구입한다면
        if(is_writer(lecture_bank_id)) throw new RequestInputException(ErrorMessage.PURCHASE_EXCEPTION);
        // 유저의 포인트가 100보다 큰가?
        if(user_point < Point.LECTURE_BANK.getPoint()) throw new RequestInputException(ErrorMessage.NOT_ENOUGH_POINT);
        // 구매
        lectureBankMapper.purchase(userID,lecture_bank_id, Point.LECTURE_BANK.getPoint()
                ,lectureBank.getUser_id(),Point.LECTURE_BANK.getTypeId(),Point.LECTURE_BANK.getTypeId());
    }


    //hits------------------------------------------------------------------------------------
    @Override
    public void pushHit(Long lecture_bank_id) throws Exception{
        Long userID = userService.getLoginUser().getId();

        // 해당 강의자료가 존재하는가?
        this.getLectureBank(lecture_bank_id);

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


    //file------------------------------------------------------------------------------------
    //DOWNLOAD====================================================================================
    @Override
    public String getObjectUrl(Long id) throws Exception{
        Long user_id = userService.getLoginUser().getId();
        Long lecturebank_id = lectureBankMapper.getLectureBankId_file(id);
        Long writer = lectureBankMapper.getWriterId(lecturebank_id);

        //저자 혹은 구매자인가?
        if(checkPurchase(lecturebank_id) || (writer.equals(user_id))){
            UploadFile uploadFile = lectureBankMapper.getUrl(id);
            URL url = s3Util.getPrivateObjectURL(uploadFile.getUrl(), uploadFile.getFileName());
            return url.toString();
        }else{
            throw new RequestInputException(ErrorMessage.DIDNT_PURCHASED);
        }

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
    public void deleteScrap(List<Long> idList) throws Exception{
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
