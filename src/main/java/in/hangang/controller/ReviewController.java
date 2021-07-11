package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.annotation.Xss;
import in.hangang.domain.Report;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Review;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.enums.Board;
import in.hangang.response.BaseResponse;
import in.hangang.service.ReportService;
import in.hangang.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class ReviewController {

    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;

    @Resource(name = "reportServiceImpl")
    private ReportService reportService;

    // CRUD ------------------------------------------------------------------------------------------------------
    // 강의 후기 전체 READ
    @ApiOperation( value = "강의 후기 리스트", notes = "등록된 모든 강의 후기를 읽어옵니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public ResponseEntity getReviewList(@ModelAttribute Criteria criteria) throws Exception {
        return new ResponseEntity (reviewService.getReviewList(criteria), HttpStatus.OK);
    }

    // 강의 후기 개별 READ
    @ApiOperation( value = "강의 후기 읽기", notes = "하나의 강의 후기를 확인할 수 있습니다. 강의 후기 ID를 파라미터로 주면 됩니다.",
            authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
    public ResponseEntity getReview(@PathVariable Long id) throws Exception{
        return new ResponseEntity (reviewService.getReview(id), HttpStatus.OK);
    }

    // 강의별 후기 READ
    @ApiOperation( value = "특정 강의에 등록된 강의 후기 읽기", notes = "해당 강의에 등록된 모든 후기를 확인할 수 있습니다.\n강의 ID를 파라미터로 주면 됩니다.",
            authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/lectures/{id}", method = RequestMethod.GET)
    public ResponseEntity getReviewByLectureId(@PathVariable Long id, @ModelAttribute LectureCriteria lectureCriteria) throws Exception {
        return new ResponseEntity (reviewService.getReviewByLectureId(id, lectureCriteria), HttpStatus.OK);
    }

    @Xss
    @Auth
    @ApiOperation( value = "강의 후기 작성", notes = "강의 후기 작성 성기능입니다." +
            "\nassignment : 과제 정보 ID\nassignment_amount : 과제량 (3:상, 2:중, 1:하)\nattendance_frequency : 출첵 빈도 (3:상, 2:중, 1:하)\ncomment : 강의 후기 (10글자 이상)" +
            "\ndifficulty : 난이도 (3:상, 2:중, 1:하)\ngrade_portion : 성적 비율 (3:상, 2:중, 1:하)\nhash_tag : 해시태그 ID\nlecture_id : 강의 ID" +
            "\nrating : 평점 (0.5이상 5.0이하)\nsemester_date : 수강 학기 ID ( 1: 20191, 2: 20192, 3: 20201, 4: 20202, 5: 20211 )",
            authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews", method = RequestMethod.POST)
    public ResponseEntity createReview(@RequestBody @Validated(ValidationGroups.createReview.class) Review review) throws Exception {
        return new ResponseEntity(reviewService.createReview(review), HttpStatus.OK);
    }

    // 추천 기능 ------------------------------------------------------------------------------------------------------
    // 추천 CREATE
    @Auth
    @ApiOperation( value = "강의 후기 추천", notes = "강의 후기 추천 기능입니다. 파라미터로 reviewID를 주면 됩니다.",
            authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review/recommend", method = RequestMethod.POST)
    public ResponseEntity createLikesReview(@RequestBody Review review) throws Exception{
        reviewService.likesReview(review);
        return new ResponseEntity(new BaseResponse("정상적으로 추천되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    // 신고 기능 ------------------------------------------------------------------------------------------------------
    // 리뷰 신고 CREATE
    @Auth
    @RequestMapping(value = "/review/report", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 신고" , notes = "파라미터는 강의 자료 id 입니다\n REPORT 내용 별 id => 1: \"욕설/비하\" ,2 : \"유출/사칭/저작권 위배\", 3: \"허위/부적절한 정보\"\n4: \"광고/도배\", 5: \"음란물\""
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity reportReview(@RequestBody @Validated Report report) throws Exception {
        reportService.createReport(Board.REVIEW.getId(), report);
        return new ResponseEntity(new BaseResponse("정상적으로 신고되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }
}
