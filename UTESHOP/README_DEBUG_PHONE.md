# 🔍 Hướng dẫn Debug: Không lưu được số điện thoại

## Tình huống

- ✅ Code backend có xử lý phone
- ✅ Frontend có input phone  
- ❌ Nhưng vẫn không lưu được vào database

## 🚀 Quick Start (5 phút)

### Bước 1: Check Database (QUAN TRỌNG NHẤT!)

Mở **SQL Server Management Studio**, chạy:

```sql
-- File: quick_check.sql
-- Copy toàn bộ nội dung và Execute
```

Hoặc chạy command đơn giản:

```sql
-- Kiểm tra cột phone có tồn tại không
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'phone';
```

**Kết quả mong đợi**:
```
COLUMN_NAME  DATA_TYPE  CHARACTER_MAXIMUM_LENGTH
phone        nvarchar   20
```

**Nếu trả về 0 rows** → **ĐÂY LÀ VẤN ĐỀ!**

**Fix ngay**:
```sql
ALTER TABLE Addresses ADD phone NVARCHAR(20) NULL;
```

### Bước 2: Test Java DAO

Truy cập: `http://localhost:8080/UTESHOP/test-address`

Trang này sẽ test trực tiếp:
1. Tạo address với phone
2. Lưu vào DB qua JPA
3. Đọc lại và verify

**Kết quả mong đợi**: Thấy chữ "🎉 SUCCESS! Phone được lưu và đọc đúng!"

### Bước 3: Test API (Postman/curl)

**Option A: Postman**
- Import file: `test_api_postman.json`
- Run request "1. Create Address with Phone"
- Check response có `"phone": "0987654321"` không

**Option B: curl** (Windows PowerShell)
```powershell
$body = @{
    name = "Test curl"
    phone = "0987654321"
    province = "Hà Nội"
    district = "Ba Đình"
    ward = "Điện Biên"
    addressDetail = "123 Test"
    isDefault = $false
} | ConvertTo-Json

Invoke-WebRequest `
    -Uri "http://localhost:8080/UTESHOP/api/user/addresses" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Bước 4: Test Frontend

Chỉ làm bước này **SAU KHI** Bước 1-3 đều OK!

1. Mở `http://localhost:8080/UTESHOP/user/profile`
2. Mở Console (F12)
3. Click "Thêm địa chỉ mới"
4. Nhập phone: `0912345678`
5. Xem console có log gì

## 📋 Files đã tạo

| File | Mục đích |
|------|----------|
| `quick_check.sql` | Check database nhanh nhất |
| `test_address_direct.sql` | Test SQL INSERT trực tiếp |
| `TestAddressServlet.java` | Test JPA/DAO (Java) |
| `test_api_postman.json` | Test API (Postman) |
| `DEBUG_PHONE_ALTERNATIVE.md` | Hướng dẫn debug chi tiết |

## 🎯 Decision Tree

```
                    START
                      |
        ┌─────────────┴─────────────┐
        |                           |
   Database có                  Database không
   cột phone?                   có cột phone
        |                           |
        |                      ALTER TABLE
        |                      thêm cột phone
        |                           |
        └─────────────┬─────────────┘
                      |
          ┌───────────┴───────────┐
          |                       |
     SQL INSERT             SQL INSERT
     trực tiếp OK?          trực tiếp FAIL
          |                       |
          |                  Check constraints,
          |                  triggers, permissions
          |                       |
          └───────────┬───────────┘
                      |
          ┌───────────┴───────────┐
          |                       |
    Test Servlet            Test Servlet
    (JPA) OK?               (JPA) FAIL
          |                       |
          |                  Check Entity mapping,
          |                  @Column annotation,
          |                  getter/setter
          |                       |
          └───────────┬───────────┘
                      |
          ┌───────────┴───────────┐
          |                       |
    API Test                API Test
    (Postman) OK?           (Postman) FAIL
          |                       |
          |                  Check Controller,
          |                  DTO mapping,
          |                  validation logic
          |                       |
          └───────────┬───────────┘
                      |
          ┌───────────┴───────────┐
          |                       |
    Frontend                Frontend
    submit OK?              submit FAIL
          |                       |
          |                  Check JavaScript,
          |                  form input id/name,
          |                  fetch payload
          |                       |
          └───────────┬───────────┘
                      |
                  ✅ DONE!
```

## 🔥 Top Issues & Fixes

### Issue #1: Database không có cột phone (90% trường hợp)

**Triệu chứng**: 
- Code đúng hết
- Console logs hiển thị phone
- Nhưng DB không lưu

**Check**:
```sql
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'phone';
```

**Fix**:
```sql
ALTER TABLE Addresses ADD phone NVARCHAR(20) NULL;
```

**Sau đó RESTART SERVER!**

### Issue #2: JPA Entity không có @Column (5% trường hợp)

**Check file**: `UTESHOP/src/main/java/ute/entities/Addresses.java`

```java
@Column(columnDefinition = "NVARCHAR(20)")
private String phone;  // ← Phải có dòng này
```

**Fix**: Thêm annotation, restart server

### Issue #3: Frontend không gửi phone (3% trường hợp)

**Check Console** (F12):
```javascript
// Paste vào console
document.getElementById('phone').value
// Phải trả về số điện thoại bạn nhập
```

**Fix**: Đảm bảo input có `id="phone"`

### Issue #4: Transaction rollback (2% trường hợp)

**Check server logs**: Tìm chữ "rollback" hoặc "constraint violation"

**Fix**: Xem lỗi cụ thể trong log

## 📞 Cần trợ giúp?

Nếu đã làm hết các bước trên mà vẫn lỗi, cung cấp:

1. **Kết quả `quick_check.sql`**
   ```sql
   -- Chạy script này và copy kết quả
   ```

2. **Screenshot test servlet**
   ```
   http://localhost:8080/UTESHOP/test-address
   ```

3. **Server console logs** (copy 20-30 dòng gần nhất)

4. **Browser console logs** (F12 → Console tab)

5. **Hibernate version** và **SQL Server version**
   ```sql
   SELECT @@VERSION;
   ```

## ✅ Checklist

Làm tuần tự từ trên xuống:

- [ ] Chạy `quick_check.sql` → Có cột phone
- [ ] ALTER TABLE nếu chưa có cột
- [ ] Restart server
- [ ] Test servlet: `http://localhost:8080/UTESHOP/test-address` → OK
- [ ] Test Postman → Response có phone
- [ ] Test frontend → Console log có phone
- [ ] Query database → Data có phone

Nếu tất cả ✅ → **DONE!** 🎉

---

**Tóm lại**: 
- 90% trường hợp là **database chưa có cột phone**
- Solution: `ALTER TABLE` và restart server
- Verify bằng test servlet và Postman


