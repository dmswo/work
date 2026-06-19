package spring.work.event.retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.retry.entity.FailEvent;

public interface FailEventRepository extends JpaRepository<FailEvent, Long> {
}
