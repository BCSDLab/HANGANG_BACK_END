package in.hangang.serviceImpl;

import in.hangang.domain.Memo;
import in.hangang.domain.User;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.MemoMapper;
import in.hangang.mapper.TimetableMapper;
import in.hangang.service.MemoService;
import in.hangang.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemoServiceImpl implements MemoService {

    @Resource
    @Qualifier("UserServiceImpl")
    UserService userService;

    @Resource
    TimetableMapper timetableMapper;

    @Resource
    MemoMapper memoMapper;

    @Override
    public void createMemo(Memo memo) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        //if(!userId.equals(memoMapper.getUserIdByTimeTablesId(memo.getTimetable_id())))
        //    throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        memoMapper.createMemo(memo);
    }

    @Override
    public Memo getMemo(Long timeTableId) throws Exception {
        Memo memo = new Memo();
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        //if(!userId.equals(timetableMapper.getUserIdByTimeTableId(timeTableId)))
            //throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        if(memoMapper.isExistsMemo(timeTableId)==null) {
            memo.setTimetable_component_id(timeTableId);
            memo.setMemo("");
            return memo;
        }

        memo = memoMapper.getMemo(timeTableId);

        return memo;
    }

    @Override
    public void updateMemo(Memo memo) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        //수정 권한 확인
        if(!userId.equals(memoMapper.getUserIdByTimeTablesId(memo.getTimetable_component_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        if(memoMapper.isExistsMemo(memo.getTimetable_component_id())==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);
        memoMapper.updateMemo(memo);
    }

    @Override
    public void deleteMemo(Memo memo) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        //수정 권한 확인
        if(!userId.equals(memoMapper.getUserIdByTimeTablesId(memo.getTimetable_component_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        if(memoMapper.isExistsMemo(memo.getTimetable_component_id())==null)
            throw new RequestInputException(ErrorMessage.CONTENT_NOT_EXISTS);

        memoMapper.deleteMemo(memo);
    }
}
