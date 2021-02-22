package in.hangang.serviceImpl;

import in.hangang.domain.Lecture;
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
        Long userId = user.getId();
        return timetableMapper.getTableListByUserId(userId);
    }

    @Override
    public boolean createTimetable(UserTimetable userTimetable) throws Exception {
        User user = userService.getLoginUser();
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        //TODO : 해당 학기가 존재하는지 확인
       timetableMapper.createTimetable(userId, userTimetable.getSemester_date_id(), userTimetable.getName());
        return true;
    }

    @Override
    public void deleteTimetable(Long timeTableId) throws Exception {
        User user = userService.getLoginUser();
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            //TODO : 아래 두개 예외처리 모두 에러 메세지 추가
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        if(timetableMapper.getUserIdByTimeTableId(timeTableId)!=userId)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        timetableMapper.deleteTimetable(timeTableId);
    }

    //TODO : 강의 시간이 겹치는 것을 어떻게 처리할지
    @Override
    public boolean createLectureOnTimeTable(Long timeTableId, Long lectureId) throws Exception {
        ArrayList<String> classTimeList = timetableMapper.getClassTimeList(timeTableId);
        String classTime = timetableMapper.getClassTime(lectureId);
        classTime = classTime.substring(1, classTime.length()-1);
        String[] classTimeArr = classTime.split(", ");

        return true;
    }
}
