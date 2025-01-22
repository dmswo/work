package spring.work.global.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDto {
    private String subject;
    private String content;
    private String toEmail;

    public static MailDto signupMailOf() {
        return MailDto.builder()
                .subject("Work 회원가입을 축하합니다.")
                .content("Work 내용")
                .toEmail("dmswo106@naver.com")
                .build();
    }
}
