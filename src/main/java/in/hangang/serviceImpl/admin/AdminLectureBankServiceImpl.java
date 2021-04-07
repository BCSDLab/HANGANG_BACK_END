package in.hangang.serviceImpl.admin;

import in.hangang.domain.LectureBank;
import in.hangang.domain.report.LectureBankCommentReport;
import in.hangang.domain.report.LectureBankReport;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.admin.AdminLectureBankMapper;
import in.hangang.response.BaseResponse;
import in.hangang.service.admin.AdminLectureBankService;
import in.hangang.serviceImpl.LectureBankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("AdminLectureBankServiceImpl")
public class AdminLectureBankServiceImpl extends LectureBankServiceImpl implements AdminLectureBankService {

    @Autowired
    private AdminLectureBankMapper adminLectureBankMapper;


    @Override
    public List<LectureBankReport> getReportedLectureBank(){
        return adminLectureBankMapper.getReportedLectureBank();
    }

    @Override
    public List<LectureBankCommentReport> getReportedLectureBankComment(){
        return adminLectureBankMapper.getReportedLectureBankComment();
    }

    @Override
    public BaseResponse deleteReportedCommentForAdmin(Long id){
        Long commentId = adminLectureBankMapper.getComment(id);
        //강의자료 댓글이 존재하지 않는 경우
        if ( commentId == null ){
            throw new RequestInputException(ErrorMessage.COMMENT_NOT_EXIST);
        }
        //존재하는 경우 아묻따 삭제
        lectureBankMapper.deleteComment(id);
        
        //신고내역에서 삭제
        adminLectureBankMapper.softDeleteReported(id, Long.valueOf(3));
        return new BaseResponse(id.toString() + "강의자료 댓글이 삭제되었습니다" , HttpStatus.OK);
    }

    @Override
    public void deleteLectureBank(Long id){
        //delete LectureBank - soft
        adminLectureBankMapper.deleteLectureBank(id);
        //delete Comment : soft
        List<Long> commentIdList= lectureBankMapper.getCommentIdList(id);
        if(commentIdList.size() != 0)
            lectureBankMapper.deleteMultiComment((ArrayList<Long>)commentIdList);

        //delete File : soft -> hard => scheduler available 2
        List<Long> fileIdList = lectureBankMapper.getFileId(id);
        if(fileIdList.size() != 0)
            lectureBankMapper.deleteMultiFile((ArrayList<Long>) fileIdList,2);

        //delete Category : hard
        List<Long> categoryList = lectureBankMapper.getCategoryIdList(id);
        if(categoryList.size() != 0)
            lectureBankMapper.deleteMultiCategory((ArrayList<Long>)categoryList);

        //delete Hit : soft
        List<Long> hitIdList = lectureBankMapper.getHitId(id);
        if(hitIdList.size() != 0)
            lectureBankMapper.deleteMultiHit((ArrayList<Long>)hitIdList);
        //delete Purchase : soft
        List<Long> purchaseId = lectureBankMapper.getPurchaseId(id);
        if(purchaseId.size() != 0)
            lectureBankMapper.deleteMultiPurchase((ArrayList<Long>)purchaseId);

        // 신고내역에서 삭제
        adminLectureBankMapper.softDeleteReported(id, Long.valueOf(2));
    }
}
