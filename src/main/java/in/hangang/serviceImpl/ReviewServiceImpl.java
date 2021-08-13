package in.hangang.serviceImpl;

import in.hangang.config.SlackNotiSender;
import in.hangang.domain.Lecture;
import in.hangang.domain.LectureTimeTable;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.Review;
import in.hangang.domain.User;
import in.hangang.domain.criteria.LectureCriteria;
import in.hangang.domain.slack.SlackAttachment;
import in.hangang.domain.slack.SlackParameter;
import in.hangang.domain.slack.SlackTarget;
import in.hangang.enums.ErrorMessage;
import in.hangang.enums.Point;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.*;
import in.hangang.service.ReviewService;
import in.hangang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    SlackNotiSender slackNotiSender;

    @Value("${report_slack_url}")
    private String notifyReportUrl;

    @Resource
    protected ReviewMapper reviewMapper;

    @Resource(name = "hashTagMapper")
    private HashTagMapper hashtagMapper;

    @Resource
    private LectureMapper lectureMapper;

    @Resource
    private LikesMapper likesMapper;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<Review> getReviewList(Criteria criteria) throws Exception {
        return reviewMapper.getReviewList(criteria, userService.getLoginUser());
    }

    @Override
    public Review getReview(Long id) throws Exception {
        if(id == null)
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        Review review = reviewMapper.getReviewById(id);
        if(review == null)
            throw new RequestInputException(ErrorMessage.REVIEW_NOT_EXIST);
        else
            return review;
    }

    @Override
    public ArrayList<Review> getReviewListByUserId() throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 있는지 확인.
        if(user == null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        return reviewMapper.getReviewListByUserId(userId);
    }

    @Override
    public Map<String, Object> getReviewByLectureId(Long id, LectureCriteria lectureCriteria) throws Exception {
        User user = userService.getLoginUser();
        //TODO : MAP 방식 아닌 DOMAIN 사용
        Map<String, Object> map = new HashMap<>();
        map.put("count", reviewMapper.getCountReviewByLectureId(id));
        map.put("result", reviewMapper.getReviewByLectureId(id, lectureCriteria, user));

        return map;
    }

    @Override
    @Transactional
    public Review createReview(Review review) throws Exception {

        //해당 강의가 존재하는지 확인.
        if(!lectureMapper.checkLectureExists(review.getLecture_id()))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        User user = userService.getLoginUser();
        Long lectureId = review.getLecture_id();

        //중복 작성 방지
        if(reviewMapper.getReviewByUserIdAndLectureId(review.getLecture_id(), user.getId())!= null)
            throw new RequestInputException(ErrorMessage.PROHIBITED_ATTEMPT);

        //작성자, 수강 학기 설정
        review.setUser_id(user.getId());
        review.setSemester_date(lectureMapper.getSemesterDateById(review.getSemester_id()));

        //리뷰 생성후 id 반환.
        review.setId(reviewMapper.createReview(review));


        reviewMapper.createReviewAssignment(review.getId(), review.getAssignment());
        hashtagMapper.insertReviewHashTag(review.getId(), review.getHash_tags());
        hashtagMapper.countUpHashTag(lectureId, review.getHash_tags());
        reviewMapper.updateReview(lectureId);

        //포인트 업데이트
        userMapper.addPointHistory(user.getId(), Point.LECTURE_REVIEW.getPoint(), Point.LECTURE_REVIEW.getTypeId());
        userMapper.addPoint(user.getId(), Point.LECTURE_REVIEW.getPoint());
        sendNoti(review);

        Review returnReview = reviewMapper.getReviewById(review.getId());
        returnReview.setMessage("리뷰가 정상적으로 작성되었습니다.");
        return returnReview;
    }

    @Override
    @Transactional
    public void likesReview(Review review) throws Exception {
        Long reviewId = review.getId();

        if(!reviewMapper.isExistsReview(reviewId))
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        User user = userService.getLoginUser();
        //유저 정보가 있는지 확인.
        if(user == null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);

        Long userId = user.getId();
        Long isLiked = likesMapper.checkIsLikedByUserId(userId, reviewId);

        if (isLiked == null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        if(isLiked == 0)
            likesMapper.createLikesReview(userId, reviewId);
        else if (isLiked == 1)
            likesMapper.deleteLikesReview(userId, reviewId);
        else //어떠한 이유로든 추천이 두번 이상 되었는지 확인.
            throw new RequestInputException(ErrorMessage.INVALID_RECOMMENDATION);
    }

    @Override
    public void sendNoti(Review review) throws Exception{

        SlackTarget slackTarget = new SlackTarget(notifyReportUrl,"");

        SlackParameter slackParameter = new SlackParameter();
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setTitle("강의평");
        slackAttachment.setAuthorName("한강 강의평");
        slackAttachment.setAuthorIcon("https://static.hangang.in/2021/05/30/49e7013f-458c-4f38-a681-b7ba03be0ca8-1622378903280.PNG");
        String message = String.format("강의평 id: %d 가  유저 %d 에 의해서 작성되었습니다.\n"
                ,review.getId(), review.getUser_id());
        message += String.format("작성된 내용\n===== [CONTENTS] ===== \n%s",review.getComment());
        slackAttachment.setText(message);
        slackParameter.getSlackAttachments().add(slackAttachment);
        slackNotiSender.send(slackTarget,slackParameter);
    }
}
