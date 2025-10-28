# 🛒 **UTEShop – Hệ thống Thương mại Điện tử Đa vai trò**

<p align="center">
  <img src="https://img.shields.io/badge/Java-22-orange?style=for-the-badge&logo=openjdk" alt="Java Badge"/>
  <img src="https://img.shields.io/badge/Jakarta%20EE-10-blue?style=for-the-badge&logo=eclipse" alt="Jakarta EE"/>
  <img src="https://img.shields.io/badge/JPA-Hibernate%206.6-59666C?style=for-the-badge&logo=hibernate" alt="Hibernate"/>
  <img src="https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap" alt="Bootstrap"/>
  <img src="https://img.shields.io/badge/WebSocket-Real--time-yellow?style=for-the-badge" alt="WebSocket"/>
  <img src="https://img.shields.io/badge/JWT-Security-green?style=for-the-badge&logo=jsonwebtokens" alt="JWT"/>
  <img src="https://img.shields.io/badge/SQL%20Server-CC2927?style=for-the-badge&logo=microsoftsqlserver" alt="Database"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven" alt="Maven"/>
</p>

<p align="center">
  <strong>💡 Nền tảng thương mại điện tử toàn diện với quản lý đa vai trò, thanh toán trực tuyến, chat real-time và hệ thống giao hàng thông minh</strong>
</p>

---

## 📋 **Mục lục**

- [🌟 Giới thiệu](#-giới-thiệu)
- [✨ Tính năng nổi bật](#-tính-năng-nổi-bật)
- [🛠️ Công nghệ sử dụng](#️-công-nghệ-sử-dụng)
- [🏗️ Kiến trúc hệ thống](#️-kiến-trúc-hệ-thống)
- [📁 Cấu trúc dự án](#-cấu-trúc-dự-án)
- [💾 Cơ sở dữ liệu](#-cơ-sở-dữ-liệu)
- [⚙️ Hướng dẫn cài đặt](#️-hướng-dẫn-cài-đặt)
- [👥 Phân quyền và Chức năng](#-phân-quyền-và-chức-năng)
- [🔌 API Endpoints](#-api-endpoints)
- [🔐 Bảo mật](#-bảo-mật)
- [🧪 Testing](#-testing)
- [🐛 Troubleshooting](#-troubleshooting)
- [🚀 Roadmap](#-roadmap)
- [🤝 Đóng góp](#-đóng-góp)
- [📄 License](#-license)
- [👥 Contributors](#-contributors)
- [📧 Liên hệ](#-liên-hệ)

---

## 🌟 **Giới thiệu**

**UTEShop** là một hệ thống thương mại điện tử được xây dựng với Java Enterprise Edition, cung cấp giải pháp toàn diện cho việc mua sắm trực tuyến. Hệ thống hỗ trợ đa vai trò người dùng với các tính năng phong phú và giao diện thân thiện.

### **Điểm nổi bật:**

✅ **Đa vai trò**: Guest, User, Manager, Admin, Shipper  
✅ **Real-time Chat**: Giao tiếp trực tiếp qua WebSocket  
✅ **Bảo mật cao**: JWT Token Authentication  
✅ **Responsive Design**: Tối ưu cho mọi thiết bị  
✅ **Quản lý thông minh**: Dashboard với thống kê chi tiết  

---

## ✨ **Tính năng nổi bật**

### 🛍️ **Cho khách hàng (User)**

| Tính năng | Mô tả |
|-----------|-------|
| 🔐 **Xác thực người dùng** | Đăng ký, đăng nhập, quên mật khẩu với OTP qua email |
| 🏠 **Trang chủ thông minh** | Hiển thị sản phẩm mới, bán chạy, đánh giá cao với lazy loading |
| 🔍 **Tìm kiếm & Lọc** | Tìm kiếm theo tên, lọc theo category, giá, thương hiệu, đánh giá |
| 🛒 **Giỏ hàng** | Thêm/sửa/xóa sản phẩm, tính tổng tiền tự động |
| 💳 **Thanh toán** | Hỗ trợ COD với voucher giảm giá |
| 📦 **Quản lý đơn hàng** | Theo dõi trạng thái: Chờ → Xác nhận → Giao hàng → Hoàn thành |
| ⭐ **Đánh giá sản phẩm** | Rating 1-5 sao, viết review, đính kèm ảnh |
| ❤️ **Yêu thích** | Lưu sản phẩm yêu thích, xem lịch sử đã xem |
| 💬 **Chat hỗ trợ** | Nhắn tin real-time với shop |
| 📍 **Quản lý địa chỉ** | CRUD địa chỉ giao hàng, đặt địa chỉ mặc định |

### 👨‍💼 **Cho Quản trị viên (Admin)**

- 👥 Quản lý người dùng (tìm kiếm, khóa/mở khóa tài khoản)
- 📦 Quản lý sản phẩm toàn hệ thống
- 🗂️ Quản lý danh mục sản phẩm
- 🎯 Quản lý voucher và khuyến mãi
- 📊 Thống kê doanh thu, đơn hàng, sản phẩm bán chạy
- 💬 Chat với khách hàng (xem tất cả cuộc trò chuyện)
- 🖼️ Quản lý banner trang chủ
- ⭐ Quản lý đánh giá (duyệt, xóa review không phù hợp)

### 🛍️ **Cho Người bán (Manager)**

- 📦 CRUD sản phẩm của shop mình
- 🏷️ Quản lý danh mục riêng
- 💰 Tạo và quản lý chương trình khuyến mãi
- 📊 Quản lý đơn hàng thuộc shop
- 📈 Thống kê doanh thu shop
- 💬 Chat với khách hàng mua hàng

### 🚚 **Cho Shipper**

- 📋 Xem danh sách đơn hàng "Đã xác nhận"
- ✅ Nhận đơn hàng để giao
- 🔄 Cập nhật trạng thái giao hàng
- ✔️ Đánh dấu giao thành công/thất bại
- 📊 Thống kê đơn hàng đã giao
- 💬 Chat với khách hàng về đơn hàng

---

## 🛠️ **Công nghệ sử dụng**

### **Backend**

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|----------|
| **Java** | 22 | Ngôn ngữ lập trình chính |
| **Jakarta EE** | 10 | Servlet, JSP, JSTL |
| **JPA/Hibernate** | 6.6.27 | ORM Framework |
| **JWT** | 0.11.5 | Token-based Authentication |
| **BCrypt** | 0.4 | Mã hóa mật khẩu |
| **WebSocket** | Jakarta WebSocket API | Real-time messaging |
| **Jakarta Mail** | 2.1.4 | Gửi email OTP |
| **Gson** | 2.10.1 | JSON Serialization |
| **HikariCP** | 5.1.0 | Connection Pool |

### **Frontend**

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|----------|
| **JSP/JSTL** | 3.x | View Template |
| **Bootstrap** | 5.3 | UI Framework |
| **JavaScript** | ES6+ | Client-side logic |
| **SiteMesh** | 3.2.1 | Layout Decorator |

### **Database**

- **SQL Server** - Database chính
- Hỗ trợ MySQL, PostgreSQL (chỉ cần đổi config)

### **Build & Deploy**

- **Maven** - Build tool
- **Apache Tomcat** 10.x - Application Server

---

## 🏗️ **Kiến trúc hệ thống**

```
┌─────────────────────────────────────────────────────────┐
│                    Client Layer                          │
│  (Browser - JSP/Bootstrap/JavaScript/WebSocket)         │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                 Presentation Layer                       │
│    (Servlet Controllers + SiteMesh Decorators)          │
│  ┌────────────┬──────────────┬────────────────────┐    │
│  │  Auth      │  Product     │  Order  │  Chat    │    │
│  │  Servlet   │  Controller  │  API    │ WebSocket│    │
│  └────────────┴──────────────┴────────────────────┘    │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                   Security Layer                         │
│         (JWT Filter + Role-based Access Control)        │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                   Business Layer                         │
│              (Service Implementation)                    │
│  ┌────────────┬──────────────┬────────────────────┐    │
│  │  User      │  Product     │  Order    │ Review │    │
│  │  Service   │  Service     │  Service  │Service │    │
│  └────────────┴──────────────┴────────────────────┘    │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                  Data Access Layer                       │
│                 (DAO + JPA/Hibernate)                    │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                  Database Layer                          │
│                 (SQL Server / MySQL)                     │
└─────────────────────────────────────────────────────────┘
```

### **Design Patterns**

- ✅ **MVC** (Model-View-Controller)
- ✅ **DAO** (Data Access Object)
- ✅ **DTO** (Data Transfer Object)
- ✅ **Builder** (Lombok @Builder)
- ✅ **Singleton** (Service instances)
- ✅ **Decorator** (SiteMesh layouts)
- ✅ **Filter Chain** (JWT Authentication)

---

## 📁 **Cấu trúc dự án**

```
UTEShop/
├── UTESHOP/
│   ├── src/main/java/ute/
│   │   ├── configs/
│   │   │   ├── JPAConfig.java              # JPA EntityManager config
│   │   │   ├── GsonConfig.java             # Gson custom config
│   │   │   └── DataInitializer.java        # Database seeding
│   │   │
│   │   ├── controllers/
│   │   │   ├── AuthServlet.java            # Login, Register, OTP
│   │   │   ├── HomeController.java         # Trang chủ
│   │   │   ├── ProductDetailController.java
│   │   │   ├── ReviewController.java       # Đánh giá sản phẩm
│   │   │   ├── CartApiController.java      # API giỏ hàng
│   │   │   ├── OrderApiController.java     # API đơn hàng
│   │   │   ├── web/
│   │   │   │   ├── CartController.java
│   │   │   │   ├── CheckoutController.java
│   │   │   │   └── ChatController.java
│   │   │   ├── admin/
│   │   │   │   ├── UserManagementController.java
│   │   │   │   ├── product/ProductController.java
│   │   │   │   ├── category/CategoriesController.java
│   │   │   │   ├── banner/BannerController.java
│   │   │   │   └── ChatManagementController.java
│   │   │   ├── manager/
│   │   │   │   ├── ManagerController.java
│   │   │   │   └── product/ProductController.java
│   │   │   └── shipper/
│   │   │       ├── ShipperController.java
│   │   │       └── ShipperChatController.java
│   │   │
│   │   ├── entities/                       # JPA Entities
│   │   │   ├── Users.java
│   │   │   ├── Product.java
│   │   │   ├── Orders.java
│   │   │   ├── OrderDetail.java
│   │   │   ├── Cart.java
│   │   │   ├── CartDetail.java
│   │   │   ├── Review.java
│   │   │   ├── Message.java                # Chat messages
│   │   │   ├── Delivery.java               # Shipping
│   │   │   ├── Voucher.java
│   │   │   ├── Addresses.java
│   │   │   ├── Categories.java
│   │   │   ├── Brand.java
│   │   │   ├── Image.java
│   │   │   ├── Banner.java
│   │   │   ├── PaymentMethod.java
│   │   │   └── Favorite.java
│   │   │
│   │   ├── dao/                            # Data Access Layer
│   │   │   ├── inter/                      # DAO Interfaces
│   │   │   └── impl/                       # DAO Implementations
│   │   │
│   │   ├── service/                        # Business Logic Layer
│   │   │   ├── inter/                      # Service Interfaces
│   │   │   └── impl/                       # Service Implementations
│   │   │
│   │   ├── dto/                            # Data Transfer Objects
│   │   │   ├── UserDTO.java
│   │   │   ├── ProductDTO.java
│   │   │   ├── OrderBasicDTO.java
│   │   │   ├── DeliveryDTO.java
│   │   │   └── ...
│   │   │
│   │   ├── filter/
│   │   │   └── TokenAuthFilter.java        # JWT Authentication
│   │   │
│   │   ├── websocket/
│   │   │   └── ChatEndpoint.java           # WebSocket handler
│   │   │
│   │   ├── utils/
│   │   │   ├── JwtUtil.java                # JWT Token utils
│   │   │   ├── OtpUtil.java                # OTP generator
│   │   │   └── ...
│   │   │
│   │   └── mapper/                         # Entity <-> DTO mappers
│   │
│   ├── src/main/resources/
│   │   ├── config.properties               # Database credentials
│   │   └── META-INF/
│   │       └── persistence.xml             # JPA configuration
│   │
│   ├── src/main/webapp/
│   │   ├── index.jsp                       # Welcome page
│   │   ├── WEB-INF/
│   │   │   ├── web.xml                     # Deployment descriptor
│   │   │   ├── sitemesh3.xml              # SiteMesh config
│   │   │   ├── decorators/                 # Layout templates
│   │   │   │   ├── admin.jsp
│   │   │   │   ├── manager.jsp
│   │   │   │   ├── shipper.jsp
│   │   │   │   └── web.jsp
│   │   │   └── views/                      # JSP Pages
│   │   │       ├── auth/
│   │   │       │   ├── login.jsp
│   │   │       │   ├── register.jsp
│   │   │       │   └── logout.jsp
│   │   │       ├── web/
│   │   │       │   ├── home.jsp
│   │   │       │   ├── productList.jsp
│   │   │       │   ├── detailProduct.jsp
│   │   │       │   ├── cart.jsp
│   │   │       │   └── checkout.jsp
│   │   │       ├── admin/
│   │   │       ├── manager/
│   │   │       └── shipper/
│   │   │
│   │   ├── commons/                        # Shared JSP components
│   │   │   ├── web/
│   │   │   │   ├── header.jsp
│   │   │   │   ├── footer.jsp
│   │   │   │   └── navbar.jsp
│   │   │   ├── admin/
│   │   │   ├── manager/
│   │   │   └── shipper/
│   │   │
│   │   └── assets/
│   │       ├── css/                        # Stylesheets
│   │       ├── js/                         # JavaScript files
│   │       │   ├── auth-utils.js          # JWT handling
│   │       │   └── cart.js                # Cart logic
│   │       ├── images/                     # Static images
│   │       └── uploads/                    # User uploads
│   │
│   └── pom.xml                             # Maven dependencies
│
├── README.md
└── DesignDataBase.drawio                   # Database diagram
```

---

## 💾 **Cơ sở dữ liệu**

### **Database Schema**

```sql
-- Core Entities
Users (userID, username, password, email, phone, avatar, role, status, ...)
Addresses (addressID, userID, name, phone, province, district, ...)
Product (productID, productName, unitPrice, stockQuantity, categoryID, brandID, ...)
Categories (categoryID, categoryName, slug, ...)
Brand (brandID, brandName, ...)

-- Shopping & Orders
Cart (cartID, userID, ...)
CartDetail (cartDetailID, cartID, productID, quantity, ...)
Orders (orderID, userID, addressID, paymentMethodID, amount, totalDiscount, orderStatus, ...)
OrderDetail (orderDetailID, orderID, productID, quantity, unitPrice, ...)
PaymentMethod (paymentMethodID, methodName, ...)

-- Reviews & Interactions
Review (reviewID, userID, productID, rating, content, image, createdAt, ...)
Favorite (favoriteID, userID, productID, ...)

-- Shipping
Delivery (deliveryID, orderID, shipperID, deliveryStatus, assignedDate, ...)

-- Marketing
Voucher (voucherID, code, discountValue, minOrderValue, expiryDate, ...)
Banner (bannerID, title, imageUrl, linkUrl, isActive, ...)

-- Messaging
Message (messageID, roomID, senderID, senderName, content, createdAt, seen, ...)

-- Media
Image (imageID, productID, imageUrl, ...)
```

### **Entity Relationships**

- **Users** `1 ---- N` **Addresses**
- **Users** `1 ---- 1` **Cart** `1 ---- N` **CartDetail** `N ---- 1` **Product**
- **Users** `1 ---- N` **Orders** `1 ---- N` **OrderDetail** `N ---- 1` **Product**
- **Users** `1 ---- N` **Review** `N ---- 1` **Product**
- **Users** `1 ---- N` **Favorite** `N ---- 1` **Product**
- **Product** `N ---- 1` **Categories**
- **Product** `N ---- 1` **Brand**
- **Product** `1 ---- N` **Image**
- **Orders** `1 ---- 1` **Delivery** `N ---- 1` **Users (Shipper)**

---

## ⚙️ **Hướng dẫn cài đặt**

### **Yêu cầu hệ thống**

- ☕ **JDK 22** hoặc cao hơn
- 🗄️ **SQL Server 2019+** (hoặc MySQL 8+)
- 🚀 **Apache Tomcat 10.1.x**
- 📦 **Maven 3.6+**
- 💻 **IDE**: IntelliJ IDEA / Eclipse / VS Code

### **Bước 1: Clone Repository**

```bash
git clone https://github.com/tanlamnguyen0905/UTEShop.git
cd UTEShop/UTESHOP
```

### **Bước 2: Cấu hình Database**

#### **Option A: SQL Server**

1. Tạo database mới:
```sql
CREATE DATABASE UTEShopDB;
```

2. Tạo file `config.properties` trong `src/main/resources/`:
```properties
jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=UTEShopDB;encrypt=false
jdbc.username=sa
jdbc.password=YourPassword123
```

3. Cập nhật `persistence.xml` (nếu cần):
```xml
<property name="jakarta.persistence.jdbc.driver" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.SQLServerDialect"/>
```

#### **Option B: MySQL**

1. Tạo database:
```sql
CREATE DATABASE UTEShopDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Cập nhật `config.properties`:
```properties
jdbc.url=jdbc:mysql://localhost:3306/UTEShopDB?useSSL=false
jdbc.username=root
jdbc.password=yourpassword
```

3. Thay đổi dependency trong `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### **Bước 3: Build Project**

```bash
mvn clean install
```

### **Bước 4: Deploy lên Tomcat**

#### **Cách 1: Manual Deploy**
1. Copy file `target/UTESHOP.war` vào thư mục `webapps/` của Tomcat
2. Start Tomcat:
```bash
# Windows
catalina.bat run

# Linux/Mac
./catalina.sh run
```

#### **Cách 2: Deploy từ IDE**
1. **IntelliJ IDEA**:
   - Run → Edit Configurations → Add New → Tomcat Server → Local
   - Deployment tab → Add `UTESHOP:war exploded`
   - Run

2. **Eclipse**:
   - Right-click project → Run As → Run on Server
   - Select Tomcat 10.x

### **Bước 5: Truy cập ứng dụng**

- 🌐 URL: `http://localhost:8080/UTESHOP`
- 👤 Default Admin: `admin` / `admin123`
- 🛍️ Default Manager: `manager` / `manager123`
- 🚚 Default Shipper: `shipper` / `shipper123`

### **Bước 6: Cấu hình Email (Optional - cho OTP)**

Cập nhật `MailServiceImpl.java`:
```java
private static final String USER = "your-email@gmail.com";
private static final String PASSWORD = "your-app-password";
```

**Lưu ý**: Với Gmail, bạn cần tạo [App Password](https://myaccount.google.com/apppasswords)

---

## 👥 **Phân quyền và Chức năng**

### 🔓 **Guest (Khách vãng lai)**

| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Xem trang chủ | `/home` | Hiển thị sản phẩm nổi bật |
| Xem sản phẩm | `/product-detail?productID=...` | Chi tiết sản phẩm |
| Đăng ký | `/auth/register` | Tạo tài khoản mới với OTP |
| Đăng nhập | `/auth/login` | Đăng nhập hệ thống |

### 👤 **User (Khách hàng)**

| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Quản lý giỏ hàng | `/cart` | CRUD giỏ hàng |
| Thanh toán | `/checkout` | Đặt hàng và thanh toán |
| Quản lý đơn hàng | `/user/orders` | Xem lịch sử đơn hàng |
| Đánh giá sản phẩm | `/review/submit` | Rating và review |
| Quản lý địa chỉ | `/user/addresses` | CRUD địa chỉ giao hàng |
| Yêu thích | `/favorite` | Quản lý sản phẩm yêu thích |
| Chat | `/chat` | Nhắn tin với shop |
| Hồ sơ | `/user/profile` | Cập nhật thông tin cá nhân |

### 🛍️ **Manager (Người bán)**

| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Dashboard | `/api/manager/dashboard` | Thống kê shop |
| Quản lý sản phẩm | `/api/manager/products/*` | CRUD sản phẩm |
| Quản lý đơn hàng | `/api/manager/orders/*` | Xem và xử lý đơn hàng |
| Quản lý voucher | `/api/manager/vouchers/*` | Tạo mã giảm giá |
| Chat | `/api/manager/chat` | Chat với khách hàng |

### 🧑‍💼 **Admin (Quản trị viên)**

| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Dashboard | `/admin/home` | Thống kê toàn hệ thống |
| Quản lý user | `/api/admin/users/*` | CRUD người dùng |
| Quản lý sản phẩm | `/api/admin/products/*` | CRUD sản phẩm toàn hệ thống |
| Quản lý danh mục | `/api/admin/categories/*` | CRUD categories |
| Quản lý đơn hàng | `/api/admin/orders/*` | Quản lý tất cả đơn hàng |
| Quản lý banner | `/api/admin/banner/*` | CRUD banner trang chủ |
| Quản lý review | `/api/admin/review/*` | Duyệt/xóa đánh giá |
| Chat | `/api/admin/chat` | Xem tất cả cuộc trò chuyện |

### 🚚 **Shipper (Nhân viên giao hàng)**

| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Đơn hàng có sẵn | `/api/shipper/feed` | Xem đơn "Đã xác nhận" |
| Nhận đơn | `/api/shipper/delivery-action?action=accept` | Nhận đơn giao hàng |
| Đơn của tôi | `/api/shipper/my-deliveries` | Quản lý đơn đang giao |
| Cập nhật trạng thái | `/api/shipper/delivery-action?action=complete` | Đánh dấu đã giao |
| Chi tiết đơn | `/api/shipper/delivery-detail?id=...` | Xem chi tiết delivery |

---

## 🔌 **API Endpoints**

### **Authentication APIs**

```http
POST   /auth/register                # Đăng ký (với OTP verification)
POST   /auth/login                   # Đăng nhập (trả về JWT token)
GET    /auth/logout                  # Đăng xuất
POST   /auth/verify-otp              # Xác thực OTP
GET    /auth/check-exist             # Kiểm tra username/email tồn tại
```

### **Product APIs**

```http
GET    /api/products                 # Danh sách sản phẩm (phân trang)
GET    /api/products/{id}            # Chi tiết sản phẩm
POST   /api/admin/products/saveOrUpdate      # Tạo/cập nhật sản phẩm
DELETE /api/admin/products/delete    # Xóa sản phẩm
GET    /api/products/search          # Tìm kiếm sản phẩm
```

### **Cart APIs**

```http
GET    /api/cart                     # Lấy giỏ hàng
POST   /api/cart/add                 # Thêm vào giỏ
PUT    /api/cart/update              # Cập nhật số lượng
DELETE /api/cart/remove              # Xóa khỏi giỏ
DELETE /api/cart/clear               # Xóa toàn bộ giỏ
```

### **Order APIs**

```http
POST   /checkout/process             # Tạo đơn hàng
GET    /api/orders                   # Danh sách đơn hàng
GET    /api/orders/{id}              # Chi tiết đơn hàng
PUT    /api/orders/status            # Cập nhật trạng thái
DELETE /api/orders/cancel            # Hủy đơn hàng
```

### **Review APIs**

```http
POST   /review/submit                # Đánh giá sản phẩm
GET    /api/reviews/product/{id}    # Lấy review của sản phẩm
DELETE /api/admin/review/delete      # Xóa review (Admin)
```

### **WebSocket APIs**

```
ws://localhost:8080/UTESHOP/ws/chat/{roomId}
```

**Events:**
- `HISTORY` - Lịch sử tin nhắn
- `CHAT` - Tin nhắn mới
- `JOIN` - User vào phòng
- `LEAVE` - User rời phòng

---

## 🔐 **Bảo mật**

### **JWT Authentication**

Hệ thống sử dụng JWT Token cho authentication:

```java
// Token được tạo khi đăng nhập
String token = JwtUtil.generateToken(username, role, userId);

// Token được lưu trong:
1. Cookie (httpOnly=false, maxAge=1h)
2. localStorage (client-side)

// Gửi token trong request:
Authorization: Bearer <token>
```

### **Password Security**

- Mật khẩu được mã hóa bằng **BCrypt** với salt tự động
- Không lưu plain text password trong database

```java
String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
boolean isValid = BCrypt.checkpw(inputPassword, hashedPassword);
```

### **Role-based Access Control**

```java
@WebFilter(urlPatterns = {"/*"})
public class TokenAuthFilter implements Filter {
    // Kiểm tra JWT token
    // Phân quyền theo role: ADMIN > MANAGER > SHIPPER > USER
}
```

### **XSS & SQL Injection Protection**

- ✅ JPA/Hibernate PreparedStatement
- ✅ Input validation
- ✅ Output encoding trong JSP

---

## 🧪 **Testing**

### **Chạy Unit Tests**

```bash
mvn test
```

### **Test Manual**

1. **Test Authentication:**
   - Đăng ký tài khoản mới
   - Kiểm tra OTP email
   - Đăng nhập và verify JWT token

2. **Test Shopping Flow:**
   - Thêm sản phẩm vào giỏ
   - Cập nhật số lượng
   - Checkout và thanh toán
   - Kiểm tra email xác nhận

3. **Test Admin Functions:**
   - CRUD sản phẩm
   - Quản lý đơn hàng
   - Xem thống kê

---

## 🐛 **Troubleshooting**

### **Lỗi kết nối Database**

```
Error: The TCP/IP connection to the host localhost, port 1433 has failed
```

**Giải pháp:**
- Kiểm tra SQL Server đã chạy chưa
- Kiểm tra firewall
- Verify connection string trong `config.properties`

### **Lỗi 404 Not Found**

```
HTTP Status 404 – Not Found
```

**Giải pháp:**
- Check context path: `/UTESHOP`
- Verify Tomcat deployment
- Check `web.xml` configuration

### **Lỗi JWT Token Invalid**

```
401 Unauthorized - Invalid or expired token
```

**Giải pháp:**
- Token hết hạn (1 giờ) → Đăng nhập lại
- Check JWT secret key
- Verify token trong localStorage

### **Lỗi Email OTP không gửi được**

```
MessagingException: 535-5.7.8 Username and Password not accepted
```

**Giải pháp:**
- Sử dụng App Password thay vì password thường (Gmail)
- Enable "Less secure app access" (không khuyến nghị)
- Check SMTP settings

---

## 🚀 **Roadmap**

### **Version 2.0 (Q1 2026)**

- [ ] Tích hợp AI gợi ý sản phẩm
- [ ] Mobile App (React Native)
- [ ] Multi-language support
- [ ] Advanced analytics dashboard
- [ ] Inventory management system

### **Version 1.5 (Q4 2025)**

- [ ] Social login (Google, Facebook)
- [ ] Push notifications
- [ ] Export reports (PDF, Excel)
- [ ] Advanced search filters
- [ ] Product comparison

---

## 🤝 **Đóng góp**

Chúng tôi hoan nghênh mọi đóng góp! Để contribute:

1. **Fork** repository
2. Tạo **branch** mới: `git checkout -b feature/AmazingFeature`
3. **Commit** changes: `git commit -m 'Add some AmazingFeature'`
4. **Push** lên branch: `git push origin feature/AmazingFeature`
5. Tạo **Pull Request**

### **Coding Standards**

- Sử dụng Java naming conventions
- Comment code rõ ràng
- Write unit tests cho features mới
- Follow MVC pattern

---

## 📄 **License**

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👥 **Contributors**

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/tanlamnguyen0905">
        <img src="https://github.com/tanlamnguyen0905.png" width="100px;" alt="Nguyễn Lâm Tấn"/><br />
        <sub><b>Nguyễn Lâm Tấn</b></sub>
      </a><br />
      <sub>Team Leader</sub>
    </td>
    <td align="center">
      <img src="https://ui-avatars.com/api/?name=Vu+Quoc+Trung&background=random" width="100px;" alt="Vũ Quốc Trung"/><br />
      <sub><b>Vũ Quốc Trung</b></sub><br />
    </td>
    <td align="center">
      <img src="https://ui-avatars.com/api/?name=Nguyen+Kim+Dien&background=random" width="100px;" alt="Nguyễn Kim Điền"/><br />
      <sub><b>Nguyễn Kim Điền</b></sub><br />
    </td>
    <td align="center">
      <img src="https://ui-avatars.com/api/?name=Tran+Ba+Thanh&background=random" width="100px;" alt="Trần Bá Thành"/><br />
      <sub><b>Trần Bá Thành</b></sub><br />
    </td>
  </tr>
</table>

---

## 📧 **Liên hệ**

📧 **Email**: uteshop99@gmail.com  
👨‍💻 **Team**: UTEShop Development Team  
📅 **Version**: 1.0.0  
📍 **GitHub**: [https://github.com/tanlamnguyen0905/UTEShop](https://github.com/tanlamnguyen0905/UTEShop)  
🏫 **Organization**: University of Transport and Engineering (UTE)

### **Team Members**
- 👤 **Nguyễn Lâm Tấn** - Team Leader & Full-stack Developer
- 👤 **Vũ Quốc Trung** - Backend Developer & Security
- 👤 **Nguyễn Kim Điền** - Frontend Developer & UI/UX
- 👤 **Trần Bá Thành** - Database Designer & DevOps

---

<p align="center">
  <strong>Made with ❤️ by UTEShop Team</strong><br>
  <sub>Nguyễn Lâm Tấn • Vũ Quốc Trung • Nguyễn Kim Điền • Trần Bá Thành</sub><br>
  © 2025 UTEShop. All rights reserved.
</p>

<p align="center">
  <a href="#-giới-thiệu">⬆️ Back to Top</a>
</p>
