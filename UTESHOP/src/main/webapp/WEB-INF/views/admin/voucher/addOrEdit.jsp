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
    <title>${voucher.voucherID != null ? 'Sửa' : 'Thêm mới'} Voucher - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-content { padding-top: 80px; }
    </style>
</head>
<body>
<div class="container-fluid px-4 admin-content">
    <h1 class="mt-5 mb-4">${voucher.voucherID != null ? 'Sửa' : 'Thêm mới'} Voucher</h1>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="card mb-4">
        <div class="card-header">
            <i class="fas fa-edit me-2"></i>${voucher.voucherID != null ? 'Cập nhật' : 'Thêm mới'} Voucher
        </div>
        <div class="card-body">
            <%
                Voucher voucher = (Voucher) request.getAttribute("voucher");
                String startValue = "";
                if (voucher != null && voucher.getStartDate() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    startValue = voucher.getStartDate().format(formatter);
                }
                String endValue = "";
                if (voucher != null && voucher.getEndDate() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    endValue = voucher.getEndDate().format(formatter);
                }
            %>
            <form method="post" action="${pageContext.request.contextPath}/api/admin/voucher/saveOrUpdate">
                <c:if test="${not empty voucher.voucherID}">
                    <input type="hidden" name="id" value="${voucher.voucherID}">
                </c:if>

                <div class="mb-3">
                    <label for="codeVoucher" class="form-label">Mã Voucher <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="codeVoucher" name="codeVoucher" required
                           value="${voucher.codeVoucher}" placeholder="Nhập mã Voucher...">
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Mô tả</label>
                    <textarea class="form-control" id="description" name="description" rows="3"
                              placeholder="Nhập mô tả Voucher...">${voucher.description}</textarea>
                </div>

                <div class="mb-3">
                    <label for="discountPercent" class="form-label">Phần trăm giảm giá (%) <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="discountPercent" name="discountPercent"
                           min="0" max="100" step="0.01" required
                           value="${voucher.discountPercent}"
                           placeholder="0 - 100">
                    <div class="form-text">Giá trị từ 0 đến 100.</div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="startDate" class="form-label">Ngày bắt đầu <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" id="startDate" name="startDate" required
                               value="<%= startValue %>">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="endDate" class="form-label">Ngày kết thúc <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" id="endDate" name="endDate" required
                               value="<%= endValue %>">
                    </div>
                </div>

                <div class="mb-3">
                    <label for="maxDiscountAmount" class="form-label">Số tiền giảm tối đa</label>
                    <input type="number" class="form-control" id="maxDiscountAmount" name="maxDiscountAmount"
                           min="0" step="0.01"
                           value="${voucher.maxDiscountAmount}"
                           placeholder="Nhập số tiền (nếu có)">
                    <div class="form-text">Để trống nếu không giới hạn.</div>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/api/admin/voucher/searchpaginated" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> ${voucher.voucherID != null ? 'Cập nhật' : 'Thêm mới'}
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>