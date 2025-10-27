<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ute.entities.Voucher" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Xem Voucher - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">Chi tiết Voucher</h1>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty voucher}">
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-eye me-1"></i>
                Xem chi tiết Voucher
            </div>
            <div class="card-body">
                <%
                    Voucher voucher = (Voucher) request.getAttribute("voucher");
                    String startFormatted = "Chưa xác định";
                    if (voucher.getStartDate() != null) {
                        startFormatted = voucher.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    }
                    String endFormatted = "Chưa xác định";
                    if (voucher.getEndDate() != null) {
                        endFormatted = voucher.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    }
                %>
                <p><strong>ID:</strong> <%= voucher.getVoucherID() %></p>
                <p><strong>Mã Voucher:</strong> <%= voucher.getCodeVoucher() %></p>
                <p><strong>Mô tả:</strong> <%= voucher.getDescription() != null ? voucher.getDescription() : "" %></p>
                <p><strong>Phần trăm giảm giá:</strong> <%= voucher.getDiscountPercent() != null ? voucher.getDiscountPercent() + "%" : "" %></p>
                <p><strong>Ngày bắt đầu:</strong> <%= startFormatted %></p>
                <p><strong>Ngày kết thúc:</strong> <%= endFormatted %></p>
                <p><strong>Số tiền giảm tối đa:</strong> <%= voucher.getMaxDiscountAmount() != null ? voucher.getMaxDiscountAmount() : "Không giới hạn" %></p>
            </div>
        </div>

        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/api/admin/voucher/searchpaginated"
               class="btn btn-primary">Quay lại danh sách</a>

            <a href="${pageContext.request.contextPath}/api/admin/voucher/saveOrUpdate?id=<%= voucher.getVoucherID() %>"
               class="btn btn-warning">Sửa</a>

            <a href="${pageContext.request.contextPath}/api/admin/voucher/delete?id=<%= voucher.getVoucherID() %>"
               class="btn btn-danger"
               onclick="return confirm('Xóa Voucher này?')">Xóa</a>
        </div>
    </c:if>
    <c:if test="${empty voucher}">
        <div class="alert alert-warning">Không tìm thấy Voucher!</div>
        <a href="${pageContext.request.contextPath}/api/admin/voucher/searchpaginated" class="btn btn-primary">Quay lại danh sách</a>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>