
# TUS - Technological University of the Shannon
## Athlone Campus

---
**Student**: Weverton de Souza Castanho  
**Email**: wevertonsc@gmail.com  
**Course**: Object-Oriented Programming I (AL_KCNCM_9_1) 29468  
**Date**: November 2025
---
# Card Authorizer Application

Credit card transaction authorization system built with Spring Boot, implementing Luhn algorithm validation, brand identification, and balance management.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Development](#development)
- [License](#license)

---

## Overview

The Card Authorizer is an enterprise-grade Spring Boot application designed to process and authorize credit card transactions. The system validates card numbers using the Luhn algorithm, identifies card brands (Visa, Mastercard, etc.), and manages transaction authorization based on available balance and card validation rules.

This application was developed as part of the Object-Oriented Programming I course at Technology University Shannon - Athlone (TUS).

---

## Features

### Core Functionality

- **Card Validation**: Luhn algorithm implementation for credit card number validation
- **Brand Identification**: Automatic detection of card brands (Visa, Mastercard, American Express, Discover)
- **Transaction Authorization**: Real-time authorization based on card validation and balance availability
- **Balance Management**: Automatic balance updates after successful transactions
- **Transaction History**: Complete audit trail of all operations
- **Cardholder Verification**: Validates cardholder name, email, expiration date, and CVV

### Technical Features

- RESTful API architecture
- H2 in-memory database for development
- JPA/Hibernate for data persistence
- Comprehensive unit and integration testing (127 tests)
- Spring Boot auto-configuration
- Lombok for reduced boilerplate code

---

## Technology Stack

### Backend
- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Data JPA**: Database abstraction layer
- **Spring Web**: REST API implementation

### Database
- **H2 Database**: In-memory database for development
- **Hibernate**: ORM framework

### Build & Dependencies
- **Maven**: Dependency management and build automation
- **Lombok**: Code generation for DTOs and entities

### Testing
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework
- **Spring Test**: Integration testing support
- **MockMvc**: REST API testing

---

## Architecture

### Design Pattern
The application follows a layered architecture pattern:

![SystemArchitecture.png](\uml%2Fimages%2FSystemArchitecture.png)

### Core Components

#### Models
- **Card**: Credit card entity with number, expiration, CVV, limits, balance
- **Client**: Cardholder information
- **Brand**: Card brand details (Visa, Mastercard, etc.)
- **History**: Transaction audit trail
- **Messages**: Operation result messages

#### Services
- **CardService**: Card retrieval and management
- **OperationService**: Transaction processing and validation

#### Controllers
- **CardController**: Card information endpoints
- **OperationController**: Transaction processing endpoints

#### Utilities
- **JavaLuhnAlgorithm**: Credit card number validation
- **CreditCardBrand**: Brand identification logic

![ClassDiagram.png](/uml%2Fimages%2FClassDiagram.png)

---

## Getting Started
![DeploymentDiagram.png](/uml%2Fimages%2FDeploymentDiagram.png)
### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)
- Postman (for API testing)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd card_authorizer
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on port 80 by default.

### Configuration

The application uses the following default configuration in `application.properties`:

```properties
spring.application.name=card_authorizer

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:card_authorizer
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
logging.level.org.hibernate.SQL= DEBUG

# Server Port Configuration
server.port=8088
```

To change the server port, modify `server.port` in `application.properties`.

---

## API Documentation

### Base URL
```
http://localhost:8088/api/v1
```

### Endpoints

#### 1. Get Card by Number

Retrieves card information by card number.

**Endpoint**: `GET /card/{number}`

**Path Parameters**:
- `number` (string, required): Credit card number

**Response**: Card object with client and brand relationships

**Example Request**:
```bash
GET http://localhost:80/api/v1/card/4532015112830366
```

**Example Response** (200 OK):
```json
{
    "id": 1,
    "number": "4532015112830366",
    "expiration": "12/25",
    "cvv": "123",
    "limits": 5000.0,
    "balance": 3000.0,
    "client": {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@example.com"
    },
    "brand": {
        "id": 1,
        "name": "Visa"
    }
}
```

**Error Response** (404 Not Found):
```
Card not found with number: 9999999999999999
```

---

#### 2. Process Transaction

Processes a credit card transaction (purchase, withdrawal, etc.).

**Endpoint**: `POST /operation`

**Request Body**: OperationDAO object

**Fields**:
- `name` (string, required): Cardholder name
- `number` (string, required): Credit card number
- `expiration` (string, required): Card expiration date (MM/YY)
- `cvv` (string, required): Card CVV code
- `email` (string, required): Cardholder email
- `brand` (string, required): Card brand
- `type` (string, required): Operation type (e.g., "PURCHASE")
- `value` (float, required): Transaction amount

**Response**: Messages object with operation result

**Example Request**:
```bash
POST http://localhost:80/api/v1/operation
Content-Type: application/json

{
    "name": "Plato of Athens",
    "number": "4532015112830366",
    "expiration": "12/25",
    "cvv": "123",
    "email": "plato.of.athens@macunaima.com",
    "brand": "Visa",
    "type": "PURCHASE",
    "value": 100.00
}
```

**Success Response** (200 OK):
```json
{
    "id": 6,
    "message": "SUCCESS",
    "description": "Operation completed successfully"
}
```

**Error Response** (200 OK with error message):
```json
{
    "id": 1,
    "message": "ERROR",
    "description": "Invalid card number"
}
```

**Error Response** (500 Internal Server Error):
```
Error processing operation: <error details>
```

---

### Validation Rules

The system applies the following validation rules for transactions:

1. **Luhn Algorithm**: Card number must pass Luhn algorithm validation
2. **Card Existence**: Card must exist in database
3. **Cardholder Name**: Must match the name on record
4. **Email**: Must match the email on record
5. **Expiration Date**: Must match the expiration date on record
6. **CVV**: Must match the CVV on record
7. **Brand**: Must match the card brand
8. **Balance**: Transaction amount must not exceed available balance

### Error Message IDs

The system uses the following message IDs for operation results:

- **ID 1**: Invalid card number (Luhn algorithm failed or card not found)
- **ID 2**: Invalid cardholder information (name or email mismatch)
- **ID 3**: Invalid expiration date
- **ID 4**: Invalid CVV
- **ID 5**: Insufficient balance
- **ID 6**: Success

---

## Testing

### Running Tests

Execute all tests:
```bash
mvn test
```

Execute specific test class:
```bash
mvn test -Dtest=CardServiceTest
```

Execute integration tests only:
```bash
mvn test -Dtest=*IntegrationTest
```

### Test Coverage

The application includes comprehensive test coverage with 127 tests:

#### Unit Tests
- **CardServiceTest**: Card service business logic (13 tests)
- **OperationServiceTest**: Operation service validation (22 tests)
- **ModelEntityTest**: Entity model validation (54 tests)
- **CreditCardBrandTest**: Brand identification logic (9 tests)
- **JavaLuhnAlgorithmTest**: Luhn algorithm implementation (17 tests)
- **OperationControllerTest**: Controller layer testing (2 tests)

#### Integration Tests
- **CardControllerIntegrationTest**: End-to-end API testing (10 tests)

### Postman Testing

Import the provided Postman collection: `Card_Authorizer_Postman_Collection.json`

The collection includes:

1. **Card Operations** (3 tests)
   - Get valid Visa card
   - Get valid Mastercard
   - Card not found scenario

2. **Transaction Operations** (4 tests)
   - Valid purchase transactions
   - Small and large amount transactions
   - Multi-brand support

3. **Validation Tests** (8 tests)
   - Invalid Luhn algorithm
   - Invalid cardholder information
   - Invalid card details (CVV, expiration)
   - Insufficient balance
   - Card number format validation

4. **Edge Cases** (6 tests)
   - Exact balance transactions
   - Zero and negative amounts
   - Missing required fields
   - Very long card numbers

5. **Performance Tests** (2 tests)
   - Sequential transaction processing
   - Card retrieval performance

#### Using Postman Collection

1. Import the collection file into Postman
2. Set the environment variable:
   - `base_url`: `http://localhost:80`
3. Run individual tests or the entire collection
4. Review test results in the Postman test runner

![TransactionStateDiagramTestsUseCase.png](\uml%2Ftests%2Fimages%2FTransactionStateDiagramTestsUseCase.png)

### USE CASE 01
![UC_001_Sequence_Diagram.png](\uml%2Ftests%2Fimages%2FUC_001_Sequence_Diagram.png)

### USE CASE 02
![UC_002_Sequence_Diagram.png](\uml%2Ftests%2Fimages%2FUC_002_Sequence_Diagram.png)

### USE CASE 03
![UC_003_Activity_Diagram.png](\uml%2Ftests%2Fimages%2FUC_003_Activity_Diagram.png)

### USE CASE 04
![UC_004_Sequence_Diagram.png](/uml%2Ftests%2Fimages%2FUC_004_Sequence_Diagram.png)

### USE CASE 05
![UC_005_Sequence_Diagram.png](/uml%2Ftests%2Fimages%2FUC_005_Sequence_Diagram.png)

### USE CASE 06
![UC_006_Sequence_Diagram.png](/uml%2Ftests%2Fimages%2FUC_006_Sequence_Diagram.png)
---

## Database Schema

### Entity Relationship Diagram


![EntityRelationshipDiagram.png](/uml%2Fimages%2FEntityRelationshipDiagram.png)

### Tables

#### card
- `id` (BIGINT, PK): Unique identifier
- `number` (VARCHAR, UNIQUE, NOT NULL): Card number
- `expiration` (VARCHAR): Expiration date
- `cvv` (VARCHAR): CVV code
- `limits` (FLOAT): Credit limit
- `balance` (FLOAT): Available balance
- `client_id` (BIGINT, FK): Client reference
- `brand_id` (BIGINT, FK): Brand reference

#### client
- `id` (BIGINT, PK): Unique identifier
- `name` (VARCHAR): Cardholder name
- `email` (VARCHAR): Cardholder email

#### brand
- `id` (BIGINT, PK): Unique identifier
- `name` (VARCHAR): Brand name (Visa, Mastercard, etc.)

#### history
- `id` (BIGINT, PK): Unique identifier
- `card_id` (BIGINT, FK): Card reference
- `messages_id` (BIGINT, FK): Message reference
- `date_operation` (TIMESTAMP): Transaction timestamp
- `value_operation` (FLOAT): Transaction amount
- `type_operation` (VARCHAR): Operation type

#### messages
- `id` (BIGINT, PK): Unique identifier
- `message` (VARCHAR): Message code (SUCCESS, ERROR)
- `description` (VARCHAR): Message description

---

## Project Structure

```
card_authorizer/
├── src/
│   ├── main/
│   │   ├── java/com/tus/oop1/card_authorizer/
│   │   │   ├── CardAuthorizerApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── CardController.java
│   │   │   │   └── OperationController.java
│   │   │   ├── dao/
│   │   │   │   └── OperationDAO.java
│   │   │   ├── model/
│   │   │   │   ├── Brand.java
│   │   │   │   ├── Card.java
│   │   │   │   ├── Client.java
│   │   │   │   ├── History.java
│   │   │   │   └── Messages.java
│   │   │   ├── repo/
│   │   │   │   ├── BrandRepo.java
│   │   │   │   ├── CardRepo.java
│   │   │   │   ├── ClientRepo.java
│   │   │   │   ├── HistoryRepo.java
│   │   │   │   └── MessagesRepo.java
│   │   │   ├── service/
│   │   │   │   ├── CardService.java
│   │   │   │   └── OperationService.java
│   │   │   └── util/
│   │   │       ├── CreditCardBrand.java
│   │   │       └── JavaLuhnAlgorithm.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/tus/oop1/card_authorizer/
│           ├── CardAuthorizerApplicationTests.java
│           ├── controller/
│           │   └── OperationControllerTest.java
│           ├── integration/
│           │   └── CardControllerIntegrationTest.java
│           ├── model/
│           │   └── ModelEntityTest.java
│           ├── service/
│           │   ├── CardServiceTest.java
│           │   └── OperationServiceTest.java
│           └── util/
│               ├── CreditCardBrandTest.java
│               └── JavaLuhnAlgorithmTest.java
├── Card_Authorizer_Postman_Collection.json
├── pom.xml
└── README.md
```

---

## Development

### Code Style

The project follows standard Java coding conventions:

- Package naming: lowercase (e.g., `com.tus.oop1.card_authorizer`)
- Class naming: PascalCase (e.g., `CardController`)
- Method naming: camelCase (e.g., `executeOperation`)
- Constant naming: UPPER_SNAKE_CASE

### Lombok Annotations

The project uses Lombok to reduce boilerplate code:

- `@Data`: Generates getters, setters, toString, equals, and hashCode
- `@Getter` / `@Setter`: Generates getter/setter methods
- `@NoArgsConstructor`: Generates no-argument constructor
- `@AllArgsConstructor`: Generates all-arguments constructor
- `@ToString`: Generates toString method
- `@EqualsAndHashCode`: Generates equals and hashCode methods

### Adding New Features

1. Create model entities in `model/` package
2. Create repository interfaces in `repo/` package
3. Implement business logic in `service/` package
4. Create REST endpoints in `controller/` package
5. Add comprehensive tests in `test/` directory

---

## Sample Test Data

### Valid Cards

#### Visa Card
```json
{
    "number": "4532015112830366",
    "expiration": "12/25",
    "cvv": "123",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "brand": "Visa",
    "balance": 3000.00,
    "limits": 5000.00
}
```

#### Mastercard
```json
{
    "number": "5425233430109903",
    "expiration": "11/24",
    "cvv": "456",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "brand": "Mastercard",
    "balance": 2000.00,
    "limits": 3000.00
}
```

### Other Valid Card Numbers (for testing Luhn algorithm)

- **Visa**: 4111111111111111
- **Mastercard**: 5555555555554444
- **American Express**: 378282246310005
- **Discover**: 6011111111111117

---

## Common Issues and Solutions

### Port Already in Use

If port 80 is already in use, change the port in `application.properties`:
```properties
server.port=8080
```

### Database Connection Issues

If you encounter database connection issues, ensure H2 configuration is correct:
```properties
spring.datasource.url=jdbc:h2:mem:card_authorizer
spring.datasource.driverClassName=org.h2.Driver
```

### Test Failures

If tests fail, ensure the application is not running on the same port:
```bash
# Stop the application before running tests
mvn test
```

### Build Failures

Clean and rebuild the project:
```bash
mvn clean install -U
```

---

## Performance Considerations

### Database Optimization

- Use an in-memory H2 database for fast development
- Indexes on card number for quick lookups
- Lazy loading for relationships to reduce memory footprint

### API Response Times

Expected response times:
- Card retrieval: < 500ms
- Transaction processing: < 2000ms

### Scalability

For production deployment, consider:
- Migrating to PostgresSQL or MySQL
- Implementing caching (Redis)
- Adding connection pooling
- Horizontal scaling with load balancer

---

## Security Considerations

**Note**: This is a demonstration application. For production use, implement:

1. **Authentication & Authorization**: Add Spring Security
2. **HTTPS**: Enable SSL/TLS encryption
3. **Input Validation**: Enhance validation rules
4. **Rate Limiting**: Prevent abuse
5. **Logging & Monitoring**: Add comprehensive logging
6. **Database Security**: Use encrypted connections
7. **CVV Handling**: Never log or store CVV permanently
8. **PCI DSS Compliance**: Follow payment card industry standards

---


## License

This project was developed as part of academic coursework at Technology University Shannon - Athlone (TUS).

**Student**: Weverton de Souza Castanho  
**Email**: wevertonsc@gmail.com  
**Course**: Object-Oriented Programming I (AL_KCNCM_9_1) 29468  
**Date**: November 2025

---

## Contact

For questions or support, please contact:

**Weverton de Souza Castanho**  

**Email:** wevertonsc@gmail.com / A00324822@student.tus.ie 

**Institution:** Technology University Shannon - Athlone (TUS)

---

## References

- Spring Boot framework and documentation
- H2 Database project
- Lombok project for code generation
- JUnit and Mockito testing frameworks
- TUS Object-Oriented Programming I course materials

---

**Version**: 1.0.0  

**Last Updated**: November 2025
