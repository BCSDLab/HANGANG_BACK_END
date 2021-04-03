package in.hangang.service;

import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;

import java.util.ArrayList;
import java.util.HashMap;


public interface LectureService {
    ArrayList<Lecture> getLectureList(LectureCriteria lectureCriteria) throws Exception;
    void updateReviewCount();
    ArrayList<HashMap<String, String>> getClassByLectureId(Long id) throws Exception;
    ArrayList<String> getSemesterDateByLectureId(Long id) throws Exception;
    void scrapLecture(Lecture lecture) throws Exception;
    ArrayList<Lecture> getScrapLectureList() throws Exception;
    void deleteScrapLecture(Lecture lecture) throws Exception;
}
