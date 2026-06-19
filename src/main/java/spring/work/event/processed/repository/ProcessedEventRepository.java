package spring.work.event.processed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.processed.entity.ProcessedEvent;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {
}
