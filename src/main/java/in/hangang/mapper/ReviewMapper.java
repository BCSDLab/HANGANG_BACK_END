package in.hangang.mapper;

import in.hangang.domain.Assignment;
import in.hangang.domain.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Repository
public interface ReviewMapper {
    ArrayList<Review> getReviewList(int cursor, int limit);
    Long createReview(Review review);
    void updateReviewedAt(Long lecture_id);
    void updateTotalRating(Long lecture_id);
    Review getReviewById(Long id);
    ArrayList<Review> getReviewByLectureId(Long id, int cursor, int limit);
    Long getReviewByUserIdAndLectureId(Long lecture_id, Long user_id);
    ArrayList<Assignment> getAssignmentByReviewId(Long review_id);
    void deleteReviewById(Long id);
    void createReviewAssignment(Long review_id, Long assignment_id);
    Long getLectureIdByReviewId(Long lecture_id);
    ArrayList<HashMap<String, String>> getClassByLectureId(Long lecture_id);

}
