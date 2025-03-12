package spring.work.global.externalApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;

import java.net.http.HttpHeaders;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ApiRequest<REQUEST, RESPONSE> {
    private String hostUrl;
    private String endPoint;
    private HttpHeaders headers;
    private REQUEST request;
    private ParameterizedTypeReference<RESPONSE> response;
}
