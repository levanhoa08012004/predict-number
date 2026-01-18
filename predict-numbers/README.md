# Predict Numbers - Hướng dẫn sử dụng

Một ứng dụng Spring Boot với chức năng quản lý người dùng, phân quyền, và tích hợp thanh toán VnPay.

## Yêu cầu hệ thống

- **Java**: 17 trở lên
- **Maven**: 3.6+
- **MySQL**: 5.7 trở lên
- **Git** (tuỳ chọn)

## Cài đặt và Setup môi trường

### 1. Clone hoặc tải project

```bash
# Clone từ repository
git clone <your-repo-url>
cd predict-numbers

# Hoặc giải nén file nếu là zip
```

### 2. Cài đặt MySQL Database

Tạo database mới:

```sql
CREATE DATABASE `predict-numbers` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Lưu ý**: Database sẽ tự động tạo bảng khi ứng dụng khởi động (do `ddl-auto: update` trong `application.yaml`).

### 3. Cấu hình database và JWT

Mở file `src/main/resources/application.yaml` và cập nhật các thông tin:

```yaml
spring:
  datasource:
    url: "jdbc:mysql://localhost:3306/predict-numbers"
    username: root          # Username MySQL của bạn
    password: 123456        # Password MySQL của bạn
  jpa:
    hibernate:
      ddl-auto: update      # Tự động tạo/cập nhật database

jwt:
  expiryHour: 1            # Thời hạn token (giờ)
  expiryDay: 15            # Thời hạn refresh token (ngày)
  secretKey: "..."         # Secret key cho JWT
  refreshKey: "..."        # Refresh key cho JWT
```

## Build & Run Project

### Cách 1: Chạy trực tiếp với Maven

```bash
# Build project
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

### Cách 2: Sử dụng Maven Wrapper (không cần cài Maven)

**Windows:**
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
chmod +x mvnw
./mvnw clean install
./mvnw spring-boot:run
```

### Cách 3: Chạy file JAR

```bash
# Build JAR file
mvn clean package

# Chạy JAR
java -jar target/predict-numbers-0.0.1-SNAPSHOT.jar
```

**Ứng dụng sẽ khởi động trên**: `http://localhost:8080`

## Xác thực (Authentication)

Ứng dụng sử dụng **JWT (JSON Web Token)** để xác thực. Bạn cần lấy token trước khi gọi các API được bảo vệ.

### Quy trình Xác thực

1. **Đăng ký tài khoản** (nếu chưa có) hoặc sử dụng tài khoản hiện có
2. **Đăng nhập** để lấy `accessToken` và `refreshToken`
3. **Sử dụng token** trong header `Authorization: Bearer <accessToken>` khi gọi API
4. **Khi token hết hạn**, dùng `refreshToken` để lấy token mới

### Lưu Token

Sau khi đăng nhập thành công, lưu `accessToken`:

**Dùng Postman:**
- Copy giá trị `accessToken` từ response
- Chọn tab **Authorization** → Type: **Bearer Token**
- Paste token vào field **Token**

**Dùng curl:**
```bash
accessToken="eyJhbGciOiJIUzUxMiJ9..."
curl -H "Authorization: Bearer $accessToken" http://localhost:8080/users
```

**Dùng insomnia:**
- Chọn **Auth** → **Bearer Token**
- Paste token

## Hướng dẫn Test API

Sử dụng **Postman**, **Insomnia**, hoặc **curl** để test các API dưới đây.

### 1. Đăng ký (Register)

**POST** `http://localhost:8080/auth/register`

```json
{
  "username": "testuser",
  "password": "password123",
  "fullName": "Test User",
  "email": "test@example.com"
}
```

**Response** (201 Created):
```json
{
  "code": 201,
  "message": "Successfully registered",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "fullName": "Test User",
    "createdAt": "2026-01-18T10:00:00"
  }
}
```

### 2. Đăng nhập (Login) - Lấy Token

**POST** `http://localhost:8080/auth/login`

```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "code": 200,
  "message": "Successfully logged in",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "expiryDate": "2026-01-18T11:00:00"
  }
}
```

**Lưu lại `accessToken` để sử dụng cho các API khác**

### 3. Refresh Token

**POST** `http://localhost:8080/auth/refresh`

**Header**:
```
Authorization: Bearer <refreshToken>
```

**Response** (200 OK): Nhận token mới

### 4. Quản lý Người dùng

#### Lấy danh sách người dùng

**GET** `http://localhost:8080/users`

**Header**:
```
Authorization: Bearer <accessToken>
```

#### Lấy chi tiết người dùng

**GET** `http://localhost:8080/users/{userId}`

**Header**:
```
Authorization: Bearer <accessToken>
```

#### Cập nhật người dùng

**PUT** `http://localhost:8080/users/{userId}`

**Header**:
```
Authorization: Bearer <accessToken>
```

**Body**:
```json
{
  "email": "newemail@example.com",
  "fullName": "Updated Name"
}
```

#### Xoá người dùng

**DELETE** `http://localhost:8080/users/{userId}`

**Header**:
```
Authorization: Bearer <accessToken>
```

### 5. Quản lý Role

#### Tạo Role

**POST** `http://localhost:8080/roles`

**Header**:
```
Authorization: Bearer <accessToken>
```

**Body**:
```json
{
  "name": "ADMIN",
  "description": "Administrator role"
}
```

#### Lấy danh sách Role

**GET** `http://localhost:8080/roles`

#### Lấy chi tiết Role

**GET** `http://localhost:8080/roles/{roleId}`

### 6. Quản lý Permission

#### Tạo Permission

**POST** `http://localhost:8080/permissions`

**Body**:
```json
{
  "name": "CREATE_USER",
  "description": "Permission to create user"
}
```

#### Lấy danh sách Permission

**GET** `http://localhost:8080/permissions`

### 7. VnPay Integration

#### Tạo đơn hàng VnPay

**POST** `http://localhost:8080/vnpay/create-order`

**Header**:
```
Authorization: Bearer <accessToken>
```

**Body**:
```json
{
  "amount": 100000,
  "description": "Thanh toán đơn hàng"
}
```

#### Kiểm tra kết quả thanh toán

**GET** `http://localhost:8080/vnpay/payment-callback?vnp_ResponseCode=00&...`

## Cấu trúc Project

```
predict-numbers/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/predict_numbers/
│   │   │       ├── configuration/      # Cấu hình (Security, JWT, VnPay)
│   │   │       ├── controller/         # REST Controllers
│   │   │       ├── dto/               # Request/Response DTOs
│   │   │       ├── entity/            # JPA Entities
│   │   │       ├── exception/         # Exception handling
│   │   │       ├── mapper/            # MapStruct mappers
│   │   │       ├── repository/        # JPA Repositories
│   │   │       ├── service/           # Business logic
│   │   │       └── util/              # Utilities
│   │   └── resources/
│   │       └── application.yaml       # Cấu hình ứng dụng
│   └── test/
└── pom.xml
```

## Các Dependencies chính

- **Spring Boot 3.5.9**: Framework chính
- **Spring Data JPA**: ORM
- **Spring Security**: Bảo mật
- **JWT (JJWT)**: Xác thực token
- **MapStruct**: Mapping DTO/Entity
- **Lombok**: Giảm boilerplate code
- **MySQL**: Database
- **Validation**: Bean validation

## Troubleshooting

### Lỗi: "Access denied for user 'root'@'localhost'"

**Giải pháp**: Kiểm tra username và password MySQL trong `application.yaml`

### Lỗi: "Database 'predict-numbers' not found"

**Giải pháp**: Tạo database mới (xem phần 2 của Setup)

### Lỗi: "Port 8080 already in use"

**Giải pháp**: Thay đổi port trong `application.yaml`:
```yaml
server:
  port: 8081  # Hoặc port khác
```

### Lỗi: JWT token expired

**Giải pháp**: Sử dụng API `/auth/refresh` với refresh token để lấy token mới

## Ghi chú

- Mặc định JWT access token hết hạn sau **1 giờ**
- Refresh token hết hạn sau **15 ngày**
- Mỗi API cần authentication bắt buộc phải gửi token trong header `Authorization: Bearer <token>`
- Ứng dụng sẽ tự động tạo bảng database khi chạy lần đầu

## Liên hệ & Hỗ trợ

Nếu gặp vấn đề, vui lòng liên hệ hoặc tạo issue trong repository.
