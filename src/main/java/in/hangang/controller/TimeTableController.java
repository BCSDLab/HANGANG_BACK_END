package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.domain.*;
import in.hangang.domain.criteria.TimeTableCriteria;
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
import java.util.HashMap;

@RestController
public class TimeTableController {

    @Resource
    TimetableService timetableService;

    @ApiOperation( value = "강의 목록 확인", notes = "강의 리스트를 보여줍니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture/list", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<LectureTimeTable>> getLectureList(@ModelAttribute TimeTableCriteria timeTableCriteria) throws Exception{
        return new ResponseEntity<ArrayList<LectureTimeTable>>(timetableService.getLectureList(timeTableCriteria), HttpStatus.OK);
    }

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
    public ResponseEntity deleteTimeTable(@RequestBody UserTimeTable userTimeTable) throws Exception{
        timetableService.deleteTimetable(userTimeTable);
        return new ResponseEntity( new BaseResponse("시간표가 삭제되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표 보기", notes = "시간표에 등록된 강의 목록을 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.GET)
    public ResponseEntity<TimeTableMap> getTimeTableWithLecture (@RequestParam Long timeTableId) throws Exception{
        return new ResponseEntity<TimeTableMap>(timetableService.getLectureListByTimeTableId(timeTableId), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표에 강의 추가", notes = "시간표에 강의를 추가할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.POST)
    public ResponseEntity createLectureOnTimeTable (@RequestBody TimeTable timeTable) throws Exception{
        timetableService.createLectureOnTimeTable(timeTable);
        return new ResponseEntity( new BaseResponse("강의가 정상적으로 추가되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "메인 시간표 보기", notes = "메인 시간표를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/main/lecture", method = RequestMethod.GET)
    public ResponseEntity<TimeTableMap> getMainTimeTable () throws Exception{
        return new ResponseEntity<TimeTableMap>(timetableService.getMainTimeTable(), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "메인 시간표 변경", notes = "메인 시간표를 변경할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/main/lecture", method = RequestMethod.PATCH)
    public ResponseEntity updateMainTimeTable(@RequestBody UserTimeTable userTimeTable) throws Exception{
        timetableService.updateMainTimeTable(userTimeTable);
        return new ResponseEntity( new BaseResponse("메인 시간표가 변경되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "강의 삭제", notes = "시간표에 등록된 강의를 삭제할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/lecture", method = RequestMethod.DELETE)
    public ResponseEntity deleteLectureOnTimeTable(@RequestBody TimeTable timeTable) throws Exception{
        timetableService.deleteLectureOnTimeTable(timeTable);
        return new ResponseEntity( new BaseResponse("해당 강의가 삭제되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "시간표에 강의 추가 직접 추가", notes = "시간표에 강의를 직접 만들어 추가할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/custom/lecture", method = RequestMethod.POST)
    public ResponseEntity createCustomLectureOnTimeTable (@RequestBody LectureTimeTable lectureTimeTable) throws Exception{
        timetableService.createCustomLectureOnTimeTable(lectureTimeTable);
        return new ResponseEntity( new BaseResponse("강의가 정상적으로 추가되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "코드를 이용하여 강의 추가", notes = "공유받은 코드로 강의를 추가할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/custom/code", method = RequestMethod.POST)
    public ResponseEntity createCustomLectureOnTimeTableByCode (@RequestBody CustomTimeTable customTimeTable) throws Exception{
        timetableService.createCustomLectureOnTableByCode(customTimeTable);
        return new ResponseEntity( new BaseResponse("강의가 정상적으로 추가되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "강의 찜", notes = "강의를 스크랩(찜)합니다", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/scrap", method = RequestMethod.POST)
    public ResponseEntity createScrapLecture(@RequestBody TimeTable timeTable) throws Exception{
        timetableService.createScrapLecture(timeTable);
        return new ResponseEntity( new BaseResponse("해당 강의를 정상적으로 찜 했습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "자신이 찜 한 강의 확인", notes = "자신이 스크랩(찜)한 강의를 확인할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/scrap", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<LectureTimeTable>> getScrapLectureList() throws Exception{
        return new ResponseEntity<ArrayList<LectureTimeTable>>(timetableService.getScrapLectureList(), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "자신이 찜 한 강의 삭제", notes = "자신이 스크랩(찜)한 강의를 삭제할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/timetable/scrap", method = RequestMethod.DELETE)
    public ResponseEntity deleteScrapLecture(@RequestBody TimeTable timeTable) throws Exception{
        timetableService.deleteScrapLecture(timeTable);
        return new ResponseEntity( new BaseResponse("해당 강의를 정상적으로 삭제했습니다.", HttpStatus.OK), HttpStatus.OK);
    }

}
