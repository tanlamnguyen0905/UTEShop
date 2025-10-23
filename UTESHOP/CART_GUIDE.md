# Hướng dẫn sử dụng chức năng Giỏ hàng - UTESHOP

## Tổng quan

Chức năng giỏ hàng đã được triển khai hoàn chỉnh với các tính năng:
- Thêm sản phẩm vào giỏ hàng
- Cập nhật số lượng sản phẩm
- Xóa sản phẩm khỏi giỏ hàng
- Xóa toàn bộ giỏ hàng
- Hiển thị tổng giá và số lượng sản phẩm
- Kiểm tra tồn kho tự động

## Cấu trúc các file

### 1. Entities
- `Cart.java` - Entity giỏ hàng
- `CartDetail.java` - Entity chi tiết giỏ hàng (sản phẩm trong giỏ)
- `Product.java` - Đã thêm method `getDiscountPrice()`

### 2. DAO Layer
- `CartDao.java` - Interface cho các operations với cart
- `CartDaoImpl.java` - Implementation của CartDao
- `UserDao.java` - Đã thêm method `findById(Long)`
- `UserDaoImpl.java` - Đã implement `findById(Long)`

### 3. Service Layer
- `CartService.java` - Interface cho business logic
- `CartServiceImpl.java` - Implementation với các tính năng:
  - Tự động tạo giỏ hàng cho user mới
  - Kiểm tra tồn kho
  - Cập nhật tổng giá tự động
  - Merge sản phẩm trùng lặp

### 4. Controller Layer
- `CartController.java` - Web controller hiển thị trang giỏ hàng (URL: `/cart`)
- `CartApiController.java` - REST API endpoints:
  - `GET /api/cart` - Lấy thông tin giỏ hàng
  - `GET /api/cart/count` - Lấy số lượng sản phẩm
  - `POST /api/cart/add` - Thêm sản phẩm
  - `POST /api/cart/update` - Cập nhật số lượng
  - `POST /api/cart/remove` - Xóa sản phẩm
  - `POST /api/cart/clear` - Xóa toàn bộ giỏ hàng

### 5. View Layer
- `cart.jsp` - Giao diện giỏ hàng với Bootstrap 5
- `cart.js` - JavaScript helper functions

## Cách sử dụng

### 1. Hiển thị nút "Thêm vào giỏ hàng" trên trang sản phẩm

```jsp
<!-- Trong trang product detail hoặc product list -->
<%@ page pageEncoding="UTF-8"%>

<!-- Include cart.js -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>

<!-- Nút thêm vào giỏ hàng - Cách 1: Sử dụng data attributes -->
<button class="btn btn-primary btn-add-to-cart" 
        data-product-id="${product.productID}" 
        data-quantity="1">
    <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
</button>

<!-- Nút thêm vào giỏ hàng - Cách 2: Gọi function trực tiếp -->
<button class="btn btn-primary" 
        onclick="addToCart(${product.productID}, 1)">
    <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
</button>

<!-- Với quantity input -->
<div class="input-group">
    <input type="number" id="quantity" class="form-control" value="1" min="1" max="${product.stockQuantity}">
    <button class="btn btn-primary" 
            onclick="addToCart(${product.productID}, document.getElementById('quantity').value)">
        <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
    </button>
</div>
```

### 2. Hiển thị số lượng sản phẩm trong giỏ hàng (Header)

```jsp
<!-- Trong header.jsp -->
<a href="${pageContext.request.contextPath}/cart" class="position-relative">
    <i class="fas fa-shopping-cart fa-lg"></i>
    <span class="badge bg-danger position-absolute top-0 start-100 translate-middle cart-count" 
          id="cart-count">0</span>
</a>

<!-- Include cart.js để tự động cập nhật -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
```

### 3. Truy cập trang giỏ hàng

Người dùng có thể truy cập trang giỏ hàng qua URL:
```
http://localhost:8080/UTESHOP/cart
```

### 4. API Usage (cho AJAX requests)

#### Thêm sản phẩm vào giỏ hàng
```javascript
fetch('/UTESHOP/api/cart/add', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        productId: 123,
        quantity: 2
    })
})
.then(response => response.json())
.then(data => {
    if (data.success) {
        console.log('Đã thêm vào giỏ hàng');
        console.log('Số lượng sản phẩm:', data.itemCount);
    } else {
        console.error('Lỗi:', data.error);
    }
});
```

#### Cập nhật số lượng
```javascript
fetch('/UTESHOP/api/cart/update', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        cartDetailId: 456,
        quantity: 3
    })
})
.then(response => response.json())
.then(data => {
    if (data.success) {
        console.log('Đã cập nhật số lượng');
    }
});
```

#### Xóa sản phẩm
```javascript
fetch('/UTESHOP/api/cart/remove', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        cartDetailId: 456
    })
})
.then(response => response.json())
.then(data => {
    if (data.success) {
        console.log('Đã xóa sản phẩm');
    }
});
```

## Security & Authentication

- Tất cả API endpoints yêu cầu user phải đăng nhập
- Thông tin user được lấy từ session attribute `"account"`
- Nếu chưa đăng nhập, API sẽ trả về lỗi 401 Unauthorized

## Database Schema

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

## Features & Business Logic

1. **Tự động tạo Cart**: Khi user thêm sản phẩm lần đầu, hệ thống tự động tạo Cart mới

2. **Merge sản phẩm trùng**: Nếu thêm sản phẩm đã có trong giỏ, số lượng sẽ được cộng dồn

3. **Kiểm tra tồn kho**: Tự động kiểm tra `stockQuantity` trước khi thêm/cập nhật

4. **Tính tổng tự động**: `totalPrice` của Cart được tự động cập nhật khi thay đổi

5. **Giá ưu đãi**: Sử dụng `discountPrice` nếu có, ngược lại dùng `unitPrice`

## Testing

### Test thêm sản phẩm vào giỏ hàng:
1. Đăng nhập vào hệ thống
2. Truy cập trang sản phẩm
3. Click "Thêm vào giỏ hàng"
4. Kiểm tra số lượng hiển thị trên icon giỏ hàng
5. Truy cập `/cart` để xem chi tiết

### Test cập nhật số lượng:
1. Vào trang giỏ hàng
2. Thay đổi số lượng bằng nút +/-
3. Kiểm tra tổng giá được cập nhật

### Test xóa sản phẩm:
1. Vào trang giỏ hàng
2. Click icon thùng rác
3. Xác nhận xóa
4. Kiểm tra sản phẩm đã bị xóa

## Troubleshooting

### Lỗi: "Vui lòng đăng nhập"
- Đảm bảo user đã đăng nhập
- Kiểm tra session attribute "account" tồn tại

### Lỗi: "Not enough stock"
- Kiểm tra `stockQuantity` của sản phẩm
- Cập nhật số lượng nhỏ hơn hoặc bằng tồn kho

### Lỗi: "Product not found"
- Kiểm tra `productID` có tồn tại trong database
- Kiểm tra product chưa bị xóa

## Mở rộng

### 1. Thêm Coupon/Discount
- Tạo bảng `Coupon` và `UserCoupon`
- Thêm logic áp dụng coupon trong `CartServiceImpl`
- Cập nhật UI trong `cart.jsp`

### 2. Lưu giỏ hàng cho guest user
- Sử dụng Session Storage hoặc Local Storage
- Sync với database khi user đăng nhập

### 3. Wishlist
- Tạo entity `Favorite` (đã có)
- Implement tương tự như Cart

### 4. Order from Cart
- Tạo `OrderService` để chuyển Cart thành Order
- Implement checkout flow

## Liên hệ & Hỗ trợ

Nếu có câu hỏi hoặc gặp vấn đề, vui lòng tạo issue hoặc liên hệ team phát triển.

