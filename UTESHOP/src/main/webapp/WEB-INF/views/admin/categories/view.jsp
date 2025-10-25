<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Xem danh mục - Admin</title>
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Chi tiết danh mục</h1>

    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-eye me-1"></i>
            Xem chi tiết danh mục
        </div>
        <div class="card-body">
            <p><strong>ID:</strong> ${category.categoryID}</p>
            <p><strong>Tên danh mục:</strong> ${category.categoryName}</p>
            <p><strong>Mô tả:</strong> ${category.description}</p>

            <c:if test="${not empty category.image}">
                <p>
                    <strong>Hình ảnh:</strong><br>
                    <img src="${pageContext.request.contextPath}/assets/${category.image}"
                         alt="${category.categoryName}"
                         width="200"
                         class="img-thumbnail"
                         onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                    <span style="display:none; color: #6c757d;">Không tải được ảnh</span>
                </p>
            </c:if>
            <c:if test="${empty category.image}">
                <p><strong>Hình ảnh:</strong> <span class="text-muted">Không có ảnh</span></p>
            </c:if>

            <p><strong>Số sản phẩm:</strong>
                ${category.products != null ? category.products.size() : 0}
            </p>
        </div>
    </div>

    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/admin/categories/searchpaginated"
           class="btn btn-primary">Quay lại danh sách</a>

        <a href="${pageContext.request.contextPath}/admin/categories/saveOrUpdate?id=${category.categoryID}"
           class="btn btn-warning">Sửa</a>

        <a href="${pageContext.request.contextPath}/admin/categories/delete?id=${category.categoryID}"
           class="btn btn-danger"
           onclick="return confirm('Xóa danh mục này?')">Xóa</a>
    </div>
</div>
</body>
</html>