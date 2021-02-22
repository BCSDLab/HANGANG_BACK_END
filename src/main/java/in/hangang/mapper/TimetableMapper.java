package in.hangang.mapper;

import in.hangang.domain.UserTimetable;

import java.util.ArrayList;

public interface TimetableMapper {
    ArrayList<UserTimetable> getTableListByUserId(Long user_id);
    void createTimetable(Long user_id, Long semester_date_id, String name);
    void deleteTimetable(Long timeTableId);
    String getClassTime(Long lectureId);
    Long getUserIdByTimeTableId(Long timeTableId);
    String getNameByTimeTableId(Long timeTableId);
    ArrayList<String> getClassTimeList(Long timeTableId);
}
