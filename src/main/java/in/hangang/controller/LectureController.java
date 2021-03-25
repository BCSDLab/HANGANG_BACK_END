package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.service.LectureService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
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
    ResponseEntity getReviewList(@ModelAttribute LectureCriteria lectureCriteria) throws Exception {
        return new ResponseEntity (lectureService.getLectureList(lectureCriteria), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "개설 학기 조회", notes = "강의 id를 통해 해당 강의가 개설 되었던 학기를 조회합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/semesterdates/lectures/{id}", method = RequestMethod.GET)
    public ResponseEntity getSemesterDateByLectureId(@PathVariable Long id) throws Exception{
        return new ResponseEntity<ArrayList<String>>(lectureService.getSemesterDateByLectureId(id), HttpStatus.OK);
    }

    //분반 정보 조회
    @Auth
    @ApiOperation(value = "분반 확인 기능", notes = "강의 id를 통해 해당 강의의 모든 분반 정보를 조회합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/class/lectures/{id}", method = RequestMethod.GET)
    public ResponseEntity getClassByLectureId(@PathVariable Long id) throws Exception{
        return new ResponseEntity(lectureService.getClassByLectureId(id), HttpStatus.OK);

    }

}
