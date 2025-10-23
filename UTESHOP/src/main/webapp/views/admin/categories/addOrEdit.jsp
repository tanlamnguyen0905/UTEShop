<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${empty category.categoryID ? 'Thêm' : 'Sửa'} danh mục - Admin</title>
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">${empty category.categoryID ? 'Thêm danh mục mới' : 'Sửa danh mục'}</h1>

    <!-- Hiển thị lỗi -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-edit me-1"></i>
            ${empty category.categoryID ? 'Thêm danh mục' : 'Sửa danh mục'}
        </div>
        <div class="card-body">
            <form method="POST" action="${pageContext.request.contextPath}/admin/categories/saveOrUpdate" enctype="multipart/form-data">
                <c:if test="${not empty category.categoryID}">
                    <input type="hidden" name="id" value="${category.categoryID}">
                </c:if>

                <div class="mb-3">
                    <label class="form-label">Tên danh mục:</label>
                    <input type="text" name="categoryName" class="form-control" value="${category.categoryName}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Mô tả:</label>
                    <textarea name="description" class="form-control" rows="3">${category.description}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Hình ảnh:</label>
                    <input type="file" name="image" class="form-control" accept="image/*">
                    <c:if test="${not empty category.image}">
                        <div class="mt-2">
                            <img src="${pageContext.request.contextPath}/assets/${category.image}"
                                 alt="${category.categoryName}"
                                 width="50" height="50" class="img-thumbnail">
                            <small>Hình ảnh hiện tại</small>
                        </div>
                    </c:if>
                </div>

                <button type="submit" class="btn btn-primary">Lưu</button>
                <a href="${pageContext.request.contextPath}/admin/categories/searchpaginated" class="btn btn-secondary">Hủy</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>