package in.hangang.service;

import in.hangang.domain.Lecture;
import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Review;

import java.util.ArrayList;
import java.util.HashMap;

public interface ReviewService {
    ArrayList<Review> getReviewList(Criteria criteria) throws Exception;
    Lecture getReviewByTimeTableLecture(Long lectureId) throws Exception;
    ArrayList<Review> getReviewListByUserId() throws Exception;
    Review getReview(Long id) throws Exception;
    ArrayList<Review> getReviewByLectureId(Long id, Criteria criteria) throws Exception;
    void createReview(Review review) throws Exception;
    void likesReview(Review review) throws Exception;
    void scrapReview(Review review) throws Exception;
    ArrayList<Review> getScrapReviewList() throws Exception;
    void deleteScrapReview(Review review) throws Exception;
    Long getCountScrapReview() throws Exception;

}
