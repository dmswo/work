package spring.work.comment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.global.entity.BaseEntity;
import spring.work.post.entity.Post;
import spring.work.user.entity.Users;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private Users user;

    public static Comment create(CreateComment comment, Post post, Users user) {
        return Comment.builder()
                .content(comment.getContent())
                .post(post)
                .user(user)
                .build();
    }

    public void modify(UpdateComment request) {
        this.content = request.getContent();
    }
}
