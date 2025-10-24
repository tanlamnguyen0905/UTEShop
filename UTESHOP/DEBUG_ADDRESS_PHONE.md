# Hướng dẫn Debug vấn đề lưu số điện thoại địa chỉ

## Vấn đề
Phần địa chỉ chưa lưu được số điện thoại vào database.

## Code đã được kiểm tra
✅ **Entity `Addresses`** - Có trường `phone` (NVARCHAR(20))  
✅ **DTO `AddressDTO`** - Có trường `phone` và các phương thức convert  
✅ **API Controller** - Validate và xử lý `phone` đúng cách  
✅ **Service & DAO** - Đều có code set `phone` đúng  

**Kết luận**: Code backend hoàn toàn đúng!

## Các bước Debug

### Bước 1: Restart Application Server
**QUAN TRỌNG**: Sau khi sửa Entity, PHẢI restart lại Tomcat/Server để Hibernate cập nhật schema database.

```bash
# Stop server
# Start server lại
```

### Bước 2: Kiểm tra Database Schema

Chạy script SQL sau để kiểm tra và thêm cột `phone` nếu cần:

```sql
-- File: fix_address_phone.sql

-- Kiểm tra xem cột phone đã tồn tại chưa
IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'phone'
)
BEGIN
    ALTER TABLE Addresses
    ADD phone NVARCHAR(20) NULL;
    
    PRINT 'Đã thêm cột phone vào bảng Addresses';
END
ELSE
BEGIN
    PRINT 'Cột phone đã tồn tại trong bảng Addresses';
END
```

### Bước 3: Kiểm tra Frontend (Browser)

1. Mở **Developer Tools** (F12)
2. Chuyển sang tab **Network**
3. Submit form thêm/sửa địa chỉ
4. Kiểm tra request đến `/api/user/addresses`:
   - **Headers**: Kiểm tra `Content-Type: application/json`
   - **Payload**: Đảm bảo có trường `phone` trong JSON:
     ```json
     {
       "name": "Nguyễn Văn A",
       "phone": "0123456789",   ← Kiểm tra trường này
       "province": "TP.HCM",
       ...
     }
     ```

### Bước 4: Kiểm tra Console Log

Đã thêm debug log vào code. Khi submit form, kiểm tra console server xem log:

**Controller Log:**
```
=== DEBUG ADDRESS CREATE ===
Name: Nguyễn Văn A
Phone: 0123456789
Province: TP.HCM
===========================
```

**DAO Log:**
```
=== DAO INSERT ADDRESS ===
Name: Nguyễn Văn A
Phone: 0123456789
Province: TP.HCM
==========================
Address inserted with ID: 123
```

Nếu `Phone: null` → Vấn đề ở frontend/JSON parsing  
Nếu `Phone: 0123456789` nhưng không lưu → Vấn đề ở database schema

### Bước 5: Test thủ công qua Postman/Thunder Client

Gửi request POST đến API:

**URL**: `http://localhost:8080/UTESHOP/api/user/addresses`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer YOUR_TOKEN
```

**Body** (JSON):
```json
{
  "name": "Test User",
  "phone": "0987654321",
  "province": "Hà Nội",
  "district": "Quận Ba Đình",
  "ward": "Phường Điện Biên",
  "addressDetail": "123 Đường ABC",
  "isDefault": false
}
```

**Expected Response**:
```json
{
  "success": true,
  "message": "Address created successfully",
  "data": {
    "addressID": 1,
    "name": "Test User",
    "phone": "0987654321",  ← Kiểm tra trường này
    ...
  }
}
```

### Bước 6: Kiểm tra lại Database

Sau khi submit, query database:

```sql
SELECT addressID, name, phone, province, district, ward, addressDetail, isDefault
FROM Addresses
ORDER BY addressID DESC;
```

Kiểm tra xem cột `phone` có giá trị không.

## Giải pháp nhanh

Nếu vẫn không work, thử các bước sau:

1. **Drop và recreate table** (CHỈ dùng cho development):
```sql
DROP TABLE IF EXISTS Addresses;
-- Restart server để Hibernate tự tạo lại table
```

2. **Thay đổi Hibernate config** (tạm thời):
```xml
<!-- persistence.xml -->
<property name="hibernate.hbm2ddl.auto" value="create-drop"/>
```
⚠️ **Cảnh báo**: Sẽ xóa toàn bộ data khi restart!

3. **Kiểm tra Hibernate SQL logs**:
File `persistence.xml` đã bật:
```xml
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
```

Xem console khi insert/update có SQL nào được execute không.

## Checklist cuối cùng

- [ ] Đã restart server sau khi sửa Entity
- [ ] Cột `phone` đã tồn tại trong database
- [ ] Frontend gửi đúng JSON có trường `phone`
- [ ] Không có lỗi validation (phone phải có giá trị)
- [ ] Token authentication đúng
- [ ] Kiểm tra console log để thấy flow hoàn chỉnh

## Liên hệ Debug

Nếu vẫn gặp vấn đề, cung cấp các thông tin sau:
1. Screenshot Network tab (request/response)
2. Console log từ server
3. Screenshot database schema (INFORMATION_SCHEMA)
4. Version: Java, Tomcat, SQL Server


