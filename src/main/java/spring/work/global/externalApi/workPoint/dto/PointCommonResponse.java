package spring.work.global.externalApi.workPoint.dto;

import lombok.Data;

@Data
public class PointCommonResponse<T> {
    private String code;
    private String msg;
    private T data;
}
