package spring.work.post.service;

import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    void saveUserPost(CreatePost post);
    void updateUserPost(Long postId, UpdatePost post);
    List<PostResponse> getPosts();
}
