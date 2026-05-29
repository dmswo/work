package spring.work.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.PageResponse;
import spring.work.global.exception.BusinessException;
import spring.work.global.redis.PostLikeRedisRepository;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.dto.response.PostResponse;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.post.service.PostService;
import spring.work.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRedisRepository postLikeRedisRepository;

    @Transactional
    @Override
    public void savePost(CreatePost request, String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        Post post = Post.create(request, user);
        postRepository.save(post);
    }

    @Transactional
    @Override
    public void updatePost(Long postId, UpdatePost request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        post.modify(request);
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<PostListResponse> getPosts(PostSearchCondition condition, Pageable pageable, String userId) {
        Page<PostListResponse> posts = postRepository.postList(condition, pageable);

        List<PostListResponse> content = posts.getContent();
        content.forEach(a -> {
            a.setLikeCount(postLikeRedisRepository.getLikeUserCount(a.getSeq()));
            a.setLiked(postLikeRedisRepository.getLiked(a.getSeq(), userId));
        });

        return PageResponse.from(posts);
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findPostDetail(postId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }
}
