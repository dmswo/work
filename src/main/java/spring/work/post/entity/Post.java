package spring.work.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.entity.BaseEntity;
import spring.work.user.entity.Users;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 200)
    private String content;

    private Long viewCnt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_seq")
    private Users user;
}
