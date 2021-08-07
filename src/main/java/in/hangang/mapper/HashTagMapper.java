package in.hangang.mapper;

import in.hangang.domain.HashTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface HashTagMapper {
    //void insertReviewHashTag(Long review_id, Long hash_tag_id);
    void insertReviewHashTag(Long reviewId, @Param("hashTags") List<HashTag> hashTags);
    void insertHashTagCount(Integer type, Long lecture_id, Long hash_tag_id);
    int getCountHashTag(Integer type, Long lecture_id, Long hash_tag_id);
    void countUpHashTag(Long lectureId, @Param("hashTags") List<HashTag> hashTags);
    void updateTop3HashTag();
    ArrayList<HashTag> getHashTagByReviewId(Integer reviewId);
    ArrayList<HashTag> getTop3HashTag(Long lecture_id);

}
