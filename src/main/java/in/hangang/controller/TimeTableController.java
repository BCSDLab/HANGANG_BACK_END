package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.domain.TimeTable;
import in.hangang.domain.UserTimetable;
import in.hangang.service.TimetableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class TimeTableController {
    @Resource
    TimetableService timetableService;

    @Auth
    @ApiOperation( value = "시간표 확인", notes = "해당 유저가 생성한 시간표를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<UserTimetable>> getTableListByUserId() throws Exception{
        return new ResponseEntity<ArrayList<UserTimetable>>(timetableService.getTableListByUserId(), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 생성", notes = "원하는 학기, 원하는 이름으로 시간표를 생성할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable", method = RequestMethod.POST)
    public ResponseEntity creatTable(@Validated(ValidationGroups.createUserTimetable.class)
                                         @RequestBody UserTimetable userTimetable) throws Exception{
        timetableService.createTimetable(userTimetable);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 삭제", notes = "자신의 시간표를 삭제할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable", method = RequestMethod.DELETE)
    public ResponseEntity deleteTimeTable(Long timeTableId) throws Exception{
        timetableService.deleteTimetable(timeTableId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.POST)
    public ResponseEntity createLectureOnTable (@RequestBody TimeTable timeTable) throws Exception{
        timetableService.createLectureOnTimeTable(timeTable);
        return new ResponseEntity(HttpStatus.OK);
    }

}
