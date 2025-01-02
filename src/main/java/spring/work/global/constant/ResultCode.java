package spring.work.global.constant;

import lombok.Getter;

@Getter
public enum ResultCode {
    OK("OK", "정상처리 되었습니다."),
    FAIL("FAIL", "특정하지 않은 오류가 발생하였습니다."),
    VALIDATION_FAILED("VALIDATION_FAILED", "FAIL");

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
