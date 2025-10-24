# Test Address API với số điện thoại

## Vấn đề hiện tại
Địa chỉ hiển thị nhưng không có số điện thoại → Có thể:
1. ❌ Database chưa có cột `phone`
2. ❌ Dữ liệu cũ có `phone = NULL`
3. ❌ Frontend không gửi `phone`
4. ❌ Backend không lưu `phone`

## Bước 1: Kiểm tra Database Schema

Chạy query sau trong SQL Server Management Studio:

```sql
-- Xem cấu trúc bảng Addresses
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses' 
ORDER BY ORDINAL_POSITION;
```

**Kết quả mong đợi**: Phải có cột `phone` với type `NVARCHAR(20)`

### Nếu KHÔNG có cột phone:

```sql
-- Thêm cột phone
ALTER TABLE Addresses
ADD phone NVARCHAR(20) NULL;
```

## Bước 2: Kiểm tra dữ liệu hiện tại

```sql
-- Xem tất cả địa chỉ và phone
SELECT 
    addressID, 
    name, 
    phone,
    CASE 
        WHEN phone IS NULL THEN '❌ NULL'
        WHEN phone = '' THEN '⚠️ EMPTY'
        ELSE '✅ ' + phone
    END as phone_status,
    province
FROM Addresses
ORDER BY addressID DESC;
```

### Nếu phone = NULL cho tất cả:

**Nguyên nhân**: Dữ liệu cũ tạo trước khi thêm cột phone.

**Giải pháp**:

**Option 1**: Cập nhật phone từ bảng Users
```sql
UPDATE a
SET a.phone = u.phone
FROM Addresses a
INNER JOIN Users u ON a.userID = u.userID
WHERE (a.phone IS NULL OR a.phone = '')
  AND u.phone IS NOT NULL;
```

**Option 2**: Xóa địa chỉ cũ và tạo lại từ UI
```sql
-- XÓA tất cả địa chỉ (CHÚ Ý: Chỉ dùng cho test/dev)
DELETE FROM Addresses;
```

## Bước 3: Test thêm địa chỉ mới

### 3.1. Mở Browser Console (F12)

Vào trang: `http://localhost:8080/UTESHOP/user/profile`

### 3.2. Thêm địa chỉ mới và quan sát

1. Click **"Thêm địa chỉ mới"**
2. Điền form:
   - Tên: `Test User`
   - **Số điện thoại**: `0987654321` ← QUAN TRỌNG
   - Tỉnh: `Hà Nội`
   - Quận: `Ba Đình`
   - Phường: `Điện Biên`
   - Địa chỉ chi tiết: `123 ABC`

3. Mở **Developer Tools** (F12) → Tab **Network**
4. Click **"Lưu địa chỉ"**

### 3.3. Kiểm tra Request/Response

**Request (Payload tab)**:
```json
{
  "name": "Test User",
  "phone": "0987654321",  ← Phải có trường này!
  "province": "Hà Nội",
  "district": "Ba Đình",
  "ward": "Điện Biên",
  "addressDetail": "123 ABC",
  "isDefault": false
}
```

**Response (Preview tab)**:
```json
{
  "success": true,
  "message": "Address created successfully",
  "data": {
    "addressID": 1,
    "name": "Test User",
    "phone": "0987654321",  ← Phải trả về phone!
    ...
  }
}
```

### 3.4. Nếu Request KHÔNG có phone:

**Nguyên nhân**: Frontend không lấy được giá trị từ input

**Kiểm tra**:
```javascript
// Paste vào Console
document.getElementById('phone').value
// Phải trả về số điện thoại bạn nhập
```

**Nếu trả về empty** → Vấn đề ở HTML form

## Bước 4: Kiểm tra Server Console Log

Khi submit form, server console phải hiển thị:

```
=== DEBUG ADDRESS CREATE ===
Name: Test User
Phone: 0987654321      ← Phải có phone ở đây!
Province: Hà Nội
===========================

=== DAO INSERT ADDRESS ===
Name: Test User
Phone: 0987654321      ← Phải có phone ở đây!
Province: Hà Nội
==========================
Address inserted with ID: 1
```

### Nếu Phone: null

**Nguyên nhân**: Frontend không gửi hoặc backend không parse đúng

**Debug thêm**:
1. Copy Request payload từ Network tab
2. Paste vào text editor
3. Kiểm tra có key `"phone"` không

## Bước 5: Kiểm tra Database sau khi thêm

```sql
-- Lấy địa chỉ mới nhất
SELECT TOP 1
    addressID, 
    name, 
    phone,  ← Kiểm tra cột này
    province,
    district,
    ward,
    addressDetail
FROM Addresses
ORDER BY addressID DESC;
```

**Kết quả mong đợi**: `phone` phải có giá trị `0987654321`

**Nếu phone = NULL** → Vấn đề ở DAO/JPA persist

## Bước 6: Test với Postman/Thunder Client

**Method**: POST  
**URL**: `http://localhost:8080/UTESHOP/api/user/addresses`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "name": "Postman Test",
  "phone": "0999888777",
  "province": "TP.HCM",
  "district": "Quận 1",
  "ward": "Phường Bến Nghé",
  "addressDetail": "456 XYZ Street",
  "isDefault": false
}
```

**Expected Response**:
```json
{
  "success": true,
  "message": "Address created successfully",
  "data": {
    "addressID": 2,
    "name": "Postman Test",
    "phone": "0999888777",  ← KIỂM TRA
    "province": "TP.HCM",
    ...
  }
}
```

Nếu thành công → Vấn đề ở Frontend (JSP/JavaScript)  
Nếu thất bại → Vấn đề ở Backend (Controller/DAO)

## Kết luận

| Triệu chứng | Nguyên nhân | Giải pháp |
|------------|-------------|-----------|
| Database không có cột `phone` | Chưa chạy migration | Chạy `fix_address_phone.sql` |
| Địa chỉ cũ không có phone | Dữ liệu tạo trước khi thêm cột | Chạy `update_existing_addresses.sql` |
| Request không có `phone` | Frontend lỗi | Kiểm tra form input `id="phone"` |
| Response không có `phone` | Backend lỗi | Kiểm tra logs, debug DAO |
| Database nhận được nhưng = NULL | JPA không persist | Restart server, check entity mapping |

## Checklist Debug

- [ ] Database có cột `phone`
- [ ] Server đã restart sau khi sửa Entity
- [ ] Form HTML có input `id="phone"` với `name="phone"`
- [ ] Request payload có trường `"phone"`
- [ ] Server log hiển thị phone đúng
- [ ] Database query trả về phone đúng
- [ ] JSP hiển thị `${addr.phone}` đúng


