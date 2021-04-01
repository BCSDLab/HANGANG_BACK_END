package in.hangang.serviceImpl;

import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureMapper;
import in.hangang.service.LectureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class LectureServiceImpl implements LectureService {

    @Resource
    private LectureMapper lectureMapper;

    @Override
    public ArrayList<Lecture>
    getLectureList(LectureCriteria lectureCriteria) throws Exception {

        String[] sortList = {"최신순", "평점순", "평가순"};
        if(lectureCriteria.getSort()!=null && !Arrays.asList(sortList).contains(lectureCriteria.getSort()))
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);

        return lectureMapper.getLectureList(lectureCriteria);
    }

    @Override
    public void updateReviewCount() {
        lectureMapper.updateReviewCount();
    }

    @Override
    public ArrayList<HashMap<String, String>> getClassByLectureId(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(lectureMapper.checkLectureExists(id)==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        return lectureMapper.getClassByLectureId(id);
    }

    @Override
    public ArrayList<String> getSemesterDateByLectureId(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(lectureMapper.checkLectureExists(id)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        String name = lectureMapper.getNameById(id);
        String professor = lectureMapper.getProfessorById(id);

        return lectureMapper.getSemesterDateByNameAndProfessor(name, professor);
    }
}
