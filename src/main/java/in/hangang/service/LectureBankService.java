package in.hangang.service;

import in.hangang.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LectureBankService {
    //Main
    List<LectureBank> searchLectureBanks(LectureBankCriteria lectureBankCriteria);
    LectureBank getLectureBank(Long id) throws Exception;
    Lecture getLecture(Long id);
    Long createLectureBank() throws Exception;
    void setLectureBank(LectureBank lectureBank) throws Exception;
    void submitLectureBank(LectureBank lectureBank) throws Exception;
    void deleteLectureBank(Long id) throws Exception;

    //file
    //List<String> getUrls(Long lecture_bank_id);
    List<Long> LectureBankFilesUpload(List<MultipartFile> fileList, Long id) throws IOException;
    Long fileUpload(MultipartFile file, Long id) throws IOException;
    List<UploadFile> getFileList(Long lecture_bank_id) throws Exception;
    org.springframework.core.io.Resource getprivateObject(Long id) throws Exception;
    void cancelUpload(Long id) throws Exception;

    //comment
    List<LectureBankComment> getComments(Long lecture_bank_id);
    void addComment(Long lecture_bank_id, String comments) throws Exception;
    void setComment(Long lecture_bank_comment_id, String comments) throws Exception;
    void deleteComment(Long lecture_bank_comment_id) throws Exception;

    //purchase
    Boolean checkPurchase(Long lecture_bank_id) throws Exception;
    void purchase(Long lecture_bank_id) throws Exception;


    //hits
    Boolean checkHits(Long lecture_bank_id) throws  Exception;
    void pushHit(Long lecture_bank_id) throws Exception;


}
