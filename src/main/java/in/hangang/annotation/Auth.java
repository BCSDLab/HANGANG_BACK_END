package in.hangang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    // 루트, 관리자, default
    enum Role { ROOT, MANAGER, NORMAL }

    // 강의자료 권한, 강의평 권한, 시간표 권한 , default
    enum Authority { LectureBank, Lecture, TimeTable, NONE }

    Role role() default Role.NORMAL;
    Authority authority() default Authority.NONE;
}
