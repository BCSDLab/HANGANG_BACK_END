package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.domain.*;
import in.hangang.response.BaseResponse;
import in.hangang.service.LectureBankService;
import in.hangang.service.ReportService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/lecture-banks")
public class LectureBankController {

    @Autowired
    @Qualifier("LectureBankServiceImpl")
    private LectureBankService lectureBankService;


    // 강의자료 MAIN------------------------------------------------------------------------------------
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 목록 가져오기" , notes = "강의자료 목록을 전체, 필터별로 가져올 수 있습니다."
            , authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity getSearchLectureBanks(@ModelAttribute("criteria") LectureBankCriteria lectureBankCriteria) throws Exception {
        //TODO THUMBNAIL URL 추가****************************************************************************************
        return new ResponseEntity<List<LectureBank>>(lectureBankService.searchLectureBanks(lectureBankCriteria), HttpStatus.OK);
    }


    @RequestMapping(value = "/main/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 상세 페이지" , notes = "강의자료의  상세한 정보\n파라미터는 강의 자료 id 입니다."
            , authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<LectureBank> getLectureBank(@PathVariable Long id) throws Exception {
        //TODO THUMBNAIL URL 추가****************************************************************************************
        return new ResponseEntity<LectureBank>(lectureBankService.getLectureBank(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/main/file/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 파일 목록" , notes = "강의자료록에 해당하는 파일의 목록만을 가져옵니다(파일이름, 확장자)\n파라미터는 강의 자료 id 입니다.")
    public @ResponseBody
    ResponseEntity<List<UploadFile>> getFileList(@PathVariable Long id) throws Exception {
        return new ResponseEntity<List<UploadFile>>(lectureBankService.getFileList(id),HttpStatus.OK);
    }


    //강의자료 CUD------------------------------------------------------------------------------------
    /*
    사용 로직
    write(GET) 작성시작시 lecturebank_id 반환 -> 파일 업로드 시 해당 파일과 함께 lecturebank_id 입력 -> /write POST로 강의자료 내용 전송
     */

    //OMG NO semester on LectureBank ()
    //**add semester to LectureBank*******************************************************
    @Auth
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    @ApiOperation(value ="강의자료 작성하기" , notes = "작성하기를 누르면 lecturebank id가 생성되어 반환됩니다"
            , authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<Long> createLectureBank() throws Exception {
        return new ResponseEntity<Long>(lectureBankService.createLectureBank(), HttpStatus.CREATED);
    }

    @Auth
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 작성완료" , notes = "id를 포함하여 작성한 강의 자료를 전송합니다(/write에서 받은 id 입력)"
            +"\nsemester_date_id : 수강 학기 ID ( 1: 20191, 2: 20192, 3: 20201, 4: 20202, 5: 20211 )"
            +"" ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity submitLectureBank(@RequestBody LectureBank lectureBank) throws Exception {
        lectureBankService.submitLectureBank(lectureBank);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Auth
    @RequestMapping(value = "/modify", method = RequestMethod.PATCH)
    @ApiOperation(value ="강의자료 수정" , notes = "강의 자료를 수정합니다\n파리미터는 강의자료 id 입니다"
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity modifyLectureBank(@RequestBody LectureBank lectureBank) throws Exception {
        lectureBankService.setLectureBank(lectureBank);
        return new ResponseEntity(HttpStatus.OK);
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

    @Auth
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value ="강의자료 작성 취소" , notes = "강의 자료를 삭제합니다\n파리미터는 강의자료 id 입니다"
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity cancelLectureBank(@PathVariable Long id) throws Exception {
        lectureBankService.cancelLectureBank(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    //File------------------------------------------------------------------------------------


    @Auth
    @RequestMapping(value = "/file/upload/{id}", method = RequestMethod.POST)
    @ApiOperation(value ="단일 파일 업로드" , notes = "파일을 1개 업로드 합니다.\n파라미터는 강의 자료 id 입니다.\n업로드된 파일의 id가 반환됩니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<Long> uploadFile(@ApiParam(required = true) @RequestBody MultipartFile file,
                                    @PathVariable Long id) throws Exception {
        return new ResponseEntity<Long>(lectureBankService.fileUpload(file, id),HttpStatus.CREATED);
    }

    @Auth
    @ApiImplicitParams(
            @ApiImplicitParam(name = "files", required = true, dataType = "__file", paramType = "form")
    )
    @RequestMapping(value = "/files/upload/{id}", method = RequestMethod.POST)
    @ApiOperation(value ="다중 파일 업로드" , notes = "여러개의 파일을 업로드 합니다.\n파라미터는 강의 자료 id 입니다.\n업로드된 파일의 id 목록이 반환됩니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity<List<Long>> uploadFiles(@ApiParam(name = "files") @RequestParam(value = "files",
            required = true) MultipartFile[] files, @PathVariable Long id) throws Exception {
        List<MultipartFile> list = new ArrayList<>();
        for(MultipartFile file : files){
            list.add(file);
        }
        return new ResponseEntity<List<Long>>(lectureBankService.LectureBankFilesUpload(list, id), HttpStatus.CREATED);
    }



    //upload_file table - available FLAG 0:업로드 대기 /  1: 업로드 완료 /  2: 삭제
    @Auth
    @RequestMapping(value = "/file/cancel_upload/{id}", method = RequestMethod.POST)
    @ApiOperation(value ="업로드 취소" , notes = "파일 업로드가 취소 됩니다\n해당파일을 제외하고 업로드 할 시 사용합니다\n파라미터는 파일의 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity cancelUpload(@PathVariable Long id) throws Exception {
        lectureBankService.cancelUpload(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Auth
    @RequestMapping(value = "/file/download/{id}", method = RequestMethod.GET)
    @ApiOperation(value ="단일 파일 다운로드" , notes = "파일을 1개 다운로드 합니다.\n파라미터는 파일의 id 입니다."
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity<String> getFile(@ApiParam(required = true) @PathVariable Long id) throws Exception {
        return new ResponseEntity<String>(lectureBankService.getObjectUrl(id),HttpStatus.OK);
    }

    //TODO file - 삭제, 수정시의 업로드


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
    public @ResponseBody
    ResponseEntity deleteComment(@PathVariable Long id) throws Exception{
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
        return new ResponseEntity(HttpStatus.OK);
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
        lectureBankService.reportLectureBank(report);
        return new ResponseEntity(new BaseResponse("정상적으로 신고되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Auth
    @RequestMapping(value = "/report/comment", method = RequestMethod.POST)
    @ApiOperation(value ="강의자료 신고" , notes = "파라미터는 강의 자료의 댓글 id 입니다\n REPORT 내용 별 id =>"
            +"1: \"욕설/비하\" ,2 : \"유출/사칭/저작권 위배\", 3: \"허위/부적절한 정보\"\n" +
            "    4: \"광고/도배\", 5: \"음란물\""
            ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public @ResponseBody
    ResponseEntity reportLectureBankCommment(@RequestBody Report report) throws Exception {
        lectureBankService.reportLectureBank(report);
        return new ResponseEntity(new BaseResponse("정상적으로 신고되었습니다.", HttpStatus.OK), HttpStatus.OK);
    }


    //TODO test------------------------------------------------------------------------------------
    @RequestMapping(value = "/file/test", method = RequestMethod.POST)
    @ApiOperation(value ="Thumbnail test" , notes = "섬네일 테스트")
    public @ResponseBody
    ResponseEntity<String> test(MultipartFile file) throws Exception {
        return new ResponseEntity<String>(lectureBankService.makeThumbnail(file),HttpStatus.CREATED);
        //return new ResponseEntity<String>("{}",HttpStatus.OK);
    }




    // TODO 썸네일 테스트 정수현
    @PostMapping("/test")
    public ResponseEntity tngusTest(@RequestBody MultipartFile multipartFile) throws Exception {
        return new ResponseEntity(lectureBankService.tngusTest(multipartFile), HttpStatus.OK);
    }

}
