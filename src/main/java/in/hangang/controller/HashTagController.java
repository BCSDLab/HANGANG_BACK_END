package in.hangang.controller;

import in.hangang.service.HashTagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HashTagController {

    @Resource(name = "hashTagServiceImpl")
    private HashTagService hashTagService;

    @ApiOperation( value = "해시태그 순위 갱신", notes = "Top3 해시태그를 수동으로 갱신합니다. (강의에 노출되는 Top3 해시태그는 10분마다 자동으 갱신됩니다.)")
    @RequestMapping(value = "/hashtag", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateTop3HashTag() throws Exception{
        hashTagService.updateTop3HashTag();
        return new ResponseEntity<String>("refresh completed", HttpStatus.OK);
    }
}
