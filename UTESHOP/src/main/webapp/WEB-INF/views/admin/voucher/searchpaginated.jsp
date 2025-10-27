<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ute.entities.Voucher" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Voucher - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Quản lý Voucher</h1>

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
            <i class="fas fa-table me-2"></i>Danh sách Voucher
        </div>
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <form method="get" action="${pageContext.request.contextPath}api/admin/voucher/searchpaginated" class="d-flex">
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="size" value="${size}">
                    <div class="input-group" style="width: 300px;">
                        <input type="text" class="form-control" name="searchKeyword" placeholder="Tìm theo mã Voucher..." value="${searchKeyword}">
                        <button class="btn btn-outline-secondary" type="submit"><i class="fas fa-search"></i></button>
                    </div>
                </form>
                <a href="${pageContext.request.contextPath}/api/admin/voucher/saveOrUpdate" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Thêm mới
                </a>
            </div>

            <div class="table-responsive">
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Mã Voucher</th>
                        <th>Mô tả</th>
                        <th>Phần trăm giảm</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Giảm tối đa</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty voucherList}">
                            <tr>
                                <td colspan="8" class="text-center">Không có dữ liệu</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="voucher" items="${voucherList}">
                                <%
                                    Voucher v = (Voucher) pageContext.findAttribute("voucher");
                                    String startFormatted = "Chưa xác định";
                                    if (v != null && v.getStartDate() != null) {
                                        startFormatted = v.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                                    }
                                    String endFormatted = "Chưa xác định";
                                    if (v != null && v.getEndDate() != null) {
                                        endFormatted = v.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                                    }
                                %>
                                <tr>
                                    <td>${voucher.voucherID}</td>
                                    <td>${voucher.codeVoucher}</td>
                                    <td>${voucher.description}</td>
                                    <td>${voucher.discountPercent}%</td>
                                    <td><%= startFormatted %></td>
                                    <td><%= endFormatted %></td>
                                    <td>${voucher.maxDiscountAmount != null ? voucher.maxDiscountAmount : 'Không giới hạn'}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/api/admin/voucher/view?id=${voucher.voucherID}" class="btn btn-info btn-sm">
                                            <i class="fas fa-eye"></i> Xem
                                        </a>
                                        <a href="${pageContext.request.contextPath}/api/admin/voucher/saveOrUpdate?id=${voucher.voucherID}" class="btn btn-warning btn-sm">
                                            <i class="fas fa-edit"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/api/admin/voucher/delete?id=${voucher.voucherID}" class="btn btn-danger btn-sm"
                                           onclick="return confirm('Xóa Voucher này?')">
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
                            <a class="page-link" href="?page=${currentPage - 1}&size=${size}${searchKeyword != null ? '&searchKeyword=' : ''}${searchKeyword}">Trước</a>
                        </li>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&size=${size}${searchKeyword != null ? '&searchKeyword=' : ''}${searchKeyword}">${i}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage + 1}&size=${size}${searchKeyword != null ? '&searchKeyword=' : ''}${searchKeyword}">Sau</a>
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