package in.hangang.mapper.admin;

import in.hangang.domain.Review;
import in.hangang.domain.report.ReviewReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminReviewMapper {
    List<ReviewReport> getReportedReview();
    void deleteReviewReported(@Param("review") Review review);
    Long getReport(Long id);
    void deleteReport(Long id);
}
