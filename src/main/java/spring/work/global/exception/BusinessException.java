package spring.work.global.exception;

import lombok.Getter;
import spring.work.global.constant.ExceptionCode;

@Getter
public class BusinessException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public BusinessException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
