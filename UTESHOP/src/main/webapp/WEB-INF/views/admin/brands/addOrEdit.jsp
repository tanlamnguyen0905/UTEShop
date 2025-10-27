<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${brand == null ? 'Thêm' : 'Sửa'} thương hiệu - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        .admin-content { padding-top: 80px; background-color: #f5f7fa; min-height: 100vh; }
        .card { border: none; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .card-header { background-color: #007bff; color: white; border-radius: 10px 10px 0 0; font-weight: 600; }
        .btn { border-radius: 5px; transition: all 0.3s; }
        .btn:hover { transform: scale(1.05); }
        .alert { border-radius: 8px; }
        .img-thumbnail { max-width: 200px; height: auto; border-radius: 5px; }
        .form-label { font-weight: 600; color: #495057; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4 fw-bold text-primary">
        <i class="fas fa-edit me-2"></i>${brand == null ? 'Thêm thương hiệu mới' : 'Sửa thương hiệu'}
    </h1>

    <!-- Error -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Form -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-file-alt me-2"></i>Thông tin thương hiệu
        </div>
        <div class="card-body">
            <form method="POST" action="${pageContext.request.contextPath}/api/admin/brands/saveOrUpdate" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${brand.brandID}">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Tên thương hiệu <span class="text-danger">*</span></label>
                        <input type="text" name="brandName" class="form-control" value="${brand.brandName}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Mô tả</label>
                        <textarea name="description" class="form-control" rows="3">${brand.description}</textarea>
                    </div>
                </div>

                <!-- Logo -->
                <div class="row mt-4">
                    <div class="col-md-6">
                        <label class="form-label">Logo hiện tại (nếu có)</label>
                        <c:if test="${not empty brand && not empty brand.brandLogo}">
                            <div class="position-relative">
                                <img src="${pageContext.request.contextPath}/assets/${brand.brandLogo}" alt="Logo" class="img-thumbnail d-block mb-2"
                                     onerror="this.style.display='none';">
                                <c:if test="${not empty brand.brandID}">  <!-- Chỉ hiển thị khi edit -->
                                    <div class="form-check">
                                        <input type="checkbox" name="deleteLogo" class="form-check-input" id="deleteLogo">
                                        <label class="form-check-label" for="deleteLogo">Xóa logo cũ</label>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                        <c:if test="${empty brand.brandLogo}">
                            <p class="text-muted">Chưa có logo.</p>
                        </c:if>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Upload logo mới</label>
                        <input type="file" name="brandLogo" class="form-control" accept="image/*">
                        <small class="text-muted">Chỉ JPG/PNG (tối đa 5MB).</small>
                    </div>
                </div>

                <!-- Buttons -->
                <div class="row mt-4">
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary btn-lg me-2">
                            <i class="fas fa-save me-2"></i>Lưu
                        </button>
                        <a href="${pageContext.request.contextPath}/api/admin/brands/searchpaginated" class="btn btn-secondary btn-lg">
                            <i class="fas fa-times me-2"></i>Hủy
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>