<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý thương hiệu - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        .admin-content { padding-top: 80px; background-color: #f5f7fa; min-height: 100vh; }
        .card { border: none; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .card-header { background-color: #007bff; color: white; border-radius: 10px 10px 0 0; font-weight: 600; }
        .table th { background-color: #e9ecef; font-weight: 600; }
        .btn { border-radius: 5px; transition: all 0.3s; }
        .btn:hover { transform: scale(1.05); }
        .img-thumbnail { width: 50px; height: 50px; object-fit: cover; border-radius: 5px; }
        .pagination .page-link { border-radius: 5px; }
        .alert { border-radius: 8px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4 fw-bold text-primary">Quản lý thương hiệu</h1>

    <!-- Thông báo -->
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i>${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <!-- Form tìm kiếm -->
    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-search me-2"></i>Tìm kiếm thương hiệu
        </div>
        <div class="card-body">
            <form method="GET" action="${pageContext.request.contextPath}/api/admin/brands/searchpaginated" class="row g-3">
                <div class="col-md-4">
                    <input type="text" name="searchKeyword" class="form-control" placeholder="Nhập tên thương hiệu..." value="${searchKeyword}">
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
                <div class="col-md-4">
                    <a href="${pageContext.request.contextPath}/api/admin/brands/saveOrUpdate" class="btn btn-success w-100">
                        <i class="fas fa-plus me-2"></i>Thêm mới
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Bảng danh sách -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-list me-2"></i>Danh sách thương hiệu (${totalBrands} kết quả)
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Mô tả</th>
                    <th>Logo</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="brand" items="${brandList}">
                    <tr>
                        <td>${brand.brandID}</td>
                        <td>${brand.brandName}</td>
                        <td>${brand.description != null ? brand.description : 'N/A'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty brand.brandLogo}">
                                    <img src="${pageContext.request.contextPath}/image?fname=${brand.brandLogo}" alt="Logo" class="img-thumbnail"
                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                    <span style="display:none; color: #6c757d;">Không tải được</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">Không có</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/api/admin/brands/view?id=${brand.brandID}" class="btn btn-info btn-sm">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/api/admin/brands/saveOrUpdate?id=${brand.brandID}" class="btn btn-warning btn-sm">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/api/admin/brands/delete?id=${brand.brandID}" class="btn btn-danger btn-sm" onclick="return confirm('Xóa thương hiệu?')">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty brandList}">
                    <tr><td colspan="5" class="text-center text-muted">Không có thương hiệu nào.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Phân trang -->
    <c:if test="${totalPages > 1}">
        <nav aria-label="Brand pagination" class="mt-4">
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