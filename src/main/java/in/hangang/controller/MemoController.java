package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.Xss;
import in.hangang.domain.Memo;
import in.hangang.domain.UserTimeTable;
import in.hangang.response.BaseResponse;
import in.hangang.service.MemoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.transform.OutputKeys;
import java.util.ArrayList;

@RestController
public class MemoController {

    @Resource
    MemoService memoService;
    @Xss
    @Auth
    @ApiOperation( value = "메모 생성", notes = "시간표에 있는 강의에 메모를 추가할 수 있습니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/memo", method = RequestMethod.POST)
    public ResponseEntity createMemo(@RequestBody Memo memo) throws Exception{
        memoService.createMemo(memo);
        return new ResponseEntity( new BaseResponse("메모가 정상적으로 추가되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }
    @Auth
    @ApiOperation( value = "메모 읽기", notes = "시간표 강의에 등록된 메모를 읽어옵니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/memo", method = RequestMethod.GET)
    public ResponseEntity<Memo> getMemo(@RequestParam Long timeTableId) throws Exception{
        return new ResponseEntity<Memo>(memoService.getMemo(timeTableId), HttpStatus.OK);
    }
    @Xss
    @Auth
    @ApiOperation( value = "메모 수정", notes = "메모를 수정합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/memo", method = RequestMethod.PATCH)
    public ResponseEntity updateMemo(@RequestBody Memo memo) throws Exception{
        memoService.updateMemo(memo);
        return new ResponseEntity( new BaseResponse("메모가 정상적으로 수정됐습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "메모 삭제", notes = "메모를 삭제합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/memo", method = RequestMethod.DELETE)
    public ResponseEntity deleteMemo(@RequestBody Memo memo) throws Exception{
        memoService.deleteMemo(memo);
        return new ResponseEntity( new BaseResponse("메모가 정상적으로 삭제됐습니다.", HttpStatus.OK), HttpStatus.OK);
    }
}
