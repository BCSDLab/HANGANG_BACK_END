package in.hangang.mapper;

import in.hangang.domain.*;
import in.hangang.domain.criteria.Criteria;
import in.hangang.domain.scrap.Scrap;
import in.hangang.domain.scrap.ScrapLectureBank;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LectureBankMapper {

    void deleteLectureBank(Long id, List<UploadFile> files, List<LectureBankComment> comments);
    List<LectureBank> getCount( @Param("lectureBankCriteria")LectureBankCriteria lectureBankCriteria);
    void insertS3Url(List<UploadFile> uploadFiles);
    String getExt(String url);
    Long postLectureBank(LectureBank lectureBank);
    void insertCategoryAndFiles(@Param("lectureBank")LectureBank lectureBank, Integer variance, Integer point_type_id);
    List<LectureBank> findLectureBankByKeyword(@Param("lectureBankCriteria")LectureBankCriteria lectureBankCriteria, @Param("user")User user);
    List<Long> getHits(Long id);
    LectureBank getLectureBankAll(Long id,  @Param("user")User user);
    Long is_writer(Long lectureBankId, Long userId);
    List<UploadFile> getFiles(Long id);
    void initLectureBank(@Param("files")List<UploadFile> files, @Param("lectureBankId") Long id);
    void updateLectureBank(LectureBank lectureBank);
    //List<LectureBankCategory> getCategoryList(Long id);
    LectureBank getLectureBank(@Param("id")Long id);
    Lecture getLectureInfo(@Param("id")Long id);
    void setLectureBank(@Param("lecture_bank_id")Long lecture_bank_id, @Param("lecture_id")Long lecture_id, @Param("title")String title, @Param("content") String content, @Param("point_price")Integer point_price, @Param("semester_date_id")Long semester_date_id);


    Long getWriterId(@Param("id")Long id);
    String getLatestSemester();
    Long getSemesterID(@Param("semester")String semester);

    //Category
    void addCategory(@Param("lecture_bank_id")Long lecture_bank_id, @Param("category")String category);
    void deleteCategory(@Param("id")Long id);
    List<Long> getCategoryIdList(@Param("lecture_bank_id")Long lecture_bank_id);
    void addMultiCategory(@Param("lecture_bank_id")Long lecture_bank_id, @Param("category_list")ArrayList<String> category_list);
    void deleteMultiCategory(@Param("id_list")ArrayList<Long> id_list);



    //comments
    Long getCommentCount(Long id);
    List<LectureBankComment> getComments(@Param("lecture_bank_id")Long lecture_bank_id, @Param("criteria") Criteria criteria);
    Long addComment(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id, @Param("comments")String comments);
    void setComment(@Param("id")Long id, @Param("comments")String comments);
    void deleteComment(@Param("id") Long id);
    Long getCommentWriterId(@Param("id") Long id);
    ArrayList<Long> getCommentIdList(@Param("lecture_bank_id")Long lecture_bank_id);
    void deleteMultiComment(@Param("id_list") ArrayList<Long> id_list);
    LectureBankComment getComment(@Param("id") Long id);


    //purchase
    Long checkPurchased(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void purchaseInsert(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void setPoint(@Param("user_id")Long user_id, @Param("point")Integer point);
    List<Long> getPurchaseId(@Param("lecture_bank_id")Long lecture_bank_id);
    void deletePurchase(@Param("id") Long id);
    void deleteMultiPurchase(@Param("id_list") ArrayList<Long> id_list);
    Integer getUserPoint(@Param("user_id")Long user_id);
    //<!--user_id, lecture_bank_id, point_price point_type_id_purchase, writer_id, point_type_id_sell-->
    void purchase(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id,
                  @Param("point_price")Integer point_price, @Param("writer_id")Long writer_id
            , @Param("point_type_id_purchase")Integer point_type_id_purchase
            , @Param("point_type_id_sell")Integer point_type_id_sell);



    //hits
    Long checkHits(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    Long checkHitExist(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void addHit(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void subHit(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void addHit_lecture_bank(@Param("lecture_bank_id")Long lecture_bank_id);
    void subHit_lecture_bank(@Param("lecture_bank_id")Long lecture_bank_id);
    void hitInsert(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void deleteHit(@Param("id") Long id);
    void deleteMultiHit(@Param("id_list") ArrayList<Long> id_list);
    List<Long> getHitId(@Param("lecture_bank_id")Long lecture_bank_id);
    Integer checkHitIsdeleted(@Param("id")Long id);
    List<LectureBank> getHitLectureBank(@Param("user")User user);
    List<LectureBank> getHitLectureBankN(@Param("user")User user, @Param("counts")Integer counts);


    //file
    void insertUpload_file(@Param("lecture_bank_id")Long lecture_bank_id, @Param("url")String url, @Param("filename")String filename, @Param("ext")String ext);
    Long getUploadFileId(@Param("lecture_bank_id")Long lecture_bank_id);
    List<Long> getFileIdList(@Param("lecture_bank_id")Long lecture_bank_id);
    void setFileAvailable(@Param("id")Long id, @Param("available")Integer available); //0->
    void setMultiFileAvailable_0(@Param("id_list")ArrayList<Long> id_list, @Param("available")Integer available);
    UploadFile getUrl(@Param("id")Long id);
    List<Long> getFileId(@Param("lecture_bank_id")Long lecture_bank_id);
    Long getLectureBankId_file(@Param("id")Long id);
    void deleteFile(@Param("id")Long id, @Param("available")Integer available); //1->
    void deleteMultiFile(@Param("id_list")ArrayList<Long> id_list, @Param("available")Integer available);
    void deleteMultiFile_UN(@Param("id_list")ArrayList<Long> id_list, @Param("available")Integer available);
    List<Long> getDelIDList();
    List<String> getDelObjectList();
    void hardDeleteFile(@Param("id")Long id);
    void hardDeleteMultiFile(@Param("id_list")ArrayList<Long> id_list);
    Long getLectureBankIDFile(@Param("upload_file_id")Long upload_file_id);
    String getFileExtofOne(@Param("lecture_bank_id")Long lecture_bank_id);
    List<Long> getUploadFileId_limit(@Param("lecture_bank_id")Long lecture_bank_id,@Param("limit")int limit);

    //else
    void addPointHistory(@Param("user_id")Long user_id, @Param("variance")Integer variance
            , @Param("point_type_id")Integer point_type_id);

    //scrap
    Long createScrap(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void unDeleteScrap(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    void deleteScrapList(@Param("id_list")List<Long> id_list);
    Long checkScrapDeleted(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    ArrayList<Boolean> checkScrapDeletedList(@Param("id_list")ArrayList<Long> id_list);
    Long getScrapID(@Param("user_id")Long user_id, @Param("lecture_bank_id")Long lecture_bank_id);
    Long checkScrapExist(@Param("id")Long id);
    ArrayList<ScrapLectureBank> getScrapLectureBankList(@Param("user_id")Long user_id);
    ArrayList<Scrap> checkScrapList(@Param("id_list")ArrayList<Long> id_list);
    List<Scrap> checkScrap(@Param("idList")List<Long > idList, @Param("user_id")Long user_id);
}
