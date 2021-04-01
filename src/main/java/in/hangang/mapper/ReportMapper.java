package in.hangang.mapper;

import in.hangang.domain.Report;

public interface ReportMapper {
    void createReport(Report report);
    Long checkAlreadyReported(Report report);
}
