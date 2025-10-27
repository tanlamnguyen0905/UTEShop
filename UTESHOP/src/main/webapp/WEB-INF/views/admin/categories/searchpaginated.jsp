<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách danh mục - Admin</title>
    <style>
        /* Custom CSS để tránh header che khuất */
        .admin-content {
            padding-top: 80px; /* Tăng padding-top để đẩy nội dung xuống dưới header fixed */
        }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Danh sách danh mục</h1>  <!-- Tăng mt-5 để thêm khoảng cách -->

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
    <form method="GET" action="${pageContext.request.contextPath}/api/admin/categories/searchpaginated" class="mb-4">
        <div class="row">
            <div class="col-md-4">
                <input type="text" name="searchKeyword" class="form-control" placeholder="Tìm kiếm theo tên danh mục" value="${searchKeyword}">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            </div>
            <div class="col-md-6 text-end">
                <a href="${pageContext.request.contextPath}/api/admin/categories/saveOrUpdate" class="btn btn-success">Thêm mới</a>
            </div>
        </div>
    </form>

    <!-- Handle empty data -->
    <c:choose>
        <c:when test="${empty categoryList or categoryList.size() == 0}">
            <div class="alert alert-info">
                Không có dữ liệu danh mục. <a href="${pageContext.request.contextPath}/api/admin/categories/saveOrUpdate">Thêm danh mục mới</a>.
            </div>
        </c:when>
        <c:otherwise>
            <!-- Bảng danh sách -->
            <div class="card mb-4">
                <div class="card-header">
                    <i class="fas fa-table me-1"></i>
                    Danh sách danh mục
                </div>
                <div class="card-body">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên danh mục</th>
                            <th>Mô tả</th>
                            <th>Hình ảnh</th>
                            <th>Số sản phẩm</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="category" items="${categoryList}">
                            <tr>
                                <td>${category.categoryID}</td>
                                <td>${category.categoryName}</td>
                                <td>${category.description}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty category.image}">
                                            <img src="${pageContext.request.contextPath}/image?fname=${category.image}"
                                                 alt="${category.categoryName}"
                                                 width="50" height="50" class="img-thumbnail"
                                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/logo.png';">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                                                 alt="No image"
                                                 width="50" height="50" class="img-thumbnail">
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${category.products != null ? category.products.size() : 0}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/api/admin/categories/view?id=${category.categoryID}" class="btn btn-info btn-sm">Xem</a>
                                    <a href="${pageContext.request.contextPath}/api/admin/categories/saveOrUpdate?id=${category.categoryID}" class="btn btn-warning btn-sm">Sửa</a>
                                    <a href="${pageContext.request.contextPath}/api/admin/categories/delete?id=${category.categoryID}" class="btn btn-danger btn-sm" onclick="return confirm('Xóa danh mục này?')">Xóa</a>
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