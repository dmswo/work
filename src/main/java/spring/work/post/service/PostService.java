package spring.work.post.service;

import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    void savePost(CreatePost request);
    void updatePost(Long postId, UpdatePost request);
    void deletePost(Long postId);
    List<PostResponse> getPosts();
}
