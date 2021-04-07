package in.hangang.service.admin;


import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.domain.report.ReviewReport;
import in.hangang.response.BaseResponse;
import in.hangang.service.ReviewService;

import java.util.List;

public interface AdminReviewService extends ReviewService {
    List<ReviewReport> getReportedReview();
    BaseResponse deleteReportedReviewForAdmin(Long id);


}
