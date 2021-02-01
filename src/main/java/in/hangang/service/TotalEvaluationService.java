package in.hangang.service;

import in.hangang.domain.Review;

import java.util.ArrayList;
import java.util.HashMap;

public interface TotalEvaluationService {
    Review getTotalEvaluation(Long id) throws Exception;
    ArrayList<HashMap<String, String>> getRatingCountByLectureId(Long id) throws Exception;
}
