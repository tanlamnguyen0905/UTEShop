<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm - ${product == null ? 'Thêm mới' : 'Sửa'} - Admin</title>
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
        .alert {
            border-radius: 8px;
        }
        .img-thumbnail {
            border-radius: 5px;
            object-fit: cover;
        }
        .position-relative .badge {
            cursor: pointer;
        }
        .form-label {
            font-weight: 600;
            color: #495057;
        }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4 fw-bold text-primary">
        <i class="fas fa-edit me-2"></i>${product == null ? 'Thêm sản phẩm mới' : 'Sửa sản phẩm'}
    </h1>

    <!-- Hiển thị error nếu có -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Form thêm/sửa sản phẩm -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-file-alt me-2"></i>Thông tin sản phẩm
        </div>
        <div class="card-body">
            <form method="POST" action="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate" enctype="multipart/form-data">
                <c:if test="${not empty product.productID}">
                    <input type="hidden" name="id" value="${product.productID}">
                </c:if>

                <div class="row g-3">
                    <!-- Tên sản phẩm -->
                    <div class="col-md-6">
                        <label class="form-label">Tên sản phẩm <span class="text-danger">*</span></label>
                        <input type="text" name="productName" class="form-control" value="${product.productName}" placeholder="Nhập tên sản phẩm..." required>
                    </div>

                    <!-- Giá sản phẩm -->
                    <div class="col-md-6">
                        <label class="form-label">Giá sản phẩm (VNĐ) <span class="text-danger">*</span></label>
                        <input type="number" name="unitPrice" class="form-control" value="${product.unitPrice}" step="0.01" min="0.01" placeholder="0.00" required>
                    </div>

                    <!-- Số lượng tồn kho -->
                    <div class="col-md-6">
                        <label class="form-label">Số lượng tồn kho <span class="text-danger">*</span></label>
                        <input type="number" name="stockQuantity" class="form-control" value="${product.stockQuantity}" min="0" placeholder="0" required>
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
                    <small class="text-muted">Chọn hình ảnh mới (nếu muốn thêm). Hình ảnh hiện tại sẽ được giữ nếu không chọn. Chỉ chấp nhận file ảnh (JPG, PNG, GIF). Kích thước tối đa 10MB.</small>
                    <c:if test="${not empty product.images}">
                        <div class="mt-2 d-flex flex-wrap">
                            <c:forEach var="image" items="${product.images}">
                                <div class="me-2 mb-2">
                                    <img src="${pageContext.request.contextPath}/image?fname=${image.dirImage}"
                                         alt="${product.productName}"
                                         width="50" height="50" class="img-thumbnail"
                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                    <span style="display:none; color: #6c757d;">Không tải được ảnh hiện tại</span>
                                    <small class="d-block">Hình ảnh hiện tại</small>
                                </div>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Thương hiệu -->
                    <div class="col-md-6">
                        <label class="form-label">Thương hiệu <span class="text-danger">*</span></label>
                        <select name="brandId" class="form-select" required>
                            <option value="">Chọn thương hiệu...</option>
                            <c:forEach var="brand" items="${brandsList}">
                                <option value="${brand.brandID}" ${product.brand != null && product.brand.brandID == brand.brandID ? 'selected' : ''}>${brand.brandName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Mô tả -->
                    <div class="col-12">
                        <label class="form-label">Mô tả</label>
                        <textarea name="describe" class="form-control" rows="4" placeholder="Nhập mô tả sản phẩm...">${product.describe}</textarea>
                    </div>
                </div>

                <!-- PHẦN ẢNH: Hiển thị ảnh cũ (nếu edit) với checkbox xóa -->
                <div class="row mt-4">
                    <div class="col-12">
                        <label class="form-label">Ảnh hiện có (chọn để xóa):</label>
                        <c:choose>
                            <c:when test="${not empty product && not empty product.images}">
                                <div class="row">
                                    <c:forEach var="img" items="${product.images}" varStatus="status">
                                        <div class="col-md-3 mb-3">
                                            <div class="position-relative">
                                                <img src="${pageContext.request.contextPath}/image?fname=${img.dirImage}"
                                                     alt="Ảnh ${status.index + 1}"
                                                     class="img-thumbnail w-100"
                                                     style="height: 150px;"
                                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/logo.png';">
                                                <div class="position-absolute top-0 end-0 p-2">
                                                    <input type="checkbox"
                                                           name="deletedImageIds"
                                                           value="${img.imageID}"
                                                           class="form-check-input"
                                                           id="deleteImg${img.imageID}"
                                                           data-bs-toggle="tooltip" title="Chọn để xóa ảnh này">
                                                    <label for="deleteImg${img.imageID}" class="badge bg-danger cursor-pointer">
                                                        <i class="fas fa-trash"></i> Xóa
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">Chưa có ảnh. Hãy thêm ảnh mới bên dưới.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Upload ảnh mới: Multiple -->
                <div class="row mt-3">
                    <div class="col-12">
                        <label class="form-label">Thêm ảnh mới (chọn nhiều file):</label>
                        <input type="file" name="images" class="form-control" multiple accept="image/*">
                        <small class="text-muted">Chọn nhiều ảnh (tối đa 5MB/ảnh, chỉ JPG, PNG, GIF). Ảnh sẽ được thêm vào sản phẩm.</small>
                    </div>
                </div>

                <!-- Buttons -->
                <div class="row mt-4">
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary btn-lg me-2">
                            <i class="fas fa-save me-2"></i>Lưu sản phẩm
                        </button>
                        <a href="${pageContext.request.contextPath}/api/admin/products/searchpaginated" class="btn btn-secondary btn-lg">
                            <i class="fas fa-times me-2"></i>Hủy và quay lại
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Tooltip cho checkbox xóa ảnh
    document.addEventListener('DOMContentLoaded', function() {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    });
</script>
</body>
</html>