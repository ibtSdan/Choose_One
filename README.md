# Choose One!   🆚
### "두 가지 선택 중 고민될 때! 실시간 투표로 빠르게 결정!"
![choose_one_play](https://github.com/user-attachments/assets/2c53a82c-b068-45eb-98da-4e4250a201f0)
<br>
<br>

<br>


## 1. 개요
- "어떤 선택이 더 좋을까?" 고민될 때, 빠르게 의견을 모을 수 있는 **실시간 투표 서비스** 입니다.
- **A VS B**, 두 가지 선택지 중 하나를 고르면 투표가 완료됩니다.
- **실시간 투표 현황**을 확인하며, 다른 사람의 의견을 바로 볼 수 있습니다.
- **더 나은 선택을 돕기 위해** 이 개인 프로젝트를 개발했습니다.

<br>
<br>
<br>

## 2. 기술 스택

| 구분         | 사용 기술                           | 역할 |
|-------------|-----------------------------------|----------------------|
| **Backend & DB**  | Spring Boot, Redis, MySQL        | REST API 개발, 데이터 저장, 캐싱 |
| **Infra**    | AWS EC2, Docker                  | 배포 및 운영 환경 구성 |
| **Monitoring** | Prometheus, Grafana            | 서버 상태 및 성능 모니터링 |

- **Spring Boot + MySQL + Redis로 백엔드 구성**  
- **Docker를 활용해 AWS EC2에서 배포 및 운영**  
- **Prometheus & Grafana로 서비스 모니터링 구축**

  <br>
  <br>
  <br>

## 3. 기능
✅ **사용자 인증&보안**     

- JWT를 활용한 사용자 인증
- Security 권한 설정 (Admin만 글 삭제 가능)
<br>

✅ **익명 투표&글 작성** 

- **익명**으로 글 작성 가능
- **익명** 투표 기능 제공
- 모든 글 조회 & 나의 글 조회
- 중복 투표 방지
<br>

✅ **실시간 투표**   

- 웹소켓(WebSocket)을 활용한 실시간 투표 현황 확인
<br>

✅ **서버 모니터링**   

- Prometheus & Grafana를 활용한 서버 모니터링


<br>
<br>
<br>

## 4. 기술적 구현
✅ **사용자 인증 & 보안**   

- JWT 기반 사용자 인증 및 Access Token 발급
- Refresh Token을 DB에 저장하여 보안 강화
- **Spring Security 권한 설정** → **Admin만 게시글 삭제 가능**
<br>

✅ **실시간 투표 기능**   

- **Spring WebSocket + STOMP 활용** → 실시간으로 투표 현황 업데이트
<br>

✅ **성능 최적화**   

- **투표수를 Redis에 캐싱**하여 DB 부하 최소화
- **글 조회 시, Redis에서 투표수를 가져와 빠른 응답 속도 제공**
<br>

✅ **서버 모니터링**   

- Prometheus를 사용하여 **서버 상태 및 API 응답 시간 수집**  
- Grafana 대시보드를 통해 **시각적인 모니터링 환경 제공**

<br>
<br>
<br>

## 5. 프로젝트 결과
- **Redis 캐싱 적용 후, CPU 사용량 100% → 50% 감소!** (전체 글 조회 성능 개선)
- **DB 부하 감소 → 평균 응답 속도 개선!**

<br>
<br>
<br>

## 6. 기타
- 이 프로젝트는 개인 프로젝트로 진행되었으며, 백엔드 설계부터 배포까지 직접 구현했습니다.

<br>
<br>

- API 명세서 간단 요약

| HTTP Method | Endpoint | 설명 |
|-------------|-----------|-------------|
| **POST** | `/user/signup` | 회원가입 |
| **POST** | `/user/login` | 로그인 (JWT 발급) |
| **POST** | `/post/create` | 글 작성 (JWT 필요) |
| **POST** | `/vote/create` | 투표하기 |
| **GET** | `/post/{postId}` | 세부 글 조회 & 실시간 투표 현황 확인 |
| **POST** | `/token/reissue` | Refresh Token을 이용한 Access Token 재발급 |

**API 명세는 포트폴리오의 Swagger 캡처에서 확인 가능합니다!**   

<br>
<br>

- **개발 기간**
  
  - API 개발: 2024.11.30 ~ 2024.12.18
  - 서버 배포: 2025.01.11 🎉
  - 모니터링 및 성능 개선: 2025.01.22 ~ 2025.03.05
