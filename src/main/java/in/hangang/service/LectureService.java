package in.hangang.service;

import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;

import java.util.ArrayList;


public interface LectureService {
    ArrayList<Lecture> getLectureList(
            //String keyword, ArrayList<String> classification, String department, ArrayList<Long> hashtag, String sort, Criteria criteria
            LectureCriteria lectureCriteria) throws Exception;
    void updateReviewCount();
}
