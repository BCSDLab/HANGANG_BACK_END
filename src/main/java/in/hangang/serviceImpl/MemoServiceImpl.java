package in.hangang.serviceImpl;

import in.hangang.domain.Memo;
import in.hangang.domain.User;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.MemoMapper;
import in.hangang.mapper.TimetableMapper;
import in.hangang.service.MemoService;
import in.hangang.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemoServiceImpl implements MemoService {

    @Resource
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

        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(memo.getTimetable_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        if(memoMapper.isExistsMemo(memo.getTimetable_id())!=null)
            //TODO : 메모가 이미 있다 에러 메세지 추가
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        memoMapper.createMemo(memo);
    }

    @Override
    public Memo getMemo(Memo memo) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();

        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(memo.getTimetable_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        if(memoMapper.isExistsMemo(memo.getTimetable_id())==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        return memoMapper.getMemo(memo);
    }

    @Override
    public void updateMemo(Memo memo) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(memo.getTimetable_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        if(memoMapper.isExistsMemo(memo.getTimetable_id())==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        System.out.println(memoMapper.isExistsMemo(memo.getTimetable_id()));
        memoMapper.updateMemo(memo);
    }

    @Override
    public void deleteMemo(Memo memo) throws Exception {
        User user = userService.getLoginUser();
        //유저 정보가 없는 경우 예외 처리
        if (user==null)
            throw new RequestInputException(ErrorMessage.INVALID_USER_EXCEPTION);
        Long userId = user.getId();
        if(!userId.equals(timetableMapper.getUserIdByTimeTableId(memo.getTimetable_id())))
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);
        if(memoMapper.isExistsMemo(memo.getTimetable_id())==null)
            throw new RequestInputException(ErrorMessage.INVALID_ACCESS_EXCEPTION);

        memoMapper.deleteMemo(memo);
    }
}
