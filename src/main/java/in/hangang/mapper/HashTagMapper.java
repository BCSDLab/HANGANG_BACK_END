package in.hangang.mapper;

import in.hangang.domain.Hash_tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface HashTagMapper {
    void insertReviewHashtag(Long review_id, Long hash_tag_id);
    void insertHashtagCount(Integer type, Long lecture_id, Long hash_tag_id);
    int getcountHashtag(Integer type, Long lecture_id, Long hash_tag_id);
    void countUpHashtag(Integer type, Long lecture_id, Long hash_tag_id);
    void updateTop3Hashtag();
    ArrayList<Hash_tag> getHashtagByReviewId(Integer review_id);
    ArrayList<Hash_tag> getTop3Hashtag(Long lecture_id);

}
