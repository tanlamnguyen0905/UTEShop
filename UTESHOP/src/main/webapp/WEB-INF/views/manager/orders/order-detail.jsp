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
        .info-card { margin-bottom: 20px; }
        .info-card .card-header {
            background-color: #f8f9fa;
            font-weight: 600;
        }
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
            <div class="card info-card">
                <div class="card-header"><i class="fas fa-info-circle me-2"></i>Thông Tin Đơn Hàng</div>
                <div class="card-body">
                    <p><strong>Mã đơn hàng:</strong> #${order.orderID}</p>
                    <p><strong>Ngày đặt:</strong>
                        <c:choose>
                            <c:when test="${not empty order.orderDate}">
                                ${order.orderDate.dayOfMonth}/${order.orderDate.monthValue}/${order.orderDate.year}
                                lúc ${order.orderDate.hour}:${order.orderDate.minute < 10 ? '0' : ''}${order.orderDate.minute}
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </p>
                    <p><strong>Trạng thái đơn:</strong>
                        <c:choose>
                            <c:when test="${order.orderStatus eq 'Hoàn thành'}">
                                <span class="badge bg-success status-badge">${order.orderStatus}</span>
                            </c:when>
                            <c:when test="${order.orderStatus eq 'Đã giao'}">
                                <span class="badge bg-info status-badge">${order.orderStatus}</span>
                            </c:when>
                            <c:when test="${order.orderStatus eq 'Đã hủy'}">
                                <span class="badge bg-danger status-badge">${order.orderStatus}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning status-badge">${order.orderStatus}</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <p><strong>Trạng thái thanh toán:</strong>
                        <c:choose>
                            <c:when test="${order.paymentStatus eq 'Đã thanh toán'}">
                                <span class="badge bg-success status-badge">${order.paymentStatus}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning status-badge">${order.paymentStatus}</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <p><strong>Ghi chú:</strong>
                        <c:choose>
                            <c:when test="${not empty order.notes}">
                                ${order.notes}
                            </c:when>
                            <c:otherwise>
                                <em class="text-muted">Không có ghi chú</em>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card info-card">
                <div class="card-header"><i class="fas fa-user me-2"></i>Thông Tin Khách Hàng</div>
                <div class="card-body">
                    <p><strong>Người nhận:</strong> ${order.recipientName}</p>
                    <p><strong>Số điện thoại:</strong> ${order.phoneNumber}</p>
                    <c:if test="${not empty order.user}">
                        <p><strong>Tài khoản:</strong> ${order.user.username}</p>
                        <c:if test="${not empty order.user.email}">
                            <p><strong>Email:</strong> ${order.user.email}</p>
                        </c:if>
                    </c:if>
                    <c:if test="${not empty order.address}">
                        <p><strong>Địa chỉ:</strong><br/>
                                ${order.address.addressDetail}<c:if test="${not empty order.address.addressDetail}">, </c:if>
                                ${order.address.ward}<c:if test="${not empty order.address.ward}">, </c:if>
                                ${order.address.district}<c:if test="${not empty order.address.district}">, </c:if>
                                ${order.address.province}
                        </p>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Thanh toán & Voucher -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card info-card">
                <div class="card-header"><i class="fas fa-credit-card me-2"></i>Phương Thức Thanh Toán</div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty order.paymentMethod}">
                            <p><strong>Phương thức:</strong> ${order.paymentMethod.name}</p>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted">Chưa chọn phương thức thanh toán</p>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${not empty order.voucher}">
                        <hr/>
                        <p><strong>Voucher áp dụng:</strong></p>
                        <p>Mã: <span class="badge bg-primary">${order.voucher.code}</span></p>
                        <p>Giảm giá: ${order.voucher.discountPercent}%</p>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card info-card">
                <div class="card-header"><i class="fas fa-calculator me-2"></i>Tổng Kết Đơn Hàng</div>
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <span>Tổng sản phẩm:</span>
                        <strong>₫<fmt:formatNumber value="${order.amount}" type="number" maxFractionDigits="0" /></strong>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Giảm giá:</span>
                        <strong class="text-danger">-₫<fmt:formatNumber value="${order.totalDiscount}" type="number" maxFractionDigits="0" /></strong>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Phí vận chuyển:</span>
                        <strong>₫<fmt:formatNumber value="${order.shippingFee}" type="number" maxFractionDigits="0" /></strong>
                    </div>
                    <hr/>
                    <div class="d-flex justify-content-between">
                        <h5 class="mb-0">Tổng thanh toán:</h5>
                        <h5 class="mb-0 text-primary">₫<fmt:formatNumber value="${total}" type="number" maxFractionDigits="0" /></h5>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Chi tiết sản phẩm (OrderDetails) -->
    <div class="card info-card">
        <div class="card-header">
            <i class="fas fa-list me-2"></i>Sản Phẩm Trong Đơn Hàng
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty order.orderDetails}">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>STT</th>
                                <th>Sản phẩm</th>
                                <th>Số lượng</th>
                                <th>Đơn giá (₫)</th>
                                <th>Thành tiền (₫)</th>
                                <th>Trạng thái</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty detail.product}">
                                                ${detail.product.productName}
                                            </c:when>
                                            <c:otherwise>
                                                <em class="text-muted">Sản phẩm không xác định</em>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${detail.quantity}</td>
                                    <td><fmt:formatNumber value="${detail.unitPrice}" type="number" maxFractionDigits="0" /></td>
                                    <td><fmt:formatNumber value="${detail.totalPrice}" type="number" maxFractionDigits="0" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${detail.status eq 'ACTIVE'}">
                                                <span class="badge bg-success">${detail.status}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${detail.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center text-muted py-4">
                        <i class="fas fa-inbox fa-3x mb-3"></i>
                        <p>Không có sản phẩm trong đơn hàng này</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Action buttons -->
    <div class="mt-4 mb-5">
        <a href="${pageContext.request.contextPath}/api/manager/orders" class="btn btn-secondary">
            <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
        </a>
        <button class="btn btn-primary" onclick="window.print()">
            <i class="fas fa-print me-2"></i>In đơn hàng
        </button>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>