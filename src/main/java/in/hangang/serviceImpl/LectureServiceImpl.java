package in.hangang.serviceImpl;

import in.hangang.domain.User;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureMapper;
import in.hangang.service.LectureService;
import in.hangang.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.ObjectUtils;

@Service("LectureServiceImpl")
public class LectureServiceImpl implements LectureService {

    @Resource
    private LectureMapper lectureMapper;

    @Qualifier("UserServiceImpl")
    @Resource
    private UserService userService;

    @Override
    public ArrayList<Lecture>
    getLectureList(LectureCriteria lectureCriteria) throws Exception {
        //User user = userService.getLoginUser();
        //Long userId = user.getId();
        String[] sortList = {"최신순", "평점순", "평가순"};
        if(lectureCriteria.getSort()!=null && !Arrays.asList(sortList).contains(lectureCriteria.getSort()))
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);

        if(lectureCriteria.getDepartment()!=null && lectureCriteria.getDepartment().size()>2)
            throw new RequestInputException(ErrorMessage.LECTURE_CRITERIA_LIMIT_DEPARTMENT);

        return lectureMapper.getLectureList(lectureCriteria);
    }

    @Override
    public void scrapLecture(Lecture lecture) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        if(lectureMapper.checkAlreadyScraped(userId, lecture.getId())==true)
            throw new RequestInputException(ErrorMessage.ALREADY_SCRAP_LECTURE);

        lectureMapper.scrapLecture(userId, lecture.getId());
    }

    @Override
    public void deleteScrapLecture(ArrayList<Long> lectureId) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        for(int i = 0; i< lectureId.size(); i++){
            if(lectureMapper.checkAlreadyScraped(userId, lectureId.get(i))==false)
                throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        }

        lectureMapper.deleteScrapLecture(userId, lectureId);
    }

    @Override
    public ArrayList<Lecture> getScrapLectureList() throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        return lectureMapper.getScrapLectureList(userId);
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
