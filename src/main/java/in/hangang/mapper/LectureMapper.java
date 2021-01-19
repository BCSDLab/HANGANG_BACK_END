package in.hangang.mapper;

import in.hangang.domain.HashTag;
import in.hangang.domain.Lecture;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LectureMapper {
    ArrayList<Lecture> getLectureList(String keyword, ArrayList<String> classification, String department,
                                      ArrayList<Long> hashtag, String sort, int cursor, int limit);
}
