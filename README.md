# 🛍️ Order Booking Microservices System

A reactive, cloud-native **Microservices Architecture** built using **Spring Boot**, **Spring Cloud**, **Spring WebFlux**, **RabbitMQ**, **JWT Authentication**, and **Docker Compose**.

This project simulates a simple order booking system with distributed services like customer management, order placement, authentication, and notifications—all communicating synchronously/asynchronously and registered under a centralized Eureka discovery service.

![Spring Cloud](https://i.loli.net/2021/11/06/ItBZXMEsqJ9knY4.png)
![JWT](https://jwt.io/img/logo-asset.svg)
![Docker](https://upload.wikimedia.org/wikipedia/commons/7/70/Docker_logo.png)
---

## 🧱 Tech Stack

| Layer             | Technology                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Backend           | Spring Boot (WebFlux), Spring Cloud, Spring Security (JWT)                 |
| Service Discovery | Eureka Server                                                              |
| API Gateway       | Spring Cloud Gateway                                                       |
| Asynchronous Comm | RabbitMQ                                                                   |
| Database          | MySQL with R2DBC                                                           |
| Containerization  | Docker, Docker Compose                                                     |
| Messaging         | RabbitMQ                                                                   |
| Reactive Stack    | Spring WebFlux, Project Reactor                                            |

---

## 📦 Services Overview

| Service Name        | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| **API Gateway**      | Entry point of all HTTP requests; routes to microservices                  |
| **Auth Service**     | Handles JWT-based registration, login, and token validation                |
| **Customer Service** | Manages customer data and communicates with Order & Notification services |
| **Order Service**    | Manages orders: create, update, delete, and fetch                          |
| **Notification Service** | Listens via RabbitMQ to send logs (simulated notifications)             |
| **Eureka Server**    | Service registry and discovery                                             |

---

## ⚙️ Project Architecture

```bash
.
├── apigateway/
├── authservice/
├── customerservice/
├── orderservice/
├── notificationservice/
├── eurekaserver/
├── orderapp-microservice.yml
└── README.md
```

## 🚀 Running the Project
### ✅ Prerequisites
- Docker & Docker Compose installed
- JDK 21

## 🐳 Run via Docker Compose
### 1. 🛠️ Build the Docker containers
```
docker compose -f orderapp-microservice.yml build --no-cache
```
### 2. ▶️ Run the Docker containers
```
docker compose -f orderapp-microservice.yml up
```
### 3. ⛔ Stop the Docker containers
```
docker compose -f orderapp-microservice.yml down
```

## 📌 Service Ports
| Service              | Port  |
|----------------------|-------|
| API Gateway          | 7777  |
| Auth Service         | 1111  |
| Customer Service     | 7070  |
| Order Service        | 9090  |
| Notification Service | 8080  |
| Eureka Server        | 8761  |
| MySQL                | 3307  |
| RabbitMQ             | 5672  |
| RabbitMQ UI          | 15672 |

## 🔐 Auth Service Endpoints
- Base URL: `http://localhost:7777/auth`

| Method | Endpoint    | Description             |
| ------ | ----------- | ----------------------- |
| POST   | `/register` | Register a new customer |
| PUT    | `/update`   | Update customer profile |
| POST   | `/login`    | Login and get JWT       |
| POST   | `/validate` | Validate JWT token      |

## 👥 Customer Service Endpoints
- Base URL: `http://localhost:7777/customers`

| Method | Endpoint             | Description                |
| ------ | -------------------- | -------------------------- |
| GET    | `/`                  | Get all customers          |
| GET    | `/{id}`              | Get customer by ID         |
| PUT    | `/add/order`         | Add order to customer      |
| PUT    | `/remove/order/{id}` | Remove order from customer |
| DELETE | `/{id}`              | Delete customer            |

## 🧾 Order Service Endpoints
- Base URL: `http://localhost:7777/orders`

| Method | Endpoint | Description           |
| ------ | -------- | --------------------- |
| GET    | `/`      | Get all orders        |
| GET    | `/{id}`  | Get order by ID       |
| POST   | `/`      | Create new order      |
| PUT    | `/`      | Update existing order |
| DELETE | `/{id}`  | Delete order by ID    |

## 🛍️ Product Endpoints (Within `Order Service`)
- Base URL: `http://localhost:7777/products`

| Method | Endpoint | Description       |
| ------ | -------- | ----------------- |
| GET    | `/`      | Get all products  |
| GET    | `/{id}`  | Get product by ID |
| POST   | `/`      | Create product    |
| PUT    | `/`      | Update product    |
| DELETE | `/{id}`  | Delete product    |

## 📬 Notification Service
- Listens to RabbitMQ messages from the Customer service.
- Logs received events (order created)

This simulates an event-driven notification system.

Sure! Here's a **short and concise version** of the authentication flow section:


## 🔐 Authentication Flow

1. **Register**

    * Users register via `POST /auth/register`.
    * Their credentials are saved securely in the database.

2. **Login**

    * Users log in via `POST /auth/login`.
    * A **JWT token** is generated and returned upon successful authentication.

3. **Make Authenticated Requests**

    * The client includes the JWT in the `Authorization: Bearer <token>` header.
    * API Gateway forwards the request to the appropriate microservice.

4. **Token Validation**

    * Services call `POST /auth/validate` to verify the JWT.
    * The Auth Service checks its validity and responds with `true` or `false`.

5. **Secure & Stateless**

    * Tokens are signed and self-contained—no session storage needed.
    * Expired or tampered tokens are rejected.

## ✅ Features Implemented
- ✅ Reactive REST APIs using Spring WebFlux

- ✅ JWT-based authentication

- ✅ Asynchronous messaging via RabbitMQ

- ✅ Service Discovery using Eureka

- ✅ API Gateway for centralized routing

- ✅ Dockerized services using Docker Compose

- ✅ Modular and clean microservices architecture

- ✅ Reactive MySQL access with Spring R2DBC

## 📧 Contact
For any further queries, feel free to reach out to me via email at mainakcr72002@gmail.com.

