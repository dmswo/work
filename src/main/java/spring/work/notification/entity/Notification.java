package spring.work.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.entity.BaseEntity;
import spring.work.notification.constant.NotificationType;
import spring.work.user.entity.Users;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id")
    private Users receiver;

    private Long targetId;

    private boolean isRead;

    public static Notification create(Users receiver, Users sender, NotificationType type, Long targetId) {
        return Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .type(type)
                .targetId(targetId)
                .isRead(false)
                .build();
    }

    public void readNotification() {
        this.isRead = true;
    }
}
