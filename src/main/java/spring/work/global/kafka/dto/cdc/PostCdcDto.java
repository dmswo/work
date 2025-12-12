package spring.work.global.kafka.dto.cdc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostCdcDto {
    private Long seq;
    private String title;
    private String content;
    @JsonProperty("view_cnt")
    private Integer viewCnt;
}
