package spring.work.postlike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.post.entity.Post;
import spring.work.postlike.entity.PostLike;
import spring.work.user.entity.Users;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, Users user);
}
