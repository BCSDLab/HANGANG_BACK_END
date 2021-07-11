package in.hangang.serviceImpl;

import in.hangang.domain.ClassTimeMap;
import in.hangang.domain.User;
import in.hangang.domain.Lecture;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureMapper;
import in.hangang.mapper.TimetableMapper;
import in.hangang.service.LectureService;
import in.hangang.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class LectureServiceImpl implements LectureService {

    @Resource
    private LectureMapper lectureMapper;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource
    private TimetableMapper timetableMapper;

    @Override
    public Map<String, Object> getLectureList(LectureCriteria lectureCriteria) throws Exception {
        String[] sortList = {"최신순", "평점순", "평가순"};
        // 정렬 기준 검사
        if(lectureCriteria.getSort()!=null && !Arrays.asList(sortList).contains(lectureCriteria.getSort()))
            throw new RequestInputException(ErrorMessage.VALIDATION_FAIL_EXCEPTION);

        // 학부 검색 검사 ( 최대 2개까지 선택 )
        if(lectureCriteria.getDepartment()!=null && lectureCriteria.getDepartment().size()>2)
            throw new RequestInputException(ErrorMessage.LECTURE_CRITERIA_LIMIT_DEPARTMENT);

        Map<String, Object> map = new HashMap<>();
        map.put("count", lectureMapper.getCountLectureList(lectureCriteria));
        map.put("result", lectureMapper.getLectureList(lectureCriteria, userService.getLoginUser()));

        return map;
    }

    @Override
    public Lecture getLecture(Long lectureId) throws Exception {
        if(!lectureMapper.checkLectureExists(lectureId))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        return lectureMapper.getLecture(lectureId, userService.getLoginUser());
    }

    @Override
    public void scrapLecture(Lecture lecture) throws Exception {
        Long userId = userService.getLoginUser().getId();

        if(!lectureMapper.checkLectureExists(lecture.getId()))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        if(lectureMapper.checkAlreadyScraped(userId, lecture.getId())!=0)
            throw new RequestInputException(ErrorMessage.ALREADY_SCRAP_LECTURE);

        lectureMapper.scrapLecture(userId, lecture.getId());
    }

    @Override
    public void deleteScrapLecture(List<Long> lectureId) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        for(int i = 0; i< lectureId.size(); i++){
            if(lectureMapper.checkAlreadyScraped(userId, lectureId.get(i))==0)
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
    public ArrayList<ClassTimeMap> getClassByLectureId(Long id) throws Exception {
        User user = userService.getLoginUser();
        Long userId = user.getId();
        //해당 강의가 존재하는지 확인.
        if(!lectureMapper.checkLectureExists(id))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        ArrayList<ClassTimeMap> resultList = lectureMapper.getClassByLectureId(id);
        for(int i = 0; i<resultList.size(); i++){
            resultList.get(i).setSelectedTableId(timetableMapper.getTimeTableIdByLecture(userId, resultList.get(i).getId()));
        }
        return resultList;
    }

    @Override
    public ArrayList<Long> getSemesterDateByLectureId(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(!lectureMapper.checkLectureExists(id))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        String name = lectureMapper.getNameById(id);
        String professor = lectureMapper.getProfessorById(id);

        return lectureMapper.getSemesterDateIdByNameAndProfessor(name, professor);
    }
}
