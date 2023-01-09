# MutsaSNS

## 💬 서비스 소개
<p align="center">
<img src="https://42place.innovationacademy.kr/wp-content/uploads/2020/09/likelion.png">
<br>
MutsaSNS는 멋쟁이 사자처럼 백엔드 스쿨 2기 파이널 프로젝트 작품으로, SNS(Social-Network-Service)를 제공하는 웹 사이트 입니다.
</p>

## 🔨 TECH STACK
![Spring Boot](https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/spring_security-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white)
![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white)

## 📃 SWAGGER
LINK : http://ec2-43-200-177-237.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/

## 📃 ERD
![erd](https://user-images.githubusercontent.com/71599552/211246134-b8359d17-9a03-4774-a39c-f268a8620484.PNG)

## 📔 Endpoints
|  구분  |  HTTP  |              URI              |          설명           |
|:----:|:------:|:-----------------------------:|:------------------------:|
| USER |  POST  |       api/v1/users/join       |         회원가입          |
| USER |  POST  |      api/v1/users/login       |      로그인 및 토큰 발급      |
| USER |  POST  | api/v1/users/{id}/role/change | 유저 권한 변경 (ONLY ADMIN) |
| POST |  GET   |         api/v1/posts          |      게시글 리스트 조회       |
| POST |  GET   |       api/v1/posts/{id}       |       게시글 상세 조회       |
| POST |  POST  |         api/v1/posts          |        게시글 등록         |
| POST |  PUT   |       api/v1/posts/{id}       |        게시글 수정         |
| POST | DELETE |       api/v1/posts/{id}       |        게시글 삭제         |

## 📔 Endpoint Example
### 회원 가입 [POST] /api/v1/users/join

**request body**
```json
{
    "userName":"testuser",
    "password":"password"
}
```

**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "userId": 12,
        "userName": "testuser"
    }
}
```

### 로그인 [POST] /api/v1/users/login

**request body**
```json
{
    "userName":"testuser",
    "password":"password"
}
```

**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "jwt": "eyJhbGciOiJIUzI1NiJ9...
    }
}
```

### 게시글 리스트 조회 [GET] /api/v1/posts

**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "content": [
            {
                "id": 26,
                "title": "관리자 입니다.",
                "body": "관리자 에요.",
                "userName": "root",
                "createdAt": "2022-12-27 14:51:43",
                "lastModifiedAt": "2022-12-27 14:51:43"
            },
            {
                "id": 11,
                "title": "새로운 글",
                "body": "제출합시다.",
                "userName": "root",
                "createdAt": "2022-12-27 13:33:00",
                "lastModifiedAt": "2022-12-27 13:33:00"
            }
        ],
        "pageable": {
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "pageSize": 20,
            "pageNumber": 0,
            "unpaged": false,
            "paged": true
        },
        "last": false,
        "totalElements": 47,
        "totalPages": 1,
        "size": 20,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "first": true,
        "numberOfElements": 20,
        "empty": false
    }
}
```
### 게시글 상세 조회 [GET] /api/v1/posts/{id}

**response body**

```json
{
    "resultCode": "SUCCESS",
    "result": {
        "id": 2,
        "title": "hello-new-title",
        "body": "hello-new-body",
        "userName": "kyeongrok34",
        "createdAt": "2022-12-27 00:52:16",
        "lastModifiedAt": "2022-12-27 00:52:16"
    }
}
```
### 게시글 등록 [POST] /api/v1/posts

**request body**

```json
{
    "title":"제목",
    "body":"내용"
}
```
**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "포스트 등록 완료",
        "postId": 54
    }
}
```
### 게시글 수정 [PUT] /api/v1/posts/{id}

**request body**
```json
{
    "title":"수정된 제목",
    "body":"수정된 내용"
}
```
**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "포스트 수정 완료",
        "postId": 54
    }
}
```
### 게시글 삭제 [DELETE] /api/v1/posts/{id}

**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "포스트 삭제 완료",
        "postId": 54
    }
}
```

### 유저 권한 변경 [POST] /api/v1/{id}/role/change

**request body**
```json
{
  "role": "ADMIN" | "USER"
}
```
**response body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "변경 되었습니다.",
        "role": "ADMIN"
    }
}
```

## 📃미션 요구사항 분석 & 체크리스트
### 체크리스트
- [x] 인증/인가 필터 구현
- [x] 로그인/회원가입 API 구현
- [x] 게시글 조회/등록/수정/삭제 API 구현
- [x] Swagger
- [x] ADMIN 회원의 등급 변경 기능
- [x] ADMIN 회원의 게시글 수정/삭제 구현

- [x] Gitlab을 통한 CI/CD 구축
- [ ] 화면 UI 개발 

### 1주차 미션 요약
**[접근 방법]**
---
- [x] **[인증/인가 필터 구현]**
- RESTful 애플리케이션의 인증/인가 처리를 위해 UsernamePasswordAuthenticationFilter 앞에 커스텀 필터 배치
- JWT(Json Web Token)을 통한, 인증 구현
- UserDetails / UserDetailsService를 활용하여 사용자의 정보를 가져오도록 구현
- "HMAC using SHA-256" 알고리즘을 사용하여 token 해싱 처리
- Token의 Claim에 UserRole(USER / ADMIN)이 담기도록 구현
- 인증이 실패할 경우의 처리를 담당하는 JwtAuthenticationEntryPoint 클래스 구현
- 권한이 없는 경우의 처리를 담당하는 JwtAccessDeniedHandler 클래스 구현
- 개인 공부 링크 : https://github.com/cmkxak/springboot-security-jwt


- [x] **[회원가입/로그인 API 구현]**
- Spring security를 활용하여 구현
- 회원,비회원 모두 접근되도록 구현
- [회원가입] 중복된 유저의 경우, 회원가입이 불가하도록 구현
- [회원가입] BCryptPasswordEncoder를 통한 암호화 된 비밀번호가 저장되도록 구현
- [로그인] userName or password 에러의 경우, 로그인이 불가하도록 구현
- [로그인] 로그인 성공 시, JWT가 발급되도록 구현


- [x] **[Gitlab을 통한 CI/CD 구축]**
- docker build와 push가 가능하도록 작성한 DockerFile을 통한 docker build 및 push 자동화
- 배포 서버(AWS EC2)에 Linux 기능인, Crontab을 통한 Container Registry(Gitlab)의 변경 사항 반영 자동화


- [x] **[Swagger]**
- API 명세를 편하게 관리할 수 있는 Swagger 라이브러리 추가


- [x] **[게시글 등록/조회/수정/삭제 API 구현]**
>게시글 등록
- 로그인 한 회원만 등록이 가능하도록 구현
- 다대일 연관관계 설정 (Post -> User)
- Post Entity 클래스에 게시글 생성을 담당하는 생성 메서드 구현
- 게시글 등록 시, 작성자가 설정되도록 구현
  <br>

>게시글 조회 (게시글 리스트 조회)
- @PageDefault 어노테이션을 통한 페이징 처리
    - defaultSize = 20, 생성일을 기준으로 내림차순 정렬
      <br>

> 게시글 조회 (상세 조회)
- post가 존재하지 않은 경우, 404 Error가 반환되도록 구현
  <br>

> 게시글 수정
- 인자로 넘어온 postId와 userName을 활용하여 작성자와 수정 요청자의 동등성 검증 로직 구현
- ADMIN의 경우, 게시글의 수정이 가능하도록 구현
- 위의 두가지 경우가 아니면, 권한이 없다는 에러가 반환되도록 구현
  <br>

> 게시글 삭제
- 인자로 넘어온, postId와 userName을 활용하여 작성자와 삭제 요청자의 동등성 검증 로직 구현
- ADMIN의 경우, 게시글의 삭제가 가능하도록 구현
- 위의 두가지 경우가 아니면, 권한이 없다는 에러가 반환되도록 구현
  <br>


- [x] **[등급업 기능(ONLY ADMIN)]**
- data.sql 파일을 통한, 애플리케이션 실행 시, 하나의 ADMIN 유저가 존재하도록 구현
- Spring Security의 hasRole() 메서드를 활용하여 admin의 경우만 가능하도록 구현


**[특이사항]**
- 테스트 코드를 설계함에 있어, 컨트롤러 테스트 코드와 서비스 테스트 코드의 확실한 차이를 아직 잘 모르겠다.
  <br>

- TDD라 함은, 테스트 코드를 먼저 개발하고, 작성된 테스트 코드를 바탕으로 실제 로직을 작성하는 순서로 알고 있다. 각 계층의 모든 테스트 코드를 작성하며 개발하는데에 많은 노력이 들어 갈 것 같은데, 주로 어떤 계층이 먼저 개발되는지 궁금하다. (혹은 개발 순서에 관한 관례가 있는지?)
  <br>

- 화면 개발 UI를 하지 못해 아쉽다. 리액트를 활용하여 FRONT-END도 구축해봐야 겠다.
  <br>
