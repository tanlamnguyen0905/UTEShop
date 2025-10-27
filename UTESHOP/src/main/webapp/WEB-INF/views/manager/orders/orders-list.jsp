<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Danh Sách Đơn Hàng - Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .table th { background-color: #f8f9fa; font-weight: 600; }
        .status-badge { font-size: 0.8em; }
        .alert { border-radius: 8px; }
    </style>
</head>
<body>
<div class="container-fluid px-4">
    <h1 class="mt-4 mb-4 fw-bold text-primary">
        <i class="fas fa-shopping-cart me-2"></i>Danh Sách Đơn Hàng
    </h1>

    <!-- Error -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-triangle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Filter Form -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="GET" class="row g-3">
                <div class="col-md-3">
                    <label>Trạng thái đơn:</label>
                    <select name="orderStatus" class="form-select">
                        <option value="">Tất cả</option>
                        <option value="Đang chờ" ${orderStatus == 'Đang chờ' ? 'selected' : ''}>Đang chờ</option>
                        <option value="Đã xác nhận" ${orderStatus == 'Đã xác nhận' ? 'selected' : ''}>Đã xác nhận</option>
                        <option value="Đã giao" ${orderStatus == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                        <option value="Hoàn thành" ${orderStatus == 'Hoàn thành' ? 'selected' : ''}>Hoàn thành</option>
                        <option value="Đã hủy" ${orderStatus == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label>Trạng thái thanh toán:</label>
                    <select name="paymentStatus" class="form-select">
                        <option value="">Tất cả</option>
                        <option value="Chưa thanh toán" ${paymentStatus == 'Chưa thanh toán' ? 'selected' : ''}>Chưa thanh toán</option>
                        <option value="Đã thanh toán" ${paymentStatus == 'Đã thanh toán' ? 'selected' : ''}>Đã thanh toán</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select name="size" class="form-select">
                        <option value="10" ${pageSize == 10 ? 'selected' : ''}>10 items</option>
                        <option value="20" ${pageSize == 20 ? 'selected' : ''}>20 items</option>
                        <option value="50" ${pageSize == 50 ? 'selected' : ''}>50 items</option>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search me-2"></i>Lọc
                    </button>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <a href="${pageContext.request.contextPath}/api/manager/orders" class="btn btn-secondary w-100">Reset</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Table -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-list me-2"></i>Kết quả (${totalOrders} đơn hàng)
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Khách hàng</th>
                        <th>Ngày đặt</th>
                        <th>Trạng thái đơn</th>
                        <th>Thanh toán</th>
                        <th>Tổng tiền (₫)</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td>${order.orderID}</td>
                            <td>${order.user.username} (${order.recipientName})</td>
                            <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td><span class="badge bg-${order.orderStatus == 'Hoàn thành' ? 'success' : order.orderStatus == 'Đã giao' ? 'info' : order.orderStatus == 'Đã hủy' ? 'danger' : 'warning'} status-badge">${order.orderStatus}</span></td>
                            <td><span class="badge bg-${order.paymentStatus == 'Đã thanh toán' ? 'success' : 'warning'} status-badge">${order.paymentStatus}</span></td>
                            <td><fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" type="number" maxFractionDigits="0" /></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/api/manager/orders/${order.orderID}" class="btn btn-info btn-sm">
                                    <i class="fas fa-eye"></i> Chi tiết
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty orders}">
                        <tr><td colspan="7" class="text-center text-muted">Không có đơn hàng nào.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Pagination">
                    <ul class="pagination justify-content-center">
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&size=${pageSize}&orderStatus=${orderStatus}&paymentStatus=${paymentStatus}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>