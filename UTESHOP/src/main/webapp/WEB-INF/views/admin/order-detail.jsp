<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết đơn hàng #${order.orderID}</title>
    <style>
        .detail-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            padding: 2rem;
            margin-bottom: 1.5rem;
        }
        .detail-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-bottom: 1.5rem;
            border-bottom: 2px solid #e9ecef;
            margin-bottom: 1.5rem;
        }
        .detail-section {
            margin-bottom: 2rem;
        }
        .detail-section h5 {
            color: #495057;
            font-weight: 600;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid #f8f9fa;
        }
        .info-row {
            display: flex;
            padding: 0.75rem 0;
            border-bottom: 1px solid #f8f9fa;
        }
        .info-label {
            width: 200px;
            font-weight: 500;
            color: #6c757d;
        }
        .info-value {
            flex: 1;
            color: #212529;
        }
        .product-table {
            width: 100%;
            margin-top: 1rem;
        }
        .product-table th {
            background: #f8f9fa;
            padding: 1rem;
            text-align: left;
            font-weight: 600;
            color: #495057;
        }
        .product-table td {
            padding: 1rem;
            border-bottom: 1px solid #f8f9fa;
        }
        .product-img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
        }
        .summary-box {
            background: #f8f9fa;
            padding: 1.5rem;
            border-radius: 8px;
            margin-top: 2rem;
        }
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 0.5rem 0;
        }
        .summary-row.total {
            font-size: 1.25rem;
            font-weight: bold;
            padding-top: 1rem;
            border-top: 2px solid #dee2e6;
            margin-top: 1rem;
            color: #0d6efd;
        }
        .status-badge {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.9rem;
        }
        .timeline {
            position: relative;
            padding-left: 2rem;
        }
        .timeline-item {
            position: relative;
            padding-bottom: 1.5rem;
        }
        .timeline-item:before {
            content: '';
            position: absolute;
            left: -2rem;
            top: 0.5rem;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: #0d6efd;
            border: 3px solid white;
            box-shadow: 0 0 0 2px #0d6efd;
        }
        .timeline-item:after {
            content: '';
            position: absolute;
            left: -1.55rem;
            top: 1.5rem;
            width: 2px;
            height: calc(100% - 1rem);
            background: #dee2e6;
        }
        .timeline-item:last-child:after {
            display: none;
        }
        .action-buttons {
            display: flex;
            gap: 0.5rem;
            margin-top: 2rem;
        }
    </style>
</head>
<body>
    <div class="container-fluid px-4">
        <!-- Back button -->
        <div class="mt-4 mb-3">
            <a href="${pageContext.request.contextPath}/api/admin/orders" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
            </a>
        </div>

        <!-- Order Header -->
        <div class="detail-container">
            <div class="detail-header">
                <div>
                    <h2 class="mb-2">Đơn hàng #${order.orderID}</h2>
                    <p class="text-muted mb-0">
                        <i class="far fa-calendar me-2"></i>
                        ${order.orderDate.toString().replace('T', ' ').substring(0, 16)}
                    </p>
                </div>
                <div class="text-end">
                    <c:choose>
                        <c:when test="${order.orderStatus == 'Đang chờ'}">
                            <span class="status-badge bg-warning text-dark">
                                <i class="fas fa-clock me-1"></i>${order.orderStatus}
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus == 'Đã xác nhận'}">
                            <span class="status-badge bg-success text-white">
                                <i class="fas fa-check me-1"></i>${order.orderStatus}
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus == 'Đang giao hàng'}">
                            <span class="status-badge bg-info text-white">
                                <i class="fas fa-truck me-1"></i>${order.orderStatus}
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus == 'Đã giao hàng'}">
                            <span class="status-badge bg-primary text-white">
                                <i class="fas fa-check-circle me-1"></i>${order.orderStatus}
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus == 'Đã hủy'}">
                            <span class="status-badge bg-danger text-white">
                                <i class="fas fa-times-circle me-1"></i>${order.orderStatus}
                            </span>
                        </c:when>
                    </c:choose>
                    <div class="mt-2">
                        <c:choose>
                            <c:when test="${order.paymentStatus == 'Đã thanh toán'}">
                                <span class="badge bg-success">
                                    <i class="fas fa-check-circle me-1"></i>Đã thanh toán
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning text-dark">
                                    <i class="fas fa-money-bill-wave me-1"></i>Chưa thanh toán
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Customer Information -->
            <div class="detail-section">
                <h5><i class="fas fa-user me-2"></i>Thông tin khách hàng</h5>
                <div class="info-row">
                    <div class="info-label">Họ tên:</div>
                    <div class="info-value">${order.user.fullname}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Email:</div>
                    <div class="info-value">${order.user.email}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Số điện thoại:</div>
                    <div class="info-value">${order.user.phone}</div>
                </div>
            </div>

            <!-- Delivery Information -->
            <div class="detail-section">
                <h5><i class="fas fa-shipping-fast me-2"></i>Thông tin giao hàng</h5>
                <div class="info-row">
                    <div class="info-label">Người nhận:</div>
                    <div class="info-value">${order.recipientName}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Số điện thoại:</div>
                    <div class="info-value">${order.phoneNumber}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Địa chỉ:</div>
                    <div class="info-value">
                        ${order.address.addressDetail}<br>
                        <small class="text-muted">${order.address.ward}, ${order.address.district}</small>
                    </div>
                </div>
                <c:if test="${not empty order.notes}">
                    <div class="info-row">
                        <div class="info-label">Ghi chú:</div>
                        <div class="info-value">${order.notes}</div>
                    </div>
                </c:if>
            </div>

            <!-- Products -->
            <div class="detail-section">
                <h5><i class="fas fa-box me-2"></i>Sản phẩm</h5>
                <table class="product-table">
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th>Đơn giá</th>
                            <th>Số lượng</th>
                            <th class="text-end">Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${order.orderDetails}" var="detail">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center gap-3">
                                        <c:choose>
                                            <c:when test="${not empty detail.product.images and not empty detail.product.images[0]}">
                                                <img src="${pageContext.request.contextPath}/assets/uploads/product/${detail.product.images[0].dirImage}" 
                                                     class="product-img" alt="${detail.product.productName}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                     class="product-img" alt="No image">
                                            </c:otherwise>
                                        </c:choose>
                                        <div>
                                            <div class="fw-semibold">${detail.product.productName}</div>
                                            <small class="text-muted">SKU: ${detail.product.productID}</small>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${detail.product.unitPrice}" 
                                                      type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </td>
                                <td>
                                    <span class="badge bg-secondary">${detail.quantity}</span>
                                </td>
                                <td class="text-end fw-semibold">
                                    <fmt:formatNumber value="${detail.product.unitPrice * detail.quantity}" 
                                                      type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Order Summary -->
                <div class="summary-box">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span>
                            <fmt:formatNumber value="${order.amount}" 
                                              type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </span>
                    </div>
                    <div class="summary-row">
                        <span>Phí vận chuyển:</span>
                        <span>
                            <fmt:formatNumber value="${order.shippingFee}" 
                                              type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </span>
                    </div>
                    <c:if test="${order.totalDiscount > 0}">
                        <div class="summary-row text-success">
                            <span>
                                <i class="fas fa-tag me-1"></i>Giảm giá
                                <c:if test="${not empty order.voucher}">
                                    (${order.voucher.codeVoucher})
                                </c:if>
                            </span>
                            <span>
                                - <fmt:formatNumber value="${order.totalDiscount}" 
                                                    type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>
                    </c:if>
                    <div class="summary-row total">
                        <span>Tổng cộng:</span>
                        <span>
                            <fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" 
                                              type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </span>
                    </div>
                </div>
            </div>

            <!-- Payment Information -->
            <div class="detail-section">
                <h5><i class="fas fa-credit-card me-2"></i>Thanh toán</h5>
                <div class="info-row">
                    <div class="info-label">Phương thức:</div>
                    <div class="info-value">
                        <c:choose>
                            <c:when test="${not empty order.paymentMethod}">
                                ${order.paymentMethod.name}
                            </c:when>
                            <c:otherwise>
                                Chưa chọn
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="info-row">
                    <div class="info-label">Trạng thái:</div>
                    <div class="info-value">
                        <c:choose>
                            <c:when test="${order.paymentStatus == 'Đã thanh toán'}">
                                <span class="badge bg-success">
                                    <i class="fas fa-check-circle me-1"></i>Đã thanh toán
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning text-dark">
                                    <i class="fas fa-money-bill-wave me-1"></i>Chưa thanh toán
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <c:if test="${order.orderStatus == 'Đang chờ'}">
                    <form action="${pageContext.request.contextPath}/api/admin/order/confirm" method="post" style="display: inline;">
                        <input type="hidden" name="orderId" value="${order.orderID}">
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-2"></i>Xác nhận đơn hàng
                        </button>
                    </form>
                </c:if>
                
                <c:if test="${order.orderStatus != 'Đã giao hàng' && order.orderStatus != 'Đã hủy'}">
                    <button type="button" class="btn btn-danger" onclick="showCancelModal()">
                        <i class="fas fa-times me-2"></i>Hủy đơn hàng
                    </button>
                </c:if>
                
                <button type="button" class="btn btn-primary" onclick="window.print()">
                    <i class="fas fa-print me-2"></i>In đơn hàng
                </button>
            </div>
        </div>
    </div>

    <!-- Cancel Modal -->
    <div class="modal fade" id="cancelModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle me-2"></i>Hủy đơn hàng
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/api/admin/order/cancel?orderId=${order.orderID}" method="post">
                    <div class="modal-body">
                        <p class="mb-3">Bạn có chắc chắn muốn hủy đơn hàng #${order.orderID}?</p>
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
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times me-2"></i>Hủy đơn hàng
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function showCancelModal() {
            const modal = new bootstrap.Modal(document.getElementById('cancelModal'));
            modal.show();
        }
    </script>
</body>
</html>

