package in.hangang.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface TotalEvaluationMapper {
    float getTotalRating(Long id);
    void updateTotalRating();
}
