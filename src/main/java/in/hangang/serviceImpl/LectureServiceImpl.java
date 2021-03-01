package in.hangang.serviceImpl;

import in.hangang.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureMapper;
import in.hangang.service.LectureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class LectureServiceImpl implements LectureService {

    @Resource
    private LectureMapper lectureMapper;

    @Override
    public ArrayList<Lecture>
    getLectureList(String keyword, ArrayList<String> classification, String department,
                   ArrayList<Long> hashtag, String sort, Criteria criteria) throws Exception {
        String[] sortList = {"최신순", "평점순", "평가순"};
        if(sort!=null && !Arrays.asList(sortList).contains(sort))
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);

        return lectureMapper.getLectureList(keyword, classification, department, hashtag, sort, criteria.getCursor(), criteria.getLimit());
    }

    @Override
    public void updateReviewCount() {
        lectureMapper.updateReviewCount();
    }
}
