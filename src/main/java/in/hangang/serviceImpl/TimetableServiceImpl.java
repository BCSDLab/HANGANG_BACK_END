package in.hangang.serviceImpl;
import com.mpatric.mp3agic.BaseException;
import in.hangang.domain.*;
import in.hangang.domain.criteria.TimeTableCriteria;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.TimetableMapper;
import in.hangang.service.TimetableService;
import in.hangang.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Resource
    TimetableMapper timetableMapper;

    @Resource
    @Qualifier("UserServiceImpl")
    UserService userService;

    @Override
    public ArrayList<LectureTimeTable> getLectureList(TimeTableCriteria timeTableCriteria) throws Exception {
        //학기 정보가 비어있으면 예외 처리
        if(timeTableCriteria.getSemesterDateId()==null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        return timetableMapper.getLectureList(timeTableCriteria);
    }

    @Override
    public ArrayList<UserTimeTable> getTableListByUserId(Long semesterDateId) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        return timetableMapper.getTableListByUserId(userId, semesterDateId);
    }

    @Override
    public void createTimetable(UserTimeTable userTimetable) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //입력된 학기가 존재하지 않을 경우 예외 처리
        if(timetableMapper.getSemesterDateId(userTimetable.getSemester_date_id())==null)
            throw new RequestInputException(ErrorMessage.INVALID_SEMESTER_DATE_EXCEPTION);

        //TODO : 나누기
        if(timetableMapper.getCountSemesterDate(userId, userTimetable.getSemester_date_id())>=5)
            throw new RequestInputException(ErrorMessage.TIME_TABLE_LIMIT_SEMESTER);
        if(timetableMapper.getCountTimeTable(userId)>=50)
            throw new RequestInputException(ErrorMessage.TIME_TABLE_LIMIT_TOTAL);

        Long timeTableId = timetableMapper.createTimetable(userId, userTimetable.getSemester_date_id(), userTimetable.getName());
        //메인으로 지정된 시간표가 없다면 메인 시간표로 지정
        if(timetableMapper.getMainTimeTableId(userId)==null)
            timetableMapper.assignMainTimeTable(timeTableId);
    }

    @Override
    public void updateTimeTable(UserTimeTable userTimeTable) throws Exception {
        Long timeTableId = userTimeTable.getId();
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //id가 입력되지 않은 경우 예외 처리
        if(userTimeTable.getId()==null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        //수정하고자 하는 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        //해당 시간표를 수정할 권한이 있는지 확인
        if(!timetableMapper.getUserIdByTimeTableId(timeTableId).equals(userId))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        timetableMapper.updateTimeTable(timeTableId, userTimeTable.getName());
    }

    @Override
    public void deleteTimetable(UserTimeTable userTimeTable) throws Exception {
        Long timeTableId = userTimeTable.getId();
        //id가 비어있다면 에러
        if(timeTableId == null)
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //삭제하고자 하는 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        //해당 시간표를 삭제할 권한이 있는지 확인
        if(!timetableMapper.getUserIdByTimeTableId(timeTableId).equals(userId))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        timetableMapper.deleteTimetable(timeTableId);
    }

    public TimeTableMap getMainTimeTable() throws Exception{
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        Long timeTableId = timetableMapper.getMainTimeTableId(userId);
        TimeTableMap timeTableMap = new TimeTableMap();
        timeTableMap = timetableMapper.getTableById(timeTableId);
        timeTableMap.setLectureList(timetableMapper.getLectureListByTimeTableId(timeTableId));

        return timeTableMap;
    }

    public void updateMainTimeTable(UserTimeTable userTimeTable) throws Exception {
        //id가 비어있다면 에러
        Long timeTableId = userTimeTable.getId();
        if(timeTableId == null)
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //지정하고자 하는 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        //해당 시간표를 지정할 권한이 있는지 확인
        if(!timetableMapper.getUserIdByTimeTableId(timeTableId).equals(userId))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        timetableMapper.updateMainTimeTable(userId, timeTableId);
    }


    @Override
    @Transactional
    public void createLectureOnTimeTable(TimeTable timeTable) throws Exception {
        Long lectureId = timeTable.getLecture_id();
        Long timeTableId = timeTable.getUser_timetable_id();
        //값이 하나라도 비어있다면 에러
        if(lectureId==null || timeTableId==null)
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);

        //해당 강의가 존재하는지 확인
        if(timetableMapper.isExists(lectureId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        //해당 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        //시간표의 학기 정보와 강의의 학기 정보가 일치하는지 확인
        if(!timetableMapper.getSemesterDateByLectureId(lectureId).equals(timetableMapper.getSemesterDateByTimeTableId(timeTableId)))
            throw new RequestInputException(ErrorMessage.NOT_MATCH_SEMESTER_DATE);

        ArrayList<LectureTimeTable> originClass = timetableMapper.getClassMapByTimeTable(timeTableId);
        ArrayList<LectureTimeTable> newClass = timetableMapper.getClassMapByLectureId(lectureId);

        checkCrashClassTime(originClass, newClass);
        timetableMapper.createLectureOnTimeTable(timeTableId, lectureId);
    }





    @Override
    public void deleteLectureOnTimeTable(TimeTable timeTable) throws Exception {
        Long timeTableId = timeTable.getUser_timetable_id();
        Long lectureId = timeTable.getLecture_id();
        //값이 하나라도 비어있다면 에러
        if(lectureId==null || timeTableId==null)
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //해당 테이블의 유저가 로그인 정보와 일치하는지 확인
        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(timeTableId)))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        if(timetableMapper.isAlreadyExists(timeTableId, lectureId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        //삭제 방식에 대한 고민
        timetableMapper.deleteLectureOnTimeTable(timeTableId, lectureId);
    }

    /*
    @Override
    @Transactional
    public void createCustomLectureOnTimeTable(LectureTimeTable lectureTimeTable) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        Long timeTableId = lectureTimeTable.getUser_timetable_id();
        ArrayList<Integer> timeListByTimeTable = getClassTimeArrayList(timetableMapper.getClassTimeByTimeTable(timeTableId));
        ArrayList<Integer> timeListByLecture = getClassTimeArrayList(new ArrayList<>(Collections.singletonList(lectureTimeTable.getClass_time())));
        for (Integer integer : timeListByLecture) {
            //시간이 중복되는지 확인
            if (timeListByTimeTable.contains(integer))
                throw new RequestInputException(ErrorMessage.TIME_LIST_CONFLICT);
        }
        String code = "";
        for(int i=0; i<10; i++){
            code = createRandomCode();
            if(timetableMapper.getLectureIdByCode(code)==null)
                break;
        }
        lectureTimeTable.setCode(code);
        lectureTimeTable.setIs_custom(true);
        timetableMapper.createLectureOnTimeTable(lectureTimeTable.getUser_timetable_id(), timetableMapper.createLecture(lectureTimeTable));;
    }
     */

    @Override
    @Transactional
    public void createCustomLectureOnTimeTable(LectureTimeTable lectureTimeTable) throws Exception {
        Long timeTableId = lectureTimeTable.getUser_timetable_id();
        String code = "";
        for(int i=0; i<10; i++){
            code = createRandomCode();
            if(timetableMapper.getLectureIdByCode(code)==null)
                break;
        }
        lectureTimeTable.setCode(code);
        lectureTimeTable.setIs_custom(true);

        ArrayList<LectureTimeTable> originClass = timetableMapper.getClassMapByTimeTable(timeTableId);
        ArrayList<LectureTimeTable> newClass = new ArrayList<LectureTimeTable>();
        newClass.add(lectureTimeTable);

        //충돌 확인
        checkCrashClassTime(originClass, newClass);

        //강의 DB에 추가
        Long lectureId = timetableMapper.createLecture(lectureTimeTable);
        //시간표에 강의 등록
        timetableMapper.createLectureOnTimeTable(timeTableId, lectureId);
    }

    @Override
    public void createCustomLectureOnTableByCode(CustomTimeTable customTimeTable) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(customTimeTable.getUser_timetable_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        Long timeTableId = customTimeTable.getUser_timetable_id();
        Long customLectureId = timetableMapper.getLectureIdByCode(customTimeTable.getCode());
        if (customLectureId==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        ArrayList<LectureTimeTable> originClass = timetableMapper.getClassMapByTimeTable(timeTableId);
        ArrayList<LectureTimeTable> newClass = timetableMapper.getClassMapByLectureId(customLectureId);
        checkCrashClassTime(originClass, newClass);

        timetableMapper.createLectureOnTimeTable(timeTableId, customLectureId);

    }



    @Override
    public TimeTableMap getLectureListByTimeTableId(Long timeTableId) throws Exception {
        if(timeTableId==null)
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //해당 테이블의 유저가 로그인 정보와 일치하는지 확인
        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(timeTableId)))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        //해당 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        TimeTableMap timeTableMap = new TimeTableMap();
        timeTableMap = timetableMapper.getTableById(timeTableId);
        timeTableMap.setLectureList(timetableMapper.getLectureListByTimeTableId(timeTableId));

        return timeTableMap;
    }

    @Override
    public void createScrapLecture(LectureTimeTable lectureTimeTable) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        if(lectureTimeTable.getLecture_id() == null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        if(timetableMapper.getScrapLectureByLectureId(userId, lectureTimeTable.getLecture_id()) != null)
            throw new RequestInputException(ErrorMessage.ALREADY_SCRAP_LECTURE);

        timetableMapper.createScrapLecture(userId, lectureTimeTable.getLecture_id());
    }

    @Override
    public ArrayList<LectureTimeTable> getScrapLectureList() throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        return timetableMapper.getScrapLectureList(userId);
    }

    @Override
    public void deleteScrapLecture(LectureTimeTable lectureTimeTable) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        if(lectureTimeTable.getLecture_id() == null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);

        timetableMapper.deleteScrapLecture(userId, lectureTimeTable.getLecture_id());
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




    //String으로 처리된 '강의시간' 정보들을 배열로 바꾸어준다
    public ArrayList<Integer> getClassTimeList(String classTime){
        ArrayList<Integer> classTimeArrayList = new ArrayList<Integer>();
        String classTimeSub = classTime.substring(1, classTime.length() - 1);
        String[] classTimeArr = classTimeSub.split(", ");
        for (String value : classTimeArr) {
            classTimeArrayList.add(Integer.parseInt(value));
        }
        return classTimeArrayList;
    }


    public String createRandomCode(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(8);
        for(int i=0; i<3; i++){
            char tmp = (char)((int)(Math.random()*25)+65);
            stringBuilder.append(tmp);
        }
        stringBuilder.append("-");
        int number = (int) (Math.random() * 9999) +1000;
        stringBuilder.append(number);

        return stringBuilder.toString();
    }

    @Override
    public void checkCrashClassTime(ArrayList<LectureTimeTable> originClass, ArrayList<LectureTimeTable> newClass) throws Exception {
        ArrayList<Integer> newClassTime = getClassTimeList(newClass.get(0).getClass_time());
        for(int i=0; i<originClass.size(); i++){
            ArrayList<Integer> classTime = getClassTimeList(originClass.get(i).getClass_time());
            for(int j=0; j<classTime.size(); j++){
                if(newClassTime.contains(classTime.get(j))) {
                    throw new BaseException(originClass.get(i).getId()+": "+
                            originClass.get(i).getName() +
                            "(" + originClass.get(i).getProfessor() + ")" +
                            " 강의와 강의 시간이 겹칩니다.");
                }
            }
        }
    }

    /*
    @Override
    public void createLectureOnTimeTable(TimeTable timeTable) throws Exception {
        Long lectureId = timeTable.getLecture_id();
        Long timeTableId = timeTable.getUser_timetable_id();
        //값이 하나라도 비어있다면 에러
        if(lectureId==null || timeTableId==null)
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);

        //해당 강의가 존재하는지 확인
        if(timetableMapper.isExists(lectureId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        //해당 강의가 이미 있는지 확인
        if(timetableMapper.isAlreadyExists(timeTableId, lectureId)!=null)
            throw new RequestInputException(ErrorMessage.TIME_LIST_CONFLICT);
        //해당 시간표가 존재하는지 확인
        if(timetableMapper.getNameByTimeTableId(timeTableId)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        //시간표의 학기 정보와 강의의 학기 정보가 일치하는지 확인
        if(!timetableMapper.getSemesterDateByLectureId(lectureId).equals(timetableMapper.getSemesterDateByTimeTableId(timeTableId)))
            throw new RequestInputException(ErrorMessage.NOT_MATCH_SEMESTER_DATE);

        //기존 시간표 시간 정보, 새로 넣을 강의의 시간 정보 가져오기
        ArrayList<Integer> timeListByTimeTable = getClassTimeArrayList(timetableMapper.getClassTimeByTimeTable(timeTableId));
        ArrayList<Integer> timeListByLecture = getClassTimeArrayList(timetableMapper.getClassTimeByLectureId(lectureId));
        HashMap<String, ArrayList<String>> timeHashMapByTimeTable = new HashMap<String, ArrayList<String>>();
        for (Integer integer : timeListByLecture) {
            //시간이 중복되는지 확인
            if (timeListByTimeTable.contains(integer))
                throw new RequestInputException(ErrorMessage.TIME_LIST_CONFLICT);
        }
        timetableMapper.createLectureOnTimeTable(timeTableId, lectureId);
    }
     */
}
