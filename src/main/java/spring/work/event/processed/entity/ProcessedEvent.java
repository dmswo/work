package spring.work.event.processed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.event.common.EventType;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_processed_event",
                        columnNames = {"eventId", "consumerGroup"}
                )
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String consumerGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private LocalDateTime processedAt;


    public static ProcessedEvent from(String eventId, String consumerGroup, EventType eventType) {
        return ProcessedEvent.builder()
                .eventId(eventId)
                .consumerGroup(consumerGroup)
                .eventType(eventType)
                .processedAt(LocalDateTime.now())
                .build();
    }
}
