package in.hangang.service;

import in.hangang.domain.Review;

import java.util.ArrayList;

public interface ReviewService {
    ArrayList<Review> getReviewList() throws Exception;
    Review getReview(Long id) throws Exception;
    ArrayList<Review> getReviewByLectureId(Long id) throws Exception;
    void createReview(Review review) throws Exception;
    void createLikesReview(Long id) throws Exception;
}
