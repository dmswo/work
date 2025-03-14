package spring.work.global.externalApi;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.net.http.HttpHeaders;

@Data
public class WebClientResponse<RESPONSE> {
    private HttpStatus status;
    private HttpHeaders headers;
    private RESPONSE body;

    public WebClientResponse(RESPONSE body) {
        this.body = body;
    }
}
