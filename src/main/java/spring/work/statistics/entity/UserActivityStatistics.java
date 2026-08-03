package spring.work.statistics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.entity.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "user_activity_statistics",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "statistics_date"}
                )
        }
)
public class UserActivityStatistics extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDate statisticsDate;
    private Long postCount = 0L;
    private Long commentCount = 0L;
    private Long likeCount = 0L;

    public void increasePostCount() {
        this.postCount++;
    }

    public void decreasePostCount() {
        this.postCount--;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if(this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public static UserActivityStatistics from(Long userId, LocalDate date) {
        return UserActivityStatistics.builder()
                .userId(userId)
                .statisticsDate(date)
                .postCount(0L)
                .commentCount(0L)
                .likeCount(0L)
                .build();
    }
}
