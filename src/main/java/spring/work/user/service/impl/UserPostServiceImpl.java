package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.user.document.PostDocument;
import spring.work.user.dto.request.post.CreatePost;
import spring.work.user.dto.request.post.UpdatePost;
import spring.work.user.dto.response.post.PostResponse;
import spring.work.user.mapper.UserAuthMapper;
import spring.work.user.mapper.UserPostMapper;
import spring.work.user.repository.ElasticSearchRepository;
import spring.work.user.service.UserPostService;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPostServiceImpl implements UserPostService {

    private final UserPostMapper userPostMapper;
    private final UserAuthMapper userAuthMapper;
    private final ElasticSearchRepository elasticSearchRepository;

    @Transactional
    @Override
    public void saveUserPost(CreatePost post) {
        if (userAuthMapper.existsByUserId(post.getUserId()) == 0) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        userPostMapper.saveUserPost(post);

        // elasticsearch 저장 로직 추가
        PostDocument doc = PostDocument.builder()
                .seq(post.getSeq())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCnt(0)
                .build();
        elasticSearchRepository.save(doc);
    }

    @Transactional
    @Override
    public void updateUserPost(Long postId, UpdatePost post) {
        userPostMapper.updateUserPost(postId, post);
    }

    @Override
    public List<PostResponse> getPosts() {
        Iterable<PostDocument> documents = elasticSearchRepository.findAll();

        return StreamSupport.stream(documents.spliterator(), false)
                .map(PostResponse::from)
                .toList();
    }
}
