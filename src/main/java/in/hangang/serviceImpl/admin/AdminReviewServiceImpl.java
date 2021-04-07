package in.hangang.serviceImpl.admin;

import in.hangang.domain.Review;
import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.domain.report.ReviewReport;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.admin.AdminLectureBankMapper;
import in.hangang.mapper.admin.AdminReviewMapper;
import in.hangang.response.BaseResponse;
import in.hangang.service.admin.AdminReviewService;
import in.hangang.serviceImpl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AdminReviewServiceImpl extends ReviewServiceImpl implements AdminReviewService {


    @Autowired
    private AdminReviewMapper adminReviewMapper;
    @Override
    public List<ReviewReport> getReportedReview(){
        return adminReviewMapper.getReportedReview();
    }

    @Override
    public BaseResponse deleteReportedReviewForAdmin(Long id){
        Review review = reviewMapper.getReviewById(id);

        // 강의평이 존재하지 않는다면
        if ( review == null){
            throw new RequestInputException(ErrorMessage.REVIEW_NOT_EXIST);
        }
        //리뷰 삭제
        // 리뷰 과제량 삭제
        //리뷰 해쉬태그 삭제
        // 리뷰 스크랩 삭제
        // 리뷰 좋아요 삭제
        // 신고내역에서 삭제
        adminReviewMapper.deleteReviewReported(id);

        return new BaseResponse("강의평이 삭제되었습니다.", HttpStatus.OK);
    }
}
