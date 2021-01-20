package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.criteria.Criteria;
import in.hangang.domain.Review;
import in.hangang.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class ReviewController {

    @Resource
    ReviewService reviewService;

    // 전체 후기 리스트
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public ResponseEntity getReviewList(@ModelAttribute Criteria criteria) throws Exception {
        return new ResponseEntity<ArrayList<Review>>(reviewService.getReviewList(criteria), HttpStatus.OK);
    }

    // 후기 Read
    @Auth
    @ApiOperation( value = "강의 후기 읽기", notes = "강의 구분 없이 강의 후기를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
    public ResponseEntity getReview(@PathVariable Long id) throws Exception{
        return new ResponseEntity<Review>(reviewService.getReview(id), HttpStatus.OK);
    }

    // 해당 강의에 등록된 후기 Read
    @Auth
    @ApiOperation( value = "강의에 등록된 후기 읽기", notes = "해당 강의에 등록된 모든 후기를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review/lecture/{id}", method = RequestMethod.GET)
    public ResponseEntity getReviewByLectureId(@PathVariable Long id, @ModelAttribute Criteria criteria) throws Exception {
        return new ResponseEntity<ArrayList<Review>>(reviewService.getReviewByLectureId(id, criteria), HttpStatus.OK);
    }

    // 후기 Create
    @Auth
    @ApiOperation( value = "강의 후기 작성", notes = "강의 후기 작성 성기능입니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseEntity createReview(@RequestBody @Validated(ValidationGroups.createReview.class) Review review) throws Exception {
        reviewService.createReview(review);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 후기 추천수 기능
    @Auth
    @ApiOperation( value = "강의 후기 추천", notes = "강의 후기 추천 기능입니다. 파라미터로 reviewID를 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review/{id}", method = RequestMethod.POST)
    public ResponseEntity createLikesReview(@PathVariable Long id) throws Exception{
        reviewService.likesReview(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation( value = "개설 학기 조회", notes = "강의 id를 통해 해당 강의가 개설 되었던 학기를 조회합니다.")
    @RequestMapping(value = "/semesterdate/{id}", method = RequestMethod.GET)
    public ResponseEntity getSemesterDateByLectureId(@PathVariable Long id) throws Exception{
        return new ResponseEntity<ArrayList<String>>(reviewService.getSemesterDateByLectureId(id), HttpStatus.OK);
    }

}
