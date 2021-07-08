package in.hangang.serviceImpl;

import in.hangang.domain.Semester;
import in.hangang.mapper.SemesterMapper;
import in.hangang.service.SemesterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Resource
    SemesterMapper semesterMapper;

    @Override
    public Semester getCurrentSemesterDate(Long isRegular) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return semesterMapper.getCurrentSemester(currentTime, isRegular);
    }
}
