package in.hangang.controller.admin;

import in.hangang.annotation.Auth;
import in.hangang.response.BaseResponse;
import in.hangang.service.admin.AdminLectureBankService;
import in.hangang.service.admin.AdminReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminLectureBankController {

    @Autowired
    @Qualifier("AdminLectureBankServiceImpl")
    private AdminLectureBankService adminLectureBankService;

    @GetMapping("/lecture-bank")
    @ApiOperation(value = "강의자료 신고 내용 조회하기", notes = "2", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    public ResponseEntity getReportedLectureBankForAdmin() {
        return new ResponseEntity(adminLectureBankService.getReportedLectureBank(), HttpStatus.OK);
    }


    @GetMapping("/comment")
    @ApiOperation(value = "강의자료 댓글 신고 내용 조회하기", notes = "3", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    public ResponseEntity getReportedLectureBankCommentForAdmin()throws Exception {
        return new ResponseEntity(adminLectureBankService.getReportedLectureBankComment(), HttpStatus.OK);
    }

    @DeleteMapping("/lecture-bank/{id}")
    @ApiOperation(value = "신고 강의자료 신고 내용 삭제하기", notes = "", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    public ResponseEntity deleteReportedLectureBankForAdmin(@PathVariable Long id ) throws Exception{
        adminLectureBankService.deleteLectureBank(id);
        return new ResponseEntity(new BaseResponse("강의자료 삭제 완료",HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/comment/{id}")
    @ApiOperation(value = "신고 강의자료 댓글 삭제하기", notes = "", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    public ResponseEntity deleteReportedCommentForAdmin(@PathVariable Long id  ) throws Exception{
        return new ResponseEntity(adminLectureBankService.deleteReportedCommentForAdmin(id), HttpStatus.OK);
    }

    @DeleteMapping("/report/{id}")
    @ApiOperation(value = "신고 강의자료 기각하기", notes = "신고 내용을 기각한다. 강의자료의 board_type_id =1 ", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    public ResponseEntity deleteReport(@PathVariable Long id ){
        return new ResponseEntity(adminLectureBankService.deleteReport(Long.valueOf(1),id), HttpStatus.OK);
    }

    @DeleteMapping("/report/comment/{id}")
    @ApiOperation(value = "신고 강의자료 댓글 기각하기", notes = "신고 내용을 기각한다. 강의자료 댓글의 board_type_id = 2 ", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    public ResponseEntity deleteCommentReport(@PathVariable Long id ){
        return new ResponseEntity(adminLectureBankService.deleteReport(Long.valueOf(2),id), HttpStatus.OK);
    }

}



