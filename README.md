# Bank Security Application – Backend

A Spring Boot-based backend for a secure banking application that handles user registration, login, account management, and transaction operations. It includes authentication, authorization, and role-based access control to enhance banking security.

## 🛡️ Features

- User & Admin account registration and login
- Role-based access control (USER and ADMIN roles)
- Account management (create, fetch, delete)
- Transaction handling with OTP verification
- JWT-based authentication
- Email notification for OTP verification
- In-memory H2 database (can be replaced with persistent DB)
- Global exception handling

## 📦 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **JWT (JSON Web Token)**
- **H2 Database**
- **JavaMailSender**
- **Lombok**

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven

### Clone the Repository

```bash
git clone https://github.com/Tanush-J/Bank-Security-Application.git
cd Bank-Security-Application
```

### Run the Application

```bash
./mvnw spring-boot:run
```

Application will start at: `http://localhost:8080`

## 🔐 Authentication & Roles

- JWT tokens are used for securing APIs.
- Roles:
  - `ROLE_USER`: Regular users
  - `ROLE_ADMIN`: Admins with elevated privileges

## 📬 Email & OTP

- OTPs are generated for sensitive operations.
- OTPs are sent via email using `JavaMailSender`.
- Tokens and OTPs expire after a set time.

## 📂 API Overview

### Auth Endpoints

- `POST /auth/register-user` – Register a user
- `POST /auth/register-admin` – Register an admin
- `POST /auth/login` – Login with email and password

### Account Endpoints

- `POST /account/create` – Create bank account
- `GET /account` – Fetch accounts
- `DELETE /account/{id}` – Delete account (admin only)

### Transaction Endpoints

- `POST /transaction/initiate` – Start a transaction
- `POST /transaction/verify-otp` – Verify OTP to complete transaction
- `GET /transaction` – List transactions

## 🧪 Testing

You can use tools like **Postman** to test API endpoints. Include the JWT token in the `Authorization` header.

```http
Authorization: Bearer <your_jwt_token>
```

## 🛠 Configuration

Environment variables (or application.properties) include:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
jwt.secret=yourSecretKey
mail.username=yourEmail
mail.password=yourAppPassword
```

## 🧾 Sample User JSON

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password"
}
```
