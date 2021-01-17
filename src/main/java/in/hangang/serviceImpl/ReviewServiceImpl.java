package in.hangang.serviceImpl;

import in.hangang.domain.Review;
import in.hangang.domain.User;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.HashTagMapper;
import in.hangang.mapper.LectureMapper;
import in.hangang.mapper.LikesMapper;
import in.hangang.mapper.ReviewMapper;
import in.hangang.service.ReviewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;


@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private HashTagMapper hashtagMapper;

    @Resource
    private LikesMapper likesMapper;

    @Override
    public ArrayList<Review> getReviewList() throws Exception {
        return reviewMapper.getReviewList();
    }

    @Override
    public void createReview(Review review) throws Exception {
        reviewMapper.createReview(review);
        Long review_id = review.getReturn_id();
        Long lecture_id = review.getLecture_id();

        for(int i = 0; i<review.getAssignment().size(); i++){
            Long assignment_id = review.getAssignment().get(i).getId();
            reviewMapper.createReviewAssignment(review_id, assignment_id);
        }

        for(int i = 0; i<review.getHash_tags().size(); i++) {
            Long hash_tag_id = review.getHash_tags().get(i).getId();
            hashtagMapper.insertReviewHashtag(review_id, hash_tag_id);

            //hash_tag_count 테이블에 insert 하는데,
            //이미 해당 hash_tag_id가 존재한다면 count 1증가, 존재하지 않는다면 새로 만들어준다.
            if(hashtagMapper.getcountHashtag(0,  lecture_id, hash_tag_id)>0) {
                hashtagMapper.countUpHashtag(0, lecture_id, hash_tag_id);
            }
            else {
                hashtagMapper.insertHashtagCount(0, lecture_id, hash_tag_id);
            }
        }
        reviewMapper.update_reviewed_at(lecture_id);
    }

    @Override
    public Review getReview(Long id) throws Exception {
        return reviewMapper.getReviewById(id);
    }

    @Override
    public void createLikesReview(Long id) throws Exception {
        //FIXME: user_id 받아오는 것은 JWT이용
        Long user_id = 1L;
        if(likesMapper.checkIsLikedByUserId(user_id, id)==0) {
            reviewMapper.createLikesReview(0, user_id, id);
        }
        else {
            throw new RequestInputException(ErrorMessage.REVIEW_ALREADY_LIKED);
        }
    }

    @Override
    public void deleteReviewById(Long id) throws Exception {
        /*
        게시글이 삭제될 때
        1. review is_deleted를 1로 바꿔준다.
        2. total_rating UPDATE
        3. review_hash_tag에서 해쉬태그 삭제
        4. hash_tag_count에서 count--
         */
        Long lecture_id = reviewMapper.getLectureIdByReviewId(id);
        reviewMapper.deleteReviewById(id);
        reviewMapper.update_total_rating(lecture_id);
    }
}
