package in.hangang.service;

import in.hangang.domain.Review;

import java.util.ArrayList;

public interface ReviewService {
    ArrayList<Review> getReviewList() throws Exception;
    void createReview(Review review) throws Exception;
    Review getReview(Long id) throws Exception;
    void deleteReviewById(Long id) throws Exception;
    void timeTest() throws Exception;

}
