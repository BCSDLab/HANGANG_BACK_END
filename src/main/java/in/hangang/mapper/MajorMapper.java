package in.hangang.mapper;

import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.domain.criteria.TimeTableCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MajorMapper {
    ArrayList<String> getMajorId(@Param("Criteria") LectureCriteria lectureCriteria);
    ArrayList<String> getMajorId(@Param("Criteria") TimeTableCriteria timeTableCriteria);
}
