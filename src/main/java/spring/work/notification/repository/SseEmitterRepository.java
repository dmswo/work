package spring.work.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public SseEmitter findByUserId(String userId) {
        return emitters.get(userId);
    }

    public void delete(String userId) {
        emitters.remove(userId);
    }
}
