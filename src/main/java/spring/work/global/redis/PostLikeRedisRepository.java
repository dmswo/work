package spring.work.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostLikeRedisRepository {

    private static final String POST_LIKE_KEY = "post:like:";

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean addLikeUser(Long postId, String userId) {
        String key = generateKey(postId);

        Long result = redisTemplate.opsForSet()
                .add(key, userId);

        return result != null && result > 0;
    }

    public void removeLikeUser(Long postId, String userId) {
        redisTemplate.opsForSet()
                .remove(generateKey(postId), userId);
    }

    public Long getLikeUserCount(Long postId) {
        return redisTemplate.opsForSet()
                .size(generateKey(postId));
    }

    public boolean getLiked(Long postId, String userId) {
        Boolean result = redisTemplate.opsForSet()
                .isMember(generateKey(postId), userId);

        return Boolean.TRUE.equals(result);
    }

    private String generateKey(Long postId) {
        return POST_LIKE_KEY + postId;
    }
}
