<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết đơn giao hàng - Shipper</title>
    <style>
        .detail-card {
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border: none;
            margin-bottom: 20px;
            overflow: hidden;
        }
        
        .detail-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
        }
        
        .detail-body {
            padding: 25px;
        }
        
        .info-section {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .info-section h5 {
            color: #667eea;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            color: #6c757d;
            font-weight: 500;
        }
        
        .info-value {
            font-weight: 600;
            text-align: right;
        }
        
        .product-table {
            width: 100%;
            margin-top: 15px;
        }
        
        .product-table th {
            background: #667eea;
            color: white;
            padding: 12px;
            font-weight: 600;
        }
        
        .product-table td {
            padding: 12px;
            border-bottom: 1px solid #e9ecef;
        }
        
        .product-img-detail {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
        }
        
        .status-badge {
            padding: 10px 20px;
            border-radius: 25px;
            font-size: 1rem;
            font-weight: 600;
            display: inline-block;
        }
        
        .timeline {
            position: relative;
            padding: 20px 0;
        }
        
        .timeline-item {
            display: flex;
            margin-bottom: 20px;
            position: relative;
        }
        
        .timeline-marker {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: #667eea;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            flex-shrink: 0;
        }
        
        .timeline-content {
            flex-grow: 1;
            padding: 10px 15px;
            background: #f8f9fa;
            border-radius: 10px;
        }
        
        .btn-action-large {
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: 600;
            font-size: 1rem;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <!-- Back Button -->
        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/api/shipper/my-deliveries" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>

        <div class="row">
            <div class="col-lg-8">
                <!-- Delivery Info Card -->
                <div class="detail-card">
                    <div class="detail-header">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h3 class="mb-2">
                                    <i class="fas fa-shipping-fast"></i> 
                                    Đơn giao hàng #${delivery.deliveryID}
                                </h3>
                                <p class="mb-0">Đơn hàng #${delivery.order.orderID}</p>
                            </div>
                            <span class="status-badge 
                                ${delivery.deliveryStatus == 'Đang giao hàng' ? 'bg-warning text-dark' : ''}
                                ${delivery.deliveryStatus == 'Đã giao hàng' ? 'bg-success text-white' : ''}
                                ${delivery.deliveryStatus == 'Giao hàng thất bại' ? 'bg-danger text-white' : ''}">
                                ${delivery.deliveryStatus}
                            </span>
                        </div>
                    </div>
                    
                    <div class="detail-body">
                        <!-- Customer Information -->
                        <div class="info-section">
                            <h5><i class="fas fa-user-circle"></i> Thông tin khách hàng</h5>
                            <div class="info-row">
                                <span class="info-label">Họ tên:</span>
                                <span class="info-value">${delivery.order.recipientName}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Số điện thoại:</span>
                                <span class="info-value">
                                    <a href="tel:${delivery.order.phoneNumber}">
                                        <i class="fas fa-phone"></i> ${delivery.order.phoneNumber}
                                    </a>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Địa chỉ:</span>
                                <span class="info-value text-end" style="max-width: 60%;">
                                    ${delivery.order.address.addressDetail}, ${delivery.order.address.ward}, 
                                    ${delivery.order.address.district}, ${delivery.order.address.province}
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Ghi chú:</span>
                                <span class="info-value text-end" style="max-width: 60%;">
                                    ${not empty delivery.order.notes ? delivery.order.notes : 'Không có'}
                                </span>
                            </div>
                        </div>
                        
                        <!-- Products -->
                        <div class="info-section">
                            <h5><i class="fas fa-box-open"></i> Sản phẩm trong đơn hàng</h5>
                            <table class="product-table table table-hover">
                                <thead>
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-center">Số lượng</th>
                                        <th class="text-end">Đơn giá</th>
                                        <th class="text-end">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="detail" items="${delivery.order.orderDetails}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${not empty detail.product.images and not empty detail.product.images[0]}">
                                                            <img src="${pageContext.request.contextPath}/image?fname=${detail.product.images[0].dirImage}" 
                                                                 class="product-img-detail me-3" 
                                                                 alt="${detail.product.productName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                                 class="product-img-detail me-3" 
                                                                 alt="No image">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <span class="fw-bold">${detail.product.productName}</span>
                                                </div>
                                            </td>
                                            <td class="text-center">${detail.quantity}</td>
                                            <td class="text-end">
                                                <fmt:formatNumber value="${detail.unitPrice}" type="currency" 
                                                                  currencySymbol="₫" maxFractionDigits="0"/>
                                            </td>
                                            <td class="text-end">
                                                <fmt:formatNumber value="${detail.quantity * detail.unitPrice}" 
                                                                  type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        
                        <!-- Order Summary -->
                        <div class="info-section">
                            <h5><i class="fas fa-receipt"></i> Tổng kết đơn hàng</h5>
                            <div class="info-row">
                                <span class="info-label">Tổng tiền hàng:</span>
                                <span class="info-value">
                                    <fmt:formatNumber value="${delivery.order.amount}" type="currency" 
                                                      currencySymbol="₫" maxFractionDigits="0"/>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Phí vận chuyển:</span>
                                <span class="info-value">
                                    <fmt:formatNumber value="${delivery.order.shippingFee}" type="currency" 
                                                      currencySymbol="₫" maxFractionDigits="0"/>
                                </span>
                            </div>
                            <c:if test="${delivery.order.totalDiscount > 0}">
                                <div class="info-row">
                                    <span class="info-label">Giảm giá:</span>
                                    <span class="info-value text-danger">
                                        -<fmt:formatNumber value="${delivery.order.totalDiscount}" type="currency" 
                                                          currencySymbol="₫" maxFractionDigits="0"/>
                                    </span>
                                </div>
                            </c:if>
                            <div class="info-row">
                                <span class="info-label fs-5 fw-bold">Tổng cộng:</span>
                                <span class="info-value fs-4 text-primary fw-bold">
                                    <fmt:formatNumber value="${delivery.order.amount + delivery.order.shippingFee - delivery.order.totalDiscount}" 
                                                      type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Phương thức thanh toán:</span>
                                <span class="info-value">
                                    <span class="badge bg-info">${delivery.order.paymentMethod.name}</span>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Trạng thái thanh toán:</span>
                                <span class="info-value">
                                    <span class="badge ${delivery.order.paymentStatus == 'Đã thanh toán' ? 'bg-success' : 'bg-warning'}">
                                        ${delivery.order.paymentStatus}
                                    </span>
                                </span>
                            </div>
                        </div>
                        
                        <!-- Actions -->
                        <c:if test="${delivery.deliveryStatus == 'Đang giao hàng'}">
                            <div class="d-grid gap-3">
                                <button type="button" class="btn btn-success btn-action-large" 
                                        data-bs-toggle="modal" data-bs-target="#completeModal">
                                    <i class="fas fa-check-circle"></i> Đánh dấu đã hoàn thành
                                </button>
                                <button type="button" class="btn btn-danger btn-action-large" 
                                        data-bs-toggle="modal" data-bs-target="#failModal">
                                    <i class="fas fa-times-circle"></i> Báo giao hàng thất bại
                                </button>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-4">
                <!-- Timeline Card -->
                <div class="detail-card">
                    <div class="detail-body">
                        <h5 class="mb-4"><i class="fas fa-history"></i> Lịch sử giao hàng</h5>
                        
                        <div class="timeline">
                            <div class="timeline-item">
                                <div class="timeline-marker">
                                    <i class="fas fa-plus"></i>
                                </div>
                                <div class="timeline-content">
                                    <h6>Đơn hàng được tạo</h6>
                                    <small class="text-muted">
                                        ${fn:substring(delivery.order.orderDate, 8, 10)}/${fn:substring(delivery.order.orderDate, 5, 7)}/${fn:substring(delivery.order.orderDate, 0, 4)} 
                                        ${fn:substring(delivery.order.orderDate, 11, 16)}
                                    </small>
                                </div>
                            </div>
                            
                            <div class="timeline-item">
                                <div class="timeline-marker bg-success">
                                    <i class="fas fa-check"></i>
                                </div>
                                <div class="timeline-content">
                                    <h6>Đã xác nhận</h6>
                                    <small class="text-muted">Shop đã xác nhận đơn hàng</small>
                                </div>
                            </div>
                            
                            <div class="timeline-item">
                                <div class="timeline-marker bg-warning">
                                    <i class="fas fa-user"></i>
                                </div>
                                <div class="timeline-content">
                                    <h6>Shipper nhận đơn</h6>
                                    <small class="text-muted">
                                        ${fn:substring(delivery.assignedDate, 8, 10)}/${fn:substring(delivery.assignedDate, 5, 7)}/${fn:substring(delivery.assignedDate, 0, 4)} 
                                        ${fn:substring(delivery.assignedDate, 11, 16)}
                                    </small>
                                    <p class="mb-0 mt-1">Shipper: ${delivery.shipper.fullname}</p>
                                </div>
                            </div>
                            
                            <c:if test="${delivery.deliveryStatus == 'Đã giao hàng'}">
                                <div class="timeline-item">
                                    <div class="timeline-marker bg-success">
                                        <i class="fas fa-check-double"></i>
                                    </div>
                                    <div class="timeline-content">
                                        <h6>Đã giao hàng thành công</h6>
                                        <small class="text-muted">
                                            ${fn:substring(delivery.completedDate, 8, 10)}/${fn:substring(delivery.completedDate, 5, 7)}/${fn:substring(delivery.completedDate, 0, 4)} 
                                            ${fn:substring(delivery.completedDate, 11, 16)}
                                        </small>
                                    </div>
                                </div>
                            </c:if>
                            
                            <c:if test="${delivery.deliveryStatus == 'Giao hàng thất bại'}">
                                <div class="timeline-item">
                                    <div class="timeline-marker bg-danger">
                                        <i class="fas fa-times"></i>
                                    </div>
                                    <div class="timeline-content">
                                        <h6>Giao hàng thất bại</h6>
                                        <p class="mb-0 text-danger">${delivery.failureReason}</p>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        
                        <c:if test="${not empty delivery.notes}">
                            <div class="alert alert-info mt-3">
                                <h6><i class="fas fa-sticky-note"></i> Ghi chú giao hàng</h6>
                                <p class="mb-0">${delivery.notes}</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Complete Modal -->
    <div class="modal fade" id="completeModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="border: none; border-radius: 20px; overflow: hidden;">
                <!-- Header với gradient xanh lá -->
                <div class="modal-header text-white position-relative" 
                     style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); 
                            border: none; padding: 2rem;">
                    <div class="position-absolute" style="top: 50%; left: 50%; transform: translate(-50%, -50%); 
                                                           width: 120px; height: 120px; 
                                                           background: rgba(255,255,255,0.1); 
                                                           border-radius: 50%; 
                                                           z-index: 0;">
                    </div>
                    <div class="w-100 text-center position-relative" style="z-index: 1;">
                        <div class="mb-3">
                            <i class="fas fa-check-circle" style="font-size: 3rem; animation: scaleUp 0.5s ease;"></i>
                        </div>
                        <h4 class="modal-title fw-bold mb-0">
                            Xác nhận hoàn thành
                        </h4>
                    </div>
                </div>
                
                <form action="${pageContext.request.contextPath}/api/shipper/delivery-action" method="post">
                    <!-- Body -->
                    <div class="modal-body text-center" style="padding: 2rem;">
                        <p class="mb-3" style="font-size: 1.1rem; color: #495057;">
                            Bạn có chắc chắn đã giao hàng thành công cho đơn hàng 
                            <strong class="text-success">#${delivery.order.orderID}</strong> này?
                        </p>
                        <div class="alert alert-warning" style="border-radius: 15px; border: none; background: #fff3cd;">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <strong>Lưu ý:</strong> Hành động này không thể hoàn tác!
                        </div>
                        <input type="hidden" name="action" value="complete">
                        <input type="hidden" name="deliveryId" value="${delivery.deliveryID}">
                    </div>
                    
                    <!-- Footer -->
                    <div class="modal-footer justify-content-center border-0" style="padding: 0 2rem 2rem 2rem; gap: 1rem;">
                        <button type="button" class="btn btn-outline-secondary px-4 py-2" 
                                data-bs-dismiss="modal" style="border-radius: 50px; min-width: 120px;">
                            <i class="fas fa-times me-2"></i>Hủy
                        </button>
                        <button type="submit" class="btn btn-success px-4 py-2" 
                                style="border-radius: 50px; min-width: 120px; 
                                       background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); 
                                       border: none; box-shadow: 0 4px 15px rgba(56, 239, 125, 0.4);">
                            <i class="fas fa-check-circle me-2"></i>Xác nhận hoàn thành
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Fail Modal -->
    <div class="modal fade" id="failModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="border: none; border-radius: 20px; overflow: hidden;">
                <!-- Header với gradient đỏ -->
                <div class="modal-header text-white position-relative" 
                     style="background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); 
                            border: none; padding: 2rem;">
                    <div class="position-absolute" style="top: 50%; left: 50%; transform: translate(-50%, -50%); 
                                                           width: 120px; height: 120px; 
                                                           background: rgba(255,255,255,0.1); 
                                                           border-radius: 50%; 
                                                           z-index: 0;">
                    </div>
                    <div class="w-100 text-center position-relative" style="z-index: 1;">
                        <div class="mb-3">
                            <i class="fas fa-times-circle" style="font-size: 3rem; animation: shake 0.5s ease;"></i>
                        </div>
                        <h4 class="modal-title fw-bold mb-0">
                            Báo giao hàng thất bại
                        </h4>
                    </div>
                </div>
                
                <form action="${pageContext.request.contextPath}/api/shipper/delivery-action" method="post">
                    <!-- Body -->
                    <div class="modal-body" style="padding: 2rem;">
                        <p class="mb-3" style="font-size: 1rem; color: #495057;">
                            Vui lòng nhập lý do giao hàng thất bại cho đơn hàng 
                            <strong class="text-danger">#${delivery.order.orderID}</strong>:
                        </p>
                        <input type="hidden" name="action" value="fail">
                        <input type="hidden" name="deliveryId" value="${delivery.deliveryID}">
                        <textarea name="failureReason" class="form-control mb-3" rows="4" 
                                  placeholder="Ví dụ: Khách không nghe máy, địa chỉ không chính xác, khách hủy đơn..." 
                                  required 
                                  style="border-radius: 15px; border: 2px solid #e9ecef; padding: 1rem;"></textarea>
                        <div class="alert alert-info" style="border-radius: 15px; border: none; background: #e7f3ff;">
                            <i class="fas fa-info-circle me-2"></i>
                            Thông tin này sẽ được gửi đến shop và khách hàng
                        </div>
                    </div>
                    
                    <!-- Footer -->
                    <div class="modal-footer justify-content-center border-0" style="padding: 0 2rem 2rem 2rem; gap: 1rem;">
                        <button type="button" class="btn btn-outline-secondary px-4 py-2" 
                                data-bs-dismiss="modal" style="border-radius: 50px; min-width: 120px;">
                            <i class="fas fa-arrow-left me-2"></i>Quay lại
                        </button>
                        <button type="submit" class="btn btn-danger px-4 py-2" 
                                style="border-radius: 50px; min-width: 120px; 
                                       background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); 
                                       border: none; box-shadow: 0 4px 15px rgba(235, 51, 73, 0.4);">
                            <i class="fas fa-exclamation-triangle me-2"></i>Xác nhận thất bại
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <style>
        @keyframes scaleUp {
            0% { transform: scale(0); }
            50% { transform: scale(1.2); }
            100% { transform: scale(1); }
        }
        
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }
        
        .modal-content {
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
    </style>
</body>
</html>
