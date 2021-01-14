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

    @RequestMapping(value = "/Top3HashTag", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity testController() throws Exception{
        hashTagService.updateTop3HashTag();
        return new ResponseEntity(HttpStatus.OK);
    }
}
