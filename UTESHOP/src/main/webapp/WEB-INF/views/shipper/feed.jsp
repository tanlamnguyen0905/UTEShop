<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đơn hàng có sẵn - Shipper</title>
    <style>
        .order-card {
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: none;
            margin-bottom: 20px;
            overflow: hidden;
        }
        
        .order-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
        }
        
        .order-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 20px;
        }
        
        .order-body {
            padding: 20px;
        }
        
        .badge-status {
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }
        
        .product-item {
            display: flex;
            align-items: center;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 10px;
            margin-bottom: 10px;
        }
        
        .product-img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
            margin-right: 15px;
        }
        
        .btn-accept {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 10px 30px;
            border-radius: 25px;
            color: white;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .btn-accept:hover {
            transform: scale(1.05);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            color: white;
        }
        
        .stats-banner {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 15px;
            margin-bottom: 25px;
            text-align: center;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
        }
        
        .empty-state i {
            font-size: 80px;
            color: #ddd;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <!-- Stats Banner -->
        <div class="stats-banner">
            <h2 class="mb-2"><i class="fas fa-box-open"></i> Đơn hàng có sẵn</h2>
            <p class="mb-0">Hiện có <strong>${count}</strong> đơn hàng đang chờ giao</p>
        </div>

        <!-- Orders List -->
        <c:choose>
            <c:when test="${not empty availableOrders}">
                <div class="row">
                    <c:forEach var="order" items="${availableOrders}">
                        <div class="col-md-6 col-lg-4">
                            <div class="order-card">
                                <div class="order-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="mb-0">
                                            <i class="fas fa-receipt"></i> Đơn #${order.orderID}
                                        </h5>
                                        <span class="badge badge-status bg-warning text-dark">
                                            ${order.orderStatus}
                                        </span>
                                    </div>
                                    <small>
                                        <i class="far fa-clock"></i> 
                                        ${fn:substring(order.orderDate, 8, 10)}/${fn:substring(order.orderDate, 5, 7)}/${fn:substring(order.orderDate, 0, 4)} 
                                        ${fn:substring(order.orderDate, 11, 16)}
                                    </small>
                                </div>
                                
                                <div class="order-body">
                                    <!-- Customer Info -->
                                    <div class="mb-3">
                                        <h6><i class="fas fa-user"></i> Thông tin khách hàng</h6>
                                        <p class="mb-1"><strong>${order.recipientName}</strong></p>
                                        <p class="mb-1"><i class="fas fa-phone"></i> ${order.phoneNumber}</p>
                                        <p class="mb-1 text-muted">
                                            <i class="fas fa-map-marker-alt"></i> 
                                            ${order.address.addressDetail}, ${order.address.ward}, 
                                            ${order.address.district}, ${order.address.province}
                                        </p>
                                    </div>
                                    
                                    <!-- Products -->
                                    <div class="mb-3">
                                        <h6><i class="fas fa-shopping-bag"></i> Sản phẩm 
                                            <span class="badge bg-secondary">${order.orderDetails.size()}</span>
                                        </h6>
                                        <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                                            <c:if test="${status.index < 2}">
                                                <div class="product-item">
                                                    <c:choose>
                                                        <c:when test="${not empty detail.product.images and not empty detail.product.images[0]}">
                                                            <img src="${pageContext.request.contextPath}/assets/uploads/product/${detail.product.images[0].dirImage}" 
                                                                 class="product-img" 
                                                                 alt="${detail.product.productName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                                 class="product-img" 
                                                                 alt="No image">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div class="flex-grow-1">
                                                        <div class="fw-bold">${detail.product.productName}</div>
                                                        <small class="text-muted">Số lượng: ${detail.quantity}</small>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${order.orderDetails.size() > 2}">
                                            <small class="text-muted">
                                                <i class="fas fa-ellipsis-h"></i> 
                                                Và ${order.orderDetails.size() - 2} sản phẩm khác
                                            </small>
                                        </c:if>
                                    </div>
                                    
                                    <!-- Order Total -->
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between">
                                            <span>Tổng tiền hàng:</span>
                                            <strong>
                                                <fmt:formatNumber value="${order.amount}" type="currency" 
                                                                  currencySymbol="₫" maxFractionDigits="0"/>
                                            </strong>
                                        </div>
                                        <div class="d-flex justify-content-between">
                                            <span>Phí ship:</span>
                                            <strong>
                                                <fmt:formatNumber value="${order.shippingFee}" type="currency" 
                                                                  currencySymbol="₫" maxFractionDigits="0"/>
                                            </strong>
                                        </div>
                                        <hr>
                                        <div class="d-flex justify-content-between">
                                            <span class="fw-bold">Tổng cộng:</span>
                                            <span class="fw-bold text-primary fs-5">
                                                <fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" 
                                                                  type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                            </span>
                                        </div>
                                        <div class="d-flex justify-content-between mt-2">
                                            <span>Thanh toán:</span>
                                            <c:choose>
                                                <c:when test="${order.paymentStatus == 'Đã thanh toán'}">
                                                    <span class="badge bg-success">
                                                        <i class="fas fa-check-circle me-1"></i>Đã thanh toán
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.paymentStatus == 'Chưa thanh toán'}">
                                                    <span class="badge bg-warning text-dark">
                                                        <i class="fas fa-money-bill-wave me-1"></i>Chưa thanh toán (COD)
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${order.paymentStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    
                                    <!-- Accept Button -->
                                    <form id="acceptForm-${order.orderID}" action="${pageContext.request.contextPath}/api/shipper/delivery-action" 
                                          method="post" class="d-grid">
                                        <input type="hidden" name="action" value="accept">
                                        <input type="hidden" name="orderId" value="${order.orderID}">
                                        <button type="button" class="btn btn-accept" onclick="showAcceptModal(${order.orderID})">
                                            <i class="fas fa-check-circle"></i> Nhận đơn hàng
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <h3 class="text-muted">Không có đơn hàng nào</h3>
                    <p class="text-muted">Hiện tại chưa có đơn hàng nào cần giao. Vui lòng quay lại sau!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Accept Order Modal -->
    <div class="modal fade" id="acceptOrderModal" tabindex="-1" aria-labelledby="acceptOrderModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="border: none; border-radius: 20px; overflow: hidden;">
                <!-- Header với gradient -->
                <div class="modal-header text-white position-relative" 
                     style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
                            border: none; padding: 2rem;">
                    <div class="position-absolute" style="top: 50%; left: 50%; transform: translate(-50%, -50%); 
                                                           width: 120px; height: 120px; 
                                                           background: rgba(255,255,255,0.1); 
                                                           border-radius: 50%; 
                                                           z-index: 0;">
                    </div>
                    <div class="w-100 text-center position-relative" style="z-index: 1;">
                        <div class="mb-3">
                            <i class="fas fa-shipping-fast" style="font-size: 3rem; animation: bounce 1s infinite;"></i>
                        </div>
                        <h4 class="modal-title fw-bold mb-0" id="acceptOrderModalLabel">
                            Xác nhận nhận đơn hàng
                        </h4>
                    </div>
                </div>
                
                <!-- Body -->
                <div class="modal-body text-center" style="padding: 2rem;">
                    <p class="mb-3" style="font-size: 1.1rem; color: #495057;">
                        Bạn có chắc chắn muốn nhận đơn hàng 
                        <strong class="text-primary">#<span id="modalOrderId"></span></strong> này không?
                    </p>
                    <div class="alert alert-info" style="border-radius: 15px; border: none; background: #e7f3ff;">
                        <i class="fas fa-info-circle me-2"></i>
                        Sau khi nhận đơn, bạn sẽ chịu trách nhiệm giao hàng đến khách hàng
                    </div>
                </div>
                
                <!-- Footer -->
                <div class="modal-footer justify-content-center border-0" style="padding: 0 2rem 2rem 2rem; gap: 1rem;">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2" 
                            data-bs-dismiss="modal" style="border-radius: 50px; min-width: 120px;">
                        <i class="fas fa-times me-2"></i>Hủy
                    </button>
                    <button type="button" class="btn btn-success px-4 py-2" 
                            onclick="confirmAccept()" 
                            style="border-radius: 50px; min-width: 120px; 
                                   background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
                                   border: none; box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);">
                        <i class="fas fa-check-circle me-2"></i>Xác nhận
                    </button>
                </div>
            </div>
        </div>
    </div>

    <style>
        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-10px); }
        }
        
        .modal-content {
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        
        .btn-accept:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }
    </style>

    <script>
        let currentOrderId = null;
        
        function showAcceptModal(orderId) {
            currentOrderId = orderId;
            document.getElementById('modalOrderId').textContent = orderId;
            const modal = new bootstrap.Modal(document.getElementById('acceptOrderModal'));
            modal.show();
        }
        
        function confirmAccept() {
            if (currentOrderId) {
                document.getElementById('acceptForm-' + currentOrderId).submit();
            }
        }
    </script>
</body>
</html>

