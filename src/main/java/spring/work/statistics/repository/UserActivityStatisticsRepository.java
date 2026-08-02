package spring.work.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.statistics.entity.UserActivityStatistics;

import java.time.LocalDate;
import java.util.Optional;

public interface UserActivityStatisticsRepository extends JpaRepository<UserActivityStatistics, Long> {
    Optional<UserActivityStatistics> findByUserIdAndStatisticsDate(Long id, LocalDate date);
}
