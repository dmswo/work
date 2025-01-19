package spring.work.global.rabbitmq.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailDto {
    private String subject;
    private String content;
    private String toEmail;
}
