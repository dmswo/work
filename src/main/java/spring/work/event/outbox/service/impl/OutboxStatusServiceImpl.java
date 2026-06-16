package spring.work.event.outbox.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.outbox.entity.OutboxEvent;
import spring.work.event.outbox.repository.OutBoxEventRepository;
import spring.work.event.outbox.service.OutboxStatusService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxStatusServiceImpl implements OutboxStatusService {

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
    public void makeSuccess(Long seq) {
        OutboxEvent outboxEvent = outBoxEventRepository.findById(seq).orElseThrow(() -> new BusinessException(ExceptionCode.EVENT_NOT_FOUND));
        outboxEvent.makeSuccess();
    }

    @Transactional
    @Override
    public void makeFailed(Long seq) {
        OutboxEvent outboxEvent = outBoxEventRepository.findById(seq).orElseThrow(() -> new BusinessException(ExceptionCode.EVENT_NOT_FOUND));
        outboxEvent.makeFailed();
    }
}
