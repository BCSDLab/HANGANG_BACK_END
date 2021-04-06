package in.hangang.controller.admin;


import com.amazonaws.services.xray.model.Http;
import in.hangang.annotation.Auth;
import in.hangang.service.AdminReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
public class AdminReportController {

    @Autowired
    private AdminReportService adminReportService;

    @GetMapping("/lecture-bank")
    @ApiOperation(value = "강의자료 신고 내용 조회하기", notes = "2", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.NONE)
    public ResponseEntity getReportedLectureBank() {
        return new ResponseEntity(adminReportService.getReportedLectureBank(), HttpStatus.OK);
    }
    @GetMapping("/review")
    @ApiOperation(value = "강의후기 신고 내용 조회하기", notes = "1", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.NONE)
    public ResponseEntity getReportedReview() {
        return new ResponseEntity(adminReportService.getReportedReview(), HttpStatus.OK);
    }

    @GetMapping("/comment")
    @ApiOperation(value = "강의자료 댓글 신고 내용 조회하기", notes = "3", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.NONE)
    public ResponseEntity getReportedLectureBankComment() {
        return new ResponseEntity(adminReportService.getReportedLectureBankComment(), HttpStatus.OK);
    }
    
    //TOOD 삭제시키는 거 만들어야함
}



