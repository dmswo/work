package spring.work.global.constant;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    FAIL("FAIL", "특정하지 않은 오류가 발생하였습니다."),
    ENCRYPT("ENCRYPT", "암호화 에러."),
    DECRYPT("DECRYPT", "복호화 에러."),
    UNAUTHORIZED("UNAUTHORIZED", "인증 에러."),
    FORBIDDEN("FORBIDDEN", "인가 에러."),

    USER_NOT_FOUND("USERNAME_NOT_FOUND", "유저 정보를 찾을 수 없습니다."),
    USER_EXIST("USER_EXIST", "이미 존재하는 유저ID입니다."),

    TOKEN_ERROR("TOKEN_ERROR", "잘못된 토큰입니다."),
    TOKEN_EXPIRE("TOKEN_EXPIRE", "만료된 토큰입니다."),
    TOKEN_UNSUPPORTED("TOKEN_UNSUPPORTED", "지원하지 않는 형식의 토큰입니다."),
    TOKEN_LOGOUT("TOKEN_LOGOUT", "이미 로그아웃 된 토큰입니다."),

    EXTERNAL_API_ERROR("EXTERNAL_API_ERROR", "외부 API 호출 에러입니다.");

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
