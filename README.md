# README.md

## **프로젝트 개요**

- 제목 : Chattravel (챗트래블)
- 소개 : 맞춤형 국내 여행 코스 생성 챗봇 서비스 입니다.  챗봇과의 대화 형식으로 간편하게 국내 여행코스를 완성시킬 수 있습니다.

<br>

## **기능 목록**

- 유저 여행 스타일 생성기능
- 챗봇 채팅 기능
- 여행 코스 생성 및 수정 기능
- 유저 피드백 기능

<br>

**서비스 전체 플로우**

![Image](https://github.com/user-attachments/assets/6188cd8e-bc66-4815-93c6-2ada5da586ba)

<br>

## **기술 스택**

- Spring Boot
- MySQL

<br>

**전체 기술 스택**

![image](https://github.com/user-attachments/assets/0e60c087-2b83-4efc-ab95-b1e460929856)

<br>

## **설치 및 실행 방법**

```bash
설치 (터미널)
git clone https://github.com/onezerosoft/chattravel-server
cd chattravel-server
./gradlew build   # 또는 ./gradlew clean build
./gradlew bootRun
```

<br>

## ERD 설계

<img width="951" alt="Image" src="https://github.com/user-attachments/assets/521bbb97-1c30-4925-8a24-fc8c16e6f1ae" />

<br>

## API 설계

🔗 [API 명세서] [API 명세서 ](https://www.notion.so/API-0657f3fe397249f8a5f7d78e99fadffa?pvs=21) 

<br>

## 고도화 계획

- 유저 인증 기능 추가
- **Github Actions**를 활용한 CI/CD구축

<br>

## 트러블 슈팅

<aside>

#### ❗ 프론트 서버와의 통신 연결과정에서 발생한 CORS 오류

로컬 테스트와 달리, 실제 배포 서버 간 통신은 까다로운 설정이 필요했습니다. DNS설정, SSL 인증서 발급, Nginx 리버스 프록시 설정 등 HTTPS 통신에 필요한 사항들을 학습하며 진행했지만, 프론트 서버의 요청에서 Origin과 관련된 오류가 계속 발생하였습니다.

원인은 **스프링 프로젝트와 Nginx의 중복된 헤더 설정**으로 인한 충돌과, Nginx 내의 잘못된 **다중 도메인 설정법**이었습니다. 특히 프론트의 실제 배포 서버와 테스트 환경의 요청을 허용하기 위해, Nginx에서는 도메인 별로 동적으로 설정해줘야 함을 배웠으며,  스프링에서는 리스트로 전달한 도메인들을 동적으로 처리해주는 간편한 기능이 있음을 알게되었습니다.

</aside>

<aside>

#### ❗ AWS EC2의 메모리 관리

AWS 프리티어 계정의 제한된 메모리로 인해 시스템이 자주 렉이 걸리고, 서비스 응답이 느려지는 문제가 발생하였습니다.
해당 문제를 해결하기 위해, **스왑 파일**을 설정하여 가상 메모리를 제공해주는 방식을 적용하였고, 이 후에는 시스템이 보다 안정적으로 동작하게 되었습니다. 이를 통해 메모리 관리가 서비스 유지와 안정성에 매우 중요함을 깨달았습니다.

</aside>
