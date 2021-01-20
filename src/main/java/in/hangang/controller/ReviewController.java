package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
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

    // 전체 리뷰 리스트
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public ResponseEntity getReviewList() throws Exception {
        return new ResponseEntity<ArrayList<Review>>(reviewService.getReviewList(), HttpStatus.OK);
    }

    // 리뷰 Read
    @Auth
    @RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
    public ResponseEntity getReview(@PathVariable Long id) throws Exception{
        return new ResponseEntity<Review>(reviewService.getReview(id), HttpStatus.OK);
    }

    // 해당 강의에 등록된 리뷰 Read
    @RequestMapping(value = "/review/lecture/{id}", method = RequestMethod.GET)
    public ResponseEntity getReviewByLectureId(@PathVariable Long id) throws Exception {
        return new ResponseEntity<ArrayList<Review>>(reviewService.getReviewByLectureId(id), HttpStatus.OK);
    }

    // 리뷰 Create
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseEntity createReview(@RequestBody @Validated(ValidationGroups.createReview.class) Review review) throws Exception {
        reviewService.createReview(review);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 리뷰 추천수 기능
    @Auth
    @ApiOperation( value = "강의 리뷰 추천",notes = "강의 리뷰 추천기능입니다. 파라미터로 reviewID를 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/review/{id}", method = RequestMethod.POST)
    public ResponseEntity createLikesReview(@PathVariable Long id) throws Exception{
        reviewService.likesReview(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //TODO : 강의 종합 평가 api 설계
}
