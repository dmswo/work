package spring.work.global.externalApi.workPoint.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import spring.work.global.exception.BusinessException;

import static spring.work.global.constant.ExceptionCode.EXTERNAL_API_ERROR;

@Data
@Slf4j
public class PointCommonResponse<T> {
    private String code;
    private String message;
    private T data;

    public void ValidateResponse() {
        if (!code.equals("OK")) {
            log.error("Error Code : {}, message : {}", code, message);
            throw new BusinessException(EXTERNAL_API_ERROR, message);
        }
    }
}
