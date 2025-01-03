package spring.work.global.constant;

import lombok.Getter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Getter
public enum ExceptionCode {
    FAIL("FAIL", "특정하지 않은 오류가 발생하였습니다."),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "유저 정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
