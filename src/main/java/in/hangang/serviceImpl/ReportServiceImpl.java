package in.hangang.serviceImpl;

import in.hangang.config.SlackNotiSender;
import in.hangang.domain.*;
import in.hangang.domain.slack.SlackAttachment;
import in.hangang.domain.slack.SlackParameter;
import in.hangang.domain.slack.SlackTarget;
import in.hangang.enums.Board;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.ReportMapper;
import in.hangang.service.ReportService;
import in.hangang.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    ReportMapper reportMapper;

    @Resource(name = "userServiceImpl")
    UserService userService;

    @Resource
    SlackNotiSender slackNotiSender;

    @Value("${report_slack_url}")
    private String notifyReportUrl;

    @Override
    @Transactional
    public void createReport(Integer boardTypeId, Report report) throws Exception{
        report.setBoard_type_id(boardTypeId);

        if(report.getReport_id()==null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        User user = userService.getLoginUser();
        //유저 정보가 있는지 확인. ===> AUTH를 붙이는 API니까 필요 없는 검증입니다 - 수현
        report.setUser_id(user.getId());

        //이미 신고한 적이 있는지 확인
        if(reportMapper.checkAlreadyReported(report)!=null)
            throw new RequestInputException(ErrorMessage.ALREADY_REPORTED);

        //게시글이 존재하는지, 삭제되지 않았는지 확인
        String contents = null;
        if(report.getBoard_type_id() == Board.LECTURE_BANK.getId()){
            LectureBank lectureBank = reportMapper.getReportedLectureBank(report.getContent_id());
            if(lectureBank!=null) contents = lectureBank.getContent();
        }else if(report.getBoard_type_id() == Board.LECTURE_BANK_COMMENT.getId()){
            LectureBankComment lectureBankComment
                    = reportMapper.getReportedLectureBankComment(report.getContent_id());
            if(lectureBankComment != null) contents = lectureBankComment.getComments();
        }else if(report.getBoard_type_id() == Board.REVIEW.getId()){
            Review review = reportMapper.getReportedReview(report.getContent_id());
            if(review != null) contents = review.getComment();
        }

        if(contents== null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        Long id =reportMapper.createReport(report);
        Report recentReport = reportMapper.getlatestReport(id);
        sendReportNoti(recentReport, contents);

    }

    @Override
    public void sendReportNoti(Report report,String contents) throws Exception{

        SlackTarget slackTarget = new SlackTarget(notifyReportUrl,"");

        SlackParameter slackParameter = new SlackParameter();
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setTitle("한강 신고");
        slackAttachment.setAuthorName("한강 컨텐츠 신고");
        slackAttachment.setAuthorIcon("https://static.hangang.in/2021/05/30/5aa690a2-9b17-41e7-9ae5-79dd078650d6-1622378988133.PNG");
        String reportName = in.hangang.enums.Report
                .nameOf(Integer.parseInt(report.getReport_id().toString()));
        String boardName = Board.nameOf(report.getBoard_type_id());

        String message = String.format("REPORT LOG ID: %s\n신고내용: %s \n"
                        +"%s 에서 (user_id: %d)에 의해 (content_id : %d)가 신고되었습니다\n"
                ,report.getId(), reportName, boardName, report.getUser_id(), report.getContent_id());
        message += String.format("신고된 %s 내용\n===== [CONTENTS] ===== \n%s",boardName,contents);

        slackAttachment.setText(message);
        slackParameter.getSlackAttachments().add(slackAttachment);
        slackNotiSender.send(slackTarget,slackParameter);
    }
}
