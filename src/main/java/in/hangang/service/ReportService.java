package in.hangang.service;

import in.hangang.domain.Report;

public interface ReportService {
    void createReport(Integer boardTypeId, Report report) throws Exception;
    void sendReportNoti(Report report,String contents) throws Exception;
}
