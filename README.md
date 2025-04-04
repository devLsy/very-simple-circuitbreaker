# 🧪 정말 간단한 서킷 브레이커 예제

## ✅ 환경 구성

- **API 1번**: 클라이언트 역할 (요청 발신)
- **API 2번**: 서버 역할 (요청 수신)
- API 1번은 API 2번을 `RestTemplate`을 통해 호출
- `Resilience4j`의 **서킷 브레이커** 적용

---

## ⚙️ 동작 흐름

### 1. 정상 상태 (`CLOSED`)
- API 2번이 정상적으로 응답하면 서킷 브레이커는 `CLOSED` 상태
- 요청과 응답이 정상적으로 이루어짐

### 2. 문제 발생 후 임계치 초과 (`OPEN`)
- API 2번이 죽거나 장애가 발생하여 일정 횟수 이상 실패
- `resilience4j` 설정에 따라 아래 조건 충족 시:
  - `slidingWindowSize`
  - `failureRateThreshold`
  - `minimumNumberOfCalls`
- 서킷 브레이커가 `OPEN` 상태로 전환됨
- 이후 요청은 API 2번을 호출하지 않고 **즉시 fallback 메서드 실행**

### 3. 일정 시간 경과 후 상태 변화 (`HALF_OPEN`)
- `waitDurationInOpenState` 시간이 지나면
- 서킷 브레이커는 `HALF_OPEN` 상태로 변경됨
- 제한된 횟수만 요청 허용:
  - `permittedNumberOfCallsInHalfOpenState` 설정 참고

### 4. 상태 복원 또는 재차 차단
- `HALF_OPEN` 상태에서 들어오는 요청을 기준으로:
  - **요청이 성공하면 → `CLOSED` 상태로 복귀**
  - **요청이 실패하면 → 다시 `OPEN` 상태로 전환**

---

## 💡 참고 사항

- `/actuator/health`, `/actuator/circuitbreakers` 등으로 상태 확인 가능
- `HALF_OPEN` 상태에서는 **요청이 실제로 들어와야 상태가 바뀜**
- `CLOSED` 상태에서도 실패 시 fallback 실행 가능  
  (`@CircuitBreaker` 어노테이션이 붙어 있을 경우)

---

## 🛠️ 주요 설정 예시 (application.yml)

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowType: COUNT_BASED
        slidingWindowSize: 5
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 5s
        slowCallDurationThreshold: 3s
        slowCallRateThreshold: 60
        permittedNumberOfCallsInHalfOpenState: 5
        automaticTransitionFromOpenToHalfOpenEnabled: true
