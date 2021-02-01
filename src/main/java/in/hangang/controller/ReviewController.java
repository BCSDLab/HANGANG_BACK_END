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
    @ApiOperation( value = "모든 강의 후기 읽기", notes = "등록된 모든 강의 후기를 읽어옵니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public ResponseEntity getReviewList(@ModelAttribute Criteria criteria) throws Exception {
        return new ResponseEntity<ArrayList<Review>>(reviewService.getReviewList(criteria), HttpStatus.OK);
    }

    // 후기 Read
    @Auth
    @ApiOperation( value = "강의 후기 읽기", notes = "하나의 강의 후기를 확인할 수 있습니다. 강의 후기 ID를 파라미터로 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
    public ResponseEntity getReview(@PathVariable Long id) throws Exception{
        return new ResponseEntity<Review>(reviewService.getReview(id), HttpStatus.OK);
    }

    // 해당 강의에 등록된 후기 Read
    @Auth
    @ApiOperation( value = "강의에 등록된 후기 읽기", notes = "해당 강의에 등록된 모든 후기를 확인할 수 있습니다.\n강의 ID를 파라미터로 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/lectures/{id}", method = RequestMethod.GET)
    public ResponseEntity getReviewByLectureId(@PathVariable Long id, @ModelAttribute Criteria criteria) throws Exception {
        return new ResponseEntity<ArrayList<Review>>(reviewService.getReviewByLectureId(id, criteria), HttpStatus.OK);
    }

    // 후기 Create
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

    // 후기 추천수 기능
    @Auth
    @ApiOperation( value = "강의 후기 추천", notes = "강의 후기 추천 기능입니다. 파라미터로 reviewID를 주면 됩니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/recommend/{id}", method = RequestMethod.POST)
    public ResponseEntity createLikesReview(@PathVariable Long id) throws Exception{
        reviewService.likesReview(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //분반 정보 조회
    @Auth
    @ApiOperation(value = "분반 확인 기능", notes = "강의 id를 통해 해당 강의의 모든 분반 정보를 조회합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/reviews/class/{id}", method = RequestMethod.GET)
    public ResponseEntity getClassByLectureId(@PathVariable Long id) throws Exception{
        return new ResponseEntity(reviewService.getClassByLectureId(id), HttpStatus.OK);

    }

    @Auth
    @ApiOperation( value = "개설 학기 조회", notes = "강의 id를 통해 해당 강의가 개설 되었던 학기를 조회합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/semesterdates/{id}", method = RequestMethod.GET)
    public ResponseEntity getSemesterDateByLectureId(@PathVariable Long id) throws Exception{
        return new ResponseEntity<ArrayList<String>>(reviewService.getSemesterDateByLectureId(id), HttpStatus.OK);
    }

}
