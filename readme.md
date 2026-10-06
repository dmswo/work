# 🚀 Event-Driven Project

## 📖 프로젝트 소개

Spring Boot 기반의 게시판 프로젝트입니다.

단순 CRUD 구현을 넘어 **이벤트 기반 아키텍처(Event-Driven
Architecture)** 를 적용하여 서비스 간 결합도를 낮추고, **Outbox
Pattern + Apache Kafka**를 통해 데이터 정합성과 안정적인 메시지 발행을
보장하도록 설계했습니다.

또한 **Spring Security + JWT 인증/인가**, **자동 재시도(Retry)**, **Dead
Letter Queue(DLQ)**, **Consumer 멱등성(Idempotency)**, **Debezium 기반
CDC(Change Data Capture) + Elasticsearch**, **Prometheus/Grafana
모니터링** 등을 적용하여 실제 운영 환경을 고려한 백엔드 시스템을
구현했습니다.

여기에 더해 **Spring AI(OpenAI) 기반 게시글 검수/환영 메일 생성**,
**SSE 기반 실시간 알림**을 적용하여 단순 게시판을 넘어서는 부가 기능까지
확장했습니다.

------------------------------------------------------------------------

# 🛠 기술 스택

## Backend

-   Java 17
-   Spring Boot
-   Spring Data JPA

## Security

-   Spring Security
-   JWT

## Database

-   MySQL

## Messaging

-   Apache Kafka

## Search

-   Elasticsearch

## AI

-   Spring AI
-   OpenAI

## CDC

-   Debezium
-   Kafka Connect

## Monitoring

-   Prometheus
-   Grafana

## Infrastructure

-   Docker

## Build Tool

-   Gradle

------------------------------------------------------------------------

# 📂 주요 기능

## 사용자 기능

-   회원가입 / 로그인(JWT)
-   게시글 CRUD (작성자 본인 검증 포함)
-   댓글 CRUD (작성자 본인 검증 포함)
-   게시글 좋아요
-   Elasticsearch 기반 게시글 검색
-   SSE 기반 실시간 알림 기능
-   AI 기반 게시글 검수 (욕설/혐오 표현 등 부적절한 콘텐츠 작성 차단)
-   회원가입 시 AI가 작성한 환영 메일 비동기 발송

## 관리자 기능

-   실패 이벤트 조회
-   실패 이벤트 수동 재처리
-   `@PreAuthorize` 기반 관리자 권한 제어

``` java
@PreAuthorize("hasRole('ADMIN')")
public void retryFailEvent(Long failEventId)
```

------------------------------------------------------------------------

# 🏗 시스템 아키텍처

``` text
                    Client
                       │
                       ▼
             Spring Boot Application
                       │
         ┌─────────────┴─────────────┐
         │                           │
         ▼                           ▼
  Business Tables             Outbox Table
         │                           │
         │                    Scheduler(Polling)
         │                           │
         │                           ▼
         │                    Kafka Producer
         │                           │
         └──────────────┬────────────┘
                        ▼
                  Kafka Broker
            ┌───────────┴───────────────┐
            │                           │
            ▼                           ▼
    Business Consumer         Debezium CDC Pipeline
            │                           │
            ▼                           ▼
 processed_event              Elasticsearch
 (Idempotency)
```

------------------------------------------------------------------------

# 📦 Outbox Pattern

비즈니스 데이터와 Outbox Event를 동일 트랜잭션에서 저장한 뒤 Scheduler가
`PENDING` 이벤트를 조회하여 Kafka로 발행합니다.

이를 통해 DB 저장은 성공했지만 메시지 발행이 실패하는 상황에서 발생할 수
있는 데이터 정합성 문제를 최소화했습니다.

## 처리 흐름

1.  비즈니스 데이터 저장
2.  Outbox Event 저장 (동일 트랜잭션)
3.  Scheduler Polling
4.  Kafka 발행
5.  ACK 수신 후 상태 변경
6.  실패 시 Retry 수행

------------------------------------------------------------------------

# 🔄 Retry 전략

-   `retryCount` 관리
-   `nextRetryAt` 기반 지연 재시도
-   최대 재시도 횟수 관리
-   최대 횟수 초과 시 `FAILED` 처리

------------------------------------------------------------------------

# ☠️ Dead Letter Queue (DLQ)

자동 재시도로도 처리되지 않는 이벤트는 DLQ를 통해 관리하며 운영자가
장애를 분석하거나 수동 재처리를 수행할 수 있도록 구현했습니다.

------------------------------------------------------------------------

# ♻️ Consumer 멱등성(Idempotency)

Kafka의 At-Least-Once 특성을 고려하여 Consumer 멱등성을 구현했습니다.

-   `event_id(UUID)` 기반 이벤트 식별
-   `processed_event` 테이블에서 처리 여부 확인
-   `event_id`를 Primary Key로 관리
-   이미 처리된 이벤트는 Skip

``` text
Kafka Message
      │
      ▼
event_id 조회
      │
      ├── 존재 → Skip
      │
      ▼
Business Logic 수행
      │
      ▼
processed_event 저장
```

------------------------------------------------------------------------

# 🔍 Elasticsearch + Debezium CDC

검색 성능 향상과 데이터 동기화를 위해 Debezium 기반 CDC를 적용했습니다.

``` text
MySQL
   │
   ▼
Debezium
   │
   ▼
Kafka
   │
   ▼
CDC Consumer / Connector
   │
   ▼
Elasticsearch
```

DB 변경 사항이 이벤트로 전파되어 Elasticsearch 인덱스에 반영되므로
별도의 애플리케이션 동기화 로직 없이 최신 검색 데이터를 유지할 수
있습니다.

------------------------------------------------------------------------

# 🤖 AI 연동 (Spring AI + OpenAI)

Spring AI의 `ChatClient`를 통해 OpenAI 모델을 두 가지 시나리오에서
활용합니다.

-   **게시글 검수**: 게시글 작성 요청을 처리하는 과정에서 AI에게 제목/
    본문을 전달해 욕설, 비방, 혐오 표현, 음란성 등 부적절한 콘텐츠
    여부(`passed`)와 판단 근거(`reason`)를 JSON으로 응답받아 검수에
    실패하면 게시글 등록을 차단합니다.
-   **환영 메일 생성**: 회원가입 이벤트(Kafka)를 소비하는 시점에 AI가
    닉네임 기반으로 환영 메일 제목/본문을 생성하고, 이를 메일 발송에
    사용합니다. 회원가입 요청 흐름과 분리되어 있어 AI 응답 지연이
    가입 API 응답 속도에 영향을 주지 않습니다.

------------------------------------------------------------------------

# 🔒 동시성 제어

Scheduler는 `FOR UPDATE SKIP LOCKED`를 사용하여 Outbox 이벤트를
조회함으로써 다중 스케줄러 환경에서도 동일 이벤트의 중복 처리를
방지하도록 설계했습니다.

------------------------------------------------------------------------

# 🔐 인증 및 인가

-   JWT 기반 인증
-   Spring Security
-   `@EnableMethodSecurity`
-   `@PreAuthorize` 기반 메서드 권한 제어
-   관리자 API 접근 제한
-   게시글/댓글 수정·삭제 시 작성자 본인 여부 검증 (IDOR 방지)
-   Access Token / Refresh Token의 Redis TTL을 각 토큰 수명에 맞게 분리 관리

------------------------------------------------------------------------

# 📊 모니터링

Prometheus와 Grafana를 통해 다음 항목을 모니터링합니다.

-   Kafka 발행 성공/실패
-   Retry 현황
-   DLQ 적재 현황
-   Outbox 처리 상태
-   JVM 및 시스템 메트릭
-   HTTP 요청 메트릭

------------------------------------------------------------------------

# 🛡 예외 처리

Global Exception Handler를 통해 Validation, Business Exception,
인증/인가 예외 및 기타 시스템 예외를 일관된 응답 형식으로 처리합니다.

------------------------------------------------------------------------

# 🎯 프로젝트에서 중점적으로 해결한 문제

-   Outbox Pattern을 통한 데이터 정합성 확보
-   Kafka 장애 시 자동 Retry를 통한 이벤트 유실 최소화
-   DLQ 기반 실패 이벤트 관리
-   `processed_event` + `event_id(UUID)` 기반 Consumer 멱등성 보장
-   `FOR UPDATE SKIP LOCKED`를 활용한 중복 처리 방지
-   Debezium CDC를 활용한 Elasticsearch 실시간 동기화
-   Elasticsearch 기반 검색 성능 향상
-   Spring Security 기반 역할(Role)별 접근 제어
-   Prometheus/Grafana 기반 운영 모니터링 구축
-   관리자 수동 재처리 기능 제공
-   게시글/댓글 소유자 검증을 통한 IDOR 취약점 방지
-   AI(Spring AI + OpenAI)를 활용한 게시글 콘텐츠 검수 및 환영 메일 생성 자동화
