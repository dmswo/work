# 🚀 Event-Driven Project

## 📖 프로젝트 소개

Spring Boot 기반의 게시판 프로젝트입니다.

단순 CRUD 구현을 넘어 **Apache Kafka 기반 이벤트 드리븐
아키텍처(Event-Driven Architecture)** 를 적용하고, **Outbox Pattern**,
**자동 재시도(Retry)**, **Dead Letter Queue(DLQ)**, **Consumer
멱등성(Idempotency)**, **Prometheus/Grafana 모니터링** 등을 구현하여
운영 환경을 고려한 안정적인 백엔드 시스템을 구축했습니다.

------------------------------------------------------------------------

# 🛠 Tech Stack

Category     Technology
  ------------ ---------------------------------
Language     Java 21
Framework    Spring Boot
Security     Spring Security, JWT
ORM          Spring Data JPA
Database     MySQL
Messaging    Apache Kafka
Monitoring   Micrometer, Prometheus, Grafana
Infra        Docker
Build        Gradle

------------------------------------------------------------------------

# ✨ 주요 기능

## 사용자 기능

-   회원가입 / 로그인(JWT)
-   게시글 CRUD
-   댓글 CRUD
-   게시글 좋아요
-   알림 이벤트 처리

## 관리자 기능

-   실패 이벤트 조회
-   실패 이벤트 수동 재처리
-   Spring Security + `@PreAuthorize` 기반 관리자 권한 제어

``` java
@PreAuthorize("hasRole('ADMIN')")
public void retryFailEvent(Long failEventId) {
    // ...
}
```

------------------------------------------------------------------------

# 🏗 시스템 아키텍처

``` text
                Client
                   │
                   ▼
          Spring Boot Application
                   │
        ┌──────────┴──────────┐
        │                     │
        ▼                     ▼
 Business Tables        Outbox Table
        │                     │
        └──────────┬──────────┘
                   ▼
          Scheduler (Polling)
                   │
                   ▼
            Kafka Producer
                   │
                   ▼
              Kafka Broker
                   │
                   ▼
            Kafka Consumer
                   │
         ┌─────────┴──────────┐
         │                    │
         ▼                    ▼
 processed_event       Business Logic
(Idempotency Check)
```

------------------------------------------------------------------------

# 📦 Outbox Pattern

## 적용 목적

비즈니스 데이터 저장과 Kafka 메시지 발행 사이의 장애로 인해 발생할 수
있는 데이터 정합성 문제를 해결하기 위해 Outbox Pattern을 적용했습니다.

## 처리 흐름

1.  비즈니스 데이터 저장
2.  Outbox Event 저장 (동일 트랜잭션)
3.  Scheduler가 `PENDING` 이벤트 조회
4.  Kafka 발행
5.  Broker ACK 수신 후 상태 변경
6.  실패 시 Retry 수행

------------------------------------------------------------------------

# 🔄 Retry 전략

-   `retryCount` 관리
-   `nextRetryAt` 기반 지연 재시도
-   최대 재시도 횟수 관리
-   재시도 실패 시 `FAILED` 상태 변경

------------------------------------------------------------------------

# ☠️ Dead Letter Queue (DLQ)

자동 재시도로도 처리되지 않는 이벤트는 Dead Letter Queue(DLQ)를 통해
별도 관리합니다.

장애 상황에서도 이벤트를 유실하지 않고 운영자가 분석 및 재처리할 수
있도록 설계했습니다.

------------------------------------------------------------------------

# ♻️ Consumer 멱등성(Idempotency)

Kafka의 At-Least-Once 특성을 고려하여 Consumer 멱등성을 구현했습니다.

-   모든 이벤트는 `event_id(UUID)`를 보유
-   `processed_event` 테이블에서 처리 여부 확인
-   `event_id`를 Primary Key로 관리하여 중복 처리 방지
-   이미 처리된 이벤트는 즉시 Skip

``` text
Kafka Message
      │
      ▼
event_id 확인
      │
      ├── 이미 처리됨 ──► Skip
      │
      ▼
Business Logic 수행
      │
      ▼
processed_event 저장
```

------------------------------------------------------------------------

# 🔒 동시성 제어

Scheduler는 다음 SQL을 사용하여 Outbox Event를 조회합니다.

``` sql
SELECT *
FROM outbox_event
WHERE status = 'PENDING'
  AND (
    next_retry_at IS NULL
    OR next_retry_at <= NOW()
  )
ORDER BY created_at
LIMIT :limit
FOR UPDATE SKIP LOCKED;
```

`FOR UPDATE SKIP LOCKED`를 통해 다중 스케줄러 환경에서도 동일 이벤트가
중복 처리되지 않도록 고려했습니다.

------------------------------------------------------------------------

# 🔐 인증 및 인가

-   JWT 기반 인증
-   Spring Security 적용
-   `@EnableMethodSecurity`
-   `@PreAuthorize` 기반 메서드 권한 제어
-   관리자 전용 API 보호

------------------------------------------------------------------------

# 📊 모니터링

Micrometer + Prometheus + Grafana를 활용하여 다음과 같은 지표를
모니터링합니다.

-   Kafka 발행 성공/실패
-   Retry 수행 현황
-   DLQ 적재 현황
-   Outbox 처리 상태
-   JVM 메모리 및 GC
-   CPU 및 HTTP 메트릭

------------------------------------------------------------------------

# 🛡 예외 처리

Global Exception Handler를 통해 Validation, Business, 인증/인가 및 기타
시스템 예외를 일관된 형식으로 처리합니다.

------------------------------------------------------------------------

# 🎯 프로젝트에서 해결한 핵심 과제

-   Outbox Pattern을 통한 데이터 정합성 확보
-   Kafka 장애 시 자동 Retry를 통한 이벤트 유실 최소화
-   DLQ 기반 실패 이벤트 관리
-   `processed_event` + `event_id(UUID)` 기반 Consumer 멱등성 보장
-   `FOR UPDATE SKIP LOCKED`를 활용한 중복 처리 방지
-   Spring Security 기반 역할별 접근 제어
-   Prometheus/Grafana 기반 운영 모니터링 구축
-   관리자 수동 재처리 기능 제공
