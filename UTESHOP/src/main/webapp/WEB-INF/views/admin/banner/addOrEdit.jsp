<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${empty banner.bannerID ? 'Thêm Banner' : 'Sửa Banner'} - UTESHOP Admin</title>
   <style>
        body { background-color: #f8f9fa; }
        .admin-content { padding-top: 80px; max-width: 800px; margin: auto; }
        .preview-img { width: 100px; height: 100px; object-fit: cover; border-radius: 6px; border: 1px solid #ccc; }
        .form-label i { color: #0d6efd; margin-right: 6px; }
    </style>
</head>

<body>
<div class="container-fluid admin-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold text-primary mb-0">
            <i class="fa-solid fa-rectangle-ad me-2"></i>
            ${empty banner.bannerID ? 'Thêm banner mới' : 'Chỉnh sửa banner'}
        </h3>
        <a href="${pageContext.request.contextPath}/api/admin/banner"
           class="btn btn-outline-secondary btn-sm">
            <i class="fa fa-arrow-left me-1"></i>Quay lại danh sách
        </a>
    </div>

    <!-- Hiển thị lỗi -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="fa-solid fa-triangle-exclamation me-1"></i>${error}
        </div>
    </c:if>

    <div class="card shadow-sm border-0">
        <div class="card-header bg-primary text-white fw-semibold">
            <i class="fa-solid fa-pen-to-square me-1"></i>
            ${empty banner.bannerID ? 'Thêm mới Banner' : 'Cập nhật Banner'}
        </div>

        <div class="card-body">
            <form method="POST"
                  action="${pageContext.request.contextPath}/api/admin/banner/saveOrUpdate"
                  enctype="multipart/form-data">

                <!-- Nếu có ID thì truyền ẩn -->
                <c:if test="${not empty banner.bannerID}">
                    <input type="hidden" name="id" value="${banner.bannerID}">
                </c:if>

                <!-- Tên banner -->
                <div class="mb-3">
                    <label class="form-label"><i class="fa-solid fa-tag"></i>Tên Banner:</label>
                    <input type="text" name="bannerName" class="form-control"
                           value="${banner.bannerName}" required
                           placeholder="Nhập tên banner...">
                </div>

                <!-- Mô tả -->
                <div class="mb-3">
                    <label class="form-label"><i class="fa-solid fa-align-left"></i>Mô tả:</label>
                    <textarea name="description" class="form-control" rows="3"
                              placeholder="Nhập mô tả ngắn...">${banner.description}</textarea>
                </div>

                <!-- Hình ảnh -->
                <div class="mb-3">
                    <label class="form-label"><i class="fa-solid fa-image"></i>Hình ảnh:</label>
                    <input type="file" name="image" id="bannerImage" class="form-control" accept="image/*"
                           onchange="previewImage(event)">
                    <small class="text-muted d-block mt-1">
                        Chấp nhận: JPG, PNG, GIF. Dung lượng tối đa 10MB.
                    </small>

                    <!-- Ảnh hiện tại -->
                    <c:if test="${not empty banner.bannerImage}">
                        <div class="mt-3">
                            <div class="fw-semibold mb-1">Ảnh hiện tại:</div>
                            <img src="${pageContext.request.contextPath}/assets/uploads/banner/${banner.bannerImage}"
                                 alt="Ảnh hiện tại" class="preview-img"
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/logo.png'">
                        </div>
                    </c:if>

                    <!-- Ảnh xem trước khi chọn -->
                    <div class="mt-3" id="previewBox" style="display:none;">
                        <div class="fw-semibold mb-1">Ảnh xem trước:</div>
                        <img id="previewImg" class="preview-img" src="#" alt="Xem trước ảnh mới" style="height: 100px; object-fit: cover;">
                    </div>
                </div>

                <!-- Nút hành động -->
                <div class="mt-4">
                    <button type="submit" class="btn btn-primary px-4">
                        <i class="fa-solid fa-floppy-disk me-1"></i>Lưu
                    </button>
                    <a href="${pageContext.request.contextPath}/api/admin/banner"
                       class="btn btn-secondary px-3">
                        <i class="fa-solid fa-xmark me-1"></i>Hủy
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap & Preview Script -->
<script>
    function previewImage(event) {
        const file = event.target.files[0];
        if (!file) return;
        const previewBox = document.getElementById('previewBox');
        const previewImg = document.getElementById('previewImg');
        previewImg.src = URL.createObjectURL(file);
        previewBox.style.display = 'block';
    }
</script>
</body>
</html>
