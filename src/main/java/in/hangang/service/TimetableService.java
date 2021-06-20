package in.hangang.service;

import in.hangang.domain.*;
import in.hangang.domain.criteria.TimeTableCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface TimetableService {
    Map<String, Object> getLectureList(TimeTableCriteria timeTableCriteria) throws Exception;
    ArrayList<UserTimeTable> getTableListByUserId(Long semesterDateId) throws Exception;
    String createTimetable(UserTimeTable userTimetable) throws Exception;
    void updateTimeTable(UserTimeTable userTimeTable) throws Exception;
    void deleteTimetable(UserTimeTable userTimeTable) throws Exception;
    LectureTimeTable createLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void deleteLectureOnTimeTable(TimeTable timeTable) throws Exception;
    LectureTimeTable createCustomLectureOnTimeTable(LectureTimeTable lectureTimeTable) throws Exception;
    void createCustomLectureOnTableByCode(CustomTimeTable customTimeTable) throws Exception;
    TimeTableMap getLectureListByTimeTableId(Long timeTableId) throws Exception;
    TimeTableMap getMainTimeTable() throws Exception;
    void updateMainTimeTable(UserTimeTable userTimeTable) throws Exception;
    void createScrapLecture(TimeTable timeTable) throws Exception;
    ArrayList<LectureTimeTable> getScrapLectureList() throws Exception;
    void deleteScrapLecture(TimeTable timeTable) throws Exception;
    void checkCrashClassTime(ArrayList<LectureTimeTable> originClass, ArrayList<LectureTimeTable> newClass) throws Exception;
}
