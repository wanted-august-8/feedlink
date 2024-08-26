# feedlink
본 서비스는 유저 계정의 해시태그(”#august8”) 를 기반으로 `인스타그램`, `스레드`, `페이스북`, `트위터(X)` 등 복수의 SNS에 게시된 게시물 중 유저의 해시태그가 포함된 게시물들을 하나의 서비스에서 확인할 수 있는 통합 Feed 어플리케이션 입니다.

</br>

## 목차
- [개발기간](#개발기간)
- [시나리오](#시나리오)
- [기술 스택](#기술-스택)
- [데이터베이스 모델링](#데이터베이스-모델링)
- [API 명세](#API-명세)
- [구현 기능](#구현-기능)
- [테스트](#테스트)
- [트러블 슈팅](#트러블-슈팅)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [팀원 소개](#팀원-소개)
  
  <br/>

## 개발기간
2024.08.20 - 2024. 08.26

<br/>

## 시나리오
- 유저는 계정(추후 해시태그로 관리), 비밀번호, 이메일로 **가입요청**을 진행합니다.
- 가입 요청 시, 이메일로 발송된 코드를 입력하여 **가입승인**을 받고 서비스 이용이 가능합니다.
- 서비스 로그인 시, 메뉴는 **통합 Feed** 단일 입니다.
- 통합 Feed 에선  `인스타그램`, `스레드`, `페이스북`, `트위터` 에서 유저의 계정이 태그된 글들을 확인합니다.
- 또는, 특정 해시태그(1건)를 입력하여, 해당 해시태그가 포함된 게시물들을 확인합니다.
- 유저는 본인 계정명 또는 특정 해시태그 일자별, 시간별 게시물 갯수 통계를 확인할 수 있습니다
  
  <br/>

## 기술 스택
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-17-blue) ![Static Badge](https://img.shields.io/badge/Springboot-3.2.8-red)<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/Mysql-8.0.1-blue) <br/>
배포 : ![Static Badge](https://img.shields.io/badge/Docker-039BC6) ![Static Badge](https://img.shields.io/badge/AWS-EC2-orange) ![Static Badge](https://img.shields.io/badge/Github-Actions-black) ![Static Badge](https://img.shields.io/badge/nginx-blue) <br/> ETC : ![Static Badge](https://img.shields.io/badge/Redis-red)

<br/>

## 데이터베이스 모델링
<img width="671" alt="image" src="https://github.com/user-attachments/assets/74777cb9-0e40-48e6-921c-b81d82caf81c">

<br/>

## API 명세
배포주소 : http://15.164.248.177:8080/swagger-ui/index.html#/

local 주소:  http://localhost:8080/swagger-ui/index.html#/

<br/>

## 구현 기능

1. 사용자 회원가입
    - 제약조건에 따라 계정, 이메일, 비밀번호를 입력 받아 회원가입
      <details>
        <summary>제약조건</summary>
        다른 개인 정보와 유사한 비밀번호는 사용할 수 없습니다.<br>
        비밀번호는 최소 10자 이상이어야 합니다.<br>
        통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다.<br>
        숫자로만 이루어진 비밀번호는 사용할 수 없습니다.<br>
        숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.<br>
        다른 개인 정보와 유사한 비밀번호는 사용할 수 없습니다.<br>
        이전 비밀번호와 동일하게 설정할 수 없습니다.<br>
        3회 이상 연속되는 문자 사용이 불가합니다.   
      </details>
2. 사용자 가입승인
    - 회원가입 후 가입승인을 완료해야 최종적으로 서비스 이용 가능
    - 계정, 비밀번호, 인증코드가 올바르게 입력되었을 시 가입승인
3. 사용자 로그인
    - JWT를 사용해 accessToken, refreshToken 발행
    - Redis에 refreshToken 및 로그아웃 시 accessToken 저장하여 관리
    - accessToken 기한 만료 시 refreshToken 사용해 accessToken 재발급
4. 게시물 목록
    - 쿼리 파라미터(hashtag, type, order_by, search_by, search, page_count, page) 사용하여 검색
        - hashtag: 미입력시 default값으로 계정명 사용
    - 게시물 내용(content) 최대 20자까지 출력
    - 해시태그와 게시물 사이의 중계 테이블을 활용하여 join한 결과를 list로 출력
5. 게시물 상세
    - 게시물 id(content_id)를 활용하여 상세 내용 검색
    - API 호출 시 해당 게시물의 view_count 1 증가
6. 통계
    - 쿼리 파라미터를 통한 해시태그별 게시물 통계
        - 통계 기준 : 일별, 시각별
        - 통계 대상 : 게시물 개수, 좋아요 합계, 조회수 합계, 공유 합계
        - 기간내 조회 되지 않는 통계 대상 값 0으로 반환

<br/>

## 프로젝트 진행 및 이슈 관리
### 프로젝트 진행
<img width="694" alt="image" src="https://github.com/user-attachments/assets/4f302418-3d4e-4b19-9514-18c4f5a87a91">
<img width="687" alt="image" src="https://github.com/user-attachments/assets/1384c632-5fd0-4e9e-ac3a-453254d32045">

<br/>

### 이슈 관리
<img width="705" alt="image" src="https://github.com/user-attachments/assets/d3e99ba5-9e1f-493a-b47a-be4995e2dcf2">

<br/>

## 팀원 소개 및 역할

### 👻김남은 [Github](https://www.github.com/perhona)

- 역할: AWS EC2 환경의 CI/CD 구축

### ⚽️김윤설 [Github](https://www.github.com/seoseo17)

- 역할 : JWT 기반으로 로그인, 로그아웃 구현

### 🐬김재령 [Github](https://www.github.com/Minerva08)

- 역할 : 게시글 기반 해시태그 통계

### 🐣손홍서 [Github](https://www.github.com/hongggs)

- 역할: 회원가입, 가입승인

### 🍀이현영 [Github](https://www.github.com/eter2)

- 역할: 게시글 목록 및 상세 정보 조회
