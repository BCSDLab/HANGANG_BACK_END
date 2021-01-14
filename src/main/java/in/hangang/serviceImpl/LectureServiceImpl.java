package in.hangang.serviceImpl;

import in.hangang.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.mapper.LectureMapper;
import in.hangang.service.LectureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class LectureServiceImpl implements LectureService {

    @Resource
    private LectureMapper lectureMapper;

    @Override
    public ArrayList<Lecture>
    getLectureList(String keyword, ArrayList<String> classification, ArrayList<Long> hashtag, String sort, Criteria criteria) throws Exception {
        return lectureMapper.getLectureList(keyword, classification, hashtag, sort, criteria.getCursor(), criteria.getLimit());
    }
}
