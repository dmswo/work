package spring.work.global.exception;

import lombok.Getter;
import spring.work.global.response.ResultCode;

@Getter
public class BusinessException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
