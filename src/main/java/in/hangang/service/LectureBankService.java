package in.hangang.service;


import in.hangang.domain.*;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.scrap.ScrapLectureBank;
import in.hangang.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LectureBankService {
    //Main====================================================================================
    Map<String, Object> searchLectureBanks(LectureBankCriteria lectureBankCriteria) throws Exception;
    LectureBank getLectureBank(Long id) throws Exception;
    BaseResponse postLectureBank(LectureBank lectureBank) throws Exception;
    BaseResponse updateLectureBank(LectureBank lectureBank, Long id) throws Exception;
    void deleteLectureBank(Long id) throws Exception;


    //comment====================================================================================
    Map<String,Object> getComments(Long lecture_bank_id , Criteria criteria);
    Long addComment(Long lecture_bank_id, String comments) throws Exception;
    BaseResponse setComment(Long lecture_bank_comment_id, Long commentId,String comments) throws Exception;
    BaseResponse deleteComment(Long lecture_bank_comment_id,Long commentId) throws Exception;

    //purchase====================================================================================
    Boolean checkPurchase(Long lecture_bank_id) throws Exception;
    void purchase(Long lecture_bank_id) throws Exception;


    //hits====================================================================================
    void pushHit(Long lecture_bank_id) throws Exception;

    //file====================================================================================

    //UPLOAD
    List<String> fileUpload(MultipartFile[] files) throws Exception;
    String getObjectUrl(Long id) throws Exception;

    //Thumbnail====================================================================================

    //Scrap====================================================================================
    Long createScrap(Long lecture_bank_id) throws Exception;
    void deleteScrap(List<Long>lectureBank_IDList) throws Exception;
    List<ScrapLectureBank> getScrapList() throws Exception;
    void sendLectureBankNoti(LectureBank lectureBank) throws Exception;
    void sendCommentNoti(Long id, Long commentId, Long userId, String comments) throws Exception;

}
