package spring.work.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.work.global.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> invalidRequestHandler(BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.failResponse(bindingResult));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException e.code : {}, e.message : {}", e.getExceptionCode().getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.errorResponse(e.getExceptionCode()));
    }
}
