<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đơn giao hàng của tôi - Shipper</title>
    <style>
        .stats-card {
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
            border: none;
        }
        
        .stats-card:hover {
            transform: translateY(-5px);
        }
        
        .stats-card .icon {
            font-size: 40px;
            margin-bottom: 10px;
        }
        
        .stats-card .number {
            font-size: 2rem;
            font-weight: 700;
            margin: 10px 0;
        }
        
        .stats-card .label {
            color: #6c757d;
            font-size: 0.9rem;
        }
        
        .delivery-card {
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border: none;
            margin-bottom: 20px;
            overflow: hidden;
            transition: all 0.3s ease;
        }
        
        .delivery-card:hover {
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
        }
        
        .delivery-header {
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
        }
        
        .delivery-body {
            padding: 20px;
        }
        
        .status-badge {
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }
        
        .status-delivering {
            background: #ffc107;
            color: #000;
        }
        
        .status-completed {
            background: #28a745;
            color: white;
        }
        
        .status-failed {
            background: #dc3545;
            color: white;
        }
        
        .filter-tabs {
            margin-bottom: 25px;
        }
        
        .filter-tabs .nav-link {
            border-radius: 10px;
            margin-right: 10px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .filter-tabs .nav-link.active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-action {
            border-radius: 20px;
            padding: 8px 20px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-action:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <!-- Page Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-truck"></i> Đơn giao hàng của tôi</h2>
        </div>

        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="stats-card bg-primary text-white">
                    <div class="icon"><i class="fas fa-box"></i></div>
                    <div class="number">${stats['total']}</div>
                    <div class="label text-white">Tổng đơn hàng</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stats-card bg-warning text-dark">
                    <div class="icon"><i class="fas fa-shipping-fast"></i></div>
                    <div class="number">${stats['delivering']}</div>
                    <div class="label">Đang giao</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stats-card bg-success text-white">
                    <div class="icon"><i class="fas fa-check-circle"></i></div>
                    <div class="number">${stats['completed']}</div>
                    <div class="label text-white">Đã hoàn thành</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stats-card bg-danger text-white">
                    <div class="icon"><i class="fas fa-times-circle"></i></div>
                    <div class="number">${stats['failed']}</div>
                    <div class="label text-white">Thất bại</div>
                </div>
            </div>
        </div>

        <!-- Filter Tabs -->
        <ul class="nav nav-pills filter-tabs">
            <li class="nav-item">
                <a class="nav-link ${empty statusFilter ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries">
                    <i class="fas fa-th-large"></i> Tất cả
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${statusFilter == 'Đang giao hàng' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries?status=Đang giao hàng">
                    <i class="fas fa-truck"></i> Đang giao
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${statusFilter == 'Đã giao hàng' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries?status=Đã giao hàng">
                    <i class="fas fa-check-circle"></i> Đã hoàn thành
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${statusFilter == 'Giao hàng thất bại' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries?status=Giao hàng thất bại">
                    <i class="fas fa-times-circle"></i> Thất bại
                </a>
            </li>
        </ul>

        <!-- Deliveries List -->
        <c:choose>
            <c:when test="${not empty deliveries}">
                <div class="row">
                    <c:forEach var="delivery" items="${deliveries}">
                        <div class="col-md-6 col-lg-4">
                            <div class="delivery-card">
                                <div class="delivery-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="mb-0">
                                            <i class="fas fa-receipt"></i> Đơn #${delivery.order.orderID}
                                        </h5>
                                        <span class="status-badge 
                                            ${delivery.deliveryStatus == 'Đang giao hàng' ? 'status-delivering' : ''}
                                            ${delivery.deliveryStatus == 'Đã giao hàng' ? 'status-completed' : ''}
                                            ${delivery.deliveryStatus == 'Giao hàng thất bại' ? 'status-failed' : ''}">
                                            ${delivery.deliveryStatus}
                                        </span>
                                    </div>
                                    <small class="text-muted">
                                        <i class="far fa-clock"></i> 
                                        Nhận: ${fn:substring(delivery.assignedDate, 8, 10)}/${fn:substring(delivery.assignedDate, 5, 7)}/${fn:substring(delivery.assignedDate, 0, 4)} 
                                        ${fn:substring(delivery.assignedDate, 11, 16)}
                                    </small>
                                </div>
                                
                                <div class="delivery-body">
                                    <!-- Customer Info -->
                                    <div class="mb-3">
                                        <h6><i class="fas fa-user"></i> Khách hàng</h6>
                                        <p class="mb-1"><strong>${delivery.order.recipientName}</strong></p>
                                        <p class="mb-1"><i class="fas fa-phone"></i> ${delivery.order.phoneNumber}</p>
                                        <p class="mb-1 text-muted small">
                                            <i class="fas fa-map-marker-alt"></i> 
                                            ${delivery.order.address.addressDetail}, ${delivery.order.address.ward}, 
                                            ${delivery.order.address.district}, ${delivery.order.address.province}
                                        </p>
                                    </div>
                                    
                                    <!-- Order Total -->
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between">
                                            <span>Tổng tiền:</span>
                                            <strong class="text-primary">
                                                <fmt:formatNumber value="${delivery.order.amount + delivery.order.shippingFee - delivery.order.totalDiscount}" 
                                                                  type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                            </strong>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center mt-2">
                                            <small class="text-muted">
                                                <i class="fas fa-money-bill-wave"></i> 
                                                ${delivery.order.paymentMethod.name}
                                            </small>
                                            <c:choose>
                                                <c:when test="${delivery.order.paymentStatus == 'Đã thanh toán'}">
                                                    <span class="badge bg-success">
                                                        <i class="fas fa-check-circle me-1"></i>Đã thanh toán
                                                    </span>
                                                </c:when>
                                                <c:when test="${delivery.order.paymentStatus == 'Chưa thanh toán'}">
                                                    <span class="badge bg-warning text-dark">
                                                        <i class="fas fa-exclamation-circle me-1"></i>COD - Chưa thanh toán
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${delivery.order.paymentStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    
                                    <!-- Completion Date -->
                                    <c:if test="${delivery.deliveryStatus == 'Đã giao hàng' && delivery.completedDate != null}">
                                        <div class="mb-3">
                                            <small class="text-success">
                                                <i class="fas fa-check"></i> 
                                                Hoàn thành: ${fn:substring(delivery.completedDate, 8, 10)}/${fn:substring(delivery.completedDate, 5, 7)}/${fn:substring(delivery.completedDate, 0, 4)} 
                                                ${fn:substring(delivery.completedDate, 11, 16)}
                                            </small>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Failure Reason -->
                                    <c:if test="${delivery.deliveryStatus == 'Giao hàng thất bại' && not empty delivery.failureReason}">
                                        <div class="mb-3">
                                            <small class="text-danger">
                                                <i class="fas fa-exclamation-circle"></i> 
                                                Lý do: ${delivery.failureReason}
                                            </small>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Actions -->
                                    <div class="d-grid gap-2">
                                        <a href="${pageContext.request.contextPath}/api/shipper/delivery-detail?id=${delivery.deliveryID}" 
                                           class="btn btn-outline-primary btn-action">
                                            <i class="fas fa-eye"></i> Xem chi tiết
                                        </a>
                                        
                                        <c:if test="${delivery.deliveryStatus == 'Đang giao hàng'}">
                                            <button type="button" class="btn btn-success btn-action" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#completeModal${delivery.deliveryID}">
                                                <i class="fas fa-check-circle"></i> Hoàn thành
                                            </button>
                                            
                                            <button type="button" class="btn btn-danger btn-action" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#failModal${delivery.deliveryID}">
                                                <i class="fas fa-times-circle"></i> Báo thất bại
                                            </button>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Complete Modal -->
                        <div class="modal fade" id="completeModal${delivery.deliveryID}" tabindex="-1">
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
                        <div class="modal fade" id="failModal${delivery.deliveryID}" tabindex="-1">
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
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5">
                    <i class="fas fa-inbox fa-4x text-muted mb-3"></i>
                    <h4 class="text-muted">Chưa có đơn giao hàng nào</h4>
                    <p class="text-muted">Các đơn hàng bạn nhận sẽ hiển thị ở đây</p>
                    <a href="${pageContext.request.contextPath}/api/shipper/feed" class="btn btn-primary">
                        <i class="fas fa-search"></i> Tìm đơn hàng
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
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

