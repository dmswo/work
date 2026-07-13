package spring.work.global.ai.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostValidationResult {
    private boolean passed;
    private String reason;
}
