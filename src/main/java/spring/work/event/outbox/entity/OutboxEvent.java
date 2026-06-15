package spring.work.event.outbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.event.constant.EventType;
import spring.work.event.constant.OutBoxStatus;
import spring.work.post.dto.request.UpdatePost;

import java.time.LocalDateTime;

@Entity
@Table(
        indexes = {
                @Index(
                        name = "idx_outbox_status_created_at",
                        columnList = "status, createdAt"
                )
        }
)
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

    public void statusUpdate(OutBoxStatus status) {
        this.status = status;
    }
}