<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm - Admin</title>
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
        .table {
            background-color: white;
            border-radius: 8px;
            overflow: hidden;
        }
        .table th {
            background-color: #e9ecef;
            font-weight: 600;
        }
        .btn {
            border-radius: 5px;
            transition: background-color 0.3s, transform 0.2s;
        }
        .btn:hover {
            transform: scale(1.05);
        }
        .pagination .page-link {
            border-radius: 5px;
        }
        .alert {
            border-radius: 8px;
        }
        .img-thumbnail {
            border-radius: 5px;
            object-fit: cover;
        }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4 fw-bold text-primary">Quản lý sản phẩm</h1>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i>${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <!-- Form tìm kiếm -->
    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-search me-2"></i>Tìm kiếm sản phẩm
        </div>
        <div class="card-body">
            <form method="GET" action="${pageContext.request.contextPath}/api/admin/products/searchpaginated" class="row g-3">
                <div class="col-md-4">
                    <input type="text" name="searchKeyword" class="form-control" placeholder="Nhập tên sản phẩm..." value="${searchKeyword}">
                </div>
                <div class="col-md-2">
                    <select name="size" class="form-select">
                        <option value="6" ${size == 6 ? 'selected' : ''}>6 items</option>
                        <option value="12" ${size == 12 ? 'selected' : ''}>12 items</option>
                        <option value="24" ${size == 24 ? 'selected' : ''}>24 items</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search me-2"></i>Tìm kiếm
                    </button>
                </div>
                <div class="col-md-2">
                    <a href="${pageContext.request.contextPath}/api/admin/products/export?searchKeyword=${searchKeyword}" class="btn btn-success w-100">
                        <i class="fas fa-download me-2"></i>Xuất Excel
                    </a>
                </div>
                <div class="col-md-2">
                    <a href="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate" class="btn btn-info w-100">
                        <i class="fas fa-plus me-2"></i>Thêm sản phẩm mới
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Bảng danh sách sản phẩm -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-list me-2"></i>Danh sách sản phẩm <span class="badge bg-secondary ms-2">Tổng: ${totalProducts}</span>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Hình ảnh</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá</th>
                        <th>Tồn kho</th>
                        <th>Danh mục</th>
                        <th>Thương hiệu</th>
                        <th>Đã bán</th>
                        <th>Đánh giá</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td>${product.productID}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty product.images && not empty product.images[0]}">
                                        <img src="${pageContext.request.contextPath}/assets/uploads/product/${product.images[0].dirImage}"
                                             alt="${product.productName}"
                                             width="50" height="50"
                                             class="img-thumbnail"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/logo.png';">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                                             alt="No image"
                                             width="50" height="50"
                                             class="img-thumbnail">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${product.productName}</td>
                            <td>${product.unitPrice} VNĐ</td>
                            <td>${product.stockQuantity}</td>
                            <td>${product.category != null ? product.category.categoryName : 'N/A'}</td>
                            <td>${product.brand != null ? product.brand.brandName : 'N/A'}</td>
                            <td>${product.soldCount}</td>
                            <td>${product.reviewCount} (${product.rating}/5)</td>
                            <td>
                                <div class="btn-group" role="group">
                                    <a href="${pageContext.request.contextPath}/api/admin/products/view?id=${product.productID}" class="btn btn-info btn-sm">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate?id=${product.productID}" class="btn btn-warning btn-sm">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/api/admin/products/delete?id=${product.productID}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa?')">
                                        <i class="fas fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty productList}">
                        <tr>
                            <td colspan="10" class="text-center text-muted">Không có sản phẩm nào.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Phân trang -->
    <c:if test="${totalPages > 1}">
        <nav aria-label="Product pagination" class="mt-4">
            <ul class="pagination justify-content-center">
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="?page=${i}&size=${size}&searchKeyword=${searchKeyword}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>