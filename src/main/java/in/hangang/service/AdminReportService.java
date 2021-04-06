package in.hangang.service;


import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.domain.report.ReviewReport;

import java.util.List;

public interface AdminReportService {
    List<ReviewReport> getReportedReview();
    List<LectureBankReport> getReportedLectureBank();
    List<LectureBankCommentReport> getReportedLectureBankComment();

}
