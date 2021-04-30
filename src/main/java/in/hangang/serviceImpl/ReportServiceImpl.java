package in.hangang.serviceImpl;

import in.hangang.domain.Report;
import in.hangang.domain.User;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.ReportMapper;
import in.hangang.service.ReportService;
import in.hangang.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    ReportMapper reportMapper;

    @Resource
    @Qualifier("UserServiceImpl")
    UserService userService;

    @Override
    public void createReport(Integer boardTypeId, Report report) throws Exception{
        report.setBoard_type_id(boardTypeId);

        if(report.getReport_id()==null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        User user = userService.getLoginUser();
        //유저 정보가 있는지 확인.
        if(user == null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        report.setUser_id(user.getId());

        //TODO : '게시글이 존재하는지 확인하는 기능' 하나의 서비스 혹은 매퍼로 통일해야 이렇게 구현 가능

        //이미 신고한 적이 있는지 확인
        if(reportMapper.checkAlreadyReported(report)!=null)
            throw new RequestInputException(ErrorMessage.ALREADY_REPORTED);

        reportMapper.createReport(report);
    }
}
