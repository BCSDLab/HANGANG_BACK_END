package in.hangang.mapper;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LikesMapper {
    Long getLikesByReviewId(Long review_id);
    Long checkIsLikedByUserId(Long user_id, Long review_id);
    ArrayList<Long> getLikedReviewList(Long user_id);
    void createLikesReview(Long user_id, Long review_id);
    void deleteLikesReview(Long user_id, Long review_id);
}
