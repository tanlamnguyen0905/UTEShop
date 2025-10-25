<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đơn giao hàng - Shipper</title>
</head>
<body>
    <div class="container-fluid">
        <!-- Page header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">
                <i class="fas fa-box me-2"></i>Đơn giao hàng
            </h2>
            
            <!-- Filter buttons -->
            <div class="btn-group" role="group">
                <a href="$#" 
                   class="btn ${empty currentStatus ? 'btn-primary' : 'btn-outline-primary'}">
                    Tất cả
                </a>
                <a href="#" 
                   class="btn ${currentStatus == 'Đang giao hàng' ? 'btn-info' : 'btn-outline-info'}">
                    Đang giao
                </a>
                <a href="#" 
                   class="btn ${currentStatus == 'Đã giao hàng' ? 'btn-success' : 'btn-outline-success'}">
                    Hoàn thành
                </a>
                <a href="#" 
                   class="btn ${currentStatus == 'Giao hàng thất bại' ? 'btn-danger' : 'btn-outline-danger'}">
                    Thất bại
                </a>
            </div>
        </div>

        <!-- Deliveries list -->
        <div class="card">
            <div class="card-body">
                <c:if test="${empty deliveries}">
                    <div class="text-center py-5">
                        <i class="fas fa-box-open fa-4x text-muted mb-3"></i>
                        <p class="text-muted">Không có đơn hàng nào</p>
                    </div>
                </c:if>

                <c:if test="${not empty deliveries}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>Mã đơn</th>
                                    <th>Khách hàng</th>
                                    <th>Địa chỉ</th>
                                    <th>Số điện thoại</th>
                                    <th>Giá trị đơn</th>
                                    <th>Trạng thái</th>
                                    <th>Ngày giao</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${deliveries}" var="delivery">
                                    <tr>
                                        <td>
                                            <strong>#${delivery.order.orderID}</strong>
                                        </td>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-user-circle fa-2x text-muted me-2"></i>
                                                <div>
                                                    <div class="fw-semibold">${delivery.order.recipientName}</div>
                                                    <small class="text-muted">${delivery.order.user.email}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td style="max-width: 250px;">
                                            <div class="text-truncate" title="${delivery.order.address.addressDetail}">
                                                ${delivery.order.address.addressDetail}
                                            </div>
                                            <small class="text-muted">
                                                ${delivery.order.address.ward}, ${delivery.order.address.district}, ${delivery.order.address.city}
                                            </small>
                                        </td>
                                        <td>
                                            <i class="fas fa-phone me-1"></i>${delivery.order.phoneNumber}
                                        </td>
                                        <td>
                                            <strong>
                                                <fmt:formatNumber value="${delivery.order.amount + delivery.order.shippingFee - delivery.order.totalDiscount}" 
                                                                  type="currency" currencyCode="VND" />
                                            </strong>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${delivery.deliveryStatus == 'Đang giao hàng'}">
                                                    <span class="badge badge-status bg-info text-white">
                                                        <i class="fas fa-truck me-1"></i>${delivery.deliveryStatus}
                                                    </span>
                                                </c:when>
                                                <c:when test="${delivery.deliveryStatus == 'Đã giao hàng'}">
                                                    <span class="badge badge-status bg-success">
                                                        <i class="fas fa-check-circle me-1"></i>${delivery.deliveryStatus}
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-status bg-danger">
                                                        <i class="fas fa-times-circle me-1"></i>${delivery.deliveryStatus}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
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
</body>
</html>

