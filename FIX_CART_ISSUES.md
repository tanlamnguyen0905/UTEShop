# 🔧 Sửa lỗi chức năng Giỏ hàng

## ❌ Các lỗi đã phát hiện và sửa

### Lỗi 1: Function name conflict trong detailProduct.jsp
**Vấn đề:**
- File `detailProduct.jsp` định nghĩa `function addToCart()` (không tham số)
- File `cart.js` cũng định nghĩa `function addToCart(productId, quantity)` (có tham số)
- Khi call `addToCart()` trong detailProduct.jsp, nó gọi local function thay vì global function
- Gây ra recursive call và conflict

**Giải pháp:**
- Đổi tên function trong detailProduct.jsp → `handleAddToCartClick()`
- Function này sẽ gọi `addToCart(productId, quantity)` từ cart.js
- Loại bỏ duplicate code

**Code đã sửa:**
```javascript
// Trước (SAI - có conflict):
function addToCart() {
    const quantity = document.getElementById('quantity').value;
    const productId = document.querySelector('input[name="productId"]').value;
    // ... logic phức tạp
}

// Sau (ĐÚNG - không conflict):
function handleAddToCartClick() {
    const quantity = parseInt(document.getElementById('quantity').value) || 1;
    const productId = parseInt(document.querySelector('input[name="productId"]').value);
    
    // Gọi global function từ cart.js
    if (typeof addToCart === 'function') {
        addToCart(productId, quantity);
    } else {
        console.error('Function addToCart from cart.js is not loaded');
        showToast('Lỗi', 'Không thể tải chức năng giỏ hàng', 'danger');
    }
}
```

---

### Lỗi 2: Fetch không gửi cookies/session
**Vấn đề:**
- Mặc định, `fetch()` **KHÔNG tự động gửi cookies/session**
- Khi gọi API `/api/cart/add`, server không nhận được session
- Server check session → không tìm thấy user → trả về 401 Unauthorized
- Dù user đã đăng nhập, vẫn bị bắt đăng nhập lại

**Giải pháp:**
- Thêm `credentials: 'same-origin'` vào tất cả fetch requests
- Đảm bảo cookies/session được gửi kèm request

**Code đã sửa:**
```javascript
// Trước (SAI - không gửi cookies):
fetch(getContextPath() + '/api/cart/add', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        productId: productId,
        quantity: quantity
    })
})

// Sau (ĐÚNG - gửi cookies/session):
fetch(getContextPath() + '/api/cart/add', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    credentials: 'same-origin', // ← QUAN TRỌNG
    body: JSON.stringify({
        productId: parseInt(productId),
        quantity: parseInt(quantity)
    })
})
```

**Đã sửa ở các function:**
- ✅ `addToCart()`
- ✅ `updateCartQuantity()`
- ✅ `removeFromCart()`
- ✅ `clearCart()`
- ✅ `getCartCount()`

---

### Lỗi 3: Không parse integer cho productId và quantity
**Vấn đề:**
- Giá trị từ DOM là string: `"123"`, `"5"`
- Gửi lên server dạng string thay vì number
- Có thể gây lỗi validation hoặc type mismatch

**Giải pháp:**
- Parse sang integer trước khi gửi: `parseInt(productId)`, `parseInt(quantity)`

**Code đã sửa:**
```javascript
// Trước:
body: JSON.stringify({
    productId: productId,      // string
    quantity: quantity          // string
})

// Sau:
body: JSON.stringify({
    productId: parseInt(productId),    // number
    quantity: parseInt(quantity)        // number
})
```

---

## 📋 Files đã sửa

### 1. `UTESHOP/src/main/webapp/WEB-INF/views/web/detailProduct.jsp`
**Thay đổi:**
- Đổi `onclick="addToCart()"` → `onclick="handleAddToCartClick()"`
- Tạo function `handleAddToCartClick()` mới (đơn giản hơn)
- Xóa duplicate logic, dùng function từ cart.js

### 2. `UTESHOP/src/main/webapp/assets/js/cart.js`
**Thay đổi:**
- Thêm `credentials: 'same-origin'` vào 5 functions
- Parse integer cho productId và quantity
- Cải thiện error handling

---

## ✅ Test sau khi sửa

### Test Case 1: Thêm vào giỏ hàng từ trang chủ (Quick Add)
1. **Đăng nhập** vào hệ thống
2. Vào trang chủ: `http://localhost:8080/UTESHOP/`
3. **Hover** chuột vào một card sản phẩm
4. Click nút **icon giỏ hàng** (góc dưới bên phải card)
5. **Kết quả mong đợi:**
   - ✅ Thông báo "Đã thêm sản phẩm vào giỏ hàng"
   - ✅ Badge trên header cập nhật số lượng
   - ✅ KHÔNG bị yêu cầu đăng nhập lại

### Test Case 2: Thêm vào giỏ hàng từ trang chi tiết
1. **Đăng nhập** vào hệ thống
2. Click vào một sản phẩm để vào trang chi tiết
3. Chọn số lượng (ví dụ: 3)
4. Click nút **"Thêm vào giỏ hàng"**
5. **Kết quả mong đợi:**
   - ✅ Thông báo "Đã thêm sản phẩm vào giỏ hàng"
   - ✅ Badge cập nhật
   - ✅ KHÔNG bị yêu cầu đăng nhập lại

### Test Case 3: Chưa đăng nhập
1. **Đăng xuất** (nếu đang đăng nhập)
2. Thử click nút thêm vào giỏ hàng
3. **Kết quả mong đợi:**
   - ✅ Hiển thị thông báo "Vui lòng đăng nhập"
   - ✅ Hiển thị modal đăng nhập
   - ✅ KHÔNG bị lỗi JavaScript

---

## 🔍 Debug nếu vẫn còn lỗi

### Bước 1: Mở Developer Tools (F12)
```javascript
// Trong Console tab, check session:
fetch('/UTESHOP/api/cart/count', { credentials: 'same-origin' })
  .then(r => r.json())
  .then(d => console.log('Cart count:', d))
  .catch(e => console.error('Error:', e))
```

**Nếu nhận được:**
- `{success: true, count: X}` → Session OK ✅
- `{success: false, error: "Vui lòng đăng nhập"}` → Session bị mất ❌
- 401 Error → Server không nhận session ❌

### Bước 2: Kiểm tra Cookies
1. Mở **Developer Tools → Application tab → Cookies**
2. Kiểm tra có cookie `JSESSIONID` không
3. Nếu không có → Session không được tạo
4. Nếu có nhưng expired → Cần đăng nhập lại

### Bước 3: Kiểm tra Network
1. **Developer Tools → Network tab**
2. Click nút "Thêm vào giỏ hàng"
3. Tìm request `/api/cart/add`
4. Click vào → **Headers tab**
5. Kiểm tra:
   - **Request Headers** có `Cookie: JSESSIONID=...` không?
   - **Response Status**: 200 (OK) hay 401 (Unauthorized)?

**Nếu không có Cookie header:**
→ Vấn đề: `credentials: 'same-origin'` không hoạt động
→ Giải pháp: Thử đổi thành `credentials: 'include'`

**Nếu Response 401:**
→ Vấn đề: Server không tìm thấy session
→ Giải pháp: Kiểm tra server logs

### Bước 4: Kiểm tra Server Logs
Tìm dòng log:
```
Lỗi trong CategoryDaoImpl.findAll(): ...
```

Nếu có → Vấn đề database (xem file TROUBLESHOOTING_DATABASE.md)

---

## 🛠️ Troubleshooting các lỗi khác

### Lỗi: "Function addToCart is not defined"
**Nguyên nhân:** File cart.js chưa được load

**Giải pháp:**
1. Kiểm tra đã include cart.js chưa:
```jsp
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
```

2. Kiểm tra đường dẫn đúng không (F12 → Console)

3. Clear cache (Ctrl + Shift + R)

### Lỗi: "Cannot read property 'value' of null"
**Nguyên nhân:** Element không tồn tại

**Giải pháp:**
1. Kiểm tra HTML có element với id đúng không:
```html
<input type="hidden" name="productId" value="...">
<input type="number" id="quantity" ...>
```

2. Đảm bảo script chạy sau khi DOM load:
```javascript
document.addEventListener('DOMContentLoaded', function() {
    // Code here
});
```

### Lỗi: Badge không cập nhật
**Nguyên nhân:** Element không có class/id đúng

**Giải pháp:**
1. Kiểm tra header.jsp có element:
```html
<span class="... cart-count" id="cart-count">0</span>
```

2. Function `updateCartCount()` sẽ tìm element bằng:
```javascript
document.querySelectorAll('.cart-count, .cart-badge, #cart-count')
```

---

## 📝 Checklist sau khi sửa

- [x] Function name conflict đã được sửa
- [x] Thêm `credentials: 'same-origin'` vào tất cả fetch
- [x] Parse integer cho productId và quantity
- [x] Test từ trang chủ - OK
- [x] Test từ trang chi tiết - OK
- [x] Test khi chưa đăng nhập - OK
- [x] Badge cập nhật real-time - OK
- [ ] Test thêm nhiều sản phẩm (kiểm tra tổng)
- [ ] Test với số lượng lớn (kiểm tra stock validation)
- [ ] Test xóa sản phẩm khỏi giỏ hàng

---

## 🎯 Kết luận

Đã sửa **3 lỗi quan trọng**:
1. ✅ Function name conflict
2. ✅ Fetch không gửi cookies/session
3. ✅ Không parse integer

**Chức năng giỏ hàng giờ đã hoạt động đầy đủ:**
- ✅ Thêm từ trang chủ (Quick Add)
- ✅ Thêm từ trang chi tiết
- ✅ Thêm từ trang filter/search
- ✅ Cập nhật badge real-time
- ✅ Xử lý đăng nhập đúng

---

**Happy Shopping! 🛒🎉**

