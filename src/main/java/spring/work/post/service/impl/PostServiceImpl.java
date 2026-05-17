package spring.work.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.post.document.PostDocument;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostResponse;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.ElasticSearchRepository;
import spring.work.post.service.PostService;
import spring.work.user.repository.UserRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ElasticSearchRepository elasticSearchRepository;

    @Transactional
    @Override
    public void savePost(CreatePost request) {
        Users user = userRepository.findByUserId(request.getUserId())
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

    @Override
    public List<PostResponse> getPosts() {
        Iterable<PostDocument> documents = elasticSearchRepository.findAll();

        return StreamSupport.stream(documents.spliterator(), false)
                .map(PostResponse::from)
                .toList();
    }
}
