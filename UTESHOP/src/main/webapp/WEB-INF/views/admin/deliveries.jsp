<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý giao hàng - Admin</title>
</head>
<body>
    <div class="container-fluid px-4">
        <!-- Page header -->
        <h1 class="mt-4">Quản lý giao hàng</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
            <li class="breadcrumb-item active">Giao hàng</li>
        </ol>

        <!-- Confirmed orders waiting for shipper assignment -->
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-clock me-2"></i>
                Đơn hàng chờ gán shipper
            </div>
            <div class="card-body">
                <c:if test="${empty confirmedOrders}">
                    <div class="alert alert-info mb-0">
                        <i class="fas fa-info-circle me-2"></i>
                        Không có đơn hàng nào chờ gán shipper
                    </div>
                </c:if>

                <c:if test="${not empty confirmedOrders}">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Mã đơn</th>
                                    <th>Khách hàng</th>
                                    <th>Địa chỉ</th>
                                    <th>Số điện thoại</th>
                                    <th>Giá trị</th>
                                    <th>Ngày đặt</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${confirmedOrders}" var="order">
                                    <tr>
                                        <td><strong>#${order.orderID}</strong></td>
                                        <td>${order.recipientName}</td>
                                        <td style="max-width: 200px;" class="text-truncate">
                                            ${order.address.addressDetail}
                                        </td>
                                        <td>${order.phoneNumber}</td>
                                        <td>
                                            <fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" 
                                                              type="currency" currencyCode="VND" />
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${order.orderDate}" 
                                                            pattern="dd/MM/yyyy HH:mm" />
                                        </td>
                                        <td>
                                            <button type="button" class="btn btn-sm btn-primary" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#assignModal"
                                                    data-order-id="${order.orderID}"
                                                    data-order-info="Đơn #${order.orderID} - ${order.recipientName}">
                                                <i class="fas fa-user-plus me-1"></i>Gán shipper
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- All deliveries -->
        <div class="card">
            <div class="card-header">
                <i class="fas fa-truck me-2"></i>
                Tất cả đơn giao hàng
            </div>
            <div class="card-body">
                <c:if test="${empty allDeliveries}">
                    <div class="alert alert-info mb-0">
                        <i class="fas fa-info-circle me-2"></i>
                        Chưa có đơn giao hàng nào
                    </div>
                </c:if>

                <c:if test="${not empty allDeliveries}">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Mã giao hàng</th>
                                    <th>Mã đơn</th>
                                    <th>Shipper</th>
                                    <th>Khách hàng</th>
                                    <th>Trạng thái</th>
                                    <th>Ngày giao</th>
                                    <th>Hoàn thành</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${allDeliveries}" var="delivery">
                                    <tr>
                                        <td><strong>#${delivery.deliveryID}</strong></td>
                                        <td>#${delivery.orderID}</td>
                                        <td>
                                            <i class="fas fa-user-circle me-1"></i>
                                            ${delivery.shipperName}
                                        </td>
                                        <td>${delivery.order.recipientName}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${delivery.deliveryStatus == 'Đang giao hàng'}">
                                                    <span class="badge bg-info">${delivery.deliveryStatus}</span>
                                                </c:when>
                                                <c:when test="${delivery.deliveryStatus == 'Đã giao hàng'}">
                                                    <span class="badge bg-success">${delivery.deliveryStatus}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger">${delivery.deliveryStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${delivery.assignedDate}" 
                                                            pattern="dd/MM/yyyy HH:mm" />
                                        </td>
                                        <td>
                                            <c:if test="${not empty delivery.completedDate}">
                                                <fmt:formatDate value="${delivery.completedDate}" 
                                                                pattern="dd/MM/yyyy HH:mm" />
                                            </c:if>
                                            <c:if test="${empty delivery.completedDate}">
                                                <span class="text-muted">-</span>
                                            </c:if>
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

    <!-- Assign Modal -->
    <div class="modal fade" id="assignModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-user-plus me-2"></i>Gán shipper cho đơn hàng
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/admin/delivery/assign" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="orderId" id="modalOrderId">
                        
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            <span id="modalOrderInfo"></span>
                        </div>

                        <div class="mb-3">
                            <label for="shipperId" class="form-label">Chọn shipper <span class="text-danger">*</span></label>
                            <select class="form-select" id="shipperId" name="shipperId" required>
                                <option value="">-- Chọn shipper --</option>
                                <c:forEach items="${shippers}" var="shipper">
                                    <option value="${shipper.userID}">
                                        ${shipper.fullname} - ${shipper.phone}
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${empty shippers}">
                                <div class="form-text text-danger">
                                    Không có shipper nào trong hệ thống
                                </div>
                            </c:if>
                        </div>

                        <div class="mb-3">
                            <label for="notes" class="form-label">Ghi chú</label>
                            <textarea class="form-control" id="notes" name="notes" rows="3" 
                                      placeholder="Ghi chú cho shipper (nếu có)"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary" ${empty shippers ? 'disabled' : ''}>
                            <i class="fas fa-check me-2"></i>Xác nhận
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Handle assign modal
        var assignModal = document.getElementById('assignModal');
        assignModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget;
            var orderId = button.getAttribute('data-order-id');
            var orderInfo = button.getAttribute('data-order-info');
            
            document.getElementById('modalOrderId').value = orderId;
            document.getElementById('modalOrderInfo').textContent = orderInfo;
        });
    </script>
</body>
</html>

