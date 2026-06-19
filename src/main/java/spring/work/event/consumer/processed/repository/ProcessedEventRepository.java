package spring.work.event.consumer.processed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.consumer.processed.entity.ProcessedEvent;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {
}
