# Debug Phone Issue - Alternative Approach

## Vấn đề

Đã thử nhiều cách nhưng vẫn không lưu được số điện thoại vào database.

## Chiến lược mới: Debug từ Backend → Frontend

Thay vì debug từ frontend xuống backend, hãy làm ngược lại:
1. ✅ Test database trực tiếp (SQL)
2. ✅ Test JPA/DAO (Java)
3. ✅ Test API (Postman/curl)
4. ✅ Test Frontend (Browser)

## Bước 1: Test Database trực tiếp

### 1.1. Kiểm tra schema

Chạy SQL này trong **SQL Server Management Studio**:

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

**Kết quả mong đợi**: Phải có dòng:
```
phone    nvarchar    20    YES
```

**Nếu KHÔNG có cột phone** → Chạy:
```sql
ALTER TABLE Addresses ADD phone NVARCHAR(20) NULL;
```

### 1.2. Test INSERT trực tiếp

```sql
-- Lấy userID (thay 'your_username')
SELECT userID, username FROM Users WHERE username = 'folders';

-- INSERT test (thay userID = 1)
INSERT INTO Addresses (name, phone, province, district, ward, addressDetail, isDefault, userID)
VALUES (N'Test SQL', '0123456789', N'Hà Nội', N'Ba Đình', N'Điện Biên', N'123 ABC', 0, 1);

-- Kiểm tra
SELECT addressID, name, phone, province FROM Addresses ORDER BY addressID DESC;
```

**Kết quả**:
- ✅ Nếu INSERT thành công và phone hiển thị → Database OK, vấn đề ở JPA/Backend
- ❌ Nếu INSERT lỗi → Vấn đề ở database (constraint, trigger...)

### 1.3. Kiểm tra constraints

```sql
-- Check constraints trên cột phone
SELECT 
    con.CONSTRAINT_NAME,
    con.CONSTRAINT_TYPE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS con
INNER JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE col 
    ON con.CONSTRAINT_NAME = col.CONSTRAINT_NAME
WHERE con.TABLE_NAME = 'Addresses' AND col.COLUMN_NAME = 'phone';
```

## Bước 2: Test JPA/DAO (Java Servlet)

### 2.1. Truy cập Test Servlet

URL: `http://localhost:8080/UTESHOP/test-address`

File: `UTESHOP/src/main/java/ute/test/TestAddressServlet.java`

Servlet này sẽ:
1. Tạo address với phone = "0999888777"
2. Lưu vào DB qua DAO
3. Đọc lại từ DB
4. Kiểm tra phone có đúng không
5. Xóa address test (cleanup)

**Kết quả mong đợi**:
```
✅ Address inserted successfully!
✅ Address retrieved successfully!
Phone: 0999888777
🎉 SUCCESS! Phone được lưu và đọc đúng!
```

**Nếu FAILED**:
- Phone = null → JPA không map đúng hoặc DB không có cột
- Exception → Xem stacktrace

### 2.2. Check Server Console Log

Khi chạy servlet, xem console:
```
=== DAO INSERT ADDRESS ===
Name: Test Servlet
Phone: 0999888777
Province: Test Province
==========================
Address inserted with ID: 123
```

## Bước 3: Test API (Postman/Thunder Client)

### 3.1. Import collection

File: `UTESHOP/test_api_postman.json`

Import vào Postman/Thunder Client

### 3.2. Test requests

**Test 1: Create Address**
```
POST http://localhost:8080/UTESHOP/api/user/addresses
Content-Type: application/json

{
  "name": "Test Postman",
  "phone": "0987654321",
  "province": "Hà Nội",
  "district": "Ba Đình",
  "ward": "Điện Biên",
  "addressDetail": "123 Test Street",
  "isDefault": false
}
```

**Response mong đợi**:
```json
{
  "success": true,
  "message": "Address created successfully",
  "data": {
    "addressID": 1,
    "name": "Test Postman",
    "phone": "0987654321",  ← Kiểm tra này!
    ...
  }
}
```

**Test 2: Get All Addresses**
```
GET http://localhost:8080/UTESHOP/api/user/addresses
```

Kiểm tra response có phone không.

**Test 3: Update Address**
```
PUT http://localhost:8080/UTESHOP/api/user/addresses/1
Content-Type: application/json

{
  "name": "folders",
  "phone": "0999888777",
  ...
}
```

### 3.3. Check console logs

Server console phải hiển thị:
```
=== DEBUG ADDRESS CREATE ===
Name: Test Postman
Phone: 0987654321
Province: Hà Nội
===========================

=== DAO INSERT ADDRESS ===
Phone: 0987654321
==========================
```

## Bước 4: Test Frontend

Chỉ test sau khi Bước 1-3 đều OK!

### 4.1. Mở Console (F12)

### 4.2. Thêm địa chỉ

Điền form và submit, xem console:
```
Phone input changed: 0912345678 | Digits: 0912345678
✅ Phone valid: 0912345678
=== FORM SUBMIT DEBUG ===
Phone value: 0912345678
Phone length: 10
=========================
Sending address payload: {..., phone: "0912345678", ...}
```

### 4.3. Check Network tab

- Request payload có `phone`
- Response có `phone`

## Decision Tree

```
1. Database có cột phone?
   ├─ NO → ALTER TABLE thêm cột
   └─ YES → Tiếp tục

2. SQL INSERT trực tiếp OK?
   ├─ NO → Check constraints, triggers
   └─ YES → Tiếp tục

3. Test Servlet OK?
   ├─ NO → Check Entity mapping, DAO logic
   └─ YES → Tiếp tục

4. API test (Postman) OK?
   ├─ NO → Check Controller, validation
   └─ YES → Tiếp tục

5. Frontend submit OK?
   ├─ NO → Check JavaScript, form data
   └─ YES → DONE!
```

## Các vấn đề thường gặp

### Issue 1: Cột phone không tồn tại

**Triệu chứng**: SQL INSERT lỗi "Invalid column name 'phone'"

**Fix**:
```sql
ALTER TABLE Addresses ADD phone NVARCHAR(20) NULL;
```

### Issue 2: JPA không map cột phone

**Triệu chứng**: Test servlet lưu OK nhưng phone = null khi đọc lại

**Check**:
```java
// Addresses.java
@Column(columnDefinition = "NVARCHAR(20)")
private String phone;  // ← Phải có annotation này
```

**Fix**: 
- Thêm getter/setter
- Restart server để JPA reload entity

### Issue 3: Transaction rollback

**Triệu chứng**: Không có lỗi nhưng data không lưu vào DB

**Check console**: Có chữ "rollback" không?

**Fix**: Check validation, constraints trong AddressApiController

### Issue 4: Frontend không gửi phone

**Triệu chứng**: Console log `phone: undefined` hoặc `phone: ""`

**Check**:
```javascript
// Console
document.getElementById('phone').value
```

**Fix**: Đảm bảo input có `id="phone"` và `name="phone"`

## Checklist cuối cùng

- [ ] Database có cột `phone` (kiểm tra bằng SQL)
- [ ] SQL INSERT trực tiếp OK
- [ ] Entity `Addresses.java` có field `phone` với @Column
- [ ] Test servlet OK (http://localhost:8080/UTESHOP/test-address)
- [ ] API test (Postman) OK
- [ ] Console logs hiển thị phone đúng
- [ ] Frontend gửi phone trong payload
- [ ] Không có error trong server logs

## Kết luận

Nếu đã làm đủ 4 bước trên mà vẫn lỗi, vui lòng cung cấp:

1. **Screenshot** kết quả query:
   ```sql
   SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_NAME = 'Addresses';
   ```

2. **Screenshot** kết quả test servlet: `http://localhost:8080/UTESHOP/test-address`

3. **Server console logs** khi thêm địa chỉ

4. **Browser console logs** (F12)

5. **Postman response** khi test API

Với đầy đủ thông tin này, chúng ta sẽ tìm ra được vấn đề chính xác!


