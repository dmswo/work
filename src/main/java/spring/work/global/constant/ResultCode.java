package spring.work.global.constant;

import lombok.Getter;

@Getter
public enum ResultCode {
    OK("OK", "정상처리 되었습니다."),
    FAIL("FAIL", "FAIL"),
    VALIDATION_FAILED("VALIDATION_FAILED", "FAIL");

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
