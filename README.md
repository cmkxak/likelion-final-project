# MutsaSNS

## 💬 서비스 소개
<p align="center">
<img src="https://42place.innovationacademy.kr/wp-content/uploads/2020/09/likelion.png">
MUTSASNS는 멋쟁이 사자처럼 백엔드 스쿨 2기 파이널 프로젝트 작품으로, SNS(Social-Network-Service)를 제공하는 사이트 입니다.
</p>

## 🔨 TECH STACK
![Spring Boot](https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/spring_security-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white)
![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white)


## 📃 SWAGGER
LINK : http://ec2-52-79-234-25.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/

## 📔 Endpoints
|  구분  |  HTTP  |        URI         | 설명 |
|:----:|:------:|:------------------:|:-----------------------------:|
| USER |  POST  | api/v1/users/join  | 회원가입
| USER |  POST  | api/v1/users/login | 로그인 및 토큰 발급
| POST |  GET   |    api/v1/posts    | 게시글 리스트 조회
| POST |  GET   | api/v1/posts/{id}  | 게시글 상세 조회
| POST |  POST  |    api/v1/posts    | 게시글 등록
| POST |  PUT   | api/v1/posts/{id}  | 게시글 수정
| POST | DELETE |  api/v1/posts/{id}  | 게시글 삭제
