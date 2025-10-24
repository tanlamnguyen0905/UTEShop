# 🛒 Hướng dẫn sử dụng chức năng Giỏ hàng - UTESHOP

## 📋 Tổng quan

Chức năng giỏ hàng đã được triển khai hoàn chỉnh với đầy đủ các tính năng cần thiết cho một website thương mại điện tử hiện đại.

### ✨ Các tính năng chính

- ✅ **Thêm sản phẩm vào giỏ hàng** - Từ trang chi tiết hoặc nút nhanh trên card sản phẩm
- ✅ **Cập nhật số lượng** - Tăng/giảm số lượng sản phẩm trong giỏ
- ✅ **Xóa sản phẩm** - Xóa từng sản phẩm hoặc xóa toàn bộ giỏ hàng
- ✅ **Hiển thị real-time** - Badge số lượng sản phẩm tự động cập nhật
- ✅ **Kiểm tra tồn kho** - Tự động kiểm tra số lượng có sẵn
- ✅ **Yêu cầu đăng nhập** - Bảo mật và quản lý giỏ hàng theo user
- ✅ **Giao diện đẹp** - UI/UX hiện đại với Bootstrap 5

---

## 🎯 Cách sử dụng cho người dùng cuối

### 1. Thêm sản phẩm vào giỏ hàng

#### **Cách 1: Từ trang chủ hoặc danh sách sản phẩm**
1. Hover chuột vào card sản phẩm
2. Click vào nút giỏ hàng (icon giỏ hàng) xuất hiện ở góc phải dưới
3. Thông báo thành công sẽ hiện ra
4. Badge số lượng trên icon giỏ hàng (header) sẽ tự động cập nhật

![Quick Add Demo](assets/quick-add-demo.gif)

#### **Cách 2: Từ trang chi tiết sản phẩm**
1. Truy cập vào trang chi tiết sản phẩm
2. Chọn số lượng muốn mua
3. Click nút **"Thêm vào giỏ hàng"**
4. Thông báo thành công và badge cập nhật

![Detail Page Add](assets/detail-add-demo.gif)

### 2. Xem giỏ hàng

- Click vào icon giỏ hàng ở header
- Hoặc truy cập: `http://localhost:8080/UTESHOP/cart`

### 3. Quản lý giỏ hàng

Trong trang giỏ hàng, bạn có thể:

- **Tăng/giảm số lượng**: Sử dụng nút +/- bên cạnh mỗi sản phẩm
- **Xóa sản phẩm**: Click icon thùng rác
- **Xóa toàn bộ**: Click nút "Xóa tất cả" (nếu có)
- **Xem tổng tiền**: Hiển thị tự động ở panel bên phải

### 4. Lưu ý quan trọng ⚠️

- ⚠️ **Phải đăng nhập** trước khi thêm sản phẩm vào giỏ hàng
- ⚠️ Số lượng thêm **không được vượt quá** số lượng tồn kho
- ⚠️ Giỏ hàng được **lưu theo user**, không bị mất khi đăng xuất
- ⚠️ Thông báo lỗi sẽ hiện nếu sản phẩm hết hàng hoặc vượt quá tồn kho

---

## 🛠️ Cấu trúc kỹ thuật

### 📁 Cấu trúc thư mục

```
UTESHOP/
├── src/main/java/ute/
│   ├── entities/
│   │   ├── Cart.java              # Entity giỏ hàng
│   │   ├── CartDetail.java        # Entity chi tiết giỏ hàng
│   │   └── Product.java           # Entity sản phẩm
│   │
│   ├── dao/
│   │   ├── inter/CartDao.java     # Interface DAO
│   │   └── impl/CartDaoImpl.java  # Implementation DAO
│   │
│   ├── service/
│   │   ├── inter/CartService.java     # Interface Service
│   │   └── impl/CartServiceImpl.java  # Business logic
│   │
│   └── controllers/
│       ├── api/CartApiController.java  # REST API endpoints
│       └── web/CartController.java     # Web controller
│
└── src/main/webapp/
    ├── WEB-INF/views/web/
    │   ├── cart.jsp              # Trang giỏ hàng
    │   ├── detailProduct.jsp     # Trang chi tiết sản phẩm
    │   ├── home.jsp              # Trang chủ
    │   ├── filter.jsp            # Trang lọc sản phẩm
    │   └── product-card.jsp      # Component card sản phẩm
    │
    └── assets/js/
        └── cart.js               # JavaScript functions
```

### 🔗 API Endpoints

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/cart` | Lấy thông tin giỏ hàng (tổng giá, số lượng) |
| GET | `/api/cart/count` | Lấy số lượng sản phẩm trong giỏ |
| POST | `/api/cart/add` | Thêm sản phẩm vào giỏ hàng |
| POST | `/api/cart/update` | Cập nhật số lượng sản phẩm |
| POST | `/api/cart/remove` | Xóa sản phẩm khỏi giỏ hàng |
| POST | `/api/cart/clear` | Xóa toàn bộ giỏ hàng |

### 📝 API Request/Response Format

#### **POST /api/cart/add**
```json
// Request
{
  "productId": 123,
  "quantity": 2
}

// Response - Success
{
  "success": true,
  "message": "Đã thêm sản phẩm vào giỏ hàng",
  "itemCount": 5
}

// Response - Error
{
  "success": false,
  "error": "Not enough stock. Available: 10"
}
```

#### **POST /api/cart/update**
```json
// Request
{
  "cartDetailId": 456,
  "quantity": 3
}

// Response
{
  "success": true,
  "message": "Đã cập nhật số lượng",
  "itemCount": 6
}
```

#### **POST /api/cart/remove**
```json
// Request
{
  "cartDetailId": 456
}

// Response
{
  "success": true,
  "message": "Đã xóa sản phẩm khỏi giỏ hàng",
  "itemCount": 4
}
```

#### **GET /api/cart/count**
```json
// Response
{
  "success": true,
  "count": 5
}
```

---

## 🔧 Hướng dẫn cho Developer

### 1. Sử dụng JavaScript Functions

File `cart.js` cung cấp các functions toàn cục:

```javascript
// Thêm sản phẩm vào giỏ hàng
addToCart(productId, quantity);

// Cập nhật số lượng
updateCartQuantity(cartDetailId, quantity);

// Xóa sản phẩm
removeFromCart(cartDetailId);

// Xóa tất cả
clearCart();

// Lấy số lượng sản phẩm
getCartCount();

// Cập nhật badge số lượng
updateCartCount(count);

// Hiển thị thông báo
showNotification(message, type);  // type: success, error, warning, info
```

### 2. Thêm nút "Thêm vào giỏ hàng" trong JSP

#### **Cách 1: Sử dụng component card sản phẩm**
```jsp
<c:forEach var="p" items="${products}">
    <div class="col-md-3">
        <jsp:include page="product-card.jsp" />
    </div>
</c:forEach>

<!-- Đừng quên include cart.js -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
```

#### **Cách 2: Custom button**
```jsp
<!-- Nút với onclick -->
<button class="btn btn-primary" 
        onclick="addToCart(${product.productID}, 1)">
    <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
</button>

<!-- Nút với data attributes -->
<button class="btn btn-primary btn-add-to-cart" 
        data-product-id="${product.productID}" 
        data-quantity="1">
    <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
</button>

<!-- Include cart.js để auto-bind events -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
```

### 3. Tùy chỉnh component product-card.jsp

Component `product-card.jsp` đã được tạo sẵn với:
- Ảnh sản phẩm
- Tên và giá sản phẩm
- Nút "Thêm vào giỏ hàng" xuất hiện khi hover
- Link đến trang chi tiết

Bạn có thể tùy chỉnh style trong file hoặc override CSS:

```css
.product-card:hover .btn-quick-add {
    opacity: 1 !important;
}

.product-card:hover {
    transform: translateY(-5px);
}
```

### 4. Xử lý lỗi và validation

Service layer tự động xử lý:
- ✅ Kiểm tra user đã đăng nhập
- ✅ Kiểm tra product tồn tại
- ✅ Kiểm tra tồn kho
- ✅ Merge sản phẩm trùng lặp
- ✅ Tính tổng giá tự động

```java
// CartServiceImpl.java
@Override
public void addToCart(Long userId, Long productId, int quantity) {
    // Validation
    if (quantity <= 0) {
        throw new IllegalArgumentException("Quantity must be greater than 0");
    }
    
    // Kiểm tra tồn kho
    if (product.getStockQuantity() < quantity) {
        throw new RuntimeException("Not enough stock. Available: " + product.getStockQuantity());
    }
    
    // Merge nếu sản phẩm đã tồn tại
    // ...
}
```

---

## 📊 Database Schema

### Table: Cart
```sql
CREATE TABLE Cart (
    cartID BIGINT PRIMARY KEY IDENTITY(1,1),
    userID BIGINT FOREIGN KEY REFERENCES Users(userID),
    totalPrice FLOAT
);
```

### Table: CartDetail
```sql
CREATE TABLE CartDetail (
    cartDetailID BIGINT PRIMARY KEY IDENTITY(1,1),
    cartID BIGINT FOREIGN KEY REFERENCES Cart(cartID),
    productID BIGINT FOREIGN KEY REFERENCES Product(productID),
    quantity INT,
    unitPrice FLOAT
);
```

### Relationships
- **Cart** ↔ **Users**: One-to-One
- **Cart** ↔ **CartDetail**: One-to-Many
- **CartDetail** ↔ **Product**: Many-to-One

---

## 🧪 Testing

### Test Cases

#### 1. Test thêm sản phẩm vào giỏ hàng
✅ User chưa đăng nhập → Hiển thị modal đăng nhập  
✅ Thêm sản phẩm mới → Tạo CartDetail mới  
✅ Thêm sản phẩm đã có → Cộng dồn số lượng  
✅ Vượt quá tồn kho → Hiển thị thông báo lỗi  
✅ Badge cập nhật real-time  

#### 2. Test cập nhật số lượng
✅ Tăng số lượng → Kiểm tra tồn kho  
✅ Giảm số lượng → Cập nhật thành công  
✅ Set về 0 → Xóa sản phẩm  
✅ Tổng giá cập nhật đúng  

#### 3. Test xóa sản phẩm
✅ Xóa từng sản phẩm → Badge giảm  
✅ Xóa toàn bộ → Giỏ hàng trống  
✅ Confirm trước khi xóa  

### Manual Testing

```bash
# 1. Start server
cd UTESHOP
mvn clean tomcat7:run

# 2. Truy cập
http://localhost:8080/UTESHOP/

# 3. Test flow
- Đăng nhập
- Thêm sản phẩm từ trang chủ (hover và click icon giỏ)
- Kiểm tra badge cập nhật
- Vào trang giỏ hàng (/cart)
- Tăng/giảm số lượng
- Xóa sản phẩm
```

---

## 🚀 Các tính năng đã triển khai

### ✅ Hoàn thành
- [x] Entity classes (Cart, CartDetail)
- [x] DAO layer (CartDao, CartDaoImpl)
- [x] Service layer (CartService, CartServiceImpl)
- [x] API Controller (REST endpoints)
- [x] Web Controller (trang giỏ hàng)
- [x] JavaScript functions (cart.js)
- [x] UI/UX trang giỏ hàng (cart.jsp)
- [x] Component card sản phẩm với quick add (product-card.jsp)
- [x] Tích hợp vào trang chủ (home.jsp)
- [x] Tích hợp vào trang lọc (filter.jsp)
- [x] Tích hợp vào trang chi tiết (detailProduct.jsp)
- [x] Header badge hiển thị số lượng
- [x] Auto-update cart count
- [x] Notification system
- [x] Authentication check
- [x] Stock validation

### 🔜 Có thể mở rộng
- [ ] Coupon/Discount system
- [ ] Guest cart (lưu vào session/cookie)
- [ ] Order from cart (checkout flow)
- [ ] Save for later
- [ ] Recently viewed products
- [ ] Cart sharing
- [ ] Email cart to customer

---

## 🎨 Screenshots

### Trang chủ với nút Quick Add
![Home Page](screenshots/home.png)

### Trang giỏ hàng
![Cart Page](screenshots/cart.png)

### Thông báo thành công
![Notification](screenshots/notification.png)

### Badge cập nhật
![Badge Update](screenshots/badge.png)

---

## ❓ Troubleshooting

### Lỗi: "Vui lòng đăng nhập"
**Nguyên nhân**: User chưa đăng nhập  
**Giải pháp**: Đăng nhập trước khi thêm vào giỏ hàng

### Lỗi: "Not enough stock"
**Nguyên nhân**: Số lượng yêu cầu > tồn kho  
**Giải pháp**: Giảm số lượng hoặc chọn sản phẩm khác

### Badge không cập nhật
**Nguyên nhân**: cart.js chưa được load  
**Giải pháy**: Kiểm tra đã include cart.js trong trang

### Nút Quick Add không hiện
**Nguyên nhân**: Chưa hover vào card hoặc CSS lỗi  
**Giải pháp**: Hover vào card hoặc check CSS của `.btn-quick-add`

### API trả về 401 Unauthorized
**Nguyên nhân**: Session hết hạn hoặc chưa đăng nhập  
**Giải pháp**: Đăng nhập lại

---

## 📞 Hỗ trợ

Nếu gặp vấn đề hoặc có câu hỏi, vui lòng:
1. Kiểm tra console log (F12 → Console)
2. Kiểm tra Network tab (F12 → Network)
3. Đọc kỹ thông báo lỗi
4. Tham khảo file `CART_GUIDE.md` gốc
5. Liên hệ team phát triển

---

## 📝 Changelog

### Version 2.0 (Current)
- ✨ Thêm component product-card.jsp với nút Quick Add
- ✨ Tích hợp Quick Add vào trang chủ
- ✨ Tích hợp Quick Add vào trang filter
- ✨ Cải thiện UI/UX với hover effects
- ✨ Tối ưu hóa cart.js
- 🐛 Sửa lỗi endpoint trong detailProduct.jsp
- 📚 Tạo tài liệu hướng dẫn chi tiết

### Version 1.0
- 🎉 Release chức năng giỏ hàng cơ bản
- API endpoints hoàn chỉnh
- Trang giỏ hàng với Bootstrap 5
- JavaScript utilities

---

## 🎓 Best Practices

### Khi sử dụng
1. **Luôn kiểm tra đăng nhập** trước khi cho phép thao tác
2. **Validate input** ở cả client và server
3. **Hiển thị feedback** rõ ràng cho user
4. **Handle errors** gracefully
5. **Optimize performance** với caching nếu cần

### Khi phát triển thêm
1. **Follow naming conventions** đã có
2. **Reuse components** (như product-card.jsp)
3. **Document code** rõ ràng
4. **Test thoroughly** trước khi merge
5. **Keep it simple** - đừng over-engineer

---

**Chúc bạn sử dụng thành công! 🎉**

*Tài liệu này được cập nhật lần cuối: ${new java.util.Date()}*

