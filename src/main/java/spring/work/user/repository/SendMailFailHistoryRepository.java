package spring.work.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.user.entity.SendMailFailHistory;

public interface SendMailFailHistoryRepository extends JpaRepository<SendMailFailHistory, Long> {
}
