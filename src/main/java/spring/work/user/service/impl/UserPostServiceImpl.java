package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.user.dto.request.CreatePost;
import spring.work.user.mapper.UserPostMapper;
import spring.work.user.service.UserPostService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPostServiceImpl implements UserPostService {

    private final UserPostMapper userPostMapper;

    @Override
    public void saveUserPost(CreatePost post) {
        userPostMapper.saveUserPost(post);
    }
}
