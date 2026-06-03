package spring.work.notification.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import spring.work.notification.dto.response.NotificationListResponse;

import java.util.List;

import static spring.work.notification.entity.QNotification.notification;
import static spring.work.user.entity.QUsers.users;

public class NotificationCustomRepositoryImpl implements NotificationCustomRepository{

    private final JPAQueryFactory queryFactory;

    public NotificationCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NotificationListResponse> getNotifications(Pageable pageable) {
        List<NotificationListResponse> content = queryFactory.select(Projections.fields(
                        NotificationListResponse.class,
                        notification.id,
                        notification.sender.nickname.as("senderNickname"),
                        notification.isRead,
                        notification.type,
                        notification.createdAt
                )).from(notification)
                .join(notification.sender, users)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notification.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(notification.count())
                .from(notification);

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne
        );
    }
}
