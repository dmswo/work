package spring.work.global.ai.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailContent {
    private String subject;
    private String content;
}
