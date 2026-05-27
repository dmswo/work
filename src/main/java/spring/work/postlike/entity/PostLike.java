package spring.work.postlike.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.entity.BaseEntity;
import spring.work.post.entity.Post;
import spring.work.user.entity.Users;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"post_seq", "user_seq"}
                )
        }
)
public class PostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private Users user;

    public static PostLike create(Post post, Users user) {
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }
}
