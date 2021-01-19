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
    public Review getReview(Long id) throws Exception {
        return reviewMapper.getReviewById(id);
    }

    @Override
    public ArrayList<Review> getReviewByLectureId(Long id) throws Exception {
        return reviewMapper.getReviewByLectureId(id);
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
            hashtagMapper.insertReviewHashTag(review_id, hash_tag_id);

            //hash_tag_count 테이블에 insert 하는데,
            //이미 해당 hash_tag_id가 존재한다면 count 1증가, 존재하지 않는다면 새로 만들어준다.
            if(hashtagMapper.getCountHashTag(0,  lecture_id, hash_tag_id)>0) {
                hashtagMapper.countUpHashTag(0, lecture_id, hash_tag_id);
            }
            else {
                hashtagMapper.insertHashTagCount(0, lecture_id, hash_tag_id);
            }
        }
        reviewMapper.updateReviewedAt(lecture_id);
    }

    @Override
    public void likesReview(Long id) throws Exception {
        //FIXME: user_id 받아오는 것은 JWT이용
        Long user_id = 1L;
        if(likesMapper.checkIsLikedByUserId(user_id, id)==0) {
            likesMapper.createLikesReview(0, user_id, id);
        }
        else {
            likesMapper.deleteLikesReview(0, user_id, id);
        }
    }
}
