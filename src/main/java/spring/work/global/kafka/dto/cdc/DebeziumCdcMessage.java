package spring.work.global.kafka.dto.cdc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumCdcMessage<T> {
    private DebeziumPayload<T> payload;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DebeziumPayload<T> {
        private T before;
        private T after;
        private String op;
    }
}
