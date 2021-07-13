package in.hangang.controller;

import in.hangang.util.CloudFrontUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class CloudFrontTestController {

    @Resource
    CloudFrontUtil cloudFrontUtil;

    @RequestMapping(value = "/cloudfront", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getTestUrl() throws Exception{
        return new ResponseEntity<String>(cloudFrontUtil.generateSignedUrl(), HttpStatus.OK);
    }
}
