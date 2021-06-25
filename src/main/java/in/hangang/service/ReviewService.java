package in.hangang.service;

import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Review;
import in.hangang.domain.criteria.LectureCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    List<Review> getReviewList(Criteria criteria) throws Exception;
    List<Review> getReviewListByUserId() throws Exception;
    Review getReview(Long id) throws Exception;
    Map<String, Object> getReviewByLectureId(Long id, LectureCriteria lectureCriteria) throws Exception;
    void createReview(Review review) throws Exception;
    void likesReview(Review review) throws Exception;
    void sendNoti(Review review) throws Exception;
}
