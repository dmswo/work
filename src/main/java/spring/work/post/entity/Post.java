package spring.work.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.comment.entity.Comment;
import spring.work.global.entity.BaseEntity;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.UpdatePost;
import spring.work.user.entity.Users;

import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public static Post create(CreatePost post, Users user) {
        return Post.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .viewCnt(0L)
                .user(user)
                .build();
    }

    public void modify(UpdatePost request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
