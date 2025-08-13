# 하나로 2차 개인 프로젝트

## 1. 배치 작업 (Batch)

이 프로젝트는 `stats` 디렉토리 내에서 일일 판매 통계를 계산하기 위한 배치 작업을 포함하고 있습니다.

## 1. 배치 작업 (Batch)

이 프로젝트는 Spring Batch 프레임워크를 사용하여 `stats` 디렉토리 내의 로직으로 일일 판매 통계를 계산합니다. Spring Batch는 대용량 데이터 처리에 안정성과 신뢰성을 제공합니다.

- **아키텍처**:
    - **`BatchConfig`**: Spring Batch의 `Job`과 `Step`을 설정하는 클래스입니다. `@EnableBatchProcessing`으로 활성화됩니다.
    - **`dailySaleJob`**: 통계 집계를 위한 전체 배치 `Job`입니다.
    - **`dailySaleStep`**: `Tasklet`을 사용하여 `DailySaleBatchService`의 통계 집계 메소드(`runDaily`)를 실행하는 간단한 `Step`으로 구성되어 있습니다.
- **`DailySaleBatchService`**: 실제 통계 계산 로직을 담고 있는 서비스입니다.
- **실행 방식**: `BatchJobScheduler`에 의해 매일 자정에 `dailySaleJob`이 실행됩니다.

## 2. 스케줄링 (Scheduling)

프로젝트는 두 가지 목적의 스케줄링을 사용합니다.

### 2.1. 배치 작업 실행
- **`BatchJobScheduler`**:
    - **실행 주기**: `@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")` 설정에 따라, 매일 자정(00:00)에 `dailySaleJob` 배치 작업을 실행합니다.

### 2.2. 주문 상태 변경
- **`OrderStatusScheduler`**:
    - **실행 주기**: `@Scheduled(fixedRate = 60_000)` 설정에 따라, 1분마다 실행됩니다.
    - **주요 기능**: 시간의 흐름에 따라 주문의 상태를 다음 단계로 자동으로 변경합니다.
        - **결제완료 (`PAID`)** -> 5분 후 -> **배달준비 (`READY`)**
        - **배달준비 (`READY`)** -> 15분 후 -> **배달중 (`TRANSIT`)**
        - **배달중 (`TRANSIT`)** -> 60분 후 -> **배달완료 (`DELIVERED`)**

- **활성화**: `HanaroApplication.java` 파일에 `@EnableScheduling` 어노테이션이 있어 스케줄링 기능이 활성화되어 있습니다.

- **실행 시점**: `@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")` 설정에 따라, 매일 자정(00:00)에 실행되어 전날의 판매 통계를 집계합니다.

- **주요 기능**:
    - 전날의 모든 주문을 기반으로 통계를 집계합니다.
    - 일일 총 판매액(`DailyTotalStat`)과 일일 상품별 판매 통계(`DailyItemStat`)를 데이터베이스에 저장합니다.

## 2. 스케줄링 (Scheduling)

프로젝트는 `order` 디렉토리 내에서 주문 상태를 자동으로 변경하는 스케줄링 작업을 포함합니다.

- **`OrderStatusScheduler`**:
    - **실행 주기**: `@Scheduled(fixedRate = 60_000)` 설정에 따라, 1분마다 실행됩니다.
  
    - **주요 기능**: 시간의 흐름에 따라 주문의 상태를 다음 단계로 자동으로 변경합니다.
        - **결제완료 (`PAID`)** -> 5분 후 -> **배달준비 (`READY`)**
        - **배달준비 (`READY`)** -> 15분 후 -> **배달중 (`TRANSIT`)**
        - **배달중 (`TRANSIT`)** -> 60분 후 -> **배달완료 (`DELIVERED`)**

- **활성화**: `HanaroApplication.java` 파일에 `@EnableScheduling` 어노테이션이 있어 스케줄링 기능이 활성화되어 있습니다.

## 3. Actuator

Spring Boot Actuator가 적용되어 있어 애플리케이션의 상태를 모니터링하고 관리할 수 있습니다.

- **설정**: `application.properties` 파일의 `management.endpoints.web.exposure.include=*` 설정으로 인해 모든 Actuator 엔드포인트가 HTTP를 통해 노출되어 있습니다.

    - **주의**: 개발 환경에서는 편리하지만, 프로덕션 환경에서는 보안을 위해 필요한 엔드포인트만 선별적으로 노출하는 것이 좋습니다.

- **주요 엔드포인트**:
    - `GET /actuator/health`: 애플리케이션의 상태를 확인합니다. (UP/DOWN)
    - `GET /actuator/info`: 애플리케이션 정보를 보여줍니다.
    - `GET /actuator/beans`: 애플리케이션에 등록된 모든 Spring Bean 목록을 보여줍니다.
    - `GET /actuator/mappings`: 모든 `@RequestMapping` 경로를 보여줍니다.
    - `GET /actuator/configprops`: 모든 `@ConfigurationProperties`를 보여줍니다.
    - `GET /actuator/env`: 애플리케이션의 환경 변수를 보여줍니다.
    - `GET /actuator/scheduledtasks`: 예약된 모든 스케줄링 작업을 보여줍니다.
    - `GET /actuator/loggers`: 애플리케이션의 로거와 그 레벨을 확인하고 동적으로 변경할 수 있습니다.

## 4. 로깅 (Logging)

로깅은 `logback-spring.xml` 설정을 따르며, 목적에 따라 로그 파일이 분리되어 있습니다. 모든 로그는 기본적으로 콘솔에도 출력됩니다.

- **로그 파일 위치**: 프로젝트 루트 디렉토리 아래의 `logs/` 폴더에 생성됩니다.

- **로그 파일 종류**:
    1.  **`business_product.log`**:
        - **패키지**: `hanaro.item`
        - **내용**: 상품(Item) 관련 비즈니스 로직에 대한 로그가 기록됩니다.
    2.  **`business_order.log`**:
        - **패키지**: `hanaro.order`
        - **내용**: 주문(Order) 관련 비즈니스 로직에 대한 로그가 기록됩니다. 
    3.  **`application.log`**:
        - **내용**: 위 두 경우를 제외한 모든 애플리케이션 로그가 기록됩니다.

