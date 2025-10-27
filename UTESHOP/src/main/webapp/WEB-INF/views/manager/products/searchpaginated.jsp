<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Thống kê tồn kho - Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .table th { background-color: #f8f9fa; font-weight: 600; }
        .btn-action { border-radius: 5px; margin: 0 2px; }
        .low-stock { background-color: #fff3cd; }  /* Highlight low stock */
        .alert { border-radius: 8px; }
    </style>
</head>
<body>
<div class="container-fluid px-4">
    <h1 class="mt-4 mb-4 fw-bold text-primary">
        <i class="fas fa-warehouse me-2"></i>Thống kê tồn kho
    </h1>

    <!-- Message/Error -->
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i>${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="message" scope="session" />
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-triangle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Filter Form -->
    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-filter me-2"></i>Lọc thống kê
        </div>
        <div class="card-body">
            <form method="GET" class="row g-3">
                <div class="col-md-3">
                    <input type="text" name="keyword" class="form-control" placeholder="Tên sản phẩm..." value="${filter.keyword}">
                </div>
                <div class="col-md-3">
                    <select name="categoryId" class="form-select">
                        <option value="">Tất cả danh mục</option>
                        <!-- Assume categoriesList from Controller -->
                        <c:forEach var="cat" items="${categoriesList}">
                            <option value="${cat.categoryID}" ${filter.categoryId == cat.categoryID ? 'selected' : ''}>${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <input type="number" name="minStock" class="form-control" placeholder="Tồn kho thấp hơn" value="${param.minStock}">
                </div>
                <div class="col-md-2">
                    <select name="size" class="form-select">
                        <option value="10" ${size == 10 ? 'selected' : ''}>10 items</option>
                        <option value="20" ${size == 20 ? 'selected' : ''}>20 items</option>
                        <option value="50" ${size == 50 ? 'selected' : ''}>50 items</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search me-2"></i>Lọc
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Table -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-list me-2"></i>Danh sách sản phẩm (${totalProducts} kết quả)
            <div class="float-end">
                <a href="${pageContext.request.contextPath}/api/manager/products/report" class="btn btn-success btn-sm">
                    <i class="fas fa-file-excel me-2"></i>Xuất báo cáo
                </a>
            </div>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th>Tồn kho</th>
                        <th>Đã bán</th>
                        <th>Giá</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="product" items="${productList}">  <!-- Sửa var="product" -->
                        <tr class="${product.stockQuantity < 10 ? 'low-stock' : ''}">
                            <td>${product.productID}</td>
                            <td>${product.productName}</td>
                            <td>${product.category.categoryName}</td>
                            <td><span class="badge bg-info">${product.stockQuantity}</span></td>
                            <td><span class="badge bg-success">${product.soldCount}</span></td>
                            <td>₫<fmt:formatNumber value="${product.unitPrice}" type="number" maxFractionDigits="0" /></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/api/manager/products/import?id=${product.productID}" class="btn btn-primary btn-sm btn-action">
                                    <i class="fas fa-plus"></i> Nhập
                                </a>
                                <a href="${pageContext.request.contextPath}/api/manager/products/export?id=${product.productID}" class="btn btn-warning btn-sm btn-action">
                                    <i class="fas fa-minus"></i> Xuất
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty productList}">  <!-- Kiểm tra attribute productList từ Controller -->
                        <tr>
                            <td colspan="7" class="text-center text-muted">Không có sản phẩm nào.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Pagination">
                    <ul class="pagination justify-content-center">
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&size=${size}&keyword=${filter.keyword}&categoryId=${filter.categoryId}&minStock=${param.minStock}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>