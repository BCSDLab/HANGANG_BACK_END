package in.hangang.controller;

import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.service.LectureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class LectureController {

    @Resource
    LectureService lectureService;

    //강의 리스트 검색
    @ApiOperation(value = "강의 목록 조회", notes = "강의 목록 조회 기능입니다.\nclassification : 이수구분\ndepartment : 개설 학부\nhash_tag : 해시태그 ID" +
            "\nkeyword : 검색어 (강의 혹은 교수명)\nsort : 정렬 기준 (평점순, 평가순, 최신순)\nlimit, page : 페이지네이션\n이수구분과 해시태그는 다중선택이 가능합니다." +
            "\n\nTop3 해시태그는 10분마다 갱신됩니다. 수동으로 갱신하고 싶다면 hash-tag-controller에서 /Top3HashTag를 호출해주세요.")
    @RequestMapping(value = "/lectures", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getReviewList(
            /*
            @ModelAttribute Criteria criteria,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "classification", required = false) ArrayList<String> classification,
            @RequestParam(value = "hash_tag", required = false) ArrayList<Long> hashtag,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "sort", required = false) String sort
            */
            @ModelAttribute LectureCriteria lectureCriteria)

             throws Exception {

        return new ResponseEntity (lectureService.getLectureList(lectureCriteria), HttpStatus.OK);
    }
}
