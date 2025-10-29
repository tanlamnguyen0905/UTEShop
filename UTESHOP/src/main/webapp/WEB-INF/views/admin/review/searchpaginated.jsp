<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ute.entities.Review" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Review - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Quản lý Review</h1>

    <!-- Message -->
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="message" scope="session" />
    </c:if>

    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-table me-2"></i>Danh sách Review
        </div>
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <form method="get" action="${pageContext.request.contextPath}/api/admin/review/searchpaginated" class="d-flex">
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="size" value="${size}">
                    <div class="input-group me-2" style="width: 250px;">
                        <input type="text" class="form-control" name="searchKeyword" placeholder="Tìm theo nội dung..." value="${searchKeyword}">
                    </div>
                    <div class="input-group me-2" style="width: 150px;">
                        <select class="form-select" name="ratingFilter">
                            <option value="all" ${ratingFilter == 'all' ? 'selected' : ''}>Tất cả Rating</option>
                            <option value="1" ${ratingFilter == '1' ? 'selected' : ''}>1 sao</option>
                            <option value="2" ${ratingFilter == '2' ? 'selected' : ''}>2 sao</option>
                            <option value="3" ${ratingFilter == '3' ? 'selected' : ''}>3 sao</option>
                            <option value="4" ${ratingFilter == '4' ? 'selected' : ''}>4 sao</option>
                            <option value="5" ${ratingFilter == '5' ? 'selected' : ''}>5 sao</option>
                        </select>
                    </div>
                    <button class="btn btn-outline-secondary" type="submit"><i class="fas fa-search"></i></button>
                </form>
                <div>
                    <a href="${pageContext.request.contextPath}/api/admin/review/average" class="btn btn-info me-2">
                        <i class="fas fa-chart-bar"></i> Rating trung bình sản phẩm
                    </a>
                    <a href="${pageContext.request.contextPath}/api/admin/review/exportExcel" class="btn btn-success">
                        <i class="fas fa-file-excel"></i> Xuất Excel
                    </a>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nội dung</th>
                        <th>Hình ảnh</th>
                        <th>Rating</th>
                        <th>Ngày tạo</th>
                        <th>User ID</th>
                        <th>Product ID</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty reviewList}">
                            <tr>
                                <td colspan="8" class="text-center">Không có dữ liệu</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="review" items="${reviewList}">
                                <%
                                    Review r = (Review) pageContext.findAttribute("review");
                                    String createAtFormatted = "Chưa xác định";
                                    if (r != null && r.getCreatedAt() != null) {
                                        createAtFormatted = r.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                                    }
                                %>
                                <tr>
                                    <td>${review.reviewID}</td>
                                    <td>${review.content}</td>
                                    <td>
                                        <c:if test="${not empty review.image}">
                                            <img src="${pageContext.request.contextPath}/assets/uploads/review/${review.image}" alt="Image" width="50" height="50">
                                        </c:if>
                                    </td>
                                    <td>${review.rating} sao</td>
                                    <td><%= createAtFormatted %></td>
                                    <td>${review.user.userID}</td>
                                    <td>${review.product.productID}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/api/admin/review/view?id=${review.reviewID}" class="btn btn-info btn-sm">
                                            <i class="fas fa-eye"></i> Xem
                                        </a>
                                        <a href="${pageContext.request.contextPath}/api/admin/review/delete?id=${review.reviewID}" class="btn btn-danger btn-sm"
                                           onclick="return confirm('Xóa Review này?')">
                                            <i class="fas fa-trash"></i> Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage - 1}&size=${size}${searchKeyword != null ? '&searchKeyword=' + searchKeyword : ''}${ratingFilter != null && ratingFilter != 'all' ? '&ratingFilter=' + ratingFilter : ''}">Trước</a>
                        </li>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&size=${size}${searchKeyword != null ? '&searchKeyword=' + searchKeyword : ''}${ratingFilter != null && ratingFilter != 'all' ? '&ratingFilter=' + ratingFilter : ''}">${i}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage + 1}&size=${size}${searchKeyword != null ? '&searchKeyword=' + searchKeyword : ''}${ratingFilter != null && ratingFilter != 'all' ? '&ratingFilter=' + ratingFilter : ''}">Sau</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>