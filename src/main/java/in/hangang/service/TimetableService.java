package in.hangang.service;

import in.hangang.domain.TimeTable;
import in.hangang.domain.UserTimetable;

import java.util.ArrayList;

public interface TimetableService {
    ArrayList<UserTimetable> getTableListByUserId() throws Exception;
    boolean createTimetable(UserTimetable userTimetable) throws Exception;
    void deleteTimetable(Long timeTableId) throws Exception;
    boolean createLectureOnTimeTable(Long timeTableId, Long lectureId) throws Exception;


}
