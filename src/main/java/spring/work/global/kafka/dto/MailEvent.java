package spring.work.global.kafka.dto;

import lombok.*;
import spring.work.user.dto.request.Signup;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailEvent implements Event {
    private String eventId; // 멱등성을 위해 이벤트Id 사용
    private String userId;
    private String toEmail;

    @Override
    public String getTopic() {
        return "mail-topic";
    }

    public static MailEvent from(Signup dto) {
        return MailEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(dto.getUserId())
                .toEmail(dto.getEmail())
                .build();
    }
}
