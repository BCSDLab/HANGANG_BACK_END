package in.hangang.serviceImpl;

import com.amazonaws.services.xray.model.Http;
import in.hangang.domain.*;
import in.hangang.domain.criteria.Criteria;
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
    protected UserService userService;

    @Autowired
    private S3Util s3Util;

    //Main------------------------------------------------------------------------------------

    /** 강의 페이지네이션 조회 메소드  -  */
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

    /**강의 단일조회 메소드 -  */
    @Override
    public LectureBank getLectureBank(Long id) throws Exception{
        LectureBank lectureBank = lectureBankMapper.getLectureBankAll(id ,userService.getLoginUser() );
        if(lectureBank == null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        else
            return lectureBank;

    }

    /** 해당 강의가 존재하는지 확인하는 메소드 - */
    private Lecture getLecture(Long id){
        Lecture lecture = lectureBankMapper.getLectureInfo(id);
        if(lecture == null) throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        return lecture;
    }


    /** url의 확장자에 따라 default 썸네일을 반환해주는 메소드  -  */
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
     * s3 업로드된 url은 private한 url이다. -
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

    /** 카테고리가 올바른 값으로 들어왔는지 검증하는 메소드  -  */
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
    /** 강의자료를 수정하는 메소드 - */
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

    /** 강의자료와 관련된 모든 내용 soft delete */
    public void deleteLectureBank(Long id) throws Exception{

        // 해당 강의자료가 존재하는가 ?
        LectureBank lectureBank = this.getLectureBank(id);
        Long dbUser =  lectureBank.getUser_id();
        // 저자인가?
        if ( dbUser != userService.getLoginUser().getId()){
            throw new RequestInputException(ErrorMessage.FORBIDDEN_EXCEPTION);
        }
        // 강의자료, 스크랩, 카테고리, 댓글, 좋아요, 구매 , 신고내역, 파일업로드 내역, 신고내역 댓글 -> is_deleted = 1
        // s3_url 사용안함처리
        lectureBankMapper.deleteLectureBank(id, lectureBank.getUploadFiles(), lectureBank.getComments());
    }

    private Boolean is_writer(Long LectureBankId) throws Exception{
        if ( lectureBankMapper.is_writer(LectureBankId, userService.getLoginUser().getId() ) == null){
            return false;
        }
        return true;
    }

    //comments------------------------------------------------------------------------------------
    @Override
    public Map<String,Object> getComments(Long lecture_bank_id , Criteria criteria){
        Map<String, Object> map = new HashMap<>();
        map.put("comments",lectureBankMapper.getComments(lecture_bank_id, criteria));
        map.put("count", lectureBankMapper.getCommentCount(lecture_bank_id));
        return map;
    }

    @Override
    public Long addComment(Long id, String comments) throws Exception{
        this.getLectureBank(id); // 해당 강의자료가 존재하는가?
        return lectureBankMapper.addComment(userService.getLoginUser().getId(),id,comments);
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
        // 댓글과 신고내역에서 삭제
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
        Long lecture_bank_id = lectureBankMapper.getLectureBankId_file(id);
        // 파일이 존재하지 않는경우  (삭제된 경우 )
        if ( lecture_bank_id == null){
            throw new RequestInputException(ErrorMessage.DELETED_FILE);
        }
        Long writer = lectureBankMapper.getWriterId(lecture_bank_id);

        //저자 혹은 구매자인가?
        if(checkPurchase(lecture_bank_id) || (writer.equals(user_id))){
            UploadFile uploadFile = lectureBankMapper.getUrl(id);
            URL url = s3Util.getPrivateObjectURL(uploadFile.getUrl(), uploadFile.getFileName());
            return url.toString();
        }else{
            throw new RequestInputException(ErrorMessage.DIDNT_PURCHASED);
        }

    }


    @Override
    public Long createScrap(Long lecture_bank_id) throws Exception{
        Long user_id = userService.getLoginUser().getId();

        if(lecture_bank_id != null){
            this.getLectureBank(lecture_bank_id); // 해당 강의자료가 존재하는가?
            Long id = lectureBankMapper.checkScrapDeleted(user_id,lecture_bank_id); // null -> 스크랩한적없음 , true 스크랩했음, false 스크랩을 취소한적이있음
            if(id != null){ // 스크랩한적이 없다면
                throw new RequestInputException(ErrorMessage.SCRAP_ALREADY_EXISTS); // 스크랩이 이미 있다면

            }else {
                return lectureBankMapper.createScrap(user_id, lecture_bank_id); //삽입
            }
        }
        else // input값에러
            throw new RequestInputException(ErrorMessage.NULL_POINTER_EXCEPTION);
    }

    @Override
    public void deleteScrap(List<Long> idList) throws Exception{
        if(idList!= null && idList.size() > 0){
            // 만약 두 list의 길이가 다르다면 idList중 잘못된 값이 있음
            if ( lectureBankMapper.checkScrap(idList, userService.getLoginUser().getId()).size() != idList.size()){
                throw new RequestInputException(ErrorMessage.SCRAP_DOES_NOT_EXIST);
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
