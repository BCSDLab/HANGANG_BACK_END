package in.hangang.service;

import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.TimeTable;
import in.hangang.domain.UserTimetable;

import java.sql.Time;
import java.util.ArrayList;

public interface TimetableService {
    ArrayList<UserTimetable> getTableListByUserId() throws Exception;
    void createTimetable(UserTimetable userTimetable) throws Exception;
    void deleteTimetable(Long timeTableId) throws Exception;
    void createLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void deleteLectureOnTimeTable(TimeTable timeTable, Long lectureId) throws Exception;
    ArrayList<LectureTimeTable> getLectureListByTimeTableId(Long timeTableId) throws Exception;


}
