package in.hangang.service;

import in.hangang.criteria.Criteria;
import in.hangang.domain.Lecture;

import java.util.ArrayList;


public interface LectureService {
    ArrayList<Lecture> getLectureList(String keyword, ArrayList<String> classification, String department,
                                      ArrayList<Long> hashtag, String sort, Criteria criteria) throws Exception;
    void updateReviewCount();
}
