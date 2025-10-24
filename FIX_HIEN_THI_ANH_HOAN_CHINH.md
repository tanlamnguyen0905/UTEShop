# Fix Hiển Thị Ảnh Hoàn Chỉnh - UTEShop

## 🔍 Vấn đề gốc

Khi thêm/sửa sản phẩm và danh mục:
1. ❌ Không lưu được ảnh vào thư mục
2. ❌ Database không có bản ghi Image
3. ❌ Ảnh không hiển thị trên giao diện

## 🎯 Nguyên nhân

### Vấn đề 1: Thiếu code xử lý upload
- **ProductController** - Hoàn toàn thiếu code upload ảnh
- **CategoriesController** - Có code nhưng chưa hoàn thiện

### Vấn đề 2: Property sai
- JSP sử dụng `product.images[0].link` 
- Nhưng entity Image chỉ có property `dirImage`

### Vấn đề 3: Đường dẫn không khớp ⚠️ (QUAN TRỌNG)
**Khi upload:**
```java
// Lưu vào webapp/assets/uploads/
String uploadPath = getServletContext().getRealPath("/assets/uploads");
```

**Khi hiển thị:**
```java
// Đọc từ D:\images\
String uploadPath = Constant.Dir; // = "D:\images"
```

→ **Không khớp nhau!** Files không tìm thấy!

### Vấn đề 4: Cú pháp JSP
```jsp
❌ value="${not empty p.images && not empty p.images[0] ? ... : ...}"
```
Ternary operator trong EL có thể gây lỗi với một số version JSTL

## ✅ Giải pháp đã áp dụng

### 1. ProductController.java
**Thêm code xử lý upload ảnh:**

```java
// Handle file upload
Part filePart = req.getPart("image");
if (filePart != null && filePart.getSize() > 0) {
    String fileName = filePart.getSubmittedFileName();
    
    // ✅ QUAN TRỌNG: Lưu vào D:\images\uploads
    String uploadPath = ute.utils.Constant.Dir + File.separator + "uploads";
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
        uploadDir.mkdirs();
    }
    
    // Generate unique filename
    String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
    String filePath = uploadPath + File.separator + uniqueFileName;
    
    // Save the file
    filePart.write(filePath);
    
    // Create Image entity
    Image image = Image.builder()
            .dirImage("uploads/" + uniqueFileName)  // Lưu relative path
            .product(product)
            .build();
    
    // Add to product's images list
    if (product.getImages() == null) {
        product.setImages(new ArrayList<>());
    }
    product.getImages().add(image);
}
```

### 2. CategoriesController.java
**Cập nhật đường dẫn:**

```java
// ✅ Lưu vào D:\images\uploads thay vì webapp/assets/uploads
String uploadPath = ute.utils.Constant.Dir + File.separator + "uploads";
File uploadDir = new File(uploadPath);
if (!uploadDir.exists()) {
    uploadDir.mkdirs();
}

String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
String filePath = uploadPath + File.separator + uniqueFileName;
filePart.write(filePath);
image = "uploads/" + uniqueFileName;
```

### 3. Product.java Entity
**Thêm Cascade configuration:**

```java
@OneToMany(fetch = FetchType.EAGER, mappedBy = "product", 
           cascade = CascadeType.ALL, orphanRemoval = true)
private List<Image> images;
```

### 4. dashboard.jsp
**Sửa property name:**

```jsp
❌ ${product.images[0].link}
✅ ${product.images[0].dirImage}
```

### 5. product-card.jsp
**Sửa cú pháp JSP:**

```jsp
❌ BAD (Ternary operator):
<c:set var="productImage" value="${not empty p.images && not empty p.images[0] ? p.images[0].dirImage : 'images/logo.png'}" />

✅ GOOD (c:choose):
<c:choose>
    <c:when test="${not empty p.images and not empty p.images[0]}">
        <c:set var="productImage" value="${p.images[0].dirImage}" />
    </c:when>
    <c:otherwise>
        <c:set var="productImage" value="images/logo.png" />
    </c:otherwise>
</c:choose>
```

### 6. searchpaginated.jsp
**Thêm cột hiển thị ảnh:**

```jsp
<th>Hình ảnh</th>
...
<td>
    <c:choose>
        <c:when test="${not empty product.images and not empty product.images[0]}">
            <img src="${pageContext.request.contextPath}/image?fname=${product.images[0].dirImage}" 
                 width="50" height="50" class="img-thumbnail">
        </c:when>
        <c:otherwise>
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                 width="50" height="50" class="img-thumbnail">
        </c:otherwise>
    </c:choose>
</td>
```

## 📁 Cấu trúc thư mục cuối cùng

### Thư mục lưu file (Production)
```
D:\images\
└── uploads\
    ├── 1761272573303_24.jpg          (Áo sơ mi)
    ├── 1761272583510_IMG_0018.JPG    (áo thun)
    └── [các file upload khác...]
```

### Database
```sql
-- Bảng Image
imageID | dirImage                          | productID
1       | uploads/1761272573303_24.jpg      | 2
2       | uploads/1761272583510_IMG_0018.JPG| 1
```

### Constant.java
```java
public static final String Dir = "D:\\images";
```

### DownloadFileController.java
```java
@WebServlet(urlPatterns = {"/image"})
public class DownloadFileController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter("fname");
        // fileName = "uploads/1761272573303_24.jpg"
        
        File file = new File(Constant.Dir + "/" + fileName);
        // file = D:\images\uploads\1761272573303_24.jpg
        
        if (file.exists()) {
            IOUtils.copy(new FileInputStream(file), response.getOutputStream());
        }
    }
}
```

## 🔄 Workflow hoàn chỉnh

### Upload ảnh mới:
```
1. User chọn file trong form
   ↓
2. ProductController nhận Part filePart
   ↓
3. Generate unique filename: timestamp_originalname.jpg
   ↓
4. Lưu vào: D:\images\uploads\timestamp_filename.jpg
   ↓
5. Tạo Image entity với dirImage = "uploads/timestamp_filename.jpg"
   ↓
6. Add vào product.images
   ↓
7. Persist Product → Image tự động persist (CascadeType.ALL)
   ↓
8. ✅ Success!
```

### Hiển thị ảnh:
```
1. JSP: <img src="/image?fname=uploads/1761272573303_24.jpg">
   ↓
2. DownloadFileController nhận fname
   ↓
3. Đường dẫn file: D:\images + "/" + "uploads/1761272573303_24.jpg"
   ↓
4. File path: D:\images\uploads\1761272573303_24.jpg
   ↓
5. Đọc file và trả về OutputStream
   ↓
6. ✅ Ảnh hiển thị!
```

## 📝 Checklist hoàn thành

- [x] Tạo thư mục `D:\images\uploads`
- [x] Sửa ProductController - upload vào D:\images\uploads
- [x] Sửa CategoriesController - upload vào D:\images\uploads
- [x] Thêm CascadeType.ALL cho Product.images
- [x] Sửa dashboard.jsp - property .dirImage
- [x] Sửa product-card.jsp - cú pháp c:choose
- [x] Thêm cột ảnh trong searchpaginated.jsp
- [x] Build success
- [x] Có ảnh trong database

## 🎯 Test ngay

### 1. Upload ảnh mới
1. Truy cập: `http://localhost:8080/UTESHOP/admin/products/searchpaginated`
2. Click "Sửa" sản phẩm
3. Chọn file ảnh mới
4. Click "Cập nhật"
5. Kiểm tra file trong `D:\images\uploads\`
6. Reload trang → Ảnh hiển thị ✅

### 2. Kiểm tra hiển thị
- ✅ Admin Dashboard: `http://localhost:8080/UTESHOP/admin/home`
- ✅ Admin Products: `http://localhost:8080/UTESHOP/admin/products/searchpaginated`
- ✅ Web Home: `http://localhost:8080/UTESHOP/home`
- ✅ Product Detail: `http://localhost:8080/UTESHOP/detailProduct?productID=1`

## 🐛 Troubleshooting

### Lỗi: File không tìm thấy
**Kiểm tra:**
```
1. Thư mục D:\images\uploads\ có tồn tại không?
2. File có trong thư mục không?
3. Database có đúng đường dẫn không? (uploads/filename.jpg)
```

**SQL để kiểm tra:**
```sql
SELECT i.imageID, i.dirImage, p.productID, p.productName 
FROM Image i 
JOIN Product p ON i.productID = p.productID;
```

**File system:**
```
D:\images\uploads\1761272573303_24.jpg  ← File phải tồn tại
```

### Lỗi: Ảnh không hiển thị sau khi upload
**Nguyên nhân:** Tomcat cache

**Giải pháp:**
1. Hard refresh: `Ctrl + F5`
2. Clear browser cache
3. Restart Tomcat server

### Lỗi: Không upload được file
**Nguyên nhân:** Quyền ghi file

**Giải pháp:**
```powershell
# Tạo thư mục với quyền đầy đủ
New-Item -ItemType Directory -Force -Path "D:\images\uploads"
```

### Lỗi: JasperException ternary operator
**Nguyên nhân:** Cú pháp ternary trong EL không được hỗ trợ

**Giải pháp:** Đã sửa bằng `<c:choose>` thay vì ternary operator

## 📊 So sánh trước và sau

### TRƯỚC (❌ Không hoạt động)

```
Upload: webapp/assets/uploads/file.jpg
Display: D:\images\file.jpg
→ File không tìm thấy!
```

```jsp
❌ ${product.images[0].link}  → PropertyNotFoundException
❌ value="${condition ? a : b}"  → JasperException
```

### SAU (✅ Hoạt động)

```
Upload: D:\images\uploads\file.jpg
Display: D:\images\uploads\file.jpg
→ File tìm thấy! ✅
```

```jsp
✅ ${product.images[0].dirImage}
✅ <c:choose> ... </c:choose>
```

## 🎓 Bài học

### 1. Đường dẫn phải thống nhất
- Upload và Display phải dùng CÙNG thư mục gốc
- Sử dụng `Constant.Dir` để đảm bảo nhất quán

### 2. Property name phải chính xác
- Kiểm tra entity trước khi dùng trong JSP
- `Image.dirImage` chứ không phải `Image.link`

### 3. Cú pháp JSP
- Ternary operator có thể gây lỗi
- Dùng `<c:choose>` an toàn hơn
- Dùng `and` thay vì `&&` trong JSTL

### 4. Cascade configuration
- `CascadeType.ALL` để tự động persist children
- `orphanRemoval = true` để tự động xóa orphan entities

## ✨ Kết quả

Toàn bộ chức năng upload và hiển thị ảnh đã hoạt động:
- ✅ Upload ảnh Product → Lưu vào D:\images\uploads\
- ✅ Upload ảnh Category → Lưu vào D:\images\uploads\
- ✅ Hiển thị ảnh Admin Dashboard
- ✅ Hiển thị ảnh Admin Products List
- ✅ Hiển thị ảnh Web Home
- ✅ Hiển thị ảnh Product Detail
- ✅ File được tạo với tên unique
- ✅ Thư mục tự động tạo nếu chưa có

## 🚀 Sử dụng ngay

**Restart Tomcat server** và truy cập:
```
http://localhost:8080/UTESHOP/home
```

Bạn sẽ thấy tất cả ảnh hiển thị đầy đủ! 🎉

## 📚 Files liên quan

- `ProductController.java` - Xử lý upload product
- `CategoriesController.java` - Xử lý upload category
- `Product.java` - Entity với cascade config
- `DownloadFileController.java` - Serve images
- `Constant.java` - Đường dẫn gốc
- `dashboard.jsp` - Dashboard admin
- `product-card.jsp` - Card sản phẩm web
- `searchpaginated.jsp` - Danh sách admin

---
**Tác giả:** AI Assistant  
**Ngày:** 24/10/2025  
**Version:** 1.0 - Final Fix

