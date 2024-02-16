# Spring Boot Security ✨
spring boot security 를 사용한 JWT Cookie 로그인, 회원가입 및 redis 적용 프로젝트 입니다
<br/>
<br/>

## 사용 스택
JAVA 11 <br/>
Spring boot 2 <br/>
MariaDB <br/>
Redis <br/>
JPA <br/>
JUnit <br/>
<br/>

## 적용 기술
- JPA를 사용한 회원가입
- 로그인 시 access token, refresh token cookie 에 저장
- 로그인 시 refresh token 및 userid redis에 저장
- custom annotation인 @LoginUser가 달린 api 호출시 쿠키 검증 및 유저 검증


