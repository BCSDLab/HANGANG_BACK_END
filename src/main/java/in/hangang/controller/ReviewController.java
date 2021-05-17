package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.domain.Lecture;
import in.hangang.domain.Report;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Review;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.enums.Board;
import in.hangang.enums.ContentType;
import in.hangang.response.BaseResponse;
import in.hangang.service.ReportService;
import in.hangang.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class ReviewController {

    @Autowired
    @Qualifier("ReviewServiceImpl")
    ReviewService reviewService;

    @Resource
    ReportService reportService;

    // CRUD ------------------------------------------------------------------------------------------------------
    // 강의 후기 전체 READ
    @ApiOperation( value = "강의 후기 리스트", notes = "등록된 모든 강의 후기를 읽어옵니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public ResponseEntity getReviewList(@ModelAttribute Criteria criteria) throws Exception {
        return new ResponseEntity (reviewService.getReviewList(criteria), HttpStatus.OK);
    }

    // 강의 후기 개별 READ
    @Auth
    @ApiOperation( value = "강의 후기 읽기", notes = "하나의 강의 후기를 확인할 수 있습니다. 강의 후기 ID를 파라미터로 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
    public ResponseEntity getReview(@PathVariable Long id) throws Exception{
        return new ResponseEntity (reviewService.getReview(id), HttpStatus.OK);
    }

    // 강의별 후기 READ
    @Auth
    @ApiOperation( value = "특정 강의에 등록된 강의 후기 읽기", notes = "해당 강의에 등록된 모든 후기를 확인할 수 있습니다.\n강의 ID를 파라미터로 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/lectures/{id}", method = RequestMethod.GET)
    public ResponseEntity getReviewByLectureId(@PathVariable Long id, @ModelAttribute LectureCriteria lectureCriteria) throws Exception {
        return new ResponseEntity (reviewService.getReviewByLectureId(id, lectureCriteria), HttpStatus.OK);
    }

    /*
    // 강의별 후기 READ : 시간표에 등록된 강의의 강의 후기를 읽을 때 사용
    @Auth
    @ApiOperation( value = "강의에 등록된 후기 읽기", notes = "해당 강의에 등록된 모든 후기를 확인할 수 있습니다.\n강의 ID를   파라미터로 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/timetable/lecture", method = RequestMethod.GET)
    public ResponseEntity<Lecture> getReviewByTimeTableLecture(Long lectureId) throws Exception{
        return new ResponseEntity<Lecture>(reviewService.getReviewByTimeTableLecture(lectureId), HttpStatus.OK);
    }
     */

    // 후기 CREATE
    @Auth
    @ApiOperation( value = "강의 후기 작성", notes = "강의 후기 작성 성기능입니다." +
            "\nassignment : 과제 정보 ID\nassignment_amount : 과제량 (3:상, 2:중, 1:하)\nattendance_frequency : 출첵 빈도 (3:상, 2:중, 1:하)\ncomment : 강의 후기 (10글자 이상)" +
            "\ndifficulty : 난이도 (3:상, 2:중, 1:하)\ngrade_portion : 성적 비율 (3:상, 2:중, 1:하)\nhash_tag : 해시태그 ID\nlecture_id : 강의 ID" +
            "\nrating : 평점 (0.5이상 5.0이하)\nsemester_date : 학기 정보 ( 20191, 20192, 20201, 20202 )", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews", method = RequestMethod.POST)
    public ResponseEntity createReview(@RequestBody @Validated(ValidationGroups.createReview.class) Review review) throws Exception {
        reviewService.createReview(review);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 추천 기능 ------------------------------------------------------------------------------------------------------
    // 추천 CREATE
    @Auth
    @ApiOperation( value = "강의 후기 추천", notes = "강의 후기 추천 기능입니다. 파라미터로 reviewID를 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review/recommend", method = RequestMethod.POST)
    public ResponseEntity createLikesReview(@RequestBody Review review) throws Exception{
        reviewService.likesReview(review);
        return new ResponseEntity(new BaseResponse("정상적으로 추천되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    // 신고 기능 ------------------------------------------------------------------------------------------------------
    // 리뷰 신고 CREATE
    @Auth
    @RequestMapping(value = "/review/report", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 신고" , notes = "파라미터는 강의 자료 id 입니다\n REPORT 내용 별 id =>"
            +"1: \"욕설/비하\" ,2 : \"유출/사칭/저작권 위배\", 3: \"허위/부적절한 정보\"\n" +
            "    4: \"광고/도배\", 5: \"음란물\""
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity reportReview(@RequestBody Report report) throws Exception {
        reportService.createReport(Board.REVIEW.getId(), report);
        return new ResponseEntity(new BaseResponse("정상적으로 신고되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    /*
    스크랩 기능 (사용하지 않음) ------------------------------------------------------------------------------------------------------
    // 스크랩 READ

    @Auth
    @ApiOperation( value = "자신이 스크랩한 강의 조회", notes = "해당 유저가 스크랩한 모든 강의를 조회할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/scrap", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Review>> getScrapLectureList() throws Exception{
        return new ResponseEntity<ArrayList<Review>>(reviewService.getScrapReviewList(), HttpStatus.OK);
    }

    // 스크랩 CREATE
    @Auth
    @ApiOperation( value = "강의 스크랩", notes = "강의를 스크랩합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/scrap", method = RequestMethod.POST)
    public ResponseEntity scrapReview(@RequestBody Review review) throws Exception{
        reviewService.scrapReview(review);
        return new ResponseEntity( new BaseResponse("리뷰가 정상적으로 추가되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    // 스크랩 DELETE
    @Auth
    @ApiOperation( value = "스크랩 삭제", notes = "스크랩한 리뷰를 삭제합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/scrap", method = RequestMethod.DELETE)
    public ResponseEntity deleteScrap(@RequestBody Review review) throws Exception{
        reviewService.deleteScrapReview(review);
        return new ResponseEntity( new BaseResponse("리뷰가 정상적으로 삭제되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "스크랩 갯수 조회", notes = "해당 유저가 스크랩한 리뷰의 갯수를 조회합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/scrap/count", method = RequestMethod.GET)
    public ResponseEntity getCountScrap() throws Exception{
        return new ResponseEntity(reviewService.getCountScrapReview(), HttpStatus.OK);
    }
    */



}
