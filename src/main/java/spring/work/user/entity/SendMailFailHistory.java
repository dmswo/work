package spring.work.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.entity.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sendMailFailHistory")
public class SendMailFailHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_seq")
    private Users user;

    public static SendMailFailHistory create(Users user) {
        return SendMailFailHistory.builder()
                .user(user)
                .build();
    }
}
