package spring.work.global.externalApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebClientResponse<RESPONSE> {
    private HttpStatus status;
    private HttpHeaders headers;
    private RESPONSE body;
}
