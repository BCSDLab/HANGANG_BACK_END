package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.service.TotalEvaluationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TotalEvaluationController {

    @Resource
    TotalEvaluationService totalEvaluationService;

    @Auth
    @ApiOperation( value = "강의 종합평가 보기", notes = "해당 강의의 종합평가를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/evaluation/total/{id}", method = RequestMethod.GET)
    public ResponseEntity getTotalEvaluation(@RequestParam Long id) throws Exception {
        return new ResponseEntity(totalEvaluationService.getTotalEvaluation(id), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "평점 그래프 보기", notes = "해당 강의에 등록된 평점의 분포를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/evaluation/rating{id}", method = RequestMethod.GET)
    public ResponseEntity getrating(@RequestParam Long id) throws Exception {
        return new ResponseEntity(totalEvaluationService.getRatingCountByLectureId(id), HttpStatus.OK);
    }
}
