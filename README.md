# Payment System with Test-Driven Development

## Project Overview
- Utilized: Java, Spring Boot, MySQL, JUnit5, SpringBootTest, Groovy (Spock Framework)

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
- **Issues** : [task-1-crud](https://github.com/ld5ehom/payment-qa/tree/task-1-crud)
- **Details** :
    - **Payment System Basic CRUD Implementation** -> [f55c86b](https://github.com/ld5ehom/payment-qa/commit/f55c86b626d720fd51113ba99530a1de3630f0f9)
      - Implemented core wallet features using Java, Spring Boot, and JPA, including creation, retrieval, and balance update with business constraints (single wallet per user, non-negative balance, and 100,000 unit limit), ensuring transactional consistency and validating all scenarios through Spock-based unit tests with mocked repositories.
    - **Implemented Wallet API Core Features** -> [d9c7200](https://github.com/ld5ehom/payment-qa/commit/d9c7200a7077d1ec33c28acfedf868d2dbc89108)
      - Implemented wallet creation, retrieval, and balance update functionalities using Spring Boot. Developed WalletController and WalletService with RESTful endpoints and transactional logic. Verified service behavior with both unit (Spock) and integration (JUnit5) tests.

### Task 2: Transaction Domain Logic Implementation & Integration Test
- **Issues** : [task-2-transaction](https://github.com/ld5ehom/payment-qa/tree/task-2-transaction)
- **Details** :
  - **Transaction Logic & Integration Test Implementation**
    - Implemented core transaction features including charge and payment flows, integrated with wallet balance updates. Added validation for duplicate transactions, and completed both Spock-based unit testing and JUnit-based integration testing for all transaction logic.

## Milestone 2 : Stability, External Integration & Load Testing
### Task 3: Repository Validation with DataJpaTest

### Task 4: Integration Testing with SpringBootTest

### Task 5: End-to-End Testing Based on User Scenarios

