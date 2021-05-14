package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.domain.*;
import in.hangang.domain.scrap.ScrapLectureBank;
import in.hangang.enums.Board;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.TimeTableException;
import in.hangang.response.BaseResponse;
import in.hangang.service.LectureBankService;
import in.hangang.service.ReportService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/lecture-banks")
public class LectureBankController {

    @Autowired
    @Qualifier("LectureBankServiceImpl")
    private LectureBankService lectureBankService;

    @Autowired
    private ReportService reportService;

    // 강의자료 MAIN------------------------------------------------------------------------------------
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 목록 가져오기" , notes = "강의자료 목록을 전체, 필터별로 가져올 수 있습니다."
            , authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity getSearchLectureBanks(@ModelAttribute("criteria") LectureBankCriteria lectureBankCriteria) throws Exception {
        return new ResponseEntity<List<LectureBank>>(lectureBankService.searchLectureBanks(lectureBankCriteria), HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 상세 페이지" , notes = "강의자료의  상세한 정보\n파라미터는 강의 자료 id 입니다."
            , authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<LectureBank> getLectureBank(@PathVariable Long id) throws Exception {
        return new ResponseEntity<LectureBank>(lectureBankService.getLectureBank(id), HttpStatus.OK);
    }


    @Auth
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 작성" , notes = "강의자료 작성)"
            +"\nsemester_date_id : 수강 학기 ID ( 1: 20191, 2: 20192, 3: 20201, 4: 20202, 5: 20211 )"
            +"" ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity postLectureBank(@RequestBody @Validated(ValidationGroups.PostLectureBank.class) LectureBank lectureBank) throws Exception {
        return new ResponseEntity( lectureBankService.postLectureBank(lectureBank), HttpStatus.OK);
    }


    @Auth
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value ="강의자료 수정" , notes = "강의 자료를 수정합니다\n파리미터는 강의자료 id 입니다",authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity modifyLectureBank(@RequestBody @Validated(ValidationGroups.PostLectureBank.class) LectureBank lectureBank , @PathVariable Long id ) throws Exception {
        return new ResponseEntity( lectureBankService.updateLectureBank(lectureBank,id), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value ="강의자료 작성 삭제" , notes = "강의 자료를 삭제합니다\n파리미터는 강의자료 id 입니다"
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity deleteLectureBank(@PathVariable Long id) throws Exception {
        lectureBankService.deleteLectureBank(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    //File------------------------------------------------------------------------------------



    @Auth
    @PostMapping("/file")
    @ApiOperation(value ="단일 파일 업로드" , notes = "파일을 1개 업로드 합니다.\n파라미터는 강의 자료 id 입니다.\n업로드된 파일의 id가 반환됩니다.",authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity uploadFile(@RequestBody MultipartFile multipartFile) throws Exception {
        return new ResponseEntity(lectureBankService.fileUpload(multipartFile), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/file/download/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="단일 파일 다운로드" , notes = "파일을 1개 다운로드 합니다.\n파라미터는 파일의 id 입니다.",authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity<String> getFile(@ApiParam(required = true) @PathVariable Long id) throws Exception {
        return new ResponseEntity<String>(lectureBankService.getObjectUrl(id),HttpStatus.OK);
    }

    //TODO file - 삭제, 수정시의 업로드?


    //구매------------------------------------------------------------------------------------


    @Auth
    @RequestMapping(value = "/purchase/check/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 구매 여부" , notes = "유저가 자료를 구매하였는지 확인힙니다\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<Boolean> checkPurchase(@PathVariable Long id) throws Exception{
        return new ResponseEntity<Boolean>(lectureBankService.checkPurchase(id), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 구매하기" , notes = "강의 자료를 구매합니다\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity purchase(@PathVariable Long id) throws Exception{
        lectureBankService.purchase(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //comment------------------------------------------------------------------------------------


    @RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 댓글 불러오기" , notes = "강의자료 댓글 전체 조회\n파라미터는 강의 자료 id 입니다.")
    public @ResponseBody
    ResponseEntity getComments(@PathVariable Long id) throws Exception{
        return new ResponseEntity<List<LectureBankComment>>(lectureBankService.getComments(id), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 댓글 작성" , notes = "강의자료 댓글을 입력합니다\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity addComment(@PathVariable Long id, @RequestParam(value = "comments") String comments) throws Exception{
        lectureBankService.addComment(id, comments);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Auth
    @RequestMapping(value = "/comment/modify/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value ="강의자료 댓글 수정" , notes = "강의자료 댓글을 수정합니다\n파라미터는 댓글 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity setComment(@PathVariable Long id, @RequestParam(value = "comments") String comments) throws Exception{
        lectureBankService.setComment(id, comments);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Auth
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value ="강의자료 댓글 삭제" , notes = "강의자료 댓글을 삭제합니다\n파라미터는 댓글 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity deleteComment(@PathVariable Long id) throws Exception{
        lectureBankService.deleteComment(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    //hit------------------------------------------------------------------------------------

    /*
    @Auth
    @RequestMapping(value = "/hit/check/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 hit 눌렀는지 체크" , notes = "유저가 hit를 눌렀는지 확인합니다\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<Boolean> checkHit(@PathVariable Long id) throws Exception{
        return new ResponseEntity<Boolean>(lectureBankService.checkHits(id), HttpStatus.OK);
    }

     */


    @Auth
    @RequestMapping(value = "/hit/push/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="hit 누르기" , notes = "hit를 누릅니다\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity pushHit(@PathVariable Long id) throws Exception{
        lectureBankService.pushHit(id);
        return new ResponseEntity(new BaseResponse("정상적으로 Hit 가 눌렸습니다", HttpStatus.OK),HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/hit/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="hit 누르기 w/ LectureBank RETURN" , notes = "hit를 누릅니다\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<LectureBank> pushHitLectureBank(@PathVariable Long id) throws Exception{
        LectureBank lb = lectureBankService.pushHitLectureBank(id);
        return new ResponseEntity<LectureBank>(lb,HttpStatus.OK);
    }


    // 신고하기------------------------------------------------------------------------------------
    @Auth
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 신고" , notes = "파라미터는 강의 자료 id 입니다\n REPORT 내용 별 id =>"
            +"1: \"욕설/비하\" ,2 : \"유출/사칭/저작권 위배\", 3: \"허위/부적절한 정보\"\n" +
            "    4: \"광고/도배\", 5: \"음란물\""
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity reportLectureBank(@RequestBody Report report) throws Exception {
        reportService.createReport(Board.LECTURE_BANK.getId(), report);
        return new ResponseEntity(new BaseResponse("정상적으로 신고되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/report/comment", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 댓글 신고" , notes = "파라미터는 강의 자료의 댓글 id 입니다\n REPORT 내용 별 id =>"
            +"1: \"욕설/비하\" ,2 : \"유출/사칭/저작권 위배\", 3: \"허위/부적절한 정보\"\n" +
            "    4: \"광고/도배\", 5: \"음란물\""
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity reportLectureBankCommment(@RequestBody Report report) throws Exception {
        reportService.createReport(Board.LECTURE_BANK_COMMENT.getId(), report);

        return new ResponseEntity(new BaseResponse("정상적으로 신고되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    // Scrap------------------------------------------------------------------------------------
    @Auth
    @RequestMapping(value = "/scrap", method = RequestMethod.POST)
    @ApiOperation(value ="scrap 하기" , notes = "강의자료를 스크랩합니다.\n파라미터는 강의 자료 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity createScrap(@ApiParam(required = true) @RequestParam(value = "id") Long id) throws Exception{
        lectureBankService.createScrap(id);
        return new ResponseEntity( new BaseResponse("강의자료가 정상적으로 스크랩되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/scrap", method = RequestMethod.DELETE)
    @ApiOperation(value ="scrap 취소하기" , notes = "스크랩된 강의자료들을 편집(삭제)합니다.\n파라미터는 스크랩한 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity deleteScrap(@ApiParam(required = true) @RequestParam Long[] lectureBank_IDList) throws Exception{
        List<Long> list = new ArrayList<>();
        for(Long id : lectureBank_IDList){
            list.add(id);
        }
        lectureBankService.deleteScrap((ArrayList<Long>)list);
        return new ResponseEntity( new BaseResponse("스크랩한 강의자료가 정상적으로 삭제되었습니다", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/scrap", method = RequestMethod.GET)
    @ApiOperation(value ="scrap 목록 가져오기" , notes = "사용자가 스크랩한 자료를 가져옵니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<List<ScrapLectureBank>> getScrap() throws Exception{
        return new ResponseEntity<List<ScrapLectureBank>>(lectureBankService.getScrapList(),HttpStatus.OK);
    }



}
