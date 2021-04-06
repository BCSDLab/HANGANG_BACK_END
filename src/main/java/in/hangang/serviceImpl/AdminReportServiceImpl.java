package in.hangang.serviceImpl;

import in.hangang.annotation.Auth;
import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.domain.report.ReviewReport;
import in.hangang.mapper.AdminReportMapper;
import in.hangang.service.AdminReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReportServiceImpl implements AdminReportService {

    @Autowired
    private AdminReportMapper adminReportMapper;

    @Override
    public List<ReviewReport> getReportedReview(){
        return adminReportMapper.getReportedReview();
    }

    @Override
    public List<LectureBankReport> getReportedLectureBank(){
        return adminReportMapper.getReportedLectureBank();
    }

    @Override
    public List<LectureBankCommentReport> getReportedLectureBankComment(){
        return adminReportMapper.getReportedLectureBankComment();
    }
}
