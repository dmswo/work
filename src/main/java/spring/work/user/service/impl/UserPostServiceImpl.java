package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.user.dto.request.CreatePost;
import spring.work.user.mapper.UserAuthMapper;
import spring.work.user.mapper.UserPostMapper;
import spring.work.user.service.UserPostService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPostServiceImpl implements UserPostService {

    private final UserPostMapper userPostMapper;
    private final UserAuthMapper userAuthMapper;

    @Override
    public void saveUserPost(CreatePost post) {
        if (userAuthMapper.existsByUserId(post.getUserId()) > 0) {
            throw new BusinessException(ExceptionCode.USER_EXIST);
        }
        userPostMapper.saveUserPost(post);
    }
}
