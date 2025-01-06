# ERD 작성
```mermaid
---
config:
  theme: default
---
erDiagram
    USER {
        BIGINT user_id PK "Primary Key, 사용자 ID"
        VARCHAR user_name "사용자 이름"
        DECIMAL point_Balance "포인트 잔액"
        DATETIME created_at "사용자 생성 시각"
        DATETIME updated_at "사용자 수정 시각"
    }
    POINT_HISTORY{
        BIGINT point_history_id PK "Primary Key, 포인트 내역 ID"
        BIGINT user_id FK "Foreign Key, 사용자 ID"
        BIGINT payment_id FK "Foreign Key, 결제 ID"
        DECIMAL recharge_amount "충전 금액"
        DECIMAL balance_before "충전 전 잔액"
        DECIMAL balance_after "충전 후 잔액"
        ENUM recharge_method "충전 수단(CARD/CASH)"
        DATETIME created_At "충전 생성 시간"
    }
    QUEUE {
        BIGINT queue_id PK "Primary Key, 대기열 ID"
        BIGINT User_id FK "Foreign Key, 사용자 ID"
        ENUM queue_status "대기열 상태(WAIT/ACTIVE/EXPIRE)"
        DATETIME created_at "토큰 생성 시각"
        DATETIME expired_at "토큰 만료 시각"
        DATETIME removed_at "토큰 제거 시각"
    }
    CONCERT {
        BIGINT concert_id PK "Primary Key, 콘서트 ID"
        VARCHAR concert_name "콘서트 이름"
        DATETIME created_at "콘서트 생성 시각"
        DATETIME updated_at "콘서트 수정 시각"
    }
    SCHEDULE {
        BIGINT schedule_id PK "Primary Key, 스케줄 ID"
        BIGINT concert_id FK "Foreign Key, 콘서트 ID"
        DECIMAL price "가격"
        DATETIME concert_date "콘서트 일자"
        DATETIME booking_start "예약 가능 시작 시간"
        DATETIME booking_end "예약 종료 시간"
        INT remaining_ticket "잔여 티켓 수"
        INT total_ticket "잔여 티켓 수"
        DATETIME created_at "스케줄 생성 시각"
        DATETIME updated_at "스케줄 수정 시각"
    }
    SEAT {
        BIGINT seat_id PK "Primary Key, 좌석 ID"
        BIGINT schedule_id FK "Foreign Key, 스케줄 ID"
        VARCHAR seat_number "좌석 번호"
        ENUM seat_status "좌석 상태 (OCCUPIED/AVAILABLE)"
        DECIMAL seat_price "좌석 가격"
        DATETIME created_at "좌석 생성 시각"
        DATETIME updated_at "좌석 수정 시각"
    }
    RESERVATION {
        BIGINT reservation_id PK "Primary Key, 예약 ID"
        BIGINT user_id FK "Foreign Key, 사용자 ID"
        BIGINT seat_id FK "Foreign Key, 좌석 ID"
        ENUM reservation_status "예약 상태 (PENDING/PAID/CANCELLED)"
        DECIMAL seat_price "좌석 가격"
        DATETIME created_at "예약 생성 시각"
        DATETIME expired_at "예약 만료 시각"
    }
    PAYMENT {
        BIGINT payment_id PK "Primary Key, 결제 ID"
        BIGINT reservation_id FK "Foreign Key, 예약 ID"
        VARCHAR seat_number "좌석 번호"
        VARCHAR concert_name "콘서트 이름"
        DATETIME concert_date_time "콘서트 일시"
        DECIMAL payment_amount "결제 금액"
        ENUM payment_status "결제 상태 (COMPLETED/FAILED/CANCELLED)"
        DATETIME created_at "결제 생성 시각"
    }
    USER ||--o{ QUEUE : "1:N"
    USER ||--o{ POINT_HISTORY : "1:N"
    CONCERT ||--o{ SCHEDULE : "1:N"
    SCHEDULE ||--o{ SEAT : "1:N"
    SEAT ||--o{ RESERVATION : "1:N"
    RESERVATION ||--o{ PAYMENT : "1:N"
    USER ||--o{ RESERVATION : "1:N"
    PAYMENT ||--o{ POINT_HISTORY : "1:N"
```

# 테이블 간 관계 설명
## 1. 사용자 테이블  ↔ 대기열 테이블
    - 1:N 관계
    - 하나의 사용자는 여러 대기열 기록을 가질 수 있음.
      
## 2. 콘서트 테이블 ↔ 콘서트 스케줄 테이블 
    - 1:N 관계
    - 하나의 콘서트는 여러 개의 스케줄을 가질 수 있음.
    
## 3. 콘서트 스케줄 테이블 ↔ 좌석 테이블
    - 1:N 관계
    - 하나의 콘서트 스케줄에 여러 좌석이 배정됨.
    
## 4. 좌석 테이블 ↔ 예약 테이블 
    - 1:N 관계
    - 하나의 좌석은 여러 예약 상태를 가질 수 있으나, 동시에 점유될 수 없음.
    
## 5. 사용자 테이블 ↔ 예약 테이블 
    - 1:N 관계
    - 하나의 사용자는 여러 예약을 생성할 수 있음.
    
## 6. 예약 테이블 ↔ 결제 테이블 
    - 1:N 관계
    - 하나의 예약은 여러개의 결제 내역을 가질 수 있다. (결제 취소/실패)
    
## 7. 결제 테이블 ↔ 좌석 테이블 
    - 좌석 번호(`SeatNumber`)와 콘서트 관련 정보는 결제 기록에 저장되지만, 직접 참조되지는 않음.
    
## 8. 결제 테이블 ↔ 포인트 내역 테이블
    - 1:N
    - 하나의 결제는 여러 포인트 충전 내역을 가질 수 있다.

