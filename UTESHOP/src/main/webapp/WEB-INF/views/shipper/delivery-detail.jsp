<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết đơn giao hàng - Shipper</title>
    <style>
        .timeline {
            position: relative;
            padding: 20px 0;
        }
        .timeline-item {
            position: relative;
            padding-left: 50px;
            margin-bottom: 30px;
        }
        .timeline-item:before {
            content: '';
            position: absolute;
            left: 15px;
            top: 30px;
            bottom: -30px;
            width: 2px;
            background: #dee2e6;
        }
        .timeline-item:last-child:before {
            display: none;
        }
        .timeline-icon {
            position: absolute;
            left: 0;
            top: 0;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
        }
        .product-image {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <!-- Back button and header -->
        <div class="mb-4">
            <a href="${pageContext.request.contextPath}/shipper/deliveries" class="btn btn-outline-secondary mb-3">
                <i class="fas fa-arrow-left me-2"></i>Quay lại
            </a>
            <h2 class="fw-bold">
                <i class="fas fa-box me-2"></i>Chi tiết đơn giao hàng #${delivery.deliveryID}
            </h2>
        </div>

        <div class="row g-4">
            <!-- Left column -->
            <div class="col-lg-8">
                <!-- Order info -->
                <div class="card mb-4">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-info-circle me-2"></i>Thông tin đơn hàng
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="text-muted small">Mã đơn hàng</label>
                                <div class="fw-bold">#${delivery.order.orderID}</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="text-muted small">Ngày đặt hàng</label>
                                <div>
                                    <fmt:formatDate value="${delivery.order.orderDate}" 
                                                    pattern="dd/MM/yyyy HH:mm" />
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="text-muted small">Trạng thái đơn hàng</label>
                                <div>
                                        <c:choose>
                                        <c:when test="${delivery.deliveryStatus == 'Đang giao hàng'}">
                                            <span class="badge bg-info text-white">
                                                <i class="fas fa-truck me-1"></i>${delivery.deliveryStatus}
                                            </span>
                                        </c:when>
                                        <c:when test="${delivery.deliveryStatus == 'Đã giao hàng'}">
                                            <span class="badge bg-success">
                                                <i class="fas fa-check-circle me-1"></i>${delivery.deliveryStatus}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">
                                                <i class="fas fa-times-circle me-1"></i>${delivery.deliveryStatus}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="text-muted small">Thanh toán</label>
                                <div>
                                    <span class="badge ${delivery.order.paymentStatus == 'Đã thanh toán' ? 'bg-success' : 'bg-warning text-dark'}">
                                        ${delivery.order.paymentStatus}
                                    </span>
                                    <small class="text-muted ms-2">${delivery.order.paymentMethod.methodName}</small>
                                </div>
                            </div>
                        </div>

                        <!-- Customer info -->
                        <hr>
                        <h6 class="fw-bold mb-3">
                            <i class="fas fa-user me-2"></i>Thông tin khách hàng
                        </h6>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="text-muted small">Người nhận</label>
                                <div class="fw-semibold">${delivery.order.recipientName}</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="text-muted small">Số điện thoại</label>
                                <div>
                                    <a href="tel:${delivery.order.phoneNumber}" class="text-decoration-none">
                                        <i class="fas fa-phone me-1"></i>${delivery.order.phoneNumber}
                                    </a>
                                </div>
                            </div>
                            <div class="col-12 mb-3">
                                <label class="text-muted small">Địa chỉ giao hàng</label>
                                <div class="fw-semibold">
                                    <i class="fas fa-map-marker-alt me-2 text-danger"></i>
                                    ${delivery.order.address.addressDetail}, 
                                    ${delivery.order.address.ward}, 
                                    ${delivery.order.address.district}, 
                                    ${delivery.order.address.city}
                                </div>
                                <a href="https://www.google.com/maps/search/?api=1&query=${delivery.order.address.addressDetail}" 
                                   target="_blank" class="btn btn-sm btn-outline-primary mt-2">
                                    <i class="fas fa-map me-1"></i>Xem bản đồ
                                </a>
                            </div>
                            <c:if test="${not empty delivery.order.notes}">
                                <div class="col-12">
                                    <label class="text-muted small">Ghi chú</label>
                                    <div class="alert alert-info mb-0">
                                        <i class="fas fa-sticky-note me-2"></i>${delivery.order.notes}
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

                <!-- Products -->
                <div class="card">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-shopping-bag me-2"></i>Sản phẩm trong đơn hàng
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-center">Số lượng</th>
                                        <th class="text-end">Đơn giá</th>
                                        <th class="text-end">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${delivery.order.orderDetails}" var="detail">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <c:if test="${not empty detail.product.images}">
                                                        <img src="${pageContext.request.contextPath}${detail.product.images[0].imageUrl}" 
                                                             alt="${detail.product.productName}" 
                                                             class="product-image me-3">
                                                    </c:if>
                                                    <div>
                                                        <div class="fw-semibold">${detail.product.productName}</div>
                                                        <small class="text-muted">${detail.product.category.categoryName}</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center">
                                                <span class="badge bg-light text-dark">x${detail.quantity}</span>
                                            </td>
                                            <td class="text-end">
                                                <fmt:formatNumber value="${detail.unitPrice}" 
                                                                  type="currency" currencyCode="VND" />
                                            </td>
                                            <td class="text-end fw-semibold">
                                                <fmt:formatNumber value="${detail.quantity * detail.unitPrice}" 
                                                                  type="currency" currencyCode="VND" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot class="table-light">
                                    <tr>
                                        <td colspan="3" class="text-end">Tạm tính:</td>
                                        <td class="text-end fw-semibold">
                                            <fmt:formatNumber value="${delivery.order.amount}" 
                                                              type="currency" currencyCode="VND" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" class="text-end">Phí vận chuyển:</td>
                                        <td class="text-end fw-semibold">
                                            <fmt:formatNumber value="${delivery.order.shippingFee}" 
                                                              type="currency" currencyCode="VND" />
                                        </td>
                                    </tr>
                                    <c:if test="${delivery.order.totalDiscount > 0}">
                                        <tr>
                                            <td colspan="3" class="text-end">Giảm giá:</td>
                                            <td class="text-end fw-semibold text-danger">
                                                -<fmt:formatNumber value="${delivery.order.totalDiscount}" 
                                                                   type="currency" currencyCode="VND" />
                                            </td>
                                        </tr>
                                    </c:if>
                                    <tr class="table-success">
                                        <td colspan="3" class="text-end fw-bold">Tổng cộng:</td>
                                        <td class="text-end fw-bold fs-5">
                                            <fmt:formatNumber value="${delivery.order.amount + delivery.order.shippingFee - delivery.order.totalDiscount}" 
                                                              type="currency" currencyCode="VND" />
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right column -->
            <div class="col-lg-4">
                <!-- Actions -->
                <div class="card mb-4">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-tasks me-2"></i>Thao tác
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${delivery.deliveryStatus == 'Đang giao hàng'}">
                                <form action="${pageContext.request.contextPath}/shipper/delivery/complete" method="post">
                                    <input type="hidden" name="deliveryId" value="${delivery.deliveryID}">
                                    <button type="submit" class="btn btn-success w-100 mb-3">
                                        <i class="fas fa-check-circle me-2"></i>Giao hàng thành công
                                    </button>
                                </form>

                                <button type="button" class="btn btn-danger w-100" data-bs-toggle="modal" data-bs-target="#failModal">
                                    <i class="fas fa-times-circle me-2"></i>Báo giao hàng thất bại
                                </button>
                            </c:when>

                            <c:otherwise>
                                <div class="alert alert-info mb-0">
                                    <i class="fas fa-info-circle me-2"></i>
                                    Đơn hàng đã hoàn tất
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Timeline -->
                <div class="card">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold">
                            <i class="fas fa-history me-2"></i>Lịch sử giao hàng
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="timeline">
                            <c:if test="${not empty delivery.completedDate}">
                                <div class="timeline-item">
                                    <div class="timeline-icon bg-success text-white">
                                        <i class="fas fa-check"></i>
                                    </div>
                                    <div>
                                        <div class="fw-semibold">Đã giao hàng</div>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${delivery.completedDate}" 
                                                            pattern="dd/MM/yyyy HH:mm" />
                                        </small>
                                    </div>
                                </div>
                            </c:if>

                            <div class="timeline-item">
                                <div class="timeline-icon bg-primary text-white">
                                    <i class="fas fa-user"></i>
                                </div>
                                <div>
                                    <div class="fw-semibold">Đã giao cho shipper</div>
                                    <small class="text-muted">
                                        <fmt:formatDate value="${delivery.assignedDate}" 
                                                        pattern="dd/MM/yyyy HH:mm" />
                                    </small>
                                </div>
                            </div>

                            <div class="timeline-item">
                                <div class="timeline-icon bg-secondary text-white">
                                    <i class="fas fa-shopping-cart"></i>
                                </div>
                                <div>
                                    <div class="fw-semibold">Đơn hàng được tạo</div>
                                    <small class="text-muted">
                                        <fmt:formatDate value="${delivery.order.orderDate}" 
                                                        pattern="dd/MM/yyyy HH:mm" />
                                    </small>
                                </div>
                            </div>
                        </div>

                        <c:if test="${not empty delivery.notes}">
                            <hr>
                            <div>
                                <label class="text-muted small">Ghi chú:</label>
                                <div>${delivery.notes}</div>
                            </div>
                        </c:if>

                        <c:if test="${not empty delivery.failureReason}">
                            <hr>
                            <div class="alert alert-danger mb-0">
                                <strong>Lý do thất bại:</strong><br>
                                ${delivery.failureReason}
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Fail Modal -->
    <div class="modal fade" id="failModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                        Báo giao hàng thất bại
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/shipper/delivery/fail" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="deliveryId" value="${delivery.deliveryID}">
                        <div class="mb-3">
                            <label for="failureReason" class="form-label">Lý do thất bại <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="failureReason" name="failureReason" 
                                      rows="4" required 
                                      placeholder="Ví dụ: Không liên lạc được với khách hàng, địa chỉ không chính xác, khách hủy đơn..."></textarea>
                        </div>
                        <div class="alert alert-warning">
                            <i class="fas fa-info-circle me-2"></i>
                            Vui lòng mô tả rõ lý do để hỗ trợ xử lý đơn hàng tốt hơn.
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times-circle me-2"></i>Xác nhận thất bại
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>

