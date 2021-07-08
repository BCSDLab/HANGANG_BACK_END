package in.hangang.service;

import in.hangang.domain.Semester;

public interface SemesterService {
    Semester getCurrentSemesterDate(Long isRegular);
}
