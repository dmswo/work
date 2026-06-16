package spring.work.event.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.work.event.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutBoxEventRepository extends JpaRepository<OutboxEvent, Long> {
    @Query(value = """
    SELECT *
    FROM outbox_event
    WHERE status = 'PENDING'
    ORDER BY created_at
    LIMIT :limit
    FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxEvent> findAndLockPending(@Param("limit") int limit);
}
