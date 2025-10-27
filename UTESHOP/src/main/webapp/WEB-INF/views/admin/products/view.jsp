<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết sản phẩm - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        .admin-content {
            padding-top: 80px;
            background-color: #f5f7fa;
            min-height: 100vh;
        }
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .card-header {
            background-color: #007bff;
            color: white;
            border-radius: 10px 10px 0 0;
            font-weight: 600;
        }
        .btn {
            border-radius: 5px;
            transition: background-color 0.3s, transform 0.2s;
        }
        .btn:hover {
            transform: scale(1.05);
        }
        .img-thumbnail {
            border-radius: 5px;
            object-fit: cover;
        }
        .alert {
            border-radius: 8px;
        }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4 fw-bold text-primary">Chi tiết sản phẩm</h1>

    <c:if test="${not empty product}">
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-info-circle me-2"></i>Thông tin sản phẩm: ${product.productName}
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <h5 class="mb-3">Thông tin cơ bản</h5>
                        <p><strong>ID:</strong> ${product.productID}</p>
                        <p><strong>Tên sản phẩm:</strong> ${product.productName}</p>
                        <p><strong>Mô tả:</strong> ${product.describe != null ? product.describe : 'Chưa có mô tả'}</p>
                        <p><strong>Giá:</strong> ${product.unitPrice} VNĐ</p>
                        <p><strong>Tồn kho:</strong> ${product.stockQuantity}</p>
                        <p><strong>Đã bán:</strong> ${product.soldCount}</p>
                        <p><strong>Số đánh giá:</strong> ${product.reviewCount}</p>
                        <p><strong>Điểm đánh giá trung bình:</strong> ${product.rating}/5</p>
                        <p><strong>Ngày nhập:</strong> ${product.importDate}</p>
                    </div>
                    <div class="col-md-6">
                        <h5 class="mb-3">Danh mục & Thương hiệu</h5>
                        <p><strong>Danh mục:</strong> ${product.category != null ? product.category.categoryName : 'N/A'}</p>
                        <p><strong>Thương hiệu:</strong> ${product.brand != null ? product.brand.brandName : 'N/A'}</p>

                        <h5 class="mb-3 mt-4">Hình ảnh</h5>
                        <c:if test="${not empty product.images}">
                            <div class="row">
                                <c:forEach var="image" items="${product.images}">
                                    <div class="col-md-4 mb-3">
                                        <img src="${pageContext.request.contextPath}/image?fname=${image.dirImage}"
                                             alt="${product.productName}"
                                             class="img-thumbnail w-100" style="height: 150px;"
                                             onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                        <span style="display:none; color: #6c757d;">Không tải được ảnh</span>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                        <c:if test="${empty product.images}">
                            <p class="text-muted">Chưa có hình ảnh.</p>
                        </c:if>
                    </div>
                </div>

                <div class="d-flex gap-2 mt-4">
                    <a href="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate?id=${product.productID}" class="btn btn-warning">
                        <i class="fas fa-edit me-2"></i>Sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/api/admin/products/searchpaginated" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
                    </a>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${empty product}">
        <div class="alert alert-warning">
            <i class="fas fa-exclamation-triangle me-2"></i>Sản phẩm không tồn tại!
        </div>
        <a href="${pageContext.request.contextPath}/api/admin/products/searchpaginated" class="btn btn-secondary">
            <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
        </a>
    </c:if>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>