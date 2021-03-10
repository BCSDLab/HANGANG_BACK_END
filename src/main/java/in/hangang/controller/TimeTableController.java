package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.Memo;
import in.hangang.domain.TimeTable;
import in.hangang.domain.UserTimeTable;
import in.hangang.response.BaseResponse;
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
    public ResponseEntity<ArrayList<UserTimeTable>> getTimeTableListByUserId(@RequestParam(required = false) Long semesterDateId) throws Exception{
        return new ResponseEntity<ArrayList<UserTimeTable>>(timetableService.getTableListByUserId(semesterDateId), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 생성", notes = "원하는 학기, 원하는 이름으로 시간표를 생성할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable", method = RequestMethod.POST)
    public ResponseEntity creatTimeTable(@Validated(ValidationGroups.createUserTimeTable.class)
                                         @RequestBody UserTimeTable userTimeTable) throws Exception{
        timetableService.createTimetable(userTimeTable);
        return new ResponseEntity( new BaseResponse("시간표가 생성되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 수정", notes = "시간표 이름을 수정할 수 있습니다. 학기는 수정이 불가능합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable", method = RequestMethod.PATCH)
    public ResponseEntity updateTimeTable(@Validated(ValidationGroups.updateUserTimeTable.class)
                                              @RequestBody UserTimeTable userTimeTable) throws Exception{
        timetableService.updateTimeTable(userTimeTable);
        return new ResponseEntity( new BaseResponse("시간표 이름이 변경되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 삭제", notes = "자신의 시간표를 삭제할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable", method = RequestMethod.DELETE)
    public ResponseEntity deleteTimeTable(@RequestBody TimeTable timeTable) throws Exception{
        timetableService.deleteTimetable(timeTable);
        return new ResponseEntity( new BaseResponse("시간표가 삭제되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 보기", notes = "시간표에 등록된 강의 목록을 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<LectureTimeTable>> getTimeTableWithLecture (@RequestParam Long timeTableId) throws Exception{
        return new ResponseEntity<ArrayList<LectureTimeTable>>(timetableService.getLectureListByTimeTableId(timeTableId), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표에 강의 추가", notes = "시간표에 강의를 추가할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.POST)
    public ResponseEntity createLectureOnTimeTable (@RequestBody TimeTable timeTable) throws Exception{
        timetableService.createLectureOnTimeTable(timeTable);
        return new ResponseEntity( new BaseResponse("강의가 정상적으로 추가되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "강의 삭제", notes = "시간표에 등록된 강의를 삭제할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.DELETE)
    public ResponseEntity deleteLectureOnTimeTable(@RequestBody TimeTable timeTable) throws Exception{
        timetableService.deleteLectureOnTimeTable(timeTable);
        return new ResponseEntity( new BaseResponse("해당 강의가 삭제되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    /*
    TODO : 메모 CRUD
    @Auth
    @ApiOperation( value = "메모 추가", notes = "강의에 메모를 추가할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture/memo", method = RequestMethod.POST)
    public ResponseEntity createMemo(@Validated(ValidationGroups.createMemo.class) @RequestBody Memo memo) throws Exception{
    }
    */


}
