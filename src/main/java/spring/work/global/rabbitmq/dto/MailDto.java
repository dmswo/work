package spring.work.global.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.user.dto.request.Signup;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDto {
    private String userId;
    private String subject;
    private String content;
    private String toEmail;

    public static MailDto signupMailOf(Signup dto) {
        return MailDto.builder()
                .userId(dto.getUserId())
                .subject(dto.getUserId()+"님의 Work프로젝트 회원가입을 축하합니다.")
                .content("Work 프로젝트에서 작업을 시작하세요!!")
                .toEmail(dto.getEmail())
                .build();
    }
}
