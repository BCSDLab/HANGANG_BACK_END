package in.hangang.mapper;

import in.hangang.domain.Assignment;
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
    ArrayList<Assignment> getAssignmentByReviewId(Long review_id);
    void deleteReviewById(Long id);
    void createReviewAssignment(Long review_id, Long assignment_id);
    Long getLectureIdByReviewId(Long lecture_id);
    void createLikesReview(Integer type, Long user_id, Long board_id);
}
