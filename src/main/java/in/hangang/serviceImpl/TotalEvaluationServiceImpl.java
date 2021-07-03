package in.hangang.serviceImpl;

import in.hangang.domain.Rating;
import in.hangang.domain.Review;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureMapper;
import in.hangang.mapper.TotalEvaluationMapper;
import in.hangang.service.TotalEvaluationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Service
public class TotalEvaluationServiceImpl implements TotalEvaluationService {

    @Resource
    TotalEvaluationMapper totalEvaluationMapper;

    @Resource
    LectureMapper lectureMapper;

    @Override
    public Review getTotalEvaluation(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(!lectureMapper.checkLectureExists(id))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        return totalEvaluationMapper.getTotalEvaluationByLectureId(id);
    }

    @Override
    public Integer[] getRatingCountByLectureId(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(!lectureMapper.checkLectureExists(id))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        Integer[] ratings = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ArrayList<Rating> ratingMap = totalEvaluationMapper.getRatingCountByLectureId(id);
        for(int i = 0; i< ratingMap.size(); i++){
            int index = (int)(ratingMap.get(i).getRating()/0.5)-1;
            ratings[index] = ratingMap.get(i).getCount();
        }
        return ratings;
    }
}
