package spring.work.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.event.constant.EventFailStatus;
import spring.work.event.constant.EventType;
import spring.work.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String topic;

    // 실패한 이벤트 전체 데이터(JSON)
    @Lob
    private String payload;

    // 예외 메시지
    private String errorMessage;

    // 재처리 여부
    @Enumerated(EnumType.STRING)
    private EventFailStatus status;
}
