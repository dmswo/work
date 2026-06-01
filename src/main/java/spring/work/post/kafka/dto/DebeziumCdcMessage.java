package spring.work.post.kafka.dto;

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
