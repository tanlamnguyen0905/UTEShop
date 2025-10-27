# 📚 REST API Documentation - UTEShop

## 🔐 Address API Endpoints

Base URL: `/api/user/addresses`

**Authentication Required**: Yes (Session-based)

---

### 1. Get All Addresses
**Endpoint**: `GET /api/user/addresses`

**Response**:
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "addressID": 1,
      "name": "Nguyễn Văn A",
      "phone": "0123456789",
      "province": "Thành phố Hồ Chí Minh",
      "district": "Quận 1",
      "ward": "Phường Bến Nghé",
      "addressDetail": "123 Nguyễn Huệ",
      "isDefault": true,
      "userID": 1
    }
  ]
}
```

---

### 2. Get Address by ID
**Endpoint**: `GET /api/user/addresses/{id}`

**Response**:
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "addressID": 1,
    "name": "Nguyễn Văn A",
    "phone": "0123456789",
    "province": "Thành phố Hồ Chí Minh",
    "district": "Quận 1",
    "ward": "Phường Bến Nghé",
    "addressDetail": "123 Nguyễn Huệ",
    "isDefault": true,
    "userID": 1
  }
}
```

---

### 3. Create New Address
**Endpoint**: `POST /api/user/addresses`

**Request Body**:
```json
{
  "name": "Nguyễn Văn A",
  "phone": "0123456789",
  "province": "Thành phố Hồ Chí Minh",
  "district": "Quận 1",
  "ward": "Phường Bến Nghé",
  "addressDetail": "123 Nguyễn Huệ",
  "isDefault": false
}
```

**Response**:
```json
{
  "success": true,
  "message": "Address created successfully",
  "data": {
    "addressID": 1,
    "name": "Nguyễn Văn A",
    "phone": "0123456789",
    "province": "Thành phố Hồ Chí Minh",
    "district": "Quận 1",
    "ward": "Phường Bến Nghé",
    "addressDetail": "123 Nguyễn Huệ",
    "isDefault": false,
    "userID": 1
  }
}
```

---

### 4. Update Address
**Endpoint**: `PUT /api/user/addresses/{id}`

**Request Body**:
```json
{
  "name": "Nguyễn Văn B",
  "phone": "0987654321",
  "province": "Hà Nội",
  "district": "Quận Ba Đình",
  "ward": "Phường Cống Vị",
  "addressDetail": "456 Hoàng Hoa Thám",
  "isDefault": true
}
```

**Response**:
```json
{
  "success": true,
  "message": "Address updated successfully",
  "data": {
    "addressID": 1,
    "name": "Nguyễn Văn B",
    "phone": "0987654321",
    "province": "Hà Nội",
    "district": "Quận Ba Đình",
    "ward": "Phường Cống Vị",
    "addressDetail": "456 Hoàng Hoa Thám",
    "isDefault": true,
    "userID": 1
  }
}
```

---

### 5. Delete Address
**Endpoint**: `DELETE /api/user/addresses/{id}`

**Response**:
```json
{
  "success": true,
  "message": "Address deleted successfully",
  "data": null
}
```

**Note**: Không thể xóa địa chỉ mặc định!

---

## 🛡️ Security Features

### 1. **DTO (Data Transfer Object)**
- Không expose Entity trực tiếp
- Chỉ trả về dữ liệu cần thiết
- Không có thông tin nhạy cảm (như password trong UserDTO)

### 2. **Authorization**
- Kiểm tra session user
- Chỉ user sở hữu mới có thể xem/sửa/xóa địa chỉ
- HTTP Status codes chuẩn:
  - `200`: Success
  - `400`: Bad Request (missing fields, invalid data)
  - `401`: Unauthorized (not logged in)
  - `403`: Forbidden (không có quyền)
  - `404`: Not Found
  - `500`: Internal Server Error

### 3. **Input Validation**
- Validate tất cả required fields
- Kiểm tra quyền sở hữu trước khi thao tác
- Không cho xóa địa chỉ mặc định

---

## 🎯 Architecture Benefits

### **Before (Entity Direct)**
```java
// ❌ BAD: Expose entity trực tiếp
@WebServlet("/address")
public void getAddress(HttpServletRequest req, HttpServletResponse resp) {
    Addresses address = service.getAddress(id);
    // Response có thể chứa toàn bộ User entity với password!
    out.print(gson.toJson(address));
}
```

### **After (DTO Pattern)**
```java
// ✅ GOOD: Sử dụng DTO
@WebServlet("/api/user/addresses")
public void getAddress(HttpServletRequest req, HttpServletResponse resp) {
    Addresses address = service.getAddress(id);
    AddressDTO dto = AddressMapper.toDTO(address); // Chỉ lấy dữ liệu cần thiết
    sendSuccess(resp, out, dto, "Success");
}
```

---

## 📝 Example Usage (JavaScript)

### Fetch All Addresses
```javascript
fetch('/api/user/addresses')
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            console.log(data.data); // Array of AddressDTO
        }
    });
```

### Create Address
```javascript
fetch('/api/user/addresses', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        name: "Nguyễn Văn A",
        phone: "0123456789",
        province: "TP.HCM",
        district: "Quận 1",
        ward: "Phường Bến Nghé",
        addressDetail: "123 Nguyễn Huệ",
        isDefault: false
    })
})
.then(response => response.json())
.then(data => {
    if (data.success) {
        console.log('Created:', data.data);
    }
});
```

### Update Address
```javascript
fetch('/api/user/addresses/1', {
    method: 'PUT',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        name: "Nguyễn Văn B",
        phone: "0987654321",
        province: "Hà Nội",
        district: "Ba Đình",
        ward: "Cống Vị",
        addressDetail: "456 Hoàng Hoa Thám",
        isDefault: true
    })
})
.then(response => response.json())
.then(data => console.log(data));
```

### Delete Address
```javascript
fetch('/api/user/addresses/1', {
    method: 'DELETE'
})
.then(response => response.json())
.then(data => console.log(data));
```

---

## 🚀 Summary

### **What Changed:**
1. ✅ Created **DTO classes** (`AddressDTO`, `UserDTO`)
2. ✅ Created **Mapper classes** for Entity ↔ DTO conversion
3. ✅ Created **REST API Controller** with `/api/` prefix
4. ✅ Proper **error handling** with HTTP status codes
5. ✅ **Authorization checks** (session + ownership)
6. ✅ **Input validation** before operations

### **Benefits:**
- 🔐 **More Secure**: Không expose entity với thông tin nhạy cảm
- 🎯 **Better Architecture**: Tách biệt layers rõ ràng
- 📱 **API-Ready**: Dễ dàng tích hợp mobile app hoặc SPA
- 🛡️ **Validated**: Input validation và authorization đầy đủ
- 📊 **Consistent**: Response format chuẩn JSON

---

**Author**: AI Assistant  
**Date**: 2025-10-23  
**Version**: 1.0

