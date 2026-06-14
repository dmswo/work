package spring.work.event.outbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.event.constant.EventType;
import spring.work.event.constant.OutBoxStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Lob
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutBoxStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;
}