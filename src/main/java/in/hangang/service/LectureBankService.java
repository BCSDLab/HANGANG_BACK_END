package in.hangang.service;

import in.hangang.domain.LectureBankCriteria;
import in.hangang.domain.LectureBank;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LectureBankService {
//    List<LectureBank> getLectureBanks(LectureBankCriteria lectureBankCriteria);
    List<LectureBank> searchLectureBanks(LectureBankCriteria lectureBankCriteria);
//    List<String> LectureBankFilesUpload(List<MultipartFile> fileList, Long id) throws IOException;
//    void createLectureBank(LectureBank lectureBank) throws Exception;
//    LectureBank getLectureBank(Long id);

}
