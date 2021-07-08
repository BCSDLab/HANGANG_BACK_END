package in.hangang.controller;

import in.hangang.service.SemesterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SemesterController {

    @Resource
    SemesterService semesterService;

    @ApiOperation(value = "현재 학기 조회", notes = "현재 진행되고 있는 학기를 조회 가능합니다")
    @RequestMapping(value = "/semester", method = RequestMethod.GET)
    public ResponseEntity getCurrentSemester(Long isRegular) throws Exception{
        return new ResponseEntity(semesterService.getCurrentSemesterDate(isRegular), HttpStatus.OK);
    }
}
