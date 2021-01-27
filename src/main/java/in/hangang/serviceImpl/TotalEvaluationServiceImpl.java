package in.hangang.serviceImpl;

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
        if(lectureMapper.checkLectureExists(id)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        return totalEvaluationMapper.getTotalEvaluationByLectureId(id);
    }

    @Override
    public ArrayList<HashMap<String, String>> getRatingCountByLectureId(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(lectureMapper.checkLectureExists(id)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        return totalEvaluationMapper.getRatingCountByLectureId(id);
    }
}
