package spring.work.user.service;

import spring.work.user.dto.request.CreatePost;

public interface UserPostService {
    void saveUserPost(CreatePost post);
}
