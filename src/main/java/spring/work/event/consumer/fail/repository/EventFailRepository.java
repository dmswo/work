package spring.work.event.consumer.fail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.consumer.fail.entity.EventFail;

public interface EventFailRepository extends JpaRepository<EventFail, Long> {
}
