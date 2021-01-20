package in.hangang.mapper;

import in.hangang.domain.HashTag;
import in.hangang.domain.Lecture;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LectureMapper {
    ArrayList<Lecture> getLectureList(String keyword, ArrayList<String> classification, String department,
                                      ArrayList<Long> hashtag, String sort, int cursor, int limit);
    ArrayList<String> getSemesterDateByNameAndProfessor(String name, String professor);
    Long checkLectureExists(Long id);
    String getProfessorById(Long id);
    void updateTotalRatingById(Long id);
    String getNameById(Long id);
}
