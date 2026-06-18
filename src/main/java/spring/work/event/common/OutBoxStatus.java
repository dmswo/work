package spring.work.event.common;

import lombok.Getter;

@Getter
public enum OutBoxStatus {
    PENDING,        // 아직 처리 대기 (또는 retry 재진입)
    PROCESSING,     // worker가 가져가서 Kafka send 중
    DEAD_LETTER     // 재시도 한계 초과 (최종 실패)
}
