# Hướng Dẫn Sửa Lỗi Upload Ảnh Cho Danh Mục và Sản Phẩm

## Vấn đề
Khi thêm danh mục (Category) hoặc sản phẩm (Product) trong phần admin, ảnh không được lưu vào database và thư mục uploads.

## Nguyên nhân

### 1. ProductController
- **Hoàn toàn thiếu code xử lý upload ảnh** trong phương thức `doPost()`
- Không có logic để:
  - Nhận file từ request
  - Lưu file vào thư mục uploads
  - Tạo entity Image và liên kết với Product

### 2. CategoriesController  
- Có code xử lý upload nhưng thiếu:
  - Kiểm tra và tạo thư mục uploads nếu chưa tồn tại
  - Tạo tên file unique để tránh trùng lặp

### 3. Product Entity
- Relationship `@OneToMany` với Image thiếu `CascadeType.ALL`
- Khi persist Product, các Image entities không được tự động persist

## Các file đã sửa

### 1. ProductController.java
**Đường dẫn:** `UTESHOP/src/main/java/ute/controllers/admin/product/ProductController.java`

**Thay đổi:**
- Thêm import: `File`, `Part`, `ArrayList`, `Image`
- Thêm code xử lý upload ảnh trong `doPost()`:
  ```java
  // Handle file upload
  Part filePart = req.getPart("image");
  if (filePart != null && filePart.getSize() > 0) {
      String fileName = filePart.getSubmittedFileName();
      
      // Ensure uploads directory exists
      String uploadPath = getServletContext().getRealPath("/assets/uploads");
      File uploadDir = new File(uploadPath);
      if (!uploadDir.exists()) {
          uploadDir.mkdirs();
      }
      
      // Generate unique filename to avoid conflicts
      String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
      String filePath = uploadPath + File.separator + uniqueFileName;
      
      // Save the file
      filePart.write(filePath);
      
      // Create Image entity
      Image image = Image.builder()
              .dirImage("uploads/" + uniqueFileName)
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
**Đường dẫn:** `UTESHOP/src/main/java/ute/controllers/admin/category/CategoriesController.java`

**Thay đổi:**
- Thêm import: `File`
- Cải thiện code xử lý upload ảnh:
  ```java
  // Handle file upload
  Part filePart = req.getPart("image");
  String image = null;
  if (filePart != null && filePart.getSize() > 0) {
      String fileName = filePart.getSubmittedFileName();
      
      // Ensure uploads directory exists
      String uploadPath = getServletContext().getRealPath("/assets/uploads");
      File uploadDir = new File(uploadPath);
      if (!uploadDir.exists()) {
          uploadDir.mkdirs();
      }
      
      // Generate unique filename to avoid conflicts
      String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
      String filePath = uploadPath + File.separator + uniqueFileName;
      
      // Save the file
      filePart.write(filePath);
      image = "uploads/" + uniqueFileName;
  } else if (id != null) {
      // If no new image is uploaded, keep the existing image
      image = category.getImage();
  }
  
  category.setImage(image);
  ```

### 3. Product.java Entity
**Đường dẫn:** `UTESHOP/src/main/java/ute/entities/Product.java`

**Thay đổi:**
- Thêm import: `CascadeType`
- Thêm cascade configuration cho relationship images:
  ```java
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", 
             cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> images;
  ```

## Cách thức hoạt động

### Upload ảnh cho sản phẩm
1. User chọn file ảnh trong form thêm/sửa sản phẩm
2. Controller nhận file qua `req.getPart("image")`
3. Kiểm tra thư mục `uploads` tồn tại, nếu không thì tạo mới
4. Tạo tên file unique bằng cách thêm timestamp vào đầu tên file gốc
5. Lưu file vào thư mục `webapp/assets/uploads/`
6. Tạo entity `Image` với đường dẫn relative: `uploads/filename`
7. Thêm Image vào list images của Product
8. Khi persist/merge Product, Image cũng được tự động persist (nhờ CascadeType.ALL)

### Upload ảnh cho danh mục
1. User chọn file ảnh trong form thêm/sửa danh mục
2. Controller nhận file qua `req.getPart("image")`
3. Kiểm tra thư mục `uploads` tồn tại, nếu không thì tạo mới
4. Tạo tên file unique bằng cách thêm timestamp
5. Lưu file vào thư mục `webapp/assets/uploads/`
6. Set đường dẫn relative vào field `image` của Category
7. Khi save/update Category, đường dẫn ảnh được lưu vào DB

## Cấu trúc thư mục lưu ảnh
```
UTESHOP/src/main/webapp/assets/
├── images/           (ảnh tĩnh của hệ thống)
│   ├── logo.png
│   ├── brands/
│   └── categories/
└── uploads/          (ảnh upload bởi admin)
    ├── 1729760123456_product1.jpg
    ├── 1729760234567_category1.png
    └── image.jpg
```

## Lưu ý quan trọng

### 1. Tên file unique
- Sử dụng `System.currentTimeMillis()` để tạo prefix unique
- Format: `{timestamp}_{original_filename}`
- Tránh xung đột khi upload nhiều file cùng tên

### 2. Đường dẫn lưu trong DB
- **Category**: Lưu string đường dẫn: `"uploads/filename"`
- **Product**: Lưu entity Image với field `dirImage`: `"uploads/filename"`

### 3. Hiển thị ảnh trong JSP
```jsp
<!-- Cho Category -->
<img src="${pageContext.request.contextPath}/assets/${category.image}" />

<!-- Cho Product -->
<c:forEach var="image" items="${product.images}">
    <img src="${pageContext.request.contextPath}/assets/${image.dirImage}" />
</c:forEach>
```

### 4. CascadeType.ALL
- Khi insert Product mới, các Image trong list cũng được tự động insert
- Khi update Product, các Image cũng được update
- Khi delete Product, các Image cũng được tự động delete (orphanRemoval = true)

## Kiểm tra và test

### Test thêm sản phẩm mới
1. Truy cập: `http://localhost:8080/UTESHOP/admin/products/searchpaginated`
2. Click "Thêm sản phẩm"
3. Điền thông tin sản phẩm
4. Chọn file ảnh
5. Click "Thêm"
6. Kiểm tra:
   - File ảnh xuất hiện trong `webapp/assets/uploads/`
   - Ảnh hiển thị đúng trong danh sách sản phẩm
   - Database có record trong bảng `Image`

### Test thêm danh mục mới
1. Truy cập: `http://localhost:8080/UTESHOP/admin/categories/searchpaginated`
2. Click "Thêm danh mục"
3. Điền tên danh mục và mô tả
4. Chọn file ảnh
5. Click "Lưu"
6. Kiểm tra:
   - File ảnh xuất hiện trong `webapp/assets/uploads/`
   - Ảnh hiển thị đúng trong danh sách danh mục
   - Database có đường dẫn ảnh trong bảng `Categories`

### Test sửa sản phẩm/danh mục
1. Mở form sửa một item có sẵn
2. Không chọn ảnh mới -> Ảnh cũ phải được giữ nguyên
3. Chọn ảnh mới -> Ảnh mới phải được lưu và hiển thị

## Xử lý lỗi

### Lỗi: Thư mục uploads không tồn tại
**Giải pháp:** Code đã tự động tạo thư mục:
```java
File uploadDir = new File(uploadPath);
if (!uploadDir.exists()) {
    uploadDir.mkdirs();
}
```

### Lỗi: Image không được persist cùng Product
**Giải pháp:** Đã thêm `cascade = CascadeType.ALL` vào @OneToMany

### Lỗi: File trùng tên
**Giải pháp:** Sử dụng timestamp để tạo tên file unique

### Lỗi: Không hiển thị được ảnh
**Kiểm tra:**
1. Đường dẫn trong DB có đúng format `"uploads/filename"` không?
2. File có tồn tại trong `webapp/assets/uploads/` không?
3. JSP có dùng đúng: `${pageContext.request.contextPath}/assets/${...}` không?

## Các cải tiến có thể làm thêm (tương lai)

1. **Validate file type:**
   - Chỉ cho phép upload file ảnh (jpg, png, gif, webp)
   - Kiểm tra MIME type

2. **Validate file size:**
   - Giới hạn kích thước file (vd: max 5MB)

3. **Xử lý nhiều ảnh cho Product:**
   - Cho phép upload nhiều ảnh cùng lúc
   - Chọn ảnh chính (primary image)

4. **Xóa ảnh cũ khi update:**
   - Xóa file ảnh cũ khỏi thư mục uploads khi upload ảnh mới

5. **Resize ảnh:**
   - Tự động resize ảnh về kích thước chuẩn
   - Tạo thumbnail

6. **Cloud storage:**
   - Upload ảnh lên cloud (AWS S3, Cloudinary, etc.)
   - Tiết kiệm không gian server

## Kết luận

Sau khi áp dụng các fix này, chức năng upload ảnh cho cả Product và Category đã hoạt động đầy đủ:
- ✅ Ảnh được lưu vào thư mục uploads
- ✅ Đường dẫn ảnh được lưu vào database
- ✅ Ảnh hiển thị đúng trong giao diện
- ✅ Xử lý trường hợp update (giữ ảnh cũ hoặc thay ảnh mới)
- ✅ Tránh xung đột tên file
- ✅ Tự động tạo thư mục nếu chưa tồn tại

