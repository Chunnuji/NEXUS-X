# Nexus Microservices Project

## Overview
Nexus is a **Spring Boot–based microservices architecture** designed to demonstrate a real-world, scalable backend system with **JWT-based authentication, shared security, Kafka-driven asynchronous communication, and synchronous REST interactions where required**.

The project is structured around multiple domain-focused microservices, following clean separation of concerns and production-style patterns.

## Microservices in the System

### 1. Auth Service
- Centralized authentication service
- Responsible for:
  - User authentication
  - JWT token generation
  - Token validation support for other services
- Works together with the **Shared Security JAR**

---

### 2. Shared Security JAR
- Common security module used by all microservices
- Contains:
  - JWT filters
  - Security configuration
  - Token validation logic
- Ensures **consistent authentication & authorization** across services
- Avoids duplication of security code

---

### 3. Order Service
- Manages order lifecycle
- Responsibilities:
  - Create orders
  - Maintain order status (CREATED / CANCELLED / COMPLETED)
- Interactions:
  - **Synchronous REST call** to Inventory Service during order creation
  - Produces Kafka events for downstream services (e.g., payment flow)

---

### 4. Inventory Service
- Manages product stock
- Responsibilities:
  - Check product availability
  - Reserve or release inventory
- Interactions:
  - Receives REST calls from Order Service
  - Can consume Kafka events if needed for async updates

---

### 5. Product Service
- Manages product catalog
- Responsibilities:
  - Product details
  - Pricing information
- Acts as a supporting service for Order & Inventory domains

---

### 6. Payment Service
- Manages payment processing
- Responsibilities:
  - Handle payment initiation
  - Update payment status (PENDING / SUCCESS / FAILED)
- Interactions:
  - Consumes Kafka events from Order Service
  - Can publish payment result events back to Kafka

---

## Communication Patterns

### Synchronous Communication
- **Order Service → Inventory Service**
  - Implemented using `RestTemplate`
  - Used during order creation to ensure stock availability

### Asynchronous Communication (Kafka)
- Kafka is used for **event-driven workflows** such as:
  - Order events
  - Payment processing events
- Ensures:
  - Loose coupling
  - Better scalability
  - Fault tolerance

---

## Security Flow
1. Client authenticates via **Auth Service**
2. Auth Service issues **JWT token**
3. Client sends JWT token with requests
4. Each microservice:
   - Uses **Shared Security JAR**
   - Validates token before processing request

---


## Technology Stack
- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Apache Kafka (Confluent Kafka)
- REST (RestTemplate)
- JPA / Hibernate
- MySQL / PostgreSQL (configurable)

---

## Key Design Highlights
- Modular microservices architecture
- Shared security layer for consistency
- Hybrid communication (REST + Kafka)
- Event-driven payment flow
- Clean separation of domains

---

## Status
- Core functionality implemented
- Kafka integrated where required
- Security fully centralized and reusable
- Ready for further scaling and enhancements

---

## Future Enhancements
- Replace RestTemplate with WebClient
- Add Saga pattern for distributed transactions
- Add centralized logging and tracing (ELK / Zipkin)
- Improve retry and dead-letter handling in Kafka

---

**Nexus** serves as a strong foundation for learning and extending real-world microservice architectures.

