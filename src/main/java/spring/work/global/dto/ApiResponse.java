package spring.work.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.constant.ResultCode;
import spring.work.global.constant.ExceptionCode;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse <T> {

    private T data;
    private String code;
    private String message;

    public static ApiResponse<ResultCode> successResponse(ResultCode resultCode) {
        return new ApiResponse<>(resultCode);
    }

    public static <T> ApiResponse<T> successResponse(T data) {
        return new ApiResponse<>(data, ResultCode.OK);
    }

    public static ApiResponse<?> failResponse(Map<String, String> list) {
        return new ApiResponse<>(list, ResultCode.VALIDATION_FAILED);
    }

    public static ApiResponse<?> errorResponse(ExceptionCode exceptionCode) {
        return new ApiResponse<>(exceptionCode);
    }

    public static ApiResponse<?> errorResponse(ExceptionCode exceptionCode, String detailMessage) {
        return new ApiResponse<>(exceptionCode, detailMessage);
    }

    private ApiResponse(ResultCode resultCode) {
        this.data = null;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    private ApiResponse(T data, ResultCode resultCode) {
        this.data = data;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    private ApiResponse(ExceptionCode exceptionCode) {
        this.data = null;
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    private ApiResponse(ExceptionCode exceptionCode, String detailMessage) {
        this.data = null;
        this.code = exceptionCode.getCode();
        this.message = detailMessage;
    }
}
