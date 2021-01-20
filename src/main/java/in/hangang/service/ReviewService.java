package in.hangang.service;

import in.hangang.criteria.Criteria;
import in.hangang.domain.Review;

import java.util.ArrayList;

public interface ReviewService {
    ArrayList<Review> getReviewList(Criteria criteria) throws Exception;
    Review getReview(Long id) throws Exception;
    ArrayList<Review> getReviewByLectureId(Long id, Criteria criteria) throws Exception;
    void createReview(Review review) throws Exception;
    void likesReview(Long id) throws Exception;
    ArrayList<String> getSemesterDateByLectureId(Long id) throws Exception;
}
