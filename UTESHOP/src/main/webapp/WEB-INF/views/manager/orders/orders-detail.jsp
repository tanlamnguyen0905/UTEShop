<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Chi Tiết Đơn Hàng - Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .status-badge { font-size: 0.9em; }
    </style>
</head>
<body>
<div class="container-fluid px-4">
    <h1 class="mt-4 mb-4 fw-bold text-primary">
        <i class="fas fa-eye me-2"></i>Chi Tiết Đơn Hàng #${order.orderID}
    </h1>

    <!-- Thông tin đơn hàng -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header"><i class="fas fa-info-circle me-2"></i>Thông Tin Đơn Hàng</div>
                <div class="card-body">
                    <p><strong>ID:</strong> ${order.orderID}</p>
                    <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" /></p>
                    <p><strong>Trạng thái đơn:</strong> <span class="badge bg-${order.orderStatus == 'Hoàn thành' ? 'success' : order.orderStatus == 'Đã giao' ? 'info' : order.orderStatus == 'Đã hủy' ? 'danger' : 'warning'} status-badge">${order.orderStatus}</span></p>
                    <p><strong>Thanh toán:</strong> <span class="badge bg-${order.paymentStatus == 'Đã thanh toán' ? 'success' : 'warning'} status-badge">${order.paymentStatus}</span></p>
                    <p><strong>Ghi chú:</strong> ${order.notes != null ? order.notes : 'Không có'}</p>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-header"><i class="fas fa-user me-2"></i>Thông Tin Khách Hàng</div>
                <div class="card-body">
                    <p><strong>Tên:</strong> ${order.user.username} (${order.recipientName})</p>
                    <p><strong>SĐT:</strong> ${order.phoneNumber}</p>
                    <p><strong>Địa chỉ:</strong> ${order.address.street} , ${order.address.ward}, ${order.address.district}, ${order.address.city}</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Thanh toán & Voucher -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header"><i class="fas fa-credit-card me-2"></i>Phương Thức Thanh Toán</div>
                <div class="card-body">
                    <p><strong>Phương thức:</strong> ${order.paymentMethod.name}</p>
                    <c:if test="${not empty order.voucher}">
                        <p><strong>Voucher:</strong> ${order.voucher.code} (Giảm ${order.voucher.discountPercent}%)</p>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-header"><i class="fas fa-calculator me-2"></i>Tính Toán</div>
                <div class="card-body">
                    <p><strong>Tổng sản phẩm:</strong> ₫<fmt:formatNumber value="${order.amount}" type="number" maxFractionDigits="0" /></p>
                    <p><strong>Giảm giá:</strong> ₫<fmt:formatNumber value="${order.totalDiscount}" type="number" maxFractionDigits="0" /></p>
                    <p><strong>Phí ship:</strong> ₫<fmt:formatNumber value="${order.shippingFee}" type="number" maxFractionDigits="0" /></p>
                    <p class="fw-bold"><strong>Tổng thanh toán:</strong> ₫<fmt:formatNumber value="${total}" type="number" maxFractionDigits="0" /></p>
                </div>
            </div>
        </div>
    </div>

    <!-- Chi tiết sản phẩm (OrderDetails) -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-list me-2"></i>Sản Phẩm Trong Đơn Hàng
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Sản phẩm</th>
                        <th>Số lượng</th>
                        <th>Đơn giá (₫)</th>
                        <th>Thành tiền (₫)</th>
                        <th>Trạng thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="detail" items="${order.orderDetails}">
                        <tr>
                            <td>${detail.product.productName}</td>
                            <td>${detail.quantity}</td>
                            <td><fmt:formatNumber value="${detail.unitPrice}" type="number" maxFractionDigits="0" /></td>
                            <td><fmt:formatNumber value="${detail.totalPrice}" type="number" maxFractionDigits="0" /></td>
                            <td><span class="badge bg-${detail.status == 'ACTIVE' ? 'success' : 'secondary'}">${detail.status}</span></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty order.orderDetails}">
                        <tr><td colspan="5" class="text-center text-muted">Không có sản phẩm.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/api/manager/orders" class="btn btn-secondary">
            <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>