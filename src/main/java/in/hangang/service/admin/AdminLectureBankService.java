package in.hangang.service.admin;

import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.response.BaseResponse;
import in.hangang.service.LectureBankService;

import java.util.List;

public interface AdminLectureBankService extends LectureBankService {
    List<LectureBankReport> getReportedLectureBank();
    List<LectureBankCommentReport> getReportedLectureBankComment();
    BaseResponse deleteReportedCommentForAdmin(Long id);
    BaseResponse deleteReport(Long id);
    BaseResponse deleteCommentReport(Long id);
}
