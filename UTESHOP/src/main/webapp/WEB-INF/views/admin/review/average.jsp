<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rating trung bình sản phẩm - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Rating trung bình của từng sản phẩm</h1>

    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-chart-bar me-2"></i>Bảng rating trung bình
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Tên sản phẩm</th>
                        <th>Rating trung bình</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty avgRatings}">
                            <tr>
                                <td colspan="3" class="text-center">Không có dữ liệu</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="avg" items="${avgRatings}">
                                <tr>
                                    <td>${avg.productID}</td>
                                    <td>${avg.productName}</td>
                                    <td>${avg.avgRating != null ? avg.avgRating : 'Chưa có rating'}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
            <a href="${pageContext.request.contextPath}/api/admin/review/searchpaginated" class="btn btn-primary mt-3">
                <i class="fas fa-arrow-left"></i> Quay lại danh sách Review
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>