package spring.work.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import spring.work.notification.repository.SseEmitterRepository;
import spring.work.notification.service.NotificationSseService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSseServiceImpl implements NotificationSseService {

    private static final Long TIMEOUT = 60 * 60 * 1000L;
    private final SseEmitterRepository sseEmitterRepository;

    @Override
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        sseEmitterRepository.save(userId, emitter);

        emitter.onCompletion(() ->
                sseEmitterRepository.delete(userId));

        emitter.onTimeout(() ->
                sseEmitterRepository.delete(userId));

        emitter.onError((e) ->
                sseEmitterRepository.delete(userId));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return emitter;
    }

    @Override
    public void send(String receiverId, Object data) {
        SseEmitter emitter = sseEmitterRepository.findByUserId(receiverId);

        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(data)
            );
        } catch (IOException e) {
            sseEmitterRepository.delete(receiverId);
        }
    }
}
