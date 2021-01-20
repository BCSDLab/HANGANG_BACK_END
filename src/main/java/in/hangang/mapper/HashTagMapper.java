package in.hangang.mapper;

import in.hangang.domain.HashTag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface HashTagMapper {
    void insertReviewHashTag(Long review_id, Long hash_tag_id);
    void insertHashTagCount(Integer type, Long lecture_id, Long hash_tag_id);
    int getCountHashTag(Integer type, Long lecture_id, Long hash_tag_id);
    void countUpHashTag(Integer type, Long lecture_id, Long hash_tag_id);
    void updateTop3HashTag();
    ArrayList<HashTag> getHashTagByReviewId(Integer review_id);
    ArrayList<HashTag> getTop3HashTag(Long lecture_id);

}
