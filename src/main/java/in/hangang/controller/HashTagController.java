package in.hangang.controller;

import in.hangang.service.HashTagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HashTagController {

    @Resource
    HashTagService hashTagService;

    // 강의의 Top3 해시태그 수동 갱신
    // Scheduler 10분에 한번식 작동
    @RequestMapping(value = "/Top3HashTag", method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity testController() throws Exception{
        hashTagService.updateTop3HashTag();
        return new ResponseEntity(HttpStatus.OK);
    }
}
