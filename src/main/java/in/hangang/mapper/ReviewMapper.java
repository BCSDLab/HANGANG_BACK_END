package in.hangang.mapper;

import in.hangang.domain.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ReviewMapper {
    ArrayList<Review> getReviewList();
    Long createReview(Review review);
    void update_reviewed_at(Long lecture_id);
    void update_total_rating(Long lecture_id);
    Review getReviewById(Long id);
    void deleteReviewById(Long id);
}
