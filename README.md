# Choose One! - 실시간 투표 서비스
<br>
<br>

![Image](https://github.com/user-attachments/assets/b3e8b528-0aa3-4432-b4ae-1a1c8b934103)

<br>
<br>
<br>

## 소개
- **Choose One**은 사용자가 투표를 생성하고, 결과를 실시간으로 확인할 수 있는 웹 기반 A/B 투표 서비스입니다.  
<br>

- **실시간성**, **동시성 제어**, **모니터링**, **성능 최적화**를 고려하였습니다.
<br>
<br>
<br>

## 개발 및 개선 이력
- 2024.11.30 ~ 2024.12.18: 핵심 기능 개발 완료
- 2025.01.11: AWS 서버 배포 성공
- 2025.01.22 / 03.02 ~ 03.05 / 04.10 ~ 04.14: 성능 개선 및 리팩토링 진행 (모니터링 환경 설정, Redis 캐시, WebSocket 구조 개선 등)
<br>
<br>
<br>
<br>

## 기술 스택
| 분류         | 기술                                                      |
|--------------|-----------------------------------------------------------|
| Backend      | Spring Boot, Spring Security, Spring Data JPA            |
| Database     | MySQL, Redis                                              |
| Infra/운영   | AWS EC2                            |
| Monitoring   | Prometheus, Grafana                                 |
| Testing Tool | Apache JMeter                                             |
<br>
<br>
<br>
<br>

## 주요 기능
 - 게시글 등록 및 조회 (로그인 유저만 가능)  
 
 - A/B 투표 선택 (로그인 유저만 가능)   
 
 - 실시간 WebSocket을 통한 투표 현황 반영   
 
 - Redis 캐시를 이용한 투표 수 저장 및 조회 속도 향상   
 
 - Prometheus + Grafana로 API 응답 시간 및 서버 모니터링   
 
 - 테스트 환경 분리(application-test.yml)   
 
<br>
<br>
<br>
<br>

## 트러블 슈팅 및 성능 개선 사례

실사용 환경을 가정해 Race Condition, 트래픽 최적화, 성능 병목 등 주요 이슈를 테스트하고 개선한 사례입니다.

[기술 블로그 상세 보기 (티스토리)](https://ibtsdan.tistory.com/1)   
<br>

1. **WebSocket 도입**  
   - 새로고침 반복 → 사용자 경험 저하 + 서버 과부하
   - WebSocket(STOMP) 도입 → 실시간 반영 + 트래픽 감소
<br>

2. **Race Condition 해결**
   - JMeter 500건 동시 요청 시, DB에는 473건만 저장되는 문제 발견
   - PostEntity에 Pessimistic Lock 적용 -> 정확히 500건 저장 확인
<br>

3. **Redis 캐시 도입**  
   - `/post/all` 호출 시 매번 DB 접근으로 성능 저하
   - Redis 적용 → 평균 응답 속도 2.05s → 1.65s (약 19.5% 개선)
<br>

4. **캐시 일관성 문제 해결**
   - DB와 Redis 간의 데이터 불일치 가능성 인지
   - 정기 동기화 (Lazy Sync) 적용 -> 일정 주기로 Redis 캐시를 DB 기준으로 덮어씌움
   - 향후 Kafka 등 메시지 큐 기반의 비동기 캐시 동기화 구조도 고려 중


<br>
<br>
<br>
<br>

## 테스트 환경 구성

- 테스트용 DB 스키마 `choose_one_test` 사용
- `application-test.yml` 구성
- `-Dspring.profiles.active=test`로 실행
- JMeter + Grafana를 통한 시각적 검증
<br>
<br>
<br>
<br>


