package in.hangang.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface LikesMapper {
    Long getLikesByReviewId(Long board_id);
}
