package spring.work.post.service;

import org.springframework.data.domain.Pageable;
import spring.work.global.dto.PageResponse;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.dto.response.PostResponse;

public interface PostService {
    void savePost(CreatePost request, String userId);
    void updatePost(Long postId, UpdatePost request);
    void deletePost(Long postId);
    PageResponse<PostListResponse> getPosts(PostSearchCondition condition, Pageable pageable, String userId);
    PostResponse getPost(Long postId);
}
