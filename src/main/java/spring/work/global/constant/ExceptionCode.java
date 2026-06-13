package spring.work.global.constant;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // 공통 에러 코드
    FAIL("FAIL", "특정하지 않은 오류가 발생하였습니다."),
    ENCRYPT("ENCRYPT", "암호화 에러."),
    DECRYPT("DECRYPT", "복호화 에러."),
    UNAUTHORIZED("UNAUTHORIZED", "인증 에러."),
    FORBIDDEN("FORBIDDEN", "인가 에러."),

    TOKEN_ERROR("TOKEN_ERROR", "잘못된 토큰입니다."),
    TOKEN_EXPIRE("TOKEN_EXPIRE", "만료된 토큰입니다."),
    TOKEN_UNSUPPORTED("TOKEN_UNSUPPORTED", "지원하지 않는 형식의 토큰입니다."),
    TOKEN_LOGOUT("TOKEN_LOGOUT", "이미 로그아웃 된 토큰입니다."),

    EXTERNAL_API_ERROR("EXTERNAL_API_ERROR", "외부 API 호출 에러입니다."),

    EVENT_NOT_FOUND("EVENT_NOT_FOUND", "이벤트를 찾을 수 없습니다."),
    UNSUPPORTED_EVENT_TYPE("UNSUPPORTED_EVENT_TYPE", "지원하지 않는 이벤트 타입입니다."),

    // User 도메인 에러 코드
    USER_NOT_FOUND("USERNAME_NOT_FOUND", "유저 정보를 찾을 수 없습니다."),
    USER_EXIST("USER_EXIST", "이미 존재하는 유저ID입니다."),

    // Post 도메인 에러 코드
    POST_NOT_FOUND("POST_NOT_FOUND", "게시물 정보를 찾을 수 없습니다."),

    // Post_Like 도메인 에러 코드
    POST_LIKE_NOT_FOUND("POST_LIKE_NOT_FOUND", "게시물 좋아요 정보를 찾을 수 없습니다."),
    ALREADY_POST_LIKED("ALREADY_POST_LIKED", "이미 좋아요를 누르셨습니다."),

    // Comment 도메인 에러 코드
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "댓글 정보를 찾을 수 없습니다."),

    // Notification 도메인 에러 코드
    NOTIFICATION_NOT_FOUND("NOTIFICATION_NOT_FOUND", "알림 정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
