package in.hangang.serviceImpl;

import in.hangang.domain.*;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureBankMapper;
import in.hangang.mapper.UserMapper;
import in.hangang.service.LectureBankService;
import in.hangang.service.UserService;
import in.hangang.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureBankServiceImpl implements LectureBankService {

    @Autowired
    private LectureBankMapper lectureBankMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
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
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        User user = userMapper.getMe(lectureBank.getUser_id());
        lectureBank.setUser(user);

        Lecture lecture = getLecture(lectureBank.getLecture_id());
        lectureBank.setLecture(lecture);

        return lectureBank;

    }

    @Override
    public Lecture getLecture(Long id){
        return lectureBankMapper.getLectureInfo(id);
    }

    @Override
    public Long createLectureBank() throws Exception{
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);

        lectureBankMapper.createLectureBank(user.getId());
        return lectureBankMapper.getLectureBankId(user.getId());
    }

    @Override
    public void setLectureBank(LectureBank lectureBank) throws Exception{
        if(lectureBank.getId() == null) throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        LectureBank lb = lectureBankMapper.getLectureBank(lectureBank.getId());

        Long lecture_id; String title, content; Integer point_price;
        if(lectureBank.getLecture_id() == null) lecture_id = lb.getLecture_id();
        else lecture_id = lectureBank.getLecture_id();
        if(lectureBank.getTitle() == null) title = lb.getTitle();
        else title = lectureBank.getTitle();
        if(lectureBank.getContent() == null) content = lb.getContent();
        else content = lectureBank.getContent();
        if(lectureBank.getPoint_price() == null) point_price = lb.getPoint_price();
        else point_price = lectureBank.getPoint_price();

        lectureBankMapper.setLectureBank(lectureBank.getId(), lecture_id, title, content, point_price);

        List<String> categoryList = lectureBank.getCategory();
        if(categoryList != null){
            //TODO 이해가 안되는 부분 왜 강의자료를 삽입할 때 댓글을 지우는지? 이 카테고리의 id는 댓글ID과 무슨 상관인지?
            //TODO 수정에서도 이 함수를 사용하던데 이 때 카테고리를 지워야하는 거 아닌가요?
            //lectueMapper add to lecturebank _ Category : category list while empty
            List<Long> idList = lectureBankMapper.getCategoryIdList(lectureBank.getId());
            for(Long id : idList){
                lectureBankMapper.deleteComment(id);
            }
            //TODO 자바에서 디비를 반복적으로 접근하는건 매우 비효율 적입니다 mybatis의 for each를 사용해주세요
            for (String s : categoryList) {
                //기출자료 필기자료 과제자료 강의자료 기타자료
                lectureBankMapper.addCategory(lectureBank.getId(), s);
            }
        }
    }

    @Override
    public void submitLectureBank(LectureBank lectureBank) throws Exception{
        // TODO 이 때 파일의 섬네일을 추출해서 같이 등록해주면 좋을 것 같습니다.
        // TODO 임시생성후 내용을 채우는 방식으로 파악했습니다 이 경우 임시생성한 USER의 ID와 채울 때 USER의 ID도 검사해준다면 더 꼼꼼해질 것 같아요
        setLectureBank(lectureBank);
        List<Long> list = lectureBankMapper.getFileIdList(lectureBank.getId());
        for(Long i : list){
            //TODO 자바에서 디비를 반복적으로 접근하는건 매우 비효율 적입니다 mybatis의 for each를 사용해주세요
            lectureBankMapper.setFileAvailable(i,1);
        }
        //TODO 어차피 인터셉터에서 걸립니다 Controller에서 @Auth 어노테이션을 사용했으므로 해당 코드는 필요가 없습니다.
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);

        //zeplin: 작성시 +50point
        lectureBankMapper.setPoint(user.getId(), 50);
    }

    @Override
    public void deleteLectureBank(Long id) throws Exception{

        //TODO Trancation 처리를 한 뒤 해당 강의자료가 지워지면 댓글, 파일, 카테고리 등 연관된 부분도 전부 지워지면 좋을 것 같습니다
        //TODO soft delete를 적용해주면 나중에 더 좋을 것 같습니다!
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        lectureBankMapper.deleteLectureBank(id, user.getId());
    }

    //comments------------------------------------------------------------------------------------
    @Override
    public ArrayList<LectureBankComment> getComments(Long lecture_bank_id){
        return lectureBankMapper.getComments(lecture_bank_id);
    }

    @Override
    public void addComment(Long lecture_bank_id, String comments) throws Exception{
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        lectureBankMapper.addComment(user.getId(), lecture_bank_id, comments);
    }

    @Override
    public void setComment(Long lecture_bank_comment_id, String comments) throws Exception{
        lectureBankMapper.setComment(lecture_bank_comment_id, comments);
    }

    @Override
    public void deleteComment(Long lecture_bank_comment_id) throws Exception{
        lectureBankMapper.deleteComment(lecture_bank_comment_id);
    }

    //purchase------------------------------------------------------------------------------------
    @Override
    public Boolean checkPurchase(Long lecture_bank_id) throws Exception {
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Integer purchase = lectureBankMapper.checkPurchased(user.getId(), lecture_bank_id);
        if(purchase == null){
            return false;
        }
        if(purchase.intValue() == 1){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void purchase(Long lecture_bank_id) throws Exception{
        User user = userService.getLoginUser();
        //TODO 인터셉터에서 검사하므로 NULL 체크는 의미가 없습니다.
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        LectureBank lectureBank = getLectureBank(lecture_bank_id);
        Integer purchase = lectureBankMapper.checkPurchased(user.getId(), lecture_bank_id);

        Integer point_price = lectureBank.getPoint_price();
        if(purchase == null){
            // purchased table에 추가
            lectureBankMapper.purchaseInsert(user.getId(), lecture_bank_id);
        }

        if(purchase.intValue()==0){
            // 구매한 유저 포인트-- 자료주인은 포인트++
            lectureBankMapper.setPoint(user.getId(), -point_price);
            lectureBankMapper.setPoint(lectureBank.getUser_id(), point_price);

        }

    }


    //hits------------------------------------------------------------------------------------
    @Override
    public Boolean checkHits(Long lecture_bank_id) throws  Exception {
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Integer hits = lectureBankMapper.checkHits(user.getId(), lecture_bank_id);
        if(hits == null){
            return false;
        }
        if(hits.intValue() ==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void pushHit(Long lecture_bank_id) throws Exception{
        User user = userService.getLoginUser();
        if (user==null) throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Integer hits = lectureBankMapper.checkHits(user.getId(), lecture_bank_id);
        if(hits == null){
            //insert mapper with hit 1
            lectureBankMapper.hitInsert(user.getId(), lecture_bank_id);
            lectureBankMapper.addHit_lecture_bank(lecture_bank_id);
        } else if(hits.intValue()==1){
            lectureBankMapper.subHit(user.getId(), lecture_bank_id);
            lectureBankMapper.subHit_lecture_bank(lecture_bank_id);
        }else{
            lectureBankMapper.addHit(user.getId(), lecture_bank_id);
            lectureBankMapper.addHit_lecture_bank(lecture_bank_id);
        }
    }


    //file------------------------------------------------------------------------------------
    @Override
    public List<Long> LectureBankFilesUpload(List<MultipartFile> fileList, Long lecture_bank_id) throws IOException {
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
    public Long fileUpload(MultipartFile file, Long lecture_bank_id) throws IOException{
        String uploadUrl = s3Util.privateUpload(file);
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String fileExt = fileName.substring(index+1);
        lectureBankMapper.insertUpload_file(lecture_bank_id, uploadUrl,fileName, fileExt);
        return lectureBankMapper.getUploadFileId(lecture_bank_id);
    }

    @Override
    public List<UploadFile> getFileList(Long lecture_bank_id) throws Exception{
        return lectureBankMapper.getFileList(lecture_bank_id);
    }

    @Override
    public org.springframework.core.io.Resource getprivateObject(Long id) throws Exception{
        //TODO 다운로드 받을 유저의 권한체크 필요할 것 같습니다 1. 게시자인지/ 2. 구매자인지
        String objectKey = lectureBankMapper.getUrl(id);
        URL url = s3Util.getPrivateObjectURL(objectKey);

        //TODO 어 그런데 생각해보니까 굳이 뻘짓할 필요없이 싹다 PRIVATE로 하고 권한 체크한뒤  그냥 파일 자체를 서버에서 S3.GET해서 던져주면 됐던거아닌가요?
        org.springframework.core.io.Resource resource = new UrlResource(url);
        return resource;
    }

    @Override
    public void cancelUpload(Long id) throws Exception{
        lectureBankMapper.setFileAvailable(id,2);
    }


}
