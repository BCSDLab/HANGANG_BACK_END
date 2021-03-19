package in.hangang.service;

import in.hangang.domain.Memo;

public interface MemoService {
    void createMemo(Memo memo) throws Exception;
    Memo getMemo(Memo memo) throws Exception;
    void deleteMemo(Memo memo) throws Exception;
    void updateMemo(Memo memo) throws Exception;
}
