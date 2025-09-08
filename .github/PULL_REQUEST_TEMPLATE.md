## 📌 작업 내용

**feat: 초기 설정 및 JPA 엔티티 구현**

---

## 📋 세부 내용

- 프로젝트 초기 설정:
    - MySQL 데이터베이스(`commerce_db`) 연결 설정을 `application.properties`에 추가했습니다.
    - `root` 계정을 사용하여 데이터베이스에 연결했습니다.

- JPA 엔티티 정의:
    - ERD에 따라 `User`, `Product`, `Cart`, `CartItem` 엔티티와 `Role` enum 클래스를 구현했습니다.
    - `@PrePersist`를 사용하여 엔티티 생성 시 `createdAt` 필드가 자동으로 기록되도록 했습니다.

- **테스트:**
    - `spring.jpa.hibernate.ddl-auto=update` 설정을 통해 애플리케이션 실행 시 데이터베이스에 테이블이 자동으로 생성되는 것을 확인했습니다.

---

## 💡 참고사항

- 이 PR은 다음 기능(회원가입, 로그인 등등) 개발의 기반을 마련하는 작업입니다.

##  To Reviewers

-ERD를 정의한대로 했는지 검토해주세요
