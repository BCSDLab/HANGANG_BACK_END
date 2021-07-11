package in.hangang.serviceImpl.admin;

import in.hangang.domain.LectureBank;
import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.admin.AdminLectureBankMapper;
import in.hangang.response.BaseResponse;
import in.hangang.service.admin.AdminLectureBankService;
import in.hangang.serviceImpl.LectureBankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AdminLectureBankServiceImpl extends LectureBankServiceImpl implements AdminLectureBankService {

    @Resource
    private AdminLectureBankMapper adminLectureBankMapper;


    @Override
    public List<LectureBankReport> getReportedLectureBank(){
        return adminLectureBankMapper.getReportedLectureBank();
    }

    @Override
    public List<LectureBankCommentReport> getReportedLectureBankComment(){
        return adminLectureBankMapper.getReportedLectureBankComment();
    }

    @Override
    public BaseResponse deleteReportedCommentForAdmin(Long id) throws Exception{

        Long userId = lectureBankMapper.getCommentWriterId(id);
        // 해당 댓글이 존재하는가?
        if (userId == null){
            throw new RequestInputException(ErrorMessage.COMMENT_NOT_EXIST);
        }
        // 댓글과 신고내역에서 삭제
        lectureBankMapper.deleteComment(id);
        return new BaseResponse("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    @Override
    public void deleteLectureBank(Long id) throws Exception{
        //delete LectureBank - soft
        //user_id 무시

        // 해당 강의자료가 존재하는가 ?
        LectureBank lectureBank = this.getLectureBank(id);

        // 강의자료, 스크랩, 카테고리, 댓글, 좋아요, 구매 , 신고내역, 파일업로드 내역, 신고내역 댓글 -> is_deleted = 1
        // s3_url 사용안함처리
        lectureBankMapper.deleteLectureBank(id, lectureBank.getUploadFiles(), lectureBank.getComments());
    }

    /** 신고내용을 기각하는 api 신고기록을 soft delete 한다.*/
    @Override
    public BaseResponse deleteReport(Long typeId , Long contentId){
        //신고내역이 존재하는지?
        if (adminLectureBankMapper.getContent(typeId, contentId) == null)
            throw new RequestInputException(ErrorMessage.REPORT_NOT_EXIST);

        //신고내용 기각
        adminLectureBankMapper.softDeleteReported(typeId,contentId);
        return new BaseResponse("신고내역을 기각했습니다.", HttpStatus.OK);
    }
}
