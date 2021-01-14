package in.hangang.controller;

import in.hangang.criteria.Criteria;
import in.hangang.service.LectureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class LectureController {

    @Resource
    LectureService lectureService;

    @RequestMapping(value = "/lecture", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getReviewList(
            @ModelAttribute Criteria criteria,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "classification", required = false) ArrayList<String> classification,
            @RequestParam(value = "hash_tag", required = false) ArrayList<Long> hashtag,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "sort", required = false) String sort) throws Exception {
        return new ResponseEntity (lectureService.getLectureList(keyword, classification, department, hashtag, sort, criteria), HttpStatus.OK);
    }
}
