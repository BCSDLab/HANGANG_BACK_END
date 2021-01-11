package in.hangang.controller;

import in.hangang.domain.LectureBankCriteria;
import in.hangang.domain.LectureBank;
import in.hangang.service.LectureBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class LectureBankController {

    @Autowired
    private LectureBankService lectureBankService;

//    @RequestMapping(value = "/lecture-banks", method = RequestMethod.GET)
//    public @ResponseBody
//    ResponseEntity getLectureBanks(@ModelAttribute("criteria") LectureBankCriteria lectureBankCriteria) {
//        return new ResponseEntity<List<LectureBank>>(lectureBankService.getLectureBanks(lectureBankCriteria), HttpStatus.OK);
//    }
/*
    @RequestMapping(value = "/lecture-banks/search", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getSearchLectureBanks(@ModelAttribute("criteria") LectureBankCriteria lectureBankCriteria) {
        return new ResponseEntity<List<LectureBank>>(lectureBankService.searchLectureBanks(lectureBankCriteria), HttpStatus.OK);
    }

 */

//    @RequestMapping(value = "/lecture-banks/files/upload", method = RequestMethod.POST)
//    public @ResponseBody
//    ResponseEntity getSearchLectureBanks(@RequestBody List<MultipartFile> files, @RequestParam("id")Long id) throws IOException {
//        return new ResponseEntity<List<String>>(lectureBankService.LectureBankFilesUpload(files, id), HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/lecture-bank", method = RequestMethod.POST)
//    public @ResponseBody
//    ResponseEntity createLectureBank(@RequestBody LectureBank lectureBank) throws Exception {
//        lectureBankService.createLectureBank(lectureBank);
//        return new ResponseEntity(HttpStatus.OK);
//    }



}
