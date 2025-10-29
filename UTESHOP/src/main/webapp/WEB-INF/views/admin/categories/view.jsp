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

            <c:choose>
                <c:when test="${not empty category.image}">
                    <p>
                        <strong>Hình ảnh:</strong><br>
                        <img src="${pageContext.request.contextPath}/image?fname=${category.image}"
                             alt="${category.categoryName}"
                             width="200"
                             class="img-thumbnail"
                             onerror="this.src='${pageContext.request.contextPath}/assets/images/logo.png';">
                    </p>
                </c:when>
                <c:otherwise>
                    <p><strong>Hình ảnh:</strong> <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="No image" width="200" class="img-thumbnail"></p>
                </c:otherwise>
            </c:choose>

            <p><strong>Số sản phẩm:</strong>
                ${category.products != null ? category.products.size() : 0}
            </p>
        </div>
    </div>

    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/api/admin/categories/searchpaginated"
           class="btn btn-primary">Quay lại danh sách</a>

        <a href="${pageContext.request.contextPath}/api/admin/categories/saveOrUpdate?id=${category.categoryID}"
           class="btn btn-warning">Sửa</a>

        <a href="${pageContext.request.contextPath}/api/admin/categories/delete?id=${category.categoryID}"
           class="btn btn-danger"
           onclick="return confirm('Xóa danh mục này?')">Xóa</a>
    </div>
</div>
</body>
</html>