<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Xem Review - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-content { padding-top: 80px; }
        .image-thumbnail { max-width: 200px; height: auto; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Chi tiết Review</h1>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty review}">
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-eye me-1"></i>
                Xem chi tiết Review
            </div>
            <div class="card-body">
                <p><strong>ID:</strong> ${review.reviewID}</p>
                <p><strong>Nội dung:</strong> ${review.content}</p>
                <p><strong>Hình ảnh:</strong>
                    <c:if test="${not empty review.image}">
                        <img src="${pageContext.request.contextPath}/${review.image}" alt="Review Image" class="image-thumbnail">
                    </c:if>
                    <c:if test="${empty review.image}">Không có</c:if>
                </p>
                <p><strong>Rating:</strong> ${review.rating} sao</p>
                <p><strong>Ngày tạo:</strong> ${createAtFormatted}</p>

                <!-- Hiển thị User với tên -->
                <p><strong>User:</strong>
                    <c:choose>
                        <c:when test="${not empty review.user}">
                            ${review.user.username} (ID: ${review.user.userID})
                        </c:when>
                        <c:otherwise>Không xác định (ID: Không có)</c:otherwise>
                    </c:choose>
                </p>

                <!-- Hiển thị Product với tên -->
                <p><strong>Sản phẩm:</strong>
                    <c:choose>
                        <c:when test="${not empty review.product}">
                            ${review.product.productName} (ID: ${review.product.productID})
                        </c:when>
                        <c:otherwise>Không xác định (ID: Không có)</c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>

        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/api/admin/review/searchpaginated"
               class="btn btn-primary">Quay lại danh sách</a>

            <a href="${pageContext.request.contextPath}/api/admin/review/delete?id=${review.reviewID}"
               class="btn btn-danger"
               onclick="return confirm('Xóa Review này?')">Xóa</a>
        </div>
    </c:if>
    <c:if test="${empty review}">
        <div class="alert alert-warning">Không tìm thấy Review!</div>
        <a href="${pageContext.request.contextPath}/api/admin/review/searchpaginated" class="btn btn-primary">Quay lại danh sách</a>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>