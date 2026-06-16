package spring.work.event.outbox.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.outbox.entity.OutboxEvent;
import spring.work.event.outbox.repository.OutBoxEventRepository;
import spring.work.event.outbox.service.OutboxLifecycleService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxLifecycleServiceImpl implements OutboxLifecycleService {

    private static final int MAX_RETRY = 5;

    private final OutBoxEventRepository outBoxEventRepository;

    @Transactional
    @Override
    public List<OutboxEvent> makeProcessing() {
        List<OutboxEvent> pendingList = outBoxEventRepository.findAndLockPending(100);

        for (OutboxEvent event : pendingList) {
            event.makeProcessing();
        }

        return pendingList;
    }

    @Transactional
    @Override
    public void increaseRetry(Long seq) {
        OutboxEvent outboxEvent = outBoxEventRepository.findById(seq).orElseThrow(() -> new BusinessException(ExceptionCode.EVENT_NOT_FOUND));
        outboxEvent.increaseRetry(MAX_RETRY);
    }

    @Override
    public void makeDeadLetter(Long seq) {
        OutboxEvent outboxEvent = outBoxEventRepository.findById(seq).orElseThrow();
        outboxEvent.makeDeadLetter();
    }
}
