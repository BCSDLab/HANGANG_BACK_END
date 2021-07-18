package in.hangang.controller;

import in.hangang.util.CloudFrontUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CloudFrontTestController {

    @Resource
    CloudFrontUtil cloudFrontUtil;

    @RequestMapping(value = "/cloudfront/url", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getTestUrl() throws Exception{
        return new ResponseEntity<String>(cloudFrontUtil.generateSignedUrl(), HttpStatus.OK);
    }

    @RequestMapping(value = "/cloudfront/cookie", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getCookie(HttpServletResponse response) throws Exception{
        cloudFrontUtil.generateSignedCookie(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
