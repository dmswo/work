package spring.work.global.externalApi.workPoint.dto;

import lombok.Data;
import spring.work.global.exception.BusinessException;

import static spring.work.global.constant.ExceptionCode.EXTERNAL_API_ERROR;

@Data
public class PointCommonResponse<T> {
    private String code;
    private String message;
    private T data;

    public void ValidateResponse() {
        if (!code.equals("OK")) {
            throw new BusinessException(EXTERNAL_API_ERROR, message);
        }
    }
}
