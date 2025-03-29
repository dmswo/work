package spring.work.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> invalidRequestHandler(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }

        return ApiResponse.failResponse(errors);
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException e.code : {}, e.message : {}", e.getExceptionCode().getCode(), e.getMessage());
        if (!e.getExceptionCode().getMessage().equals(e.getMessage())) {
            return ApiResponse.errorResponse(e.getExceptionCode(), e.getMessage());
        }
        return ApiResponse.errorResponse(e.getExceptionCode());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        log.error("handleException e.message : {}", e.getMessage());
        return ApiResponse.errorResponse(ExceptionCode.FAIL);
    }
}