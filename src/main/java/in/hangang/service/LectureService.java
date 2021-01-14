package in.hangang.service;

import in.hangang.criteria.Criteria;
import in.hangang.domain.Lecture;

import java.util.ArrayList;


public interface LectureService {
    ArrayList<Lecture> getLectureList(String keyword, ArrayList<String> classification, ArrayList<Long> hashtag, String sort, Criteria criteria) throws Exception;
    //ArrayList<Lecture> getLectureListTest(ArrayList<String> classification, ArrayList<String> hash_tag, String sort) throws Exception;
}
