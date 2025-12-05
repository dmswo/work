package spring.work.user.service;

import spring.work.user.dto.request.post.CreatePost;
import spring.work.user.dto.request.post.UpdatePost;
import spring.work.user.dto.response.post.PostResponse;

import java.util.List;

public interface UserPostService {
    void saveUserPost(CreatePost post);
    void updateUserPost(Long postId, UpdatePost post);
    List<PostResponse> getPosts();
}
