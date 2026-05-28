package spring.work.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostLikeRedisRepository {

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

    private String generateKey(Long postId) {
        return "post:like:" + postId;
    }
}
