package in.hangang.serviceImpl;

import in.hangang.mapper.TotalEvaluationMapper;
import in.hangang.service.TotalEvaluationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TotalEvaluationServiceImpl implements TotalEvaluationService {

    @Resource
    TotalEvaluationMapper totalEvaluationMapper;

    @Override
    public void updateTotalRating() {
        totalEvaluationMapper.updateTotalRating();
    }
}
