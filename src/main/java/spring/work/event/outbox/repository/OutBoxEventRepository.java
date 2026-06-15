package spring.work.event.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.constant.OutBoxStatus;
import spring.work.event.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutBoxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutBoxStatus status);
}
