package spring.work.postlike.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.postlike.entity.PostLike;
import spring.work.postlike.repository.PostLikeRepository;
import spring.work.postlike.service.PostLikeService;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    @Override
    public void savePostLike(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));
        Users users = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        if (postLikeRepository.existsByPostAndUser(post, users)) {
            throw new BusinessException(ExceptionCode.ALREADY_POST_LIKED);
        }

        PostLike postLike = PostLike.builder()
                .post(post)
                .user(users)
                .build();

        postLikeRepository.save(postLike);
    }

    @Transactional
    @Override
    public void deletePostLike(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));
        Users users = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        PostLike postLike = postLikeRepository.findByPostAndUser(post, users).orElseThrow(() -> new BusinessException(ExceptionCode.POST_LIKE_NOT_FOUND));

        postLikeRepository.delete(postLike);
    }
}