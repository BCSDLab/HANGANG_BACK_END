package in.hangang.mapper;

import in.hangang.domain.Hash_tag;

import java.util.ArrayList;

public interface HashTagMapper {
    void createReview_hash_tag(Long review_id, Long hash_tag_id);
    int countHash_tag(Integer type, Long lecture_id, Long hash_tag_id);
    void countUpHash_tag(Integer type, Long lecture_id, Long hash_tag_id);
    void createLecture_hash_tag(Integer type, Long lecture_id, Long hash_tag_id);
    ArrayList<Integer> getCount(Integer type, Long lecture_id);
    ArrayList<String> getHash_tag(Integer id1, Integer id2, Integer id3);
    ArrayList<Hash_tag> getHashTagByReviewId(Integer review_id);
    ArrayList<Hash_tag> getTop3HashTag(Long id);
    void insertTest(int lecture_id, int hash_tag_id, int count);
    void updateTop3HashTag();
}
