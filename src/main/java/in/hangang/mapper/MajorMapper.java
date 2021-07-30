package in.hangang.mapper;

import in.hangang.domain.criteria.LectureCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MajorMapper {
    ArrayList<String> getMajorFromId(@Param("lectureCriteria") LectureCriteria lectureCriteria);
}
