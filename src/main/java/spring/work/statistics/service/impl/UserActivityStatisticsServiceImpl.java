package spring.work.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.statistics.entity.UserActivityStatistics;
import spring.work.statistics.repository.UserActivityStatisticsRepository;
import spring.work.statistics.service.UserActivityStatisticsService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityStatisticsServiceImpl implements UserActivityStatisticsService {

    private final UserActivityStatisticsRepository userActivityStatisticsRepository;

    @Transactional
    @Override
    public void increaseLikeCount(Long id) {
        LocalDate today = LocalDate.now();
        UserActivityStatistics statistics = userActivityStatisticsRepository.findByUserIdAndStatisticsDate(id, today)
                        .orElseGet(() -> UserActivityStatistics.from(id, today));

        statistics.increaseLikeCount();

        userActivityStatisticsRepository.save(statistics);
    }
}
