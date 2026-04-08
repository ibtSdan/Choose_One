# Choose One! - 실시간 투표 서비스


![Image](https://github.com/user-attachments/assets/b3e8b528-0aa3-4432-b4ae-1a1c8b934103)


<br>

## 소개
- **Choose One**은 사용자가 투표를 생성하고, 결과를 실시간으로 확인할 수 있는 웹 서비스입니다.  

- 실시간성과 성능을 고려한 백엔드 아키텍처를 직접 설계하고 구현했습니다.

<br>

## 개발 및 개선 이력
- 2024.11.30 ~ 2024.12.18: 핵심 기능 개발 완료
- 2025.01.11: AWS EC2 서버 배포 성공
- 2025.01.22 / 03.02 ~ 03.05 / 04.10 ~ 04.14: 성능 개선 및 리팩토링 진행
<br>


## 기술 스택
- **Backend**: Spring Boot, JPA, Java
- **Database & Caching**: MySQL
- **Infrastructure**: AWS EC2
- **Testing Tool**: Apache JMeter
<br>



## 주요 기능
 - 게시글 등록 및 조회 (로그인 유저만 가능)  
 
 - A/B 투표 선택 (로그인 유저만 가능)   
 
 - 실시간 WebSocket을 통한 투표 현황 반영   
 
 - 테스트 환경 분리(application-test.yml)   
 
<br>


## 트러블 슈팅 및 성능 개선 사례

실사용 환경을 가정해 Race Condition 등 주요 이슈를 테스트하고 개선한 사례입니다.
 
<br>

1. **WebSocket 도입** - UX 개선 및 서버 트래픽 감소
   - 새로고침 반복 → 사용자 경험 저하 + 서버 과부하
   - WebSocket(STOMP) 도입 → 실시간 반영 + 트래픽 감소
<br>

2. **Race Condition 해결** - 비관적 락으로 동시 저장 문제 해결
   - JMeter 500건 동시 요청 시, DB에는 473건만 저장되는 문제 발견
   - PostEntity에 Pessimistic Lock 적용 -> 정확히 500건 저장 확인
<br>


<br>
<br>


## 테스트 환경 구성

- 테스트용 DB 스키마 `choose_one_test` 사용
- `application-test.yml` 구성
- `-Dspring.profiles.active=test`로 실행
<br>
<br>
<br>
<br>


