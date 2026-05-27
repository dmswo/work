package spring.work.post.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.entity.Post;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static spring.work.comment.entity.QComment.comment;
import static spring.work.post.entity.QPost.post;
import static spring.work.user.entity.QUsers.users;

public class PostCustomRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;

    public PostCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostListResponse> postList(PostSearchCondition condition, Pageable pageable) {
        List<PostListResponse> content = queryFactory
                .select(Projections.fields(
                        PostListResponse.class,
                        post.seq,
                        post.title,
                        post.content,
                        post.viewCnt,
                        users.nickname,
                        post.createdUser,
                        post.createdAt
                ))
                .from(post)
                .join(post.user, users)
                .where(titleContains(condition.getTitle()),
                        createdAtGoe(condition.getStartDate()),
                        createdAtLoe(condition.getEndDate()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.seq.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post);

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne
        );
    }

    private Predicate titleContains(String title) {
        if (title == null) {
            return null;
        }
        return post.title.contains(title);
    }

    private BooleanExpression createdAtGoe(LocalDate startDate) {

        return startDate != null
                ? post.createdAt.goe(startDate.atStartOfDay())
                : null;
    }

    private BooleanExpression createdAtLoe(LocalDate endDate) {

        return endDate != null
                ? post.createdAt.loe(endDate.atTime(LocalTime.MAX))
                : null;
    }

    @Override
    public Optional<Post> findPostDetail(Long postId) {
        Post result = queryFactory
                .selectFrom(post)
                .leftJoin(post.comments, comment).fetchJoin()
                .where(post.seq.eq(postId))
                .distinct()
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
