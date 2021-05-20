package in.hangang.mapper.admin;


import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminLectureBankMapper {

    List<LectureBankReport> getReportedLectureBank();
    List<LectureBankCommentReport> getReportedLectureBankComment();
    void softDeleteReported(Long typeId, Long contentId);
    Long getContent(Long typeId, Long contentId);

}
