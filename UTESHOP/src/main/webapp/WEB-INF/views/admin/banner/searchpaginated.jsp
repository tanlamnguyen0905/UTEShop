<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Danh sách banner - Admin</title>
    <style>
        /* Custom CSS để tránh header che khuất */
        .admin-content {
            padding-top: 80px; /* Tăng padding-top để đẩy nội dung xuống dưới header fixed */
        }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Danh sách banner</h1>  <!-- Tăng mt-5 để thêm khoảng cách -->

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <!-- Hiển thị lỗi nếu có -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">Lỗi: ${error}</div>
    </c:if>

    <!-- Form tìm kiếm -->
    <form method="GET" action="${pageContext.request.contextPath}/api/admin/banner" class="mb-4">
        <div class="row">
            <div class="col-md-4">
                <input type="text" name="searchKeyword" class="form-control" placeholder="Tìm kiếm theo tên banner" value="${searchKeyword}">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            </div>
            <div class="col-md-6 text-end">
                <a href="${pageContext.request.contextPath}/api/admin/banner/saveOrUpdate" class="btn btn-success">Thêm mới</a>
            </div>
        </div>
    </form>

    <!-- Handle empty data -->
    <c:choose>
        <c:when test="${empty bannerList or bannerList.size() == 0}">
            <div class="alert alert-info">
                Không có dữ liệu banner. <a href="${pageContext.request.contextPath}/api/admin/banner/saveOrUpdate">Thêm banner mới</a>.
            </div>
        </c:when>
        <c:otherwise>
            <!-- Bảng danh sách -->
            <div class="card mb-4">
                <div class="card-header">
                    <i class="fas fa-table me-1"></i>
                    Danh sách banner
                </div>
                <div class="card-body">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên banner</th>
                            <th>Mô tả</th>
                            <th>Hình ảnh</th>
                            <th>Số sản phẩm</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="banner" items="${bannerList}">
                            <tr>
                                <td>${banner.bannerID}</td>
                                <td>${banner.bannerName}</td>
                                <td>${banner.description}</td>
                                <td>
                                    <c:if test="${not empty banner.bannerImage}">
                                        <img src="${pageContext.request.contextPath}/image?fname=${banner.bannerImage}"
                                             alt="${banner.bannerName}"
                                             width="100" height="100" class="img-thumbnail"
                                             onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                        <span style="display:none; color: #6c757d; font-size: 0.875em;">Không tải được ảnh</span>
                                    </c:if>
                                    <c:if test="${empty banner.bannerImage}">
                                        <span class="text-muted">Không có ảnh</span>
                                    </c:if>
                                </td>
                                <td>${banner.products != null ? banner.products.size() : 0}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/api/admin/banner/view?id=${banner.bannerID}" class="btn btn-info btn-sm">Xem</a>
                                    <a href="${pageContext.request.contextPath}/api/admin/banner/saveOrUpdate?id=${banner.bannerID}" class="btn btn-warning btn-sm">Sửa</a>
                                    <a href="${pageContext.request.contextPath}/api/admin/banner/delete?id=${banner.bannerID}" class="btn btn-danger btn-sm" onclick="return confirm('Xóa banner này?')">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Phân trang -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Pagination">
                    <ul class="pagination justify-content-center">
                        <c:if test="${currentPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${currentPage - 1}&size=${size}&searchKeyword=${searchKeyword}">Trước</a>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <li class="page-item active"><span class="page-link">${i}</span></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item"><a class="page-link" href="?page=${i}&size=${size}&searchKeyword=${searchKeyword}">${i}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${currentPage + 1}&size=${size}&searchKeyword=${searchKeyword}">Sau</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>