# 🛒 **UTEShop – Nền tảng Web Bán Hàng Trực Tuyến**

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java" alt="Java Badge"/>
  <img src="https://img.shields.io/badge/Jakarta%20EE-10-blue?style=flat-square&logo=eclipse" alt="Jakarta EE"/>
  <img src="https://img.shields.io/badge/Servlet-JSP-yellow?style=flat-square&logo=java" alt="Servlet JSP"/>
  <img src="https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square&logo=hibernate" alt="JPA Hibernate"/>
  <img src="https://img.shields.io/badge/Bootstrap-5.3-purple?style=flat-square&logo=bootstrap" alt="Bootstrap"/>
  <img src="https://img.shields.io/badge/Sitemesh-Decorator-success?style=flat-square" alt="Sitemesh"/>
  <img src="https://img.shields.io/badge/JWT-Security-green?style=flat-square&logo=jsonwebtokens" alt="JWT"/>
  <img src="https://img.shields.io/badge/Database-SQLServer-red?style=flat-square&logo=databricks" alt="Database"/>
  <img src="https://img.shields.io/badge/Maven-Build-success?style=flat-square&logo=apachemaven" alt="Maven"/>
</p>

<p align="center">
💡 Một nền tảng thương mại điện tử đa vai trò (Guest, User, Manager, Admin, Shipper)  
hỗ trợ thanh toán trực tuyến, nhắn tin thời gian thực và quản lý sản phẩm – đơn hàng.
</p>

---

## 🧭 **Mục lục**

- [🌐 Tổng quan](#-tổng-quan)
- [⚙️ Công nghệ sử dụng](#️-công-nghệ-sử-dụng)
- [🧩 Cấu trúc dự án](#-cấu-trúc-dự-án)
- [🛠️ Cài đặt dự án](#️-cài-đặt-dự-án)
- [👥 Các vai trò và chức năng](#-các-vai-trò-và-chức-năng)
- [🗂️ Cấu trúc cơ sở dữ liệu](#️-cấu-trúc-cơ-sở-dữ-liệu)
- [🧠 Hướng phát triển tương lai](#-hướng-phát-triển-tương-lai)
- [🤝 Đóng góp & Liên hệ](#-đóng-góp--liên-hệ)

---

## 🌐 **Tổng quan**

**UTEShop** là hệ thống web thương mại điện tử toàn diện cho phép người dùng mua sắm trực tuyến, quản lý sản phẩm, theo dõi đơn hàng, đánh giá – bình luận sản phẩm, và thanh toán bằng **COD, VNPAY, hoặc MOMO**.

Hệ thống hỗ trợ đa vai trò:

> 👥 Guest • 👤 User • 🛍️ Manager  • 🧑‍💼 Admin • 🚚 Shipper

---

## ⚙️ **Công nghệ sử dụng**

| Thành phần | Công nghệ |
|-------------|------------|
| **Ngôn ngữ** | Java 17 |
| **Back-end** | Servlet, Jakarta, JWT |
| **Template Decorator** | Sitemesh (Decorator Pattern) |
| **ORM / Persistence** | JPA (Hibernate) |
| **Front-end** | JSP, CSS, Bootstrap 5 |
| **CSDL hỗ trợ** | SQL Server |
| **Xác thực bảo mật** | JWT (JSON Web Token) |
| **Thanh toán** | VNPAY, MOMO, COD |
| **Build Tool** | Maven |
| **Server** | Apache Tomcat 10.x |
| **IDE khuyến nghị** | VS Code / Eclipse / IntelliJ IDEA / String tool suite |

---

## 🧩 **Cấu trúc dự án**

```bash
UTEShop/
├── src/main/java/ute/
│   ├── configs/          # Cấu hình ứng dụng
│   ├── controllers/      # Servlet xử lý logic (Auth, Product, Order, Chat,...)
│   ├── entities/         # Entity JPA (User, Product, Order, Review,...)
│   ├── dao/              # Data Access Object (DAO)
│   ├── services/         # Business Logic (Service Layer)
│   ├── dto/              # Data Transfer Object
│   ├── security/         # JWT Filter, TokenProvider
│   └── utils/            # Các hàm tiện ích (Validator, Formatter,...)
├── src/main/webapp/
│   ├── commons/          # JSP Layout sử dụng Sitemesh Decorator
│   ├── WEB-INF/views/    # JSP Pages (web, admin, manager,...)
│   ├── assets/           # CSS, JS, hình ảnh
│   ├── uploads/          # Ảnh sản phẩm, avatar, review
├── pom.xml
└── README.md
```

---

## 🛠️ **Cài đặt dự án**

### 1️⃣ Clone repository
```bash
git clone https://github.com/<username>/UTEShop.git
```

### 2️⃣ Import vào IDE  
Mở trong **VS Code**, **Eclipse**, hoặc **IntelliJ IDEA**, chọn `Import Maven Project`.

### 3️⃣ Cấu hình CSDL
- Tạo database `UTEShopDB` trong SQL Server, MySQL hoặc PostgreSQL  
- Chỉnh sửa file `persistence.xml` tương ứng:
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:sqlserver://localhost:1433;databaseName=UTEShopDB"/>
<property name="jakarta.persistence.jdbc.user" value="sa"/>
<property name="jakarta.persistence.jdbc.password" value="123456"/>
```

### 4️⃣ Chạy trên Tomcat
- Deploy dự án trên **Apache Tomcat 10.x**  
- Truy cập: 👉 [http://localhost:8080/UTESHOP](http://localhost:8080/UTESHOP)

---

## 👥 **Các vai trò và chức năng**

### 🧍 Guest
- 🔑 Đăng nhập / Đăng ký / Quên mật khẩu (OTP qua email)  
- 🔍 Xem danh sách sản phẩm, tìm kiếm, lọc theo danh mục  
- 📄 Xem chi tiết sản phẩm  

---

### 👤 User

| Tính năng | Mô tả |
|------------|--------|
| 🏠 **Trang chủ** | Hiển thị 20 sản phẩm mới, bán chạy, đánh giá cao, yêu thích (lazy loading) |
| 🗂️ **Danh mục sản phẩm** | Lọc theo category, giá, thương hiệu |
| 🛒 **Giỏ hàng** | Thêm, sửa, xóa, tính tổng tiền tự động |
| 💳 **Thanh toán** | COD, VNPAY, MOMO |
| 💬 **Tin nhắn** | Chat trực tiếp với shop (real-time) |
| 👤 **Hồ sơ cá nhân** | Cập nhật thông tin, quản lý địa chỉ nhận hàng |
| 🧾 **Đơn hàng** | Theo dõi trạng thái: Mới, Xác nhận, Đang giao, Đã giao, Hủy, Hoàn tiền |
| ❤️ **Yêu thích** | Lưu danh sách sản phẩm yêu thích |
| 👀 **Đã xem** | Lưu lại sản phẩm đã xem |
| ⭐ **Đánh giá & Bình luận** | Chỉ sản phẩm đã mua (≥50 ký tự, có thể kèm ảnh/video) |
| 🎟️ **Mã giảm giá** | Áp dụng khi thanh toán |

---

### 🛍️ Manager (Chủ shop)
- 📦 CRUD sản phẩm, hiển thị / ẩn sản phẩm  
- 🏷️ Quản lý danh mục riêng  
- 💰 Quản lý chương trình khuyến mãi (giảm %, giảm phí ship)  
- 📊 Quản lý đơn hàng thuộc shop  
- 💬 Nhắn tin với khách hàng  

---

### 🧑‍💼 Admin
- 🔍 Tìm kiếm, khóa / mở khóa tài khoản người dùng  
- 🗃️ Quản lý sản phẩm của toàn bộ shop  
- 🧩 Quản lý danh mục chung  
- 🎯 Quản lý khuyến mãi toàn hệ thống  
- 📦 Quản lý đơn hàng toàn hệ thống  
- 📈 Thống kê doanh thu tổng thể  

---

### 🚚 Shipper (mở rộng)
- 🗓️ Xem danh sách đơn hàng được phân công  
- 🔁 Cập nhật trạng thái giao hàng (Đang giao → Đã giao)  
- 📊 Thống kê số đơn hàng giao thành công  

---

## 🗂️ **Cấu trúc cơ sở dữ liệu (tóm tắt)**

| Bảng | Mô tả |
|------|--------|
| `Users` | Thông tin người dùng |
| `Product` | Sản phẩm |
| `Category` | Danh mục |
| `Orders` | Đơn hàng |
| `OrderDetail` | Chi tiết đơn hàng |
| `Review` | Đánh giá & bình luận |
| `Message` | Tin nhắn giữa user & shop |
| `UserCoupon` | Mã giảm giá |
| `Address` | Địa chỉ giao hàng |
| `Banner` | Banner |
| `PaymentMethod` | Phương thức thanh toán |
| `Image` | Ảnh |
| `Cart` | Giỏ hàng |
| `Cart Detail` | Chi tiết giỏ hàng |
| `Brand` | Thương hiệu |
| `Favorite` | Yêu thích |
---

## 🧠 **Hướng phát triển tương lai**
- [ ] Thêm hệ thống **gợi ý sản phẩm bằng AI**  
- [ ] Tối ưu **UI/UX responsive** cho mobile  
- [ ] Mở rộng **phân quyền chi tiết hơn** (multi-role per user)   

---

## 🤝 **Đóng góp & Liên hệ**

Mọi đóng góp đều được hoan nghênh!  
Hãy **fork dự án**, tạo **branch mới**, và gửi **Pull Request** ✨  

📧 **Liên hệ:** [uteshop@gmail.com](mailto:uteshop@gamil.com)  
👨‍💻 **Tác giả:** Nhóm phát triển **UTEShop**  
📅 **Phiên bản:** v1.0.0  
📍 **GitHub:** [https://github.com/tanlamnguyen0905/UTEShop](https://github.com/tanlamnguyen0905/UTEShop)

---

<p align="center">
  Made with ❤️ by <b>UTEShop Team</b>
</p>
