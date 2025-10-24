# QUAN TRỌNG: ProductDTO vs Product Entity

## ⚠️ Vấn đề quan trọng

Trong project có **2 loại data** được dùng ở các nơi khác nhau:

### 1. **ProductDTO** (Web - Trang người dùng)
- Dùng trong: `home.jsp`, `filter.jsp`, và các trang web khác
- Field images: **`List<String>`** (đã là đường dẫn file)

```java
public class ProductDTO {
    public List<String> images;  // ← List of String paths
}
```

### 2. **Product Entity** (Admin)
- Dùng trong: `dashboard.jsp`, `searchpaginated.jsp`
- Field images: **`List<Image>`** (là objects)

```java
public class Product {
    @OneToMany
    private List<Image> images;  // ← List of Image objects
}
```

## 🎯 Cách sử dụng trong JSP

### Trong trang WEB (ProductDTO)

```jsp
<%-- p là ProductDTO --%>
<c:forEach var="p" items="${listBestSell}">
    <%-- p.images[0] ĐÃ là String --%>
    <c:set var="productImage" value="${p.images[0]}" />
    ✅ ĐÚNG
    
    <c:set var="productImage" value="${p.images[0].dirImage}" />
    ❌ SAI - Error: Property [dirImage] not found on type [String]
</c:forEach>
```

### Trong trang ADMIN (Product Entity)

```jsp
<%-- product là Product entity --%>
<c:forEach var="product" items="${productList}">
    <%-- product.images[0] là Image object --%>
    <c:set var="productImage" value="${product.images[0].dirImage}" />
    ✅ ĐÚNG
    
    <c:set var="productImage" value="${product.images[0]}" />
    ❌ SAI - Sẽ in ra object reference
</c:forEach>
```

## 📊 So sánh chi tiết

| Tiêu chí | ProductDTO (Web) | Product Entity (Admin) |
|----------|------------------|------------------------|
| **Data Type** | `List<String>` | `List<Image>` |
| **Được load từ** | `loadData.java` | Direct từ Controller |
| **Images[0]** | `"uploads/file.jpg"` | `Image{imageID=1, dirImage="uploads/file.jpg"}` |
| **Cách dùng** | `${p.images[0]}` | `${product.images[0].dirImage}` |
| **Attribute names** | `listBestSell`, `listNewProducts`, `listTopReviewProducts`, `listTopFavoriteProducts` | `productList`, `recentProducts`, `bestSellers` |

## 🔍 Làm sao biết đang dùng DTO hay Entity?

### Cách 1: Xem Controller

**Web Controllers:**
```java
// HomeController.java
loadData.loadProductBestSeller(request, response);
// → set attribute "listBestSell" với ProductDTO
```

**Admin Controllers:**
```java
// AdminHomeController.java
List<Product> recentProducts = productService.findNewProduct(5);
req.setAttribute("recentProducts", recentProducts);
// → set attribute "recentProducts" với Product entity
```

### Cách 2: Xem loadData.java

```java
public static void loadProductBestSeller(...) {
    List<Product> listProducts = service.findBestSeller(limit);
    List<ProductDTO> listDTO = service.MapToProductDTO(listProducts);  // ← Convert
    request.setAttribute("listBestSell", listDTO);  // ← DTO
}
```

### Cách 3: Xem attribute name trong JSP

```jsp
<%-- Web - Dùng DTO --%>
<c:forEach var="p" items="${listBestSell}">        ← DTO
<c:forEach var="p" items="${listNewProducts}">     ← DTO
<c:forEach var="p" items="${listTopReviewProducts}"> ← DTO
<c:forEach var="p" items="${listTopFavoriteProducts}"> ← DTO

<%-- Admin - Dùng Entity --%>
<c:forEach var="product" items="${productList}">   ← Entity
<c:forEach var="product" items="${recentProducts}"> ← Entity
<c:forEach var="product" items="${bestSellers}">   ← Entity
```

## 🛠️ Files đã sửa đúng

### ✅ Web Files (dùng DTO)
- `product-card.jsp` - Sửa từ `.dirImage` thành không có
  ```jsp
  <c:set var="productImage" value="${p.images[0]}" />
  ```

### ✅ Admin Files (dùng Entity)
- `dashboard.jsp` - Giữ nguyên `.dirImage`
  ```jsp
  <img src="...?fname=${product.images[0].dirImage}" />
  ```

- `searchpaginated.jsp` - Giữ nguyên `.dirImage`
  ```jsp
  <img src="...?fname=${product.images[0].dirImage}" />
  ```

## 💡 Tại sao lại có 2 loại?

### ProductDTO (Web)
**Mục đích:**
- Tối ưu performance - chỉ gửi data cần thiết
- Bảo mật - không expose toàn bộ entity structure
- Đơn giản hóa - images đã là String paths sẵn

**Ưu điểm:**
```java
// DTO - Đơn giản, lightweight
{
  "productName": "Áo thun",
  "images": ["uploads/file1.jpg", "uploads/file2.jpg"]
}
```

### Product Entity (Admin)
**Mục đích:**
- Quản lý đầy đủ - cần access toàn bộ relationships
- Có thể edit - cần update images, categories, etc.
- Real-time data - không cache

**Ưu điểm:**
```java
// Entity - Đầy đủ, có relationships
{
  "productName": "Áo thun",
  "images": [
    { "imageID": 1, "dirImage": "uploads/file1.jpg" },
    { "imageID": 2, "dirImage": "uploads/file2.jpg" }
  ],
  "category": { "categoryID": 1, "categoryName": "Quần áo" },
  "brand": { "brandID": 5, "brandName": "Nike" }
}
```

## 📝 Checklist khi làm việc với images

Khi code JSP mới, hỏi bản thân:

1. **Trang này dùng DTO hay Entity?**
   - Web (home, filter, etc.) → DTO
   - Admin (dashboard, manage, etc.) → Entity

2. **Attribute name là gì?**
   - `listBestSell`, `listNewProducts`, etc. → DTO
   - `productList`, `recentProducts`, etc. → Entity

3. **Variable name trong forEach?**
   - Convention: `p` → DTO
   - Convention: `product` → Entity

4. **Cách access images:**
   - DTO: `${p.images[0]}`
   - Entity: `${product.images[0].dirImage}`

## 🐛 Common Errors

### Error 1: Property [dirImage] not found on type [String]
```jsp
❌ <c:set var="img" value="${p.images[0].dirImage}" />
```
**Nguyên nhân:** Đang dùng DTO nhưng treat như Entity

**Fix:**
```jsp
✅ <c:set var="img" value="${p.images[0]}" />
```

### Error 2: Ảnh không hiển thị (hiện object reference)
```jsp
❌ <img src="...?fname=${product.images[0]}" />
```
**Nguyên nhân:** Đang dùng Entity nhưng treat như DTO

**Fix:**
```jsp
✅ <img src="...?fname=${product.images[0].dirImage}" />
```

## ✨ Kết luận

**Quy tắc vàng:**
- Trang **WEB** → DTO → `p.images[0]` (String)
- Trang **ADMIN** → Entity → `product.images[0].dirImage` (Image object)

**Luôn kiểm tra:**
1. Controller set attribute gì?
2. Attribute name là gì?
3. Dữ liệu là DTO hay Entity?
4. Dùng đúng cú pháp tương ứng!

---
**Lưu ý:** Tài liệu này CỰC KỲ quan trọng để hiểu được structure của project!

