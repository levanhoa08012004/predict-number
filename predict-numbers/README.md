# Predict Numbers 

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
git clone https://github.com/levanhoa08012004/predict-number.git
cd predict-numbers

# Hoặc giải nén file nếu là zip
```

### 2. Cài đặt MySQL Database

Tạo database mới:

```sql
/* =========================
   CREATE DATABASE
========================= */
DROP DATABASE IF EXISTS `predict-numbers`;
CREATE DATABASE `predict-numbers`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `predict-numbers`;

/* =========================
   TABLE: roles
========================= */
CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       updated_at DATETIME(6),
                       active BIT(1) NOT NULL,
                       description VARCHAR(255),
                       name VARCHAR(50) NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY UK_roles_name (name)
) ENGINE=InnoDB;

/* =========================
   TABLE: permissions
========================= */
CREATE TABLE permissions (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             created_at DATETIME(6),
                             updated_at DATETIME(6),
                             name VARCHAR(100) NOT NULL,
                             PRIMARY KEY (id),
                             UNIQUE KEY UK_permissions_name (name)
) ENGINE=InnoDB;

/* =========================
   TABLE: role_permissions
========================= */
CREATE TABLE role_permissions (
                                  role_id BIGINT NOT NULL,
                                  permission_id BIGINT NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT FK_rp_role FOREIGN KEY (role_id) REFERENCES roles(id),
                                  CONSTRAINT FK_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions(id)
) ENGINE=InnoDB;

/* =========================
   TABLE: users
========================= */
CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       updated_at DATETIME(6),
                       password VARCHAR(255),
                       score INT NOT NULL,
                       turns INT NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       role_id BIGINT NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY UK_users_username (username),
                       KEY idx_users_leaderboard (score DESC, updated_at, id),
                       CONSTRAINT FK_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB;

/* =========================
   TABLE: tokens
========================= */
CREATE TABLE tokens (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        updated_at DATETIME(6),
                        access_token VARCHAR(255),
                        refresh_token VARCHAR(255),
                        username VARCHAR(255),
                        PRIMARY KEY (id),
                        UNIQUE KEY UK_tokens_username (username)
) ENGINE=InnoDB;

/* =========================
   INSERT DATA: roles
========================= */
INSERT INTO roles VALUES
    (2,'2026-01-16 15:12:10.495277','2026-01-17 04:56:24.139641',_binary '\0','Role user','USER');

/* =========================
   INSERT DATA: permissions
========================= */
INSERT INTO permissions VALUES
                            (2,'2026-01-16 15:05:07.174680','2026-01-16 15:05:07.174680','PERMISSION:READ'),
                            (3,'2026-01-16 15:05:16.477316','2026-01-16 15:05:16.477316','PERMISSION:CREATE'),
                            (4,'2026-01-16 15:05:20.455062','2026-01-16 15:05:20.455062','PERMISSION:UPDATE'),
                            (5,'2026-01-16 15:05:24.241560','2026-01-16 15:05:24.241560','PERMISSION:DELETE'),
                            (6,'2026-01-16 15:05:31.787397','2026-01-16 15:05:31.787397','PERMISSION:READ:ALL'),
                            (7,'2026-01-16 15:05:46.358864','2026-01-16 15:05:46.358864','USER:READ:ALL'),
                            (8,'2026-01-16 15:05:50.279670','2026-01-16 15:05:50.279670','USER:READ'),
                            (9,'2026-01-16 15:05:54.773601','2026-01-16 15:05:54.773601','USER:CREATE'),
                            (10,'2026-01-16 15:05:59.494903','2026-01-16 15:05:59.494903','USER:UPDATE'),
                            (13,'2026-01-17 04:48:18.204185','2026-01-17 04:48:18.204185','USER:BUYTURN'),
                            (14,'2026-01-17 04:48:30.250971','2026-01-17 04:48:30.250971','USER:PREDICT'),
                            (15,'2026-01-17 04:52:25.653566','2026-01-17 04:52:25.653566','ROLE:CREATE'),
                            (16,'2026-01-17 04:52:30.831920','2026-01-17 04:52:30.831920','ROLE:UPDATE'),
                            (17,'2026-01-17 04:52:37.098377','2026-01-17 04:52:37.098377','ROLE:READ'),
                            (18,'2026-01-17 04:52:41.548913','2026-01-17 04:52:41.548913','ROLE:READ:ALL'),
                            (19,'2026-01-17 04:52:53.146159','2026-01-17 04:52:53.146159','ROLE:DELETE');

/* =========================
   INSERT DATA: role_permissions
========================= */
INSERT INTO role_permissions VALUES
                                 (2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),
                                 (2,10),(2,13),(2,14),(2,15),(2,16),(2,17),(2,18),(2,19);

/* =========================
   INSERT DATA: users
========================= */
INSERT INTO users VALUES
    (1,'2026-01-16 15:22:48.522370','2026-01-18 15:40:02.744499',
     '$2a$10$SIOFYiECkI0tB3uVTFOwDO6BjP6W1gMtKAV/wD6WOv458ky/kU/KG',
     8,0,'hoa123',2,'hoa@gmail.com');

/* =========================
   INSERT DATA: tokens
========================= */
INSERT INTO tokens VALUES
    (1,'2026-01-18 16:21:19.880056','2026-01-18 16:21:19.880056',
     'access-token','refresh-token','hoa123');
```

**Lưu ý**: 
    Database sẽ tự động tạo bảng khi ứng dụng khởi động (do `ddl-auto: update` trong `application.yaml`).

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

### Chạy file JAR

```bash
# Build JAR file
mvn clean package

# Chạy JAR
java -jar target/predict-numbers-0.0.1-SNAPSHOT.jar
```

**Ứng dụng sẽ khởi động trên**: `http://localhost:8080`

## Mô tả cách lấy token/đăng nhập.


### Quy trình đăng nhập

1. Người dùng gửi username và password lên server
2. AuthenticationManager gọi UserDetailsService để lấy thông tin người dùng từ database
3. Spring Security so sánh password bằng PasswordEncoder
4. Nếu xác thực thành công, Authentication được tạo và chứa UserDetails
5. Hệ thống tạo access token và refresh token từ thông tin UserDetails
6. Access token và Refresh token có thể được lưu vào database 
7. Server trả access token và refresh token cho client

### Quy trình xác thực và phân quyền

1. Mỗi request gọi đến API được bảo vệ phải gửi kèm header: Authorization: Bearer <accessToken>

2. PreFilter (JWT Filter) được thực thi trước UsernamePasswordAuthenticationFilter.

3. Filter trích xuất accessToken từ header

4. Kiểm tra accessToken có rỗng hay có Bearer không sau 
5. Lấy username từ token kiểm tra user đó có rỗng hay request hiện tại chưa được xác thực
6. Query database để lấy thông tin UserDetails và kiểm tra xem thông tin user có khớp và token có còn hạn không 
7. Tạo Authentication chứa UserDetails và danh sách permission vào SecurityContextHolder
8. Khi phân quyền thì sẽ lấy danh sách quyền trong SecurityContextHolder và dùng @PreAuthorize("hasAuthority('')") để cấp quyền

## Cách test Api trên postman

### POST /register

1. url: http://localhost:8080/auth/register
2. Method: POST
3. body json: 
```yaml
{
  "username":"hoa234",
  "password":"12345678",
  "email": "hoa123@gmail.com"
}
```

### POST /login

1. url: http://localhost:8080/auth/login
2. Method: POST
3. body json:
```yaml
{
  "username":"hoa123",
  "password":"12345678"
}
```


### POST /guess

1. url: http://localhost:8080/user/guess
2. Method: POST
3. body json:
```yaml
{
  "guessNumber":2
}
```
4. Vào Authorization chọn Bearer Token và nhập access token 

### GET /buy-turns

1. url: http://localhost:8080/buy-turns
2. Method: GET
3. Vào Authorization chọn Bearer Token và nhập access token 
4. Lấy đường dẫn trong data response trả về dán lên trình duyệt
```yaml
{
  "code": 200,
  "message": "Create method payment success",
  "data": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=1000000&vnp_BankCode=NCB&vnp_Command=pay&vnp_CreateDate=20260118223920&vnp_CurrCode=VND&vnp_ExpireDate=20260118225420&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3Ahoa123_1768750760999&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8080%2Fvnpay-return&vnp_TmnCode=4YUP19I4&vnp_TxnRef=hoa123_1768750760999&vnp_Version=2.1.0&vnp_SecureHash=9f812c79171c56e13b36dddc03d34fe0806c3afe62c989b67a149fa147dc67329ba3ccbef7f586ca35fb5fc3c00dfcb2e1a86349f39fec2b0cca35989b578a2b"
}
```
5. Nhập thông tin để thanh toán 
```yaml
   Số thẻ	        9704198526191432198
   Tên chủ thẻ	    NGUYEN VAN A
   Ngày phát hành	07/15
   Mật khẩu OTP	    123456
```
### GET /leaderboard

1. url: http://localhost:8080/users/leaderboard
2. Method: GET
3. Vào Authorization chọn Bearer Token và nhập access token 

### GET /me

1. url: http://localhost:8080/users/me
2. Method: GET
3. Vào Authorization chọn Bearer Token và nhập access token 

