package in.hangang.mapper;

import in.hangang.domain.Semester;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Map;

@Repository
public interface SemesterMapper {
    Semester getCurrentSemester(Long isRegular);
}
