package spring.work.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.user.entity.UserLoginHistory;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {
}
