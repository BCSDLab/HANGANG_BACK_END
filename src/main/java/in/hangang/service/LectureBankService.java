package in.hangang.service;


import in.hangang.domain.*;
import in.hangang.domain.scrap.ScrapLectureBank;
import in.hangang.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface LectureBankService {
    //Main====================================================================================
    List<LectureBank> searchLectureBanks(LectureBankCriteria lectureBankCriteria) throws Exception;
    LectureBank getLectureBank(Long id) throws Exception;
    Lecture getLecture(Long id);
    //======
    BaseResponse postLectureBank(LectureBank lectureBank) throws Exception;
    void deleteLectureBank(Long id) throws Exception;
    void cancelLectureBank(Long id) throws Exception;
    Boolean checkWriter(Long lecture_bank_id) throws Exception;
    Boolean checkLectureBankAvailable(Long lecture_bank_id) throws Exception;


    //comment====================================================================================
    List<LectureBankComment> getComments(Long lecture_bank_id);
    void addComment(Long lecture_bank_id, String comments) throws Exception;
    void setComment(Long lecture_bank_comment_id, String comments) throws Exception;
    void deleteComment(Long lecture_bank_comment_id) throws Exception;
    Boolean checkCommentWriter(Long lecture_bank_comment_id)throws Exception;

    //purchase====================================================================================
    Boolean checkPurchase(Long lecture_bank_id) throws Exception;
    void purchase(Long lecture_bank_id) throws Exception;


    //hits====================================================================================
    //Boolean checkHits(Long lecture_bank_id) throws  Exception;
    void pushHit(Long lecture_bank_id) throws Exception;
    LectureBank pushHitLectureBank(Long lecture_bank_id) throws Exception;

    //file====================================================================================

    //UPLOAD
    String fileUpload(MultipartFile file) throws Exception;
    void hardDeleteFile() throws Exception;
    void deleteFile(Long id) throws Exception;
    //DOWNLOAD
    List<UploadFile> getFileList(Long lecture_bank_id) throws Exception;
    //org.springframework.core.io.Resource getprivateObject(Long id) throws Exception;
    String getObjectUrl(Long id) throws Exception;

    //Thumbnail====================================================================================
    String getThumbnailURL() throws Exception;

    //Scrap====================================================================================
    void createScrap(Long lecture_bank_id) throws Exception;
    void deleteScrap(ArrayList<Long> lectureBank_IDList) throws Exception;
    List<ScrapLectureBank> getScrapList() throws Exception;

}
