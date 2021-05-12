package in.hangang.service;

import in.hangang.domain.*;
import in.hangang.domain.criteria.TimeTableCriteria;

import java.util.ArrayList;
import java.util.HashMap;

public interface TimetableService {
    ArrayList<LectureTimeTable> getLectureList(TimeTableCriteria timeTableCriteria) throws Exception;
    ArrayList<UserTimeTable> getTableListByUserId(Long semesterDateId) throws Exception;
    void createTimetable(UserTimeTable userTimetable) throws Exception;
    void updateTimeTable(UserTimeTable userTimeTable) throws Exception;
    void deleteTimetable(UserTimeTable userTimeTable) throws Exception;
    void createLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void deleteLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void createCustomLectureOnTimeTable(LectureTimeTable lectureTimeTable) throws Exception;
    void createCustomLectureOnTableByCode(CustomTimeTable customTimeTable) throws Exception;
    TimeTableMap getLectureListByTimeTableId(Long timeTableId) throws Exception;
    TimeTableMap getMainTimeTable() throws Exception;
    void updateMainTimeTable(UserTimeTable userTimeTable) throws Exception;
    void createScrapLecture(LectureTimeTable lectureTimeTable) throws Exception;
    ArrayList<LectureTimeTable> getScrapLectureList() throws Exception;
    void deleteScrapLecture(LectureTimeTable lectureTimeTable) throws Exception;
    void checkCrashClassTime(ArrayList<LectureTimeTable> originClass, ArrayList<LectureTimeTable> newClass) throws Exception;
}
