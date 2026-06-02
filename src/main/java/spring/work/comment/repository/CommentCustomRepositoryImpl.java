package spring.work.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import spring.work.comment.dto.response.CommentListResponse;
import spring.work.comment.entity.QComment;

import java.util.List;

import static spring.work.comment.entity.QComment.comment;
import static spring.work.user.entity.QUsers.users;

public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CommentCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CommentListResponse> commentList(Long postId, Pageable pageable) {

        QComment reply = new QComment("reply");
        List<CommentListResponse> content = queryFactory
                .select(Projections.fields(
                        CommentListResponse.class,
                        comment.seq.as("commentId"),
                        comment.content,
                        comment.user.nickname.as("nickname"),
                        reply.seq.count().as("replyCount"),
                        comment.createdAt
                )).from(comment)
                .join(comment.user, users)
                .leftJoin(reply)
                .on(reply.parent.eq(comment))
                .where(
                        comment.post.seq.eq(postId),
                        comment.parent.isNull())
                .groupBy(comment.seq,
                        comment.content,
                        comment.user.nickname,
                        comment.createdAt)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.seq.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.post.seq.eq(postId),
                        comment.parent.isNull()
                );

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne
        );
    }

    @Override
    public Page<CommentListResponse> commentReplyList(Long commentId, Pageable pageable) {
        QComment reply = new QComment("reply");
        List<CommentListResponse> content = queryFactory
                .select(Projections.fields(
                        CommentListResponse.class,
                        comment.seq.as("commentId"),
                        comment.content,
                        comment.user.nickname.as("nickname"),
                        reply.seq.count().as("replyCount"),
                        comment.createdAt
                )).from(comment)
                .join(comment.user, users)
                .leftJoin(reply)
                .on(reply.parent.eq(comment))
                .where(comment.parent.seq.eq(commentId))
                .groupBy(comment.seq,
                        comment.content,
                        comment.user.nickname,
                        comment.createdAt)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.seq.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.parent.seq.eq(commentId)
                );

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne
        );
    }
}
