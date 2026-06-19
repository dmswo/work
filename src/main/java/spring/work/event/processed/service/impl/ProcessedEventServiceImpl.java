package spring.work.event.processed.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.common.EventType;
import spring.work.event.processed.entity.ProcessedEvent;
import spring.work.event.processed.repository.ProcessedEventRepository;
import spring.work.event.processed.service.ProcessedEventService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessedEventServiceImpl implements ProcessedEventService {

    private final ProcessedEventRepository processedEventRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean exists(String eventId) {
        return processedEventRepository.existsById(eventId);
    }

    @Override
    public void save(String eventId, EventType eventType) {
        ProcessedEvent processedEvent = ProcessedEvent.from(eventId, eventType);
        processedEventRepository.save(processedEvent);
    }

}
