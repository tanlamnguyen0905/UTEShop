<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Shipper</title>
    <style>
        .stat-card {
            border-left: 4px solid;
            transition: all 0.3s ease;
        }
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .stat-card.primary { border-left-color: #0d6efd; }
        .stat-card.success { border-left-color: #198754; }
        .stat-card.warning { border-left-color: #ffc107; }
        .order-card {
            transition: all 0.2s ease;
            border: 1px solid #e0e0e0;
        }
        .order-card:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            border-color: #0d6efd;
        }
        .badge-status {
            padding: 0.5rem 1rem;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <!-- Page header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">
                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
            </h2>
            <div class="text-muted">
                <i class="far fa-calendar-alt me-1"></i>
                <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm" />
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
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session" />
        </c:if>

        <!-- Statistics cards -->
        <div class="row g-4 mb-4">
            <div class="col-xl-4 col-md-6">
                <div class="card stat-card warning">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-2">Đơn hàng có sẵn</h6>
                                <h2 class="mb-0 fw-bold">${stats.pendingCount}</h2>
                                <small class="text-muted">Chưa có shipper nhận</small>
                            </div>
                            <div class="text-warning">
                                <i class="fas fa-box fa-3x opacity-50"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-xl-4 col-md-6">
                <div class="card stat-card primary">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-2">Đang giao</h6>
                                <h2 class="mb-0 fw-bold">${stats.deliveringCount}</h2>
                                <small class="text-muted">Đơn hàng của bạn</small>
                            </div>
                            <div class="text-primary">
                                <i class="fas fa-truck fa-3x opacity-50"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-xl-4 col-md-6">
                <div class="card stat-card success">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-2">Đã hoàn thành</h6>
                                <h2 class="mb-0 fw-bold">${stats.completedCount}</h2>
                                <small class="text-muted">Tổng cộng</small>
                            </div>
                            <div class="text-success">
                                <i class="fas fa-check-circle fa-3x opacity-50"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Pending Orders (Available to accept) -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-warning text-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-box me-2"></i>Đơn hàng có sẵn
                            <span class="badge bg-light text-warning ms-2">${stats.pendingCount}</span>
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty pendingOrders}">
                            <div class="text-center py-5">
                                <i class="fas fa-box-open fa-4x text-muted mb-3"></i>
                                <p class="text-muted">Không có đơn hàng nào đang chờ</p>
                            </div>
                        </c:if>

                        <c:if test="${not empty pendingOrders}">
                            <div class="row g-3">
                                <c:forEach items="${pendingOrders}" var="order">
                                    <div class="col-md-6 col-lg-4">
                                        <div class="card order-card h-100">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-start mb-3">
                                                    <div>
                                                        <h6 class="mb-1">Đơn hàng #${order.orderID}</h6>
                                                        <small class="text-muted">
                                                            <i class="far fa-clock me-1"></i>
                                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM HH:mm" />
                                                        </small>
                                                    </div>
                                                    <span class="badge bg-success">Đã xác nhận</span>
                                                </div>
                                                
                                                <div class="mb-2">
                                                    <i class="fas fa-user text-primary me-2"></i>
                                                    <strong>${order.recipientName}</strong>
                                                </div>
                                                
                                                <div class="mb-2 text-truncate" title="${order.address.addressDetail}">
                                                    <i class="fas fa-map-marker-alt text-danger me-2"></i>
                                                    <small>${order.address.addressDetail}</small>
                                                </div>
                                                
                                                <div class="mb-3">
                                                    <i class="fas fa-phone text-success me-2"></i>
                                                    <small>${order.phoneNumber}</small>
                                                </div>
                                                
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div class="text-primary fw-bold">
                                                        <fmt:formatNumber value="${order.amount}" type="currency" 
                                                                         currencySymbol="₫" maxFractionDigits="0"/>
                                                    </div>
                                                    <form action="${pageContext.request.contextPath}/api/shipper/order/accept" 
                                                          method="post" style="display: inline;">
                                                        <input type="hidden" name="orderId" value="${order.orderID}">
                                                        <button type="submit" class="btn btn-warning btn-sm">
                                                            <i class="fas fa-hand-paper me-1"></i>Nhận đơn
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Active deliveries (My orders) -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-primary text-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-truck me-2"></i>Đơn hàng đang giao của bạn
                            <span class="badge bg-light text-primary ms-2">${stats.deliveringCount}</span>
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty activeDeliveries}">
                            <div class="text-center py-5">
                                <i class="fas fa-truck fa-4x text-muted mb-3"></i>
                                <p class="text-muted">Bạn chưa có đơn hàng nào đang giao</p>
                            </div>
                        </c:if>

                        <c:if test="${not empty activeDeliveries}">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Khách hàng</th>
                                            <th>Địa chỉ</th>
                                            <th>Số điện thoại</th>
                                            <th>Trạng thái</th>
                                            <th>Thời gian</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${activeDeliveries}" var="delivery">
                                            <tr>
                                                <td>
                                                    <strong>#${delivery.orderID}</strong>
                                                </td>
                                                <td>${delivery.recipientName}</td>
                                                <td class="text-truncate" style="max-width: 200px;">
                                                    ${delivery.addressDetail}
                                                </td>
                                                <td>${delivery.phoneNumber}</td>
                                                <td>
                                                    <span class="badge badge-status bg-primary">
                                                        ${delivery.deliveryStatus}
                                                    </span>
                                                </td>
                                                <td>
                                                    <small>${delivery.assignedDate.toString().replace('T', ' ').substring(0, 16)}</small>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/api/shipper/delivery/detail?id=${delivery.deliveryID}" 
                                                       class="btn btn-sm btn-outline-primary">
                                                        <i class="fas fa-eye"></i> Chi tiết
                                                    </a>
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
        </div>
    </div>
</body>
</html>
