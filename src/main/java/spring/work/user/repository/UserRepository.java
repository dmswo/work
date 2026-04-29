package spring.work.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userId);
}
