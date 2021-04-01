package in.hangang.service;

import in.hangang.domain.Report;

public interface ReportService {
    void createReport(Integer typeId, Report report) throws Exception;
}
