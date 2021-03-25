package in.hangang.mapper;

import in.hangang.domain.HashTag;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface LectureMapper {
    ArrayList<Lecture> getLectureList(LectureCriteria lectureCriteria);
    ArrayList<String> getSemesterDateByNameAndProfessor(String name, String professor);
    Long checkLectureExists(Long id);
    String getProfessorById(Long id);
    void updateTotalRatingById(Long id);
    void updateReviewCountById(Long id);
    void updateReviewCount();
    String getNameById(Long id);
    ArrayList<HashMap<String, String>> getClassByLectureId(Long lecture_id);
}
