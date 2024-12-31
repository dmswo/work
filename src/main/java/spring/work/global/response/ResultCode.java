package spring.work.global.response;

import lombok.Getter;

@Getter
public enum ResultCode {
    OK("200", "정상처리 되었습니다."),
    FAIL("500", "FAIL");

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
