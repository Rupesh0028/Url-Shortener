# 🔗 URL Shortener API

A secure **URL Shortener REST API** built using **Java, Spring Boot, Spring Security, JWT, JPA, and MySQL**.

The project implements **JWT-based Authentication and Role-Based Authorization** to secure the APIs.

---

## 🚀 Features

* User Registration
* User Login
* BCrypt password hashing
* JWT-based Authentication
* Stateless Authentication
* Role-Based Authorization
* `USER` and `ADMIN` roles
* Protected REST APIs
* Create and manage shortened URLs
* Associate URLs with authenticated users
* MySQL database integration
* Spring Data JPA
* DTO-based API design

---

## 🛠️ Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Security**
* **JWT**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **Lombok**

---

# 🔐 Authentication & Authorization

This project implements two important security concepts:

### Authentication

Authentication answers:

> **"Who are you?"**

The user logs in with:

```text
Username
Password
```

Spring Security verifies the credentials and generates a JWT.

### Authorization

Authorization answers:

> **"What are you allowed to do?"**

After authentication, Spring Security checks the user's role before allowing access to protected endpoints.

Example:

```text
USER
 ↓
Can access normal protected APIs

ADMIN
 ↓
Can access ADMIN-only APIs
```

---

# 🔄 Authentication Flow

## 1. User Registration

```text
Client
   ↓
POST /auth/register
   ↓
AuthController
   ↓
UserService
   ↓
PasswordEncoder
   ↓
BCrypt Password Hash
   ↓
MySQL
```

The user's password is encoded using BCrypt before being stored.

The plain-text password is **never stored in the database**.

---

## 2. User Login

```text
Client
   ↓
POST /auth/login
   ↓
AuthController
   ↓
UserService
   ↓
AuthenticationManager
   ↓
AuthenticationProvider
   ↓
UserDetailsService
   ↓
MySQL
   ↓
PasswordEncoder
   ↓
Credentials Verified
   ↓
JwtService
   ↓
JWT Token
```

If the username or password is incorrect, authentication fails.

---

# 🎫 JWT Authentication

After successful login, the server generates a JWT.

Example:

```text
eyJhbGciOiJIUzI1NiJ9...
```

The client sends this token with protected requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

For every protected request:

```text
Client
   ↓
Request + JWT
   ↓
SecurityFilterChain
   ↓
JWT Authentication
   ↓
JwtDecoder
   ↓
Verify JWT
   ↓
SecurityContext
   ↓
Authenticated User
   ↓
Authorization Check
   ↓
Controller
```

---

# 🔑 Authorization Flow

After the JWT is successfully verified, Spring Security knows which user is making the request and what authorities/roles the user has.

For example:

```text
JWT
 ↓
username = rupesh
role = ADMIN
 ↓
SecurityContext
```

Then Spring Security applies the authorization rules.

---

## 👤 USER Role

A normal user can access authenticated APIs.

Example:

```text
GET /url
POST /url
GET /url/{id}
```

The user must provide a valid JWT.

---

## 👑 ADMIN Role

Some endpoints require the `ADMIN` role.

Example:

```java
.requestMatchers("/project/delete/**")
.hasRole("ADMIN")
```

Therefore:

```text
USER
 ↓
DELETE /project/delete/10
 ↓
403 Forbidden
```

But:

```text
ADMIN
 ↓
DELETE /project/delete/10
 ↓
Allowed
```

---

# 🔒 Security Rules

The application uses the following authorization rules:

| Endpoint             | Access              |
| -------------------- | ------------------- |
| `/auth/register`     | Public              |
| `/auth/login`        | Public              |
| `/project/delete/**` | ADMIN only          |
| Other endpoints      | Authenticated users |

In Spring Security:

```java
.requestMatchers(
        "/auth/register",
        "/auth/login"
).permitAll()

.requestMatchers("/project/delete/**")
.hasRole("ADMIN")

.anyRequest()
.authenticated()
```

---

# 🧠 Authentication vs Authorization

```text
                 SECURITY
                    │
          ┌─────────┴─────────┐
          ↓                   ↓
   AUTHENTICATION       AUTHORIZATION
          ↓                   ↓
     "Who are you?"      "What can you do?"
          ↓                   ↓
    Username/Password       USER / ADMIN
          ↓                   ↓
        JWT              Access Control
```

### Authentication

```text
Username + Password
        ↓
AuthenticationManager
        ↓
Verify credentials
        ↓
Generate JWT
```

### Authorization

```text
JWT
 ↓
JwtDecoder
 ↓
Authenticated User
 ↓
Check Role
 ↓
Allow / Deny
```

---

# 🗄️ Database User Example

A user stored in the database may look like:

```text
username: rupesh
password: $2a$10$................
role: USER
```

An administrator:

```text
username: admin
password: $2a$10$................
role: ADMIN
```

The password is stored as a BCrypt hash rather than plain text.

---

# 🔐 Password Security

Passwords are encoded using:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

During registration:

```java
passwordEncoder.encode(password)
```

During authentication, Spring Security uses the configured password encoder to verify the submitted password against the stored hash.

---

# 🧩 Security Components

### `AuthenticationManager`

Main entry point for username/password authentication.

```java
authenticationManager.authenticate(...)
```

### `AuthenticationProvider`

Performs the actual authentication using components such as `UserDetailsService` and `PasswordEncoder`.

### `UserDetailsService`

Loads the user from the database.

```java
loadUserByUsername(username)
```

### `PasswordEncoder`

Hashes and verifies passwords.

### `JwtService`

Creates JWT tokens after successful login.

### `JwtDecoder`

Validates incoming JWTs on protected requests.

### `SecurityContext`

Stores the authentication information for the current request.

### `SecurityFilterChain`

Defines the application's security and authorization rules.

---

# ⚙️ Configuration

Add the following to `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=YOUR_SECRET_KEY
jwt.expiration=3600000
```

`3600000` milliseconds = **1 hour**.

> Never commit your real database password or JWT secret to a public repository.

---

# 📡 API Usage

## Register

```http
POST /auth/register
Content-Type: application/json
```

```json
{
    "username": "rupesh",
    "password": "123456"
}
```

---

## Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
    "username": "rupesh",
    "password": "123456"
}
```

Response:

```text
eyJhbGciOiJIUzI1NiJ9...
```

---

## Access Protected API

```http
GET /url
Authorization: Bearer <JWT_TOKEN>
```

A valid JWT is required.

---

## Access ADMIN API

```http
DELETE /project/delete/10
Authorization: Bearer <JWT_TOKEN>
```

The authenticated user must have:

```text
ROLE_ADMIN
```

Otherwise:

```text
403 Forbidden
```

---

# 🔄 Complete Security Architecture

```text
                    CLIENT
                       │
                       │
              POST /auth/login
                       │
                       ↓
                 AuthController
                       │
                       ↓
                  UserService
                       │
                       ↓
            AuthenticationManager
                       │
                       ↓
            AuthenticationProvider
                       │
                       ↓
             UserDetailsService
                       │
                       ↓
                   Database
                       │
                       ↓
               PasswordEncoder
                       │
                       ↓
              Authentication OK
                       │
                       ↓
                  JwtService
                       │
                       ↓
                     JWT
                       │
                       │
              Future API Request
                       │
                       ↓
               SecurityFilterChain
                       │
                       ↓
                  JwtDecoder
                       │
                       ↓
                Verify JWT
                       │
                       ↓
                SecurityContext
                       │
                       ↓
                Authorization
                       │
                ┌──────┴──────┐
                ↓             ↓
              USER          ADMIN
                ↓             ↓
        Allowed APIs    Admin APIs
```

---

# 📌 Key Concepts Demonstrated

This project demonstrates practical implementation of:

* REST API development
* Spring Boot
* Spring Security
* JWT Authentication
* Role-Based Authorization
* AuthenticationManager
* AuthenticationProvider
* UserDetailsService
* PasswordEncoder
* SecurityFilterChain
* SecurityContext
* Stateless Authentication
* BCrypt Password Hashing
* Spring Data JPA
* MySQL
* DTOs
* Dependency Injection
* Role-based access control

---

# 🔮 Future Improvements

* URL redirect functionality
* Custom short URLs
* URL expiration
* Click analytics
* Pagination
* Global exception handling
* Swagger/OpenAPI
* Refresh tokens
* Redis caching
* Rate limiting
* Docker deployment
* User dashboard
* Admin dashboard

---

## 👨‍💻 Author

**Rupesh**

A Spring Boot backend project built to understand and implement **REST APIs, JWT Authentication, and Role-Based Authorization**.
