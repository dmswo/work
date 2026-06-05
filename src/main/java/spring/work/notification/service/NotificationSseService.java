package spring.work.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationSseService {
    SseEmitter subscribe(String userId);
    void send(String receiverId, Object data);
}
