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
    void deleteTimetable(TimeTable timeTable) throws Exception;
    void createLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void deleteLectureOnTimeTable(TimeTable timeTable) throws Exception;
    void createCustomLectureOnTimeTable(LectureTimeTable lectureTimeTable) throws Exception;
    void createCustomLectureOnTableByCode(CustomTimeTable customTimeTable) throws Exception;
    ArrayList<LectureTimeTable> getLectureListByTimeTableId(Long timeTableId) throws Exception;
    TimeTableMap getMainTimeTable() throws Exception;
    void updateMainTimeTable(TimeTable timeTable) throws Exception;


}
