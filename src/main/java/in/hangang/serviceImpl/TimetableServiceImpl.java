package in.hangang.serviceImpl;
import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.TimeTable;
import in.hangang.domain.User;
import in.hangang.domain.UserTimetable;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.TimetableMapper;
import in.hangang.service.TimetableService;
import in.hangang.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Resource
    TimetableMapper timetableMapper;

    @Resource
    UserService userService;

    @Override
    public ArrayList<UserTimetable> getTableListByUserId() throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        return timetableMapper.getTableListByUserId(userId);
    }

    @Override
    public void createTimetable(UserTimetable userTimetable) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //입력된 학기가 존재하지 않을 경우 예외 처리
        if(timetableMapper.getSemesterDateId(userTimetable.getSemester_date_id())==null)
            throw new RequestInputException(ErrorMessage.INVALID_SEMESTER_DATE_EXCEPTION);

        timetableMapper.createTimetable(userId, userTimetable.getSemester_date_id(), userTimetable.getName());
    }

    @Override
    public void deleteTimetable(TimeTable timeTable) throws Exception {
        Long timeTableId = timeTable.getUser_timetable_id();
        //id가 비어있다면 에러
        if(timeTableId == null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //삭제하고자 하는 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        //해당 시간표를 삭제할 권한이 있는지 확인
       if(!timetableMapper.getUserIdByTimeTableId(timeTableId).equals(userId))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        timetableMapper.deleteTimetable(timeTableId);
    }

    @Override
    public void createLectureOnTimeTable(TimeTable timeTable) throws Exception {
        Long lectureId = timeTable.getLecture_id();
        Long timeTableId = timeTable.getUser_timetable_id();
        //값이 하나라도 비어있다면 에러
        if(lectureId==null || timeTableId==null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        //해당 강의가 존재하는지 확인
        if(timetableMapper.isExists(lectureId)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        //해당 강의가 이미 있는지 확인
        if(timetableMapper.isAlreadyExists(timeTableId, lectureId)!=null)
            throw new RequestInputException(ErrorMessage.TIME_LIST_CONFLICT);
        //해당 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        //시간표의 학기 정보와 강의의 학기 정보가 일치하는지 확인
        if(!timetableMapper.getSemesterDateByLectureId(lectureId).equals(timetableMapper.getSemesterDateByTimeTableId(timeTableId)))
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        //기존 시간표 시간 정보, 새로 넣을 강의의 시간 정보 가져오기
        ArrayList<Integer> timeListByTimeTable = getClassTimeArrayList(timetableMapper.getClassTimeByTimeTable(timeTableId));
        ArrayList<Integer> timeListByLecture = getClassTimeArrayList(timetableMapper.getClassTimeByLectureId(lectureId));
        for (Integer integer : timeListByLecture) {
            //시간이 중복되는지 확인
            if (timeListByTimeTable.contains(integer))
                throw new RequestInputException(ErrorMessage.TIME_LIST_CONFLICT);
        }
        timetableMapper.createLectureOnTimeTable(timeTableId, lectureId);
    }

    @Override
    public void deleteLectureOnTimeTable(TimeTable timeTable) throws Exception {
        Long timeTableId = timeTable.getUser_timetable_id();
        Long lectureId = timeTable.getLecture_id();
        //값이 하나라도 비어있다면 에러
        if(lectureId==null || timeTableId==null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //해당 테이블의 유저가 로그인 정보와 일치하는지 확인
        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(timeTableId)))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        timetableMapper.deleteLectureOnTimeTable(timeTableId, lectureId);
    }

    @Override
    public ArrayList<LectureTimeTable> getLectureListByTimeTableId(Long timeTableId) throws Exception {
        return timetableMapper.getLectureListByTimeTableId(timeTableId);
    }


    //String으로 처리된 '강의시간' 정보들을 배열로 바꾸어준다
    public ArrayList<Integer> getClassTimeArrayList(ArrayList<String> classTimeList){
        ArrayList<Integer> classTimeArrayList = new ArrayList<Integer>();
        for (String s : classTimeList) {
            String classTime = s.substring(1, s.length() - 1);
            String[] classTimeArr = classTime.split(", ");
            for (String value : classTimeArr) {
                classTimeArrayList.add(Integer.parseInt(value));
            }
        }
        return classTimeArrayList;
    }
}
