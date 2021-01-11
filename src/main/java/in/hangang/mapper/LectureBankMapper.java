package in.hangang.mapper;

import in.hangang.domain.LectureBank;
import in.hangang.domain.LectureBankCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LectureBankMapper {

    List<LectureBank> getLectureBankList(@Param("cursor")int cursor,
                                     @Param("limit")int limit,
                                     @Param("order") String order,
                                     @Param("category")ArrayList<String> category);
    List<LectureBank> findLectureBankByKeyword(@Param("cursor")int cursor,
                                               @Param("limit")int limit,
                                               @Param("order") String order,
                                               @Param("category")ArrayList<String> category,
                                               @Param("keyword")String keyword,
                                               @Param("department")String department);


    void createLectureBank(LectureBank lectureBank);
    void setUpload_file(String uploadUrl, Long id);
    LectureBank getLectureBank(Long id);

    List<LectureBankCategory> getCategoryList(Long id);




}
