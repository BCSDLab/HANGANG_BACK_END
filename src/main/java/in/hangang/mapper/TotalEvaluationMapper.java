package in.hangang.mapper;

import in.hangang.domain.Review;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface TotalEvaluationMapper {
    float getTotalRating(Long id);
    //void updateTotalRating();
    Review getTotalEvaluationByLectureId(Long id);
    ArrayList<HashMap<String, String>> getRatingCountByLectureId(Long id);
}
