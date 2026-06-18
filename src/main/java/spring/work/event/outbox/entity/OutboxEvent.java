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
@Table(
        indexes = {
                @Index(
                        name = "idx_outbox_status_next_retry",
                        columnList = "status, nextRetryAt"
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

    // retry 관련
    private int retryCount;

    private LocalDateTime lastRetriedAt;

    private LocalDateTime nextRetryAt;

    @Column(length = 1000)
    private String lastErrorMessage;

    public void makeProcessing() {
        this.status = OutBoxStatus.PROCESSING;
    }

    public void makeDeadLetter() {
        this.status = OutBoxStatus.DEAD_LETTER;
    }

    public void increaseRetry(int maxRetry, String errorMessage) {
        this.retryCount += 1;
        this.lastRetriedAt = LocalDateTime.now();
        this.lastErrorMessage = errorMessage;

        if (this.retryCount >= maxRetry) {
            this.status = OutBoxStatus.DEAD_LETTER;
            this.nextRetryAt = null;
        } else {
            this.status = OutBoxStatus.PENDING;
            this.nextRetryAt = calculateNextRetry();
        }
    }

    private LocalDateTime calculateNextRetry() {
        long delayMinutes;

        switch (retryCount) {
            case 0 -> delayMinutes = 1;
            case 1 -> delayMinutes = 2;
            case 2 -> delayMinutes = 5;
            case 3 -> delayMinutes = 10;
            case 4 -> delayMinutes = 30;
            default -> delayMinutes = 60;
        }

        return LocalDateTime.now().plusMinutes(delayMinutes);
    }
}