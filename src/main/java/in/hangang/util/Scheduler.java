package in.hangang.util;

import in.hangang.mapper.LectureMapper;
import in.hangang.service.HashTagService;
import in.hangang.service.LectureBankService;
import in.hangang.service.LectureService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Scheduler {

    @Resource
    private HashTagService hashTagService;

    @Resource
    @Qualifier("LectureServiceImpl")
    private LectureService lectureService;

    @Resource
    @Qualifier("LectureBankServiceImpl")
    private LectureBankService lectureBankService;

    /**
    1초에 한번씩 호출하는 fixedDelay
     */
    //@Scheduled(fixedDelay = 1000)
    public void scheduleFixedRateTask() {
        System.out.println("Current Thread : "+ Thread.currentThread().getName());
    }
    /**
     1초에 한번씩 호출하는 cron expression

     그 전에crontab 주기설정 방법부터 알아보자.

     *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     초(0-59)   분(0-5)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     각 별 위치에 따라 주기를 다르게 설정 할 수 있다.
     순서대로 초-분-시간-일-월-요일 순이다. 그리고 괄호 안의 숫자 범위 내로 별 대신 입력 할 수도 있다.
     요일에서 0과 7은 일요일이며, 1부터 월요일이고 6이 토요일이다.
     */

    @Scheduled(cron = "0 */10 * * * *")
    public void updateTop3HashTag() {
        hashTagService.updateTop3HashTag();
    }

    @Scheduled(cron = "0 0 0 */1 * *")
    public void updateReviewCount() {lectureService.updateReviewCount();}

    @Scheduled(cron = "0 0 0 ? * 6")
    public void cleanUnavailableFiles() throws Exception {lectureBankService.hardDeleteFile();}
}
