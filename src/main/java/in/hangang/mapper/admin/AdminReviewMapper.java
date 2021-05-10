package in.hangang.mapper.admin;

import in.hangang.domain.report.ReviewReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminReviewMapper {
    List<ReviewReport> getReportedReview();
    void deleteReviewReported(Long id);
    ReviewReport getReport(Long id);
    Long getContent(Long id);
    void deleteReport(Long id);
}
