package in.hangang.mapper;


import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.domain.report.ReviewReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminReportMapper {
    List<ReviewReport> getReportedReview();
    List<LectureBankReport> getReportedLectureBank();
    List<LectureBankCommentReport> getReportedLectureBankComment();
}
