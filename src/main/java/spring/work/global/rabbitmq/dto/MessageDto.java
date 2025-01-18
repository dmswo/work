package spring.work.global.rabbitmq.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String subject;
    private String content;
    private String toEmail;
}
