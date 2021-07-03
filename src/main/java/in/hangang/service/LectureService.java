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
    List<ClassTimeMap> getClassByLectureId(Long id) throws Exception;
    List<Long> getSemesterDateByLectureId(Long id) throws Exception;
    void scrapLecture(Lecture lecture) throws Exception;
    List<Lecture> getScrapLectureList() throws Exception;
    void deleteScrapLecture(List<Long> lectureId) throws Exception;
}
