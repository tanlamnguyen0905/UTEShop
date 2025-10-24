# 🔧 Hướng dẫn khắc phục lỗi Database

## ❌ Lỗi: EntityManager is null

### Nguyên nhân
EntityManager không thể kết nối đến database SQL Server.

### Kiểm tra từng bước:

#### 1️⃣ **Kiểm tra SQL Server đã chạy chưa**

**Windows:**
```bash
# Mở Services (nhấn Win+R, gõ services.msc)
# Tìm "SQL Server (MSSQLSERVER)" hoặc "SQL Server (SQLEXPRESS)"
# Kiểm tra Status = Running
```

**Hoặc dùng Command Line:**
```bash
# Kiểm tra service
sc query MSSQLSERVER

# Nếu chưa chạy, start service:
net start MSSQLSERVER
```

#### 2️⃣ **Kiểm tra Database UTESHOP đã tồn tại chưa**

**Mở SQL Server Management Studio (SSMS):**
1. Connect vào SQL Server (localhost)
2. Kiểm tra database "UTESHOP" có trong danh sách không
3. Nếu chưa có, tạo database:

```sql
CREATE DATABASE UTESHOP;
GO

USE UTESHOP;
GO
```

**Hoặc dùng sqlcmd:**
```bash
sqlcmd -S localhost -U sa -P 1 -Q "CREATE DATABASE UTESHOP"
```

#### 3️⃣ **Kiểm tra thông tin đăng nhập**

File: `UTESHOP/src/main/resources/config.properties`

```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=UTESHOP;encrypt=false;trustServerCertificate=true
db.username=sa
db.password=1
```

**Cần kiểm tra:**
- ✅ Port 1433 đúng chưa? (mặc định SQL Server)
- ✅ Username `sa` đúng chưa?
- ✅ Password `1` đúng chưa?
- ✅ Database name `UTESHOP` đúng chưa?

**Test connection:**
```bash
sqlcmd -S localhost -U sa -P 1 -d UTESHOP
```

Nếu kết nối thành công, bạn sẽ thấy:
```
1>
```

#### 4️⃣ **Kiểm tra JDBC Driver**

Đảm bảo trong `pom.xml` có dependency:

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.2.0.jre11</version>
</dependency>
```

Sau đó chạy:
```bash
mvn clean install
```

#### 5️⃣ **Kiểm tra file persistence.xml**

File: `UTESHOP/src/main/resources/META-INF/persistence.xml`

Đảm bảo:
- ✅ Có tag `<persistence-unit name="UTESHOP">`
- ✅ Tất cả entities đã được khai báo
- ✅ Dialect: `org.hibernate.dialect.SQLServerDialect`
- ✅ Driver: `com.microsoft.sqlserver.jdbc.SQLServerDriver`

#### 6️⃣ **Xem log chi tiết**

Sau khi restart server, xem console log. Bạn sẽ thấy:

**Nếu thành công:**
```
Đang tạo EntityManagerFactory...
Database URL: jdbc:sqlserver://localhost:1433;databaseName=UTESHOP;...
EntityManagerFactory đã được tạo thành công!
```

**Nếu lỗi:**
```
========================================
LỖI KHI TẠO ENTITYMANAGER
========================================
Chi tiết lỗi: [thông báo lỗi cụ thể]
...
Vui lòng kiểm tra:
1. SQL Server đã chạy chưa?
2. Database UTESHOP đã tồn tại chưa?
3. Username/password trong config.properties đúng chưa?
4. File persistence.xml có lỗi cú pháp không?
========================================
```

---

## 🔍 Các lỗi thường gặp

### Lỗi 1: "Login failed for user 'sa'"

**Nguyên nhân:** Password sai

**Giải pháp:**
1. Reset password SQL Server:
```sql
-- Trong SSMS, chạy với Windows Authentication
ALTER LOGIN sa WITH PASSWORD = '1';
ALTER LOGIN sa ENABLE;
```

2. Cập nhật `config.properties` với password mới

### Lỗi 2: "Cannot open database UTESHOP"

**Nguyên nhân:** Database chưa tồn tại

**Giải pháp:**
```sql
CREATE DATABASE UTESHOP;
```

### Lỗi 3: "TCP/IP connection to host localhost, port 1433 failed"

**Nguyên nhân:** SQL Server không lắng nghe trên TCP/IP

**Giải pháp:**
1. Mở **SQL Server Configuration Manager**
2. SQL Server Network Configuration → Protocols for MSSQLSERVER
3. Enable **TCP/IP**
4. Restart SQL Server service

### Lỗi 4: "The driver could not establish a secure connection"

**Nguyên nhân:** SSL/TLS configuration issue

**Giải pháp:** Đã fix trong URL với:
```
encrypt=false;trustServerCertificate=true
```

### Lỗi 5: "Không tìm thấy file config.properties"

**Nguyên nhân:** File không nằm đúng vị trí

**Giải pháp:**
1. Kiểm tra file tại: `src/main/resources/config.properties`
2. Rebuild project:
```bash
mvn clean compile
```

---

## ✅ Checklist trước khi chạy

- [ ] SQL Server đang chạy
- [ ] Database UTESHOP đã tồn tại
- [ ] TCP/IP enabled trên SQL Server
- [ ] Port 1433 không bị firewall block
- [ ] Username/password trong config.properties đúng
- [ ] File config.properties tồn tại tại `src/main/resources/`
- [ ] Đã chạy `mvn clean install`
- [ ] JDBC driver đã được tải về (check trong `.m2/repository/`)

---

## 🚀 Restart server sau khi sửa

```bash
# Dừng server hiện tại (Ctrl+C)

# Rebuild và chạy lại
mvn clean tomcat7:run
```

---

## 📝 Test kết nối Database

Tạo file test:

```java
// src/main/java/ute/test/TestConnection.java
package ute.test;

import jakarta.persistence.EntityManager;
import ute.configs.JPAConfig;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Đang test kết nối database...");
            EntityManager em = JPAConfig.getEntityManager();
            
            if (em != null) {
                System.out.println("✅ Kết nối thành công!");
                System.out.println("EntityManager: " + em);
                em.close();
            } else {
                System.err.println("❌ EntityManager is null");
            }
            
            JPAConfig.closeEntityManagerFactory();
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

Chạy test:
```bash
mvn exec:java -Dexec.mainClass="ute.test.TestConnection"
```

---

## 🆘 Vẫn không được?

### Option 1: Sử dụng SQL Server Express

Nếu dùng SQL Server Express, URL phải là:
```properties
db.url=jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=UTESHOP;encrypt=false;trustServerCertificate=true
```

### Option 2: Kiểm tra Firewall

```bash
# Mở port 1433
netsh advfirewall firewall add rule name="SQL Server" dir=in action=allow protocol=TCP localport=1433
```

### Option 3: Dùng Named Pipes thay vì TCP/IP

```properties
db.url=jdbc:sqlserver://localhost;databaseName=UTESHOP;integratedSecurity=false;encrypt=false;trustServerCertificate=true
```

### Option 4: Enable Mixed Mode Authentication

1. Mở SSMS
2. Right-click server → Properties
3. Security → SQL Server and Windows Authentication mode
4. Restart SQL Server service

---

## 📞 Liên hệ

Nếu vẫn gặp lỗi sau khi làm tất cả các bước trên:
1. Copy toàn bộ stack trace
2. Copy nội dung file `config.properties`
3. Chụp ảnh SQL Server services đang chạy
4. Liên hệ team support

---

**Good luck! 🍀**

