package spring.work.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.user.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userId);
    Optional<Users> findByUserId(String userId);
}
