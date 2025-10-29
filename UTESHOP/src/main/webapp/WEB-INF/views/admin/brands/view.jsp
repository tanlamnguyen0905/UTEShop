<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xem thương hiệu - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        .admin-content { padding-top: 80px; background-color: #f5f7fa; min-height: 100vh; }
        .card { border: none; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .card-header { background-color: #007bff; color: white; border-radius: 10px 10px 0 0; font-weight: 600; }
        .img-fluid { max-width: 300px; height: auto; border-radius: 5px; }
        .btn { border-radius: 5px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4 fw-bold text-primary">
        <i class="fas fa-eye me-2"></i>Chi tiết thương hiệu
    </h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:if test="${not empty brand}">
        <div class="card">
            <div class="card-header">
                <i class="fas fa-info-circle me-2"></i>Thông tin
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>ID:</strong> ${brand.brandID}</p>
                        <p><strong>Tên:</strong> ${brand.brandName}</p>
                        <p><strong>Mô tả:</strong> ${brand.description != null ? brand.description : 'N/A'}</p>
                    </div>
                    <div class="col-md-6">
                        <c:if test="${not empty brand.brandLogo}">
                            <p><strong>Logo:</strong></p>
                            <img src="${pageContext.request.contextPath}/image?fname=${brand.brandLogo}" alt="Logo" class="img-fluid">
                        </c:if>
                        <c:if test="${empty brand.brandLogo}">
                            <p class="text-muted">Không có logo.</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/api/admin/brands/saveOrUpdate?id=${brand.brandID}" class="btn btn-warning">
                <i class="fas fa-edit me-2"></i>Sửa
            </a>
            <a href="${pageContext.request.contextPath}/api/admin/brands/searchpaginated" class="btn btn-secondary">
                <i class="fas fa-arrow-left me-2"></i>Quay lại
            </a>
        </div>
    </c:if>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>