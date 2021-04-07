package in.hangang.controller.admin;

import in.hangang.annotation.Auth;
import in.hangang.service.admin.AdminReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/review")
public class AdminReviewController {


    @Autowired
    private AdminReviewService adminReviewService;

    @GetMapping("/review")
    @ApiOperation(value = "강의후기 신고 내용 조회하기", notes = "1", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.Lecture)
    public ResponseEntity getReportedReviewForAdmin() {
        return new ResponseEntity(adminReviewService.getReportedReview(), HttpStatus.OK);
    }

    @DeleteMapping("/review/{id}")
    @ApiOperation(value = "신고 강의평 댓글 삭제하기", notes = "", authorizations = @Authorization(value = "Bearer +accessToken"))
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.Lecture)
    public ResponseEntity deleteReportedReviewForAdmin(@PathVariable Long id ){
        return new ResponseEntity(adminReviewService.deleteReportedReviewForAdmin(id), HttpStatus.OK);
    }
}
