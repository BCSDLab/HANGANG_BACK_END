package in.hangang.mapper.admin;


import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminLectureBankMapper {

    List<LectureBankReport> getReportedLectureBank();
    List<LectureBankCommentReport> getReportedLectureBankComment();
    Long getComment(Long id);
    void deleteLectureBank(Long id);
    void softDeleteReported(Long contentId, Long typeId);

}
