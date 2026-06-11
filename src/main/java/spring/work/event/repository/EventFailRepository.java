package spring.work.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.entity.EventFail;

public interface EventFailRepository extends JpaRepository<EventFail, Long> {
}
