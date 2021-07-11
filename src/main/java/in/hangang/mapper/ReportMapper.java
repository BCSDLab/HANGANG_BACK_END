package in.hangang.mapper;

import in.hangang.domain.LectureBank;
import in.hangang.domain.LectureBankComment;
import in.hangang.domain.Report;
import in.hangang.domain.Review;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportMapper {
    Long createReport(Report report);
    Long checkAlreadyReported(Report report);
    Report getlatestReport(Long id);

    LectureBank getReportedLectureBank(@Param("id")Long id);
    LectureBankComment getReportedLectureBankComment(@Param("id")Long id);
    Review getReportedReview(@Param("id")Long id);
}
