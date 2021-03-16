package in.hangang.mapper;

import in.hangang.domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LectureBankMapper {

    //get Lecture Main
    List<LectureBank> findLectureBankByKeyword(@Param("cursor")int cursor,
                                               @Param("limit")int limit,
                                               @Param("order") String order,
                                               @Param("category")ArrayList<String> category,
                                               @Param("keyword")String keyword,
                                               @Param("department")String department);

    List<LectureBankCategory> getCategoryList(Long id);
    LectureBank getLectureBank(@Param("id")Long id);
    Lecture getLectureInfo(@Param("id")Long id);
    Long getLectureBankId(@Param("user_id")Long user_id);
    void setLectureBank(@Param("lecture_bank_id")Long lecture_bank_id, @Param("lecture_id")Long lecture_id, @Param("title")String title, @Param("content") String content, @Param("point_price")Integer point_price);
    void createLectureBank(@Param("user_id")Long user_id);
    void deleteLectureBank(@Param("id")Long id, @Param("user_id")Long user_id);

    void addCategory(@Param("lecture_bank_id")Long lecture_bank_id, @Param("category")String category);
    void deleteCategory(@Param("id")Long id);
    List<Long> getCategoryIdList(@Param("lecture_bank_id")Long lecture_bank_id);

    //comments
    ArrayList<LectureBankComment> getComments(@Param("lecture_bank_id")Long lecture_bank_id);
    void addComment(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id, @Param("comments")String comments);
    void setComment(@Param("id")Long id, @Param("comments")String comments);
    void deleteComment(@Param("id") Long id);


    //purchase
    Integer checkPurchased(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void purchaseInsert(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void setPoint(@Param("user_id")Long user_id, @Param("point")Integer point);


    //hits
    Integer checkHits(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void addHit(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void subHit(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void addHit_lecture_bank(@Param("lecture_bank_id")Long lecture_bank_id);
    void subHit_lecture_bank(@Param("lecture_bank_id")Long lecture_bank_id);
    void hitInsert(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);


    //file
    void insertUpload_file(@Param("lecture_bank_id")Long lecture_bank_id, @Param("url")String url, @Param("filename")String filename, @Param("ext")String ext);
    List<Upload_File> getFileList(@Param("lecture_bank_id")Long lecture_bank_id);
    Long getUploadFileId(@Param("lecture_bank_id")Long lecture_bank_id);
    List<Long> getFileIdList(@Param("lecture_bank_id")Long lecture_bank_id);
    void setFileAvailable(@Param("id")Long id, @Param("available")Integer available);
    String getUrl(@Param("id")Long id);
    List<Long> getFileId(@Param("lecture_bank_id")Long lecture_bank_id);


}
