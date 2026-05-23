package spring.work.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.entity.Post;

public interface PostCustomRepository {
    Page<PostListResponse> postList(PostSearchCondition condition, Pageable pageable);
}
