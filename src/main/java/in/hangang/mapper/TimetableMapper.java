package in.hangang.mapper;

import in.hangang.domain.*;
import in.hangang.domain.criteria.TimeTableCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface TimetableMapper {
    LectureTimeTable getLectureByLectureId(Long lectureId);
    Long getCountLectureList(@Param("timeTableCriteria") TimeTableCriteria timeTableCriteria);
    ArrayList<LectureTimeTable> getLectureList(@Param("timeTableCriteria") TimeTableCriteria timeTableCriteria, @Param("user") User user);
    ArrayList<UserTimeTable> getTableListByUserId(Long userId, Long semesterDateId);
    TimeTableMap getTableById(Long id);
    Long createTimetable(Long user_id, Long semester_date_id, String name);
    void updateTimeTable(Long timeTableId, String name);
    void deleteTimetable(Long timeTableId);
    Long createLectureOnTimeTable(Long timeTableId, Long lectureId);
    void deleteLectureOnTimeTable(Long timeTableId, Long lectureId);
    ArrayList<String> getClassTimeByLectureId(Long lectureId);
    Long getSemesterDateByLectureId(Long lectureId);
    Long getSemesterDateByTimeTableId(Long timeTableId);
    Long getCountSemesterDate(Long userId, Long semesterDateId);
    Long getCountTimeTable(Long userId);
    Long isAlreadyExists(Long timeTableId, Long lectureId);
    Long isExists(Long lectureId);
    Long getUserIdByTimeTableId(Long timeTableId);
    ArrayList<LectureTimeTable> getLectureListByTimeTableId(Long timeTableId, Long userId);
    LectureTimeTable getLecture(Long timeTableId, Long lectureId);
    Long getLectureIdByLectureTimeTableId(Long id);
    String getNameByTimeTableId(Long timeTableId);
    ArrayList<String> getClassTimeByTimeTable(Long timeTableId);
    Long getSemesterDateId(Long semesterDateId);
    Long getLectureIdByCode(String code);
    Long createLecture(LectureTimeTable lectureTimeTable);
    void updateMainTimeTable(Long userId, Long timeTableId);
    void assignMainTimeTable(Long timeTableId);
    Long getMainTimeTableId(Long userId);
    void createScrapLecture(Long userId, Long lectureId);
    ArrayList<Lecture> getLectureByScrap(Long userId);
    ArrayList<LectureTimeTable> getScrapLectureList(Long userId);
    Long getScrapLectureByLectureId(Long userId, Long lectureId);
    void deleteScrapLecture(Long userId, Long lectureId);
    ArrayList<LectureTimeTable>getClassMapByTimeTable(Long timetableId);
    ArrayList<LectureTimeTable>getClassMapByLectureId(Long lectureId);
    void deleteLecture(Long lectureId);
    ArrayList<Long> getTimeTableIdByLecture(Long userId, Long lectureId);
    Long getLatestSemesterDateId();
    void createDefaultTimeTable(Long userId, Long semesterDateId);
}
