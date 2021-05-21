package in.hangang.service;

import in.hangang.domain.ClassTimeMap;
import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface LectureService {
    Map<String, Object> getLectureList(LectureCriteria lectureCriteria) throws Exception;
    Lecture getLecture(Long lectureId) throws Exception;
    void updateReviewCount();
    ArrayList<ClassTimeMap> getClassByLectureId(Long id) throws Exception;
    ArrayList<String> getSemesterDateByLectureId(Long id) throws Exception;
    void scrapLecture(Lecture lecture) throws Exception;
    ArrayList<Lecture> getScrapLectureList() throws Exception;
    void deleteScrapLecture(ArrayList<Long> lectureId) throws Exception;
}
