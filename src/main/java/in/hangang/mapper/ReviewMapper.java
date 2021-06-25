package in.hangang.mapper;

import in.hangang.domain.*;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.criteria.LectureCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ReviewMapper {
    List<Review> getReviewList(@Param("criteria") Criteria criteria, @Param("user") User user);
    ArrayList<Review> getReviewListByUserId(Long userId);
    Long createReview(Review review);
    void updateReview(Long id);
    Review getReviewById(Long id);
    ArrayList<Review> getReviewByLectureId(@Param("id") Long id, @Param("lectureCriteria") LectureCriteria lectureCriteria, @Param("user") User user);
    Long getCountReviewByLectureId(Long id);
    Long getReviewByUserIdAndLectureId(Long lecture_id, Long user_id);
    ArrayList<Assignment> getAssignmentByReviewId(Long review_id);
    void deleteReviewById(Long id);
    void createReviewAssignment(Long reviewId, @Param("assignment") List<Assignment> assignment);
    Long getLectureIdByReviewId(Long lecture_id);
    void createScrap(Long userId, Long reviewId);
    ArrayList<Review> getScrapReviewList(Long userId);
    Long getScrapCountByUserId(Long userId);
    void deleteScrapReview(Long userId, Long reviewId);
    Long isExistsReview(Long reviewId);
    Long isExistsScrap(Long userId, Long reviewId);
    Lecture getReviewByTimeTableLecture(Long lectureId);


}
