package in.hangang.mapper;

import in.hangang.domain.UserTimetable;

import java.util.ArrayList;

public interface TimetableMapper {
    ArrayList<UserTimetable> getTableListByUserId(Long user_id);
    void createTimetable(Long user_id, Long semester_date_id, String name);
    void deleteTimetable(Long timeTableId);
    void createLectureOnTimeTable(Long timeTableId, Long lectureId);
    ArrayList<String> getClassTimeByLectureId(Long lectureId);
    Long isAlreadyExists(Long timeTableId, Long lectureId);
    Long isExists(Long lectureId);
    Long getUserIdByTimeTableId(Long timeTableId);
    String getNameByTimeTableId(Long timeTableId);
    ArrayList<String> getClassTimeByTimeTable(Long timeTableId);
    Long getSemesterDateId(Long semesterDateId);
}
