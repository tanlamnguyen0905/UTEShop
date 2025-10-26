<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý đơn hàng - Admin</title>
    <style>
        .filter-tabs {
            background: #f8f9fa;
            padding: 0.5rem;
            border-radius: 8px;
            display: inline-flex;
            gap: 0.25rem;
            margin-bottom: 1.5rem;
        }
        .filter-tab {
            padding: 0.75rem 1.5rem;
            border: none;
            background: transparent;
            color: #6c757d;
            font-weight: 500;
            text-decoration: none;
            transition: all 0.3s ease;
            border-radius: 6px;
            display: inline-block;
        }
        .filter-tab:hover {
            background: #e9ecef;
            color: #495057;
        }
        .filter-tab.active {
            background: white;
            color: #0d6efd;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .filter-tab.active.warning {
            color: #ffc107;
        }
        .filter-tab.active.success {
            color: #198754;
        }
        .filter-tab.active.info {
            color: #0dcaf0;
        }
        .filter-tab.active.primary {
            color: #0d6efd;
        }
        .filter-tab.active.danger {
            color: #dc3545;
        }
        .stats-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        .stat-card {
            background: white;
            border-radius: 8px;
            padding: 1rem;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            text-align: center;
            border-left: 4px solid;
        }
        .stat-card.total { border-left-color: #6c757d; }
        .stat-card.pending { border-left-color: #ffc107; }
        .stat-card.confirmed { border-left-color: #198754; }
        .stat-card.delivering { border-left-color: #0dcaf0; }
        .stat-card.delivered { border-left-color: #0d6efd; }
        .stat-card.cancelled { border-left-color: #dc3545; }
        .stat-card .number {
            font-size: 2rem;
            font-weight: bold;
            display: block;
            margin-bottom: 0.25rem;
        }
        .stat-card .label {
            font-size: 0.875rem;
            color: #6c757d;
        }
        .order-row {
            transition: all 0.2s ease;
        }
        .order-row:hover {
            background: #f8f9fa;
        }
        .badge-order-status {
            padding: 0.5rem 1rem;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
    <div class="container-fluid px-4">
        <!-- Page header -->
        <div class="d-flex justify-content-between align-items-center mt-4 mb-4">
            <div>
                <h1 class="h3 mb-0">Quản lý đơn hàng</h1>
                <p class="text-muted mb-0">Xử lý và theo dõi tất cả đơn hàng</p>
            </div>
        </div>

        <!-- Success/Error messages -->
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="success" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.warning}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.warning}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="warning" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session" />
        </c:if>

        <!-- Statistics -->
        <div class="stats-row">
            <div class="stat-card total">
                <span class="number">${totalOrders}</span>
                <span class="label">Tổng đơn</span>
            </div>
            <div class="stat-card pending">
                <span class="number">${pendingCount}</span>
                <span class="label">Đang chờ</span>
            </div>
            <div class="stat-card confirmed">
                <span class="number">${confirmedCount}</span>
                <span class="label">Đã xác nhận</span>
            </div>
            <div class="stat-card delivering">
                <span class="number">${deliveringCount}</span>
                <span class="label">Đang giao</span>
            </div>
            <div class="stat-card delivered">
                <span class="number">${deliveredCount}</span>
                <span class="label">Đã giao</span>
            </div>
            <div class="stat-card cancelled">
                <span class="number">${cancelledCount}</span>
                <span class="label">Đã hủy</span>
            </div>
        </div>

        <!-- Filter tabs -->
        <div class="filter-tabs">
            <a href="${pageContext.request.contextPath}/api/admin/orders" 
               class="filter-tab ${empty currentStatus ? 'active' : ''}">
                Tất cả
            </a>
            <a href="${pageContext.request.contextPath}/api/admin/orders?status=Đang chờ" 
               class="filter-tab ${currentStatus == 'Đang chờ' ? 'active warning' : ''}">
                Đang chờ
            </a>
            <a href="${pageContext.request.contextPath}/api/admin/orders?status=Đã xác nhận" 
               class="filter-tab ${currentStatus == 'Đã xác nhận' ? 'active success' : ''}">
                Đã xác nhận
            </a>
            <a href="${pageContext.request.contextPath}/api/admin/orders?status=Đang giao hàng" 
               class="filter-tab ${currentStatus == 'Đang giao hàng' ? 'active info' : ''}">
                Đang giao
            </a>
            <a href="${pageContext.request.contextPath}/api/admin/orders?status=Đã giao hàng" 
               class="filter-tab ${currentStatus == 'Đã giao hàng' ? 'active primary' : ''}">
                Hoàn thành
            </a>
            <a href="${pageContext.request.contextPath}/api/admin/orders?status=Đã hủy" 
               class="filter-tab ${currentStatus == 'Đã hủy' ? 'active danger' : ''}">
                Đã hủy
            </a>
        </div>

        <!-- Orders table -->
        <div class="card">
            <div class="card-body">
                <c:if test="${empty orders}">
                    <div class="text-center py-5">
                        <i class="fas fa-inbox fa-4x text-muted mb-3"></i>
                        <h5 class="text-muted">Không có đơn hàng nào</h5>
                        <p class="text-muted">
                            <c:choose>
                                <c:when test="${not empty currentStatus}">
                                    Không có đơn hàng với trạng thái "${currentStatus}"
                                </c:when>
                                <c:otherwise>
                                    Chưa có đơn hàng nào trong hệ thống
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </c:if>

                <c:if test="${not empty orders}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>Mã đơn</th>
                                    <th>Khách hàng</th>
                                    <th>Người nhận</th>
                                    <th>Số điện thoại</th>
                                    <th>Địa chỉ</th>
                                    <th>Giá trị</th>
                                    <th>Trạng thái đơn</th>
                                    <th>Thanh toán</th>
                                    <th>Ngày đặt</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${orders}" var="order">
                                    <tr class="order-row">
                                        <td>
                                            <strong class="text-primary">#${order.orderID}</strong>
                                        </td>
                                        <td>
                                            <div>
                                                <div class="fw-semibold">${order.user.fullname}</div>
                                                <small class="text-muted">${order.user.email}</small>
                                            </div>
                                        </td>
                                        <td>
                                            ${order.recipientName}
                                        </td>
                                        <td>
                                            <i class="fas fa-phone text-success me-1"></i>
                                            ${order.phoneNumber}
                                        </td>
                                        <td style="max-width: 250px;">
                                            <div class="text-truncate" title="${order.address.addressDetail}">
                                                <i class="fas fa-map-marker-alt text-danger me-1"></i>
                                                ${order.address.addressDetail}
                                            </div>
                                            <small class="text-muted">
                                                ${order.address.ward}, ${order.address.district}
                                            </small>
                                        </td>
                                        <td>
                                            <strong class="text-primary">
                                                <fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" 
                                                                  type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                            </strong>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${order.orderStatus == 'Đang chờ'}">
                                                    <span class="badge badge-order-status bg-warning text-dark">
                                                        <i class="fas fa-clock me-1"></i>${order.orderStatus}
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus == 'Đã xác nhận'}">
                                                    <span class="badge badge-order-status bg-success">
                                                        <i class="fas fa-check me-1"></i>${order.orderStatus}
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus == 'Đang giao hàng'}">
                                                    <span class="badge badge-order-status bg-info">
                                                        <i class="fas fa-truck me-1"></i>${order.orderStatus}
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus == 'Đã giao hàng'}">
                                                    <span class="badge badge-order-status bg-primary">
                                                        <i class="fas fa-check-circle me-1"></i>${order.orderStatus}
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus == 'Đã hủy'}">
                                                    <span class="badge badge-order-status bg-danger">
                                                        <i class="fas fa-times-circle me-1"></i>${order.orderStatus}
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-order-status bg-secondary">
                                                        ${order.orderStatus}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${order.paymentStatus == 'Đã thanh toán'}">
                                                    <span class="badge badge-order-status bg-success">
                                                        <i class="fas fa-check-circle me-1"></i>Đã thanh toán
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.paymentStatus == 'Chưa thanh toán'}">
                                                    <span class="badge badge-order-status bg-warning text-dark">
                                                        <i class="fas fa-money-bill-wave me-1"></i>Chưa thanh toán
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-order-status bg-secondary">
                                                        ${order.paymentStatus}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <small class="text-muted">
                                                ${order.orderDate.toString().replace('T', ' ').substring(0, 16)}
                                            </small>
                                        </td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <!-- Xác nhận đơn (chỉ hiện khi đang chờ) -->
                                                <c:if test="${order.orderStatus == 'Đang chờ'}">
                                                    <form action="${pageContext.request.contextPath}/api/admin/order/confirm" 
                                                          method="post" style="display: inline;">
                                                        <input type="hidden" name="orderId" value="${order.orderID}">
                                                        <button type="submit" class="btn btn-sm btn-success" 
                                                                title="Xác nhận đơn hàng">
                                                            <i class="fas fa-check"></i>
                                                        </button>
                                                    </form>
                                                </c:if>
                                                
                                                <!-- Chi tiết -->
                                                <a href="${pageContext.request.contextPath}/api/admin/order/detail?id=${order.orderID}" 
                                                   class="btn btn-sm btn-outline-primary" title="Xem chi tiết">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                
                                                <!-- Hủy đơn (không hiện nếu đã giao hoặc đã hủy) -->
                                                <c:if test="${order.orderStatus != 'Đã giao hàng' && order.orderStatus != 'Đã hủy'}">
                                                    <button type="button" class="btn btn-sm btn-danger" 
                                                            onclick="cancelOrder(${order.orderID})" title="Hủy đơn hàng">
                                                        <i class="fas fa-times"></i>
                                                    </button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Cancel Order Modal -->
    <div class="modal fade" id="cancelModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle me-2"></i>Hủy đơn hàng
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="cancelForm" method="post">
                    <div class="modal-body">
                        <p class="mb-3">Bạn có chắc chắn muốn hủy đơn hàng này?</p>
                        <div class="mb-3">
                            <label for="cancelReason" class="form-label">Lý do hủy</label>
                            <textarea class="form-control" id="cancelReason" name="reason" 
                                      rows="3" placeholder="Nhập lý do hủy đơn..."></textarea>
                        </div>
                        <p class="text-danger mb-0">
                            <i class="fas fa-info-circle me-1"></i>
                            Hành động này không thể hoàn tác!
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Đóng
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times me-2"></i>Hủy đơn hàng
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function cancelOrder(orderId) {
            const form = document.getElementById('cancelForm');
            form.action = '${pageContext.request.contextPath}/api/admin/order/cancel?orderId=' + orderId;
            const modal = new bootstrap.Modal(document.getElementById('cancelModal'));
            modal.show();
        }
    </script>
</body>
</html>

