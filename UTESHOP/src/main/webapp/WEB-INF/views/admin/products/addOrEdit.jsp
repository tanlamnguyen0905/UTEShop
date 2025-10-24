<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty product.productID ? 'Thêm' : 'Sửa'} sản phẩm - Admin</title>
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
        .form-label {
            font-weight: 500;
        }
        .btn-primary, .btn-secondary {
            border-radius: 5px;
            transition: background-color 0.3s, transform 0.2s;
        }
        .btn-primary:hover, .btn-secondary:hover {
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
    <h1 class="mt-5 mb-4 fw-bold text-primary">${empty product.productID ? 'Thêm sản phẩm mới' : 'Sửa sản phẩm'}</h1>

    <!-- Hiển thị lỗi -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-edit me-2"></i>${empty product.productID ? 'Thêm sản phẩm' : 'Sửa sản phẩm'}
        </div>
        <div class="card-body">
            <form method="POST" action="${pageContext.request.contextPath}/admin/products/saveOrUpdate" enctype="multipart/form-data">
                <c:if test="${not empty product.productID}">
                    <input type="hidden" name="id" value="${product.productID}">
                </c:if>

                <div class="mb-3">
                    <label class="form-label">Tên sản phẩm:</label>
                    <input type="text" name="productName" class="form-control" value="${product.productName}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Mô tả:</label>
                    <textarea name="describe" class="form-control" rows="4">${product.describe}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Giá tiền:</label>
                    <input type="number" name="unitPrice" class="form-control" value="${product.unitPrice}" step="0.01" min="0.01" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Số lượng tồn kho:</label>
                    <input type="number" name="stockQuantity" class="form-control" value="${product.stockQuantity}" min="0" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Danh mục:</label>
                    <select name="categoryId" class="form-select" required>
                        <option value="" disabled ${empty product.category ? 'selected' : ''}>Chọn danh mục</option>
                        <c:forEach var="category" items="${categoriesList}">
                            <option value="${category.categoryID}" ${product.category != null && product.category.categoryID == category.categoryID ? 'selected' : ''}>
                                    ${category.categoryName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Thương hiệu:</label>
                    <select name="brandId" class="form-select" required>
                        <option value="" disabled ${empty product.brand ? 'selected' : ''}>Chọn thương hiệu</option>
                        <c:forEach var="brand" items="${brandsList}">
                            <option value="${brand.brandID}" ${product.brand != null && product.brand.brandID == brand.brandID ? 'selected' : ''}>
                                    ${brand.brandName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Hình ảnh:</label>
                    <input type="file" name="image" class="form-control" accept="image/*">
                    <small class="text-muted">Chọn hình ảnh mới (nếu muốn thay đổi). Hình ảnh hiện tại sẽ được giữ nếu không chọn.</small>
                    <c:if test="${not empty product.images}">
                        <div class="mt-2 d-flex flex-wrap">
                            <c:forEach var="image" items="${product.images}">
                                <div class="me-2 mb-2">
                                    <img src="${pageContext.request.contextPath}/assets/${image.dirImage}"
                                         alt="${product.productName}"
                                         width="50" height="50" class="img-thumbnail">
                                    <small class="d-block">Hình ảnh hiện tại</small>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-2"></i>${empty product.productID ? 'Thêm' : 'Cập nhật'}
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/products/searchpaginated" class="btn btn-secondary">
                        <i class="fas fa-times me-2"></i>Hủy
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>