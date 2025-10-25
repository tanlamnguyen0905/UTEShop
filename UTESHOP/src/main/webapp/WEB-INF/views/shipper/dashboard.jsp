<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Shipper</title>
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

        <!-- Statistics cards -->
        <div class="row g-4 mb-4">
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card primary">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-2">Tổng đơn hàng</h6>
                                <h2 class="mb-0 fw-bold">${stats.totalDeliveries}</h2>
                            </div>
                            <div class="text-primary">
                                <i class="fas fa-box fa-3x opacity-50"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-xl-3 col-md-6">
                <div class="card stat-card primary">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-2">Đang giao</h6>
                                <h2 class="mb-0 fw-bold">${stats.deliveringCount}</h2>
                            </div>
                            <div class="text-primary">
                                <i class="fas fa-truck fa-3x opacity-50"></i>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/shipper/deliveries?status=Đang giao hàng" 
                           class="stretched-link text-decoration-none"></a>
                    </div>
                </div>
            </div>

            <div class="col-xl-3 col-md-6">
                <div class="card stat-card success">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-2">Đã hoàn thành</h6>
                                <h2 class="mb-0 fw-bold">${stats.completedCount}</h2>
                            </div>
                            <div class="text-success">
                                <i class="fas fa-check-circle fa-3x opacity-50"></i>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/shipper/deliveries?status=Đã giao hàng" 
                           class="stretched-link text-decoration-none"></a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Active deliveries -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-truck me-2"></i>Đơn hàng đang giao
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty activeDeliveries}">
                            <div class="text-center py-5">
                                <i class="fas fa-box-open fa-4x text-muted mb-3"></i>
                                <p class="text-muted">Không có đơn hàng đang giao</p>
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
                                                    <strong>#${delivery.order.orderID}</strong>
                                                </td>
                                                <td>${delivery.order.recipientName}</td>
                                                <td class="text-truncate" style="max-width: 200px;">
                                                    ${delivery.order.address.addressDetail}
                                                </td>
                                                <td>${delivery.order.phoneNumber}</td>
                                                <td>
                                                    <span class="badge badge-status bg-primary">
                                                        ${delivery.deliveryStatus}
                                                    </span>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${delivery.assignedDate}" 
                                                                    pattern="dd/MM/yyyy HH:mm" />
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/shipper/delivery/detail?id=${delivery.deliveryID}" 
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

