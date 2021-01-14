package in.hangang.mapper;

import in.hangang.domain.Hash_tag;
import in.hangang.domain.Lecture;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LectureMapper {
    ArrayList<Lecture> getLectureList(String keyword, ArrayList<String> classification, String department,
                                      ArrayList<Long> hashtag, String sort, int cursor, int limit);
    ArrayList<Hash_tag> getTop3HashTag(Long id);
    Long getLectureIdByReviewId(Long id);

}
