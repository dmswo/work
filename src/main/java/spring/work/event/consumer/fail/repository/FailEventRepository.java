package spring.work.event.consumer.fail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.event.consumer.fail.entity.FailEvent;

public interface FailEventRepository extends JpaRepository<FailEvent, Long> {
}
