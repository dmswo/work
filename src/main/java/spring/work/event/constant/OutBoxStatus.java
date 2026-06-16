package spring.work.event.constant;

import lombok.Getter;

@Getter
public enum OutBoxStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    DEAD
}
