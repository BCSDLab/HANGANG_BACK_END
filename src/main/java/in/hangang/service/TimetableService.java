package in.hangang.service;

import in.hangang.domain.CustomTimeTable;
import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.TimeTable;
import in.hangang.domain.UserTimeTable;
import java.util.ArrayList;

public interface TimetableService {
    ArrayList<UserTimeTable> getTableListByUserId(Long semesterDateId) throws Exception;
    void createTimetable(UserTimeTable userTimetable) throws Exception;
    void updateTimeTable(UserTimeTable userTimeTable) throws Exception;
    void deleteTimetable(TimeTable timeTable) throws Exception;
    void createLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void deleteLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void createCustomLectureOnTimeTable(LectureTimeTable lectureTimeTable) throws Exception;
    void createCustomLectureOnTableByCode(CustomTimeTable customTimeTable) throws Exception;
    ArrayList<LectureTimeTable> getLectureListByTimeTableId(Long timeTableId) throws Exception;
    ArrayList<LectureTimeTable> getMainTimeTable() throws Exception;
    void updateMainTimeTable(TimeTable timeTable) throws Exception;


}
