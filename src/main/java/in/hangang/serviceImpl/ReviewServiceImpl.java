package in.hangang.serviceImpl;

import in.hangang.criteria.Criteria;
import in.hangang.domain.Review;
import in.hangang.domain.User;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.HashTagMapper;
import in.hangang.mapper.LectureMapper;
import in.hangang.mapper.LikesMapper;
import in.hangang.mapper.ReviewMapper;
import in.hangang.service.LectureService;
import in.hangang.service.ReviewService;
import in.hangang.service.UserService;
import io.swagger.models.auth.In;
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
    private LectureMapper lectureMapper;

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private UserService userService;

    @Override
    public ArrayList<Review> getReviewList(Criteria criteria) throws Exception {
        return reviewMapper.getReviewList(criteria.getCursor(), criteria.getLimit());
    }

    @Override
    public Review getReview(Long id) throws Exception {
        Review review = reviewMapper.getReviewById(id);
        if(review == null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        else
            return review;
    }

    @Override
    public ArrayList<Review> getReviewByLectureId(Long id, Criteria criteria) throws Exception {
        if(lectureMapper.checkLectureExists(id)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        return reviewMapper.getReviewByLectureId(id, criteria.getCursor(), criteria.getLimit());
    }

    @Override
    public void createReview(Review review) throws Exception {

        //해당 강의가 존재하는지 확인.
        if(lectureMapper.checkLectureExists(review.getLecture_id())==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        //유저 정보가 있는지 확인.
        User user = userService.getLoginUser();
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);

        //해당 유저가 동일한 강의에 리뷰를 작성한 적 있는지 확인.
        if(reviewMapper.getReviewByUserIdAndLectureId(review.getLecture_id(), user.getId())!=null)
            throw new RequestInputException(ErrorMessage.PROHIBITED_ATTEMPT);

        review.setUser_id(user.getId());

        ArrayList<String> semester = getSemesterDateByLectureId(review.getLecture_id());
        //입력된 학기 정보가 강의가 개설된 학기에 포함되어있늕지 확인.
        if(!semester.contains(review.getSemester_date()))
            throw new RequestInputException(ErrorMessage.INVALID_SEMESTER_DATE_EXCEPTION);

        //리뷰를 create후 작성된 id 반환.
        reviewMapper.createReview(review);
        Long reviewId = review.getReturn_id();
        Long lectureId = review.getLecture_id();

        for(int i = 0; i<review.getAssignment().size(); i++){
            Long assignment_id = review.getAssignment().get(i).getId();
            reviewMapper.createReviewAssignment(reviewId, assignment_id);
        }

        for(int i = 0; i<review.getHash_tags().size(); i++) {
            Long hashTagId = review.getHash_tags().get(i).getId();
            hashtagMapper.insertReviewHashTag(reviewId, hashTagId);

            //hash_tag_count 테이블에 insert 하는데,
            //이미 해당 hashTagId가 존재한다면 count 1증가, 존재하지 않는다면 새로 만들어준다.
            //type은 '작성', '검색'을 나눠놓기 위하여 만들었다.
            if(hashtagMapper.getCountHashTag(0,  lectureId, hashTagId)>0) {
                hashtagMapper.countUpHashTag(0, lectureId, hashTagId);
            }
            else {
                hashtagMapper.insertHashTagCount(0, lectureId, hashTagId);
            }
        }
        //TODO : 속도 향상을 위해 서비스 호출 줄여보기.
        reviewMapper.updateReviewedAt(lectureId);
        lectureMapper.updateReviewCountById(lectureId);
        lectureMapper.updateTotalRatingById(lectureId);
    }

    @Override
    public void likesReview(Long id) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 있는지 확인.
        if(user == null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);

        Long userId = user.getId();
        Long isLiked = likesMapper.checkIsLikedByUserId(userId, id);

        if(isLiked == 0)
            likesMapper.createLikesReview(0, userId, id);
        else if (isLiked == 1)
            likesMapper.deleteLikesReview(0, userId, id);
        //어떠한 이유로든 추천이 두번 이상 되었는지 확인.
        else
            throw new RequestInputException(ErrorMessage.INVALID_RECOMMENDATION);
    }

    @Override
    public ArrayList<String> getSemesterDateByLectureId(Long id) throws Exception {
        //해당 강의가 존재하는지 확인.
        if(lectureMapper.checkLectureExists(id)==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        String name = lectureMapper.getNameById(id);
        String professor = lectureMapper.getProfessorById(id);

        return lectureMapper.getSemesterDateByNameAndProfessor(name, professor);
    }
}
