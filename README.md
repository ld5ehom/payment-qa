# Payment System Test-Driven Development

##  Project Overview
- Utilized: Java, Spring Boot, JUnit5, SpringBootTest, Groovy, MySQL 

-----
## ## Reference Site
- hexagonal-architecture: https://alistair.cockburn.us/hexagonal-architecture/
- junit: https://junit.org/junit5/
- assertj: https://assertj.github.io/doc/
- mockito: https://github.com/mockito/mockito
- Gradle plugins: https://plugins.gradle.org

-----
## Milestones
-   M1 : Core Feature Development
-   M2 : Stability, External Integration & Load Testing

-----
## Milestone 1 : Core Feature Development
### Task 1: Balance Feature Implementation
- **Issues** : [task-1-crud](https://github.com/ld5ehom/inventory-qa/tree/task-1-crud)
- **Details** :
    - **Payment System Basic CRUD Implementation** -> [f55c86b](https://github.com/ld5ehom/payment-qa/commit/f55c86b626d720fd51113ba99530a1de3630f0f9)
      - Implemented core wallet features using Java, Spring Boot, and JPA, including creation, retrieval, and balance update with business constraints (single wallet per user, non-negative balance, and 100,000 unit limit), ensuring transactional consistency and validating all scenarios through Spock-based unit tests with mocked repositories.
    - **Implemented Wallet API Core Features** 
      - Implemented wallet creation, retrieval, and balance update functionalities using Spring Boot. Developed WalletController and WalletService with RESTful endpoints and transactional logic. Verified service behavior with both unit (Spock) and integration (JUnit5) tests.

### Task 2: Payment Logic & Integration Testing
- **Issues** : [task-2-payment](https://github.com/ld5ehom/inventory-qa/tree/task-2-payment)
- **Details** :

## Milestone 2 : Stability, External Integration & Load Testing
### Task 3: Repository Validation with DataJpaTest

### Task 4: Integration Testing with SpringBootTest

### Task 5: End-to-End Testing Based on User Scenarios

