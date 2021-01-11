package in.hangang.serviceImpl;

import in.hangang.domain.LectureBankCategory;
import in.hangang.domain.LectureBankCriteria;
import in.hangang.domain.LectureBank;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.LectureBankMapper;
import in.hangang.service.LectureBankService;
import in.hangang.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureBankServiceImpl implements LectureBankService {

    @Autowired
    private LectureBankMapper lectureBankMapper;

    @Autowired
    private S3Util s3Util;

//    @Override
//    public List<LectureBank> getLectureBanks(LectureBankCriteria lectureBankCriteria) {
//        return lectureBankMapper.getLectureBankList(lectureBankCriteria.getCursor(), lectureBankCriteria.getLimit(), lectureBankCriteria.getOrder(), lectureBankCriteria.getCategory());
//    }

    @Override
    public List<LectureBank> searchLectureBanks(LectureBankCriteria lectureBankCriteria) {

        List<LectureBank> result =  lectureBankMapper.findLectureBankByKeyword(lectureBankCriteria.getCursor(),
                lectureBankCriteria.getLimit(),
                lectureBankCriteria.getOrder(), lectureBankCriteria.getCategory(),
                lectureBankCriteria.getKeyword(), lectureBankCriteria.getDepartment());

        for(int i=0; i<result.size(); i++){
            List<LectureBankCategory> categories = lectureBankMapper.getCategoryList(result.get(i).getId());
            ArrayList<String> categoryList = new ArrayList<>();
            for(int j=0; j<categories.size(); j++){
                categoryList.add(categories.get(j).getCategory());
            }
            result.get(i).setCategory(categoryList);
        }
        //강의자료의 카테고리를 lecture_bank_category테이블에서 가져와 넣어준다.
        return result;
    }

//    @Override
//    public List<String> LectureBankFilesUpload(List<MultipartFile> fileList, Long id) throws IOException {
//        List<String> result = new ArrayList<>();
//        String uploadPath = "/upload/lectureBank/";
//        if(!fileList.isEmpty()){
//            for(MultipartFile file : fileList){
//                String uploadUrl = s3Util.uploadObject(file, uploadPath);
//                result.add(uploadUrl);
//                lectureBankMapper.setUpload_file(uploadUrl, id);
//            }
//        }
//
//        return result;
//    }


}
