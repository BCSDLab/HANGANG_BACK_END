package in.hangang.serviceImpl;

import in.hangang.domain.Review;
import in.hangang.mapper.HashTagMapper;
import in.hangang.mapper.LectureMapper;
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
    private LectureMapper lectureMapper;

    @Resource
    private HashTagMapper hashtagMapper;

    @Override
    public ArrayList<Review> getReviewList() throws Exception {
        return reviewMapper.getReviewList();
    }

    @Override
    public void createReview(Review review) throws Exception {
        reviewMapper.createReview(review);
        Long review_id = review.getReturn_id();
        Long lecture_id = review.getLecture_id();

        //JSON 객체를 이용하는 방식으로 바꿔야 할 것 같음
        for(int i = 0; i<review.getHash_tag().size(); i++) {
            Integer hash_tag_id = review.getHash_tag().get(i);
            hashtagMapper.createReview_hash_tag(review_id, hash_tag_id);
            if(hashtagMapper.countHash_tag(0,  lecture_id, hash_tag_id)>0) {
                hashtagMapper.countUpHash_tag(0, lecture_id, hash_tag_id);
            }
            else{
                hashtagMapper.createLecture_hash_tag(0, lecture_id, hash_tag_id);
            }
        }

        //mybatis 사용해보기
        reviewMapper.update_reviewed_at(lecture_id);
        reviewMapper.update_total_rating(lecture_id);
    }

    @Override
    public Review getReview(Long id) throws Exception {
        return reviewMapper.getReviewById(id);
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
        Long lecture_id = lectureMapper.getLectureIdByReviewId(id);
        reviewMapper.deleteReviewById(id);
        reviewMapper.update_total_rating(lecture_id);
    }

    @Override
    public void timeTest() throws Exception{
        for(int lecture_id = 1;  lecture_id<=2000; lecture_id++){
            for(int hash_tag_id = 1; hash_tag_id<=9; hash_tag_id++) {
                hashtagMapper.insertTest(lecture_id, hash_tag_id, hash_tag_id);
            }
        }
    }
}
