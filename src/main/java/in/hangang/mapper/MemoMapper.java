package in.hangang.mapper;

import in.hangang.domain.Memo;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoMapper {
    void createMemo(Memo memo);
    Memo getMemo(Long timeTableId);
    void updateMemo(Memo memo);
    void deleteMemo(Memo memo);
    Long isExistsMemo(Long timeTableId);
    Long getUserIdByTimeTablesId(Long timetable_id);
}
