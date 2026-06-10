package spring.work.global.kafka.dto;

import lombok.*;
import spring.work.user.dto.request.Signup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailEvent implements Event {
    private String userId;
    private String subject;
    private String content;
    private String toEmail;

    @Override
    public String getTopic() {
        return "mail-topic";
    }

    public static MailEvent from(Signup dto) {
        return MailEvent.builder()
                .userId(dto.getUserId())
                .subject(dto.getUserId()+"님의 Work프로젝트 회원가입을 축하합니다.")
                .content("Work 프로젝트에서 작업을 시작하세요!!")
                .toEmail(dto.getEmail())
                .build();
    }
}
