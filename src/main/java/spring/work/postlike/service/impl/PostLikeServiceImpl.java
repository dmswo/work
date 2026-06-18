package spring.work.postlike.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.common.EventType;
import spring.work.event.producer.outbox.entity.OutboxEvent;
import spring.work.event.producer.outbox.repository.OutBoxEventRepository;
import spring.work.event.producer.outbox.service.OutBoxEventService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.kafka.dto.NotificationEvent;
import spring.work.global.redis.PostLikeRedisRepository;
import spring.work.notification.constant.NotificationType;
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
    private final PostLikeRedisRepository postLikeRedisRepository;
    private final OutBoxEventService outBoxEventService;
    private final OutBoxEventRepository outBoxEventRepository;

    @Transactional
    @Override
    public void savePostLike(Long postId, String userId) {
        // Redis 선점
        boolean added = postLikeRedisRepository.addLikeUser(postId, userId);

        if (!added) {
            throw new BusinessException(ExceptionCode.ALREADY_POST_LIKED);
        }

        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));
            Users sender = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(sender)
                    .build();

            postLikeRepository.save(postLike);

            // 알림 전송(Outbox 저장)
            Users receiver = post.getUser();
            NotificationEvent event = NotificationEvent.from(receiver.getSeq(), sender.getSeq(), NotificationType.POST_LIKE, post.getSeq());
            OutboxEvent outboxEvent = outBoxEventService.createOutbox(EventType.NOTIFICATION, event);
            outBoxEventRepository.save(outboxEvent);

        } catch (Exception e) {
            postLikeRedisRepository.removeLikeUser(postId, userId);
            throw e;
        }
    }

    @Transactional
    @Override
    public void deletePostLike(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));
        Users users = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        PostLike postLike = postLikeRepository.findByPostAndUser(post, users).orElseThrow(() -> new BusinessException(ExceptionCode.POST_LIKE_NOT_FOUND));

        postLikeRepository.delete(postLike);
        postLikeRedisRepository.removeLikeUser(postId, userId);
    }
}