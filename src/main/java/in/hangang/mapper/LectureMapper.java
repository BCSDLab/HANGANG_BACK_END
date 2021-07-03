package in.hangang.mapper;

import in.hangang.domain.ClassTimeMap;
import in.hangang.domain.HashTag;
import in.hangang.domain.Lecture;
import in.hangang.domain.User;
import in.hangang.domain.criteria.LectureCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface LectureMapper {
    void scrapLecture(Long userId, Long lectureId);
    void deleteScrapLecture(Long userId, List<Long> lectureId);
    ArrayList<Lecture> getScrapLectureList(Long userId);
    Long checkAlreadyScraped(Long userId, Long lectureId);
    List<Lecture> getLectureList(@Param("lectureCriteria") LectureCriteria lectureCriteria, @Param("user") User user);
    Long getCountLectureList(@Param("lectureCriteria") LectureCriteria lectureCriteria);
    ArrayList<Long> getScrapLectureId(Long userId);
    ArrayList<Long> getSemesterDateIdByNameAndProfessor(String name, String professor);
    ArrayList<String> getSemesterDateByNameAndProfessor(String name, String professor);
    String getSemesterDateById(Long id);
    Boolean checkLectureExists(Long id);
    String getProfessorById(Long id);
    void updateTotalRatingById(Long id);
    void updateReviewCount();
    String getNameById(Long id);
    ArrayList<ClassTimeMap> getClassByLectureId(Long lecture_id);
    Lecture getLecture(Long id, @Param("user") User user);
}
