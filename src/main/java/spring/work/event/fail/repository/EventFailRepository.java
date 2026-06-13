package spring.work.event.fail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.fail.entity.EventFail;

public interface EventFailRepository extends JpaRepository<EventFail, Long> {
}
