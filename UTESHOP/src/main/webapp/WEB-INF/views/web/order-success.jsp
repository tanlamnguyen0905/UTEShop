<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt hàng thành công - UTESHOP</title>
    <style>
        .success-container {
            max-width: 800px;
            margin: 60px auto;
            padding: 0 15px;
            text-align: center;
        }
        
        .success-icon {
            width: 120px;
            height: 120px;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
            animation: scaleIn 0.5s ease-out;
        }
        
        .success-icon i {
            font-size: 60px;
            color: white;
        }
        
        @keyframes scaleIn {
            from {
                transform: scale(0);
                opacity: 0;
            }
            to {
                transform: scale(1);
                opacity: 1;
            }
        }
        
        .success-title {
            font-size: 2rem;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 15px;
        }
        
        .success-message {
            font-size: 1.1rem;
            color: #7f8c8d;
            margin-bottom: 40px;
        }
        
        .order-info-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            padding: 30px;
            text-align: left;
            margin-bottom: 30px;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #e9ecef;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #495057;
        }
        
        .info-value {
            color: #6c757d;
        }
        
        .order-total {
            font-size: 1.5rem;
            font-weight: 700;
            color: #e74c3c;
        }
        
        .status-badge {
            display: inline-block;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 600;
        }
        
        .status-pending {
            background: #fff3cd;
            color: #856404;
        }
        
        .btn-action {
            display: inline-block;
            padding: 14px 32px;
            font-size: 1rem;
            font-weight: 600;
            border-radius: 8px;
            text-decoration: none;
            margin: 10px;
            transition: all 0.3s ease;
        }
        
        .btn-primary-action {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
            color: white;
        }
        
        .btn-secondary-action {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }
        
        .btn-secondary-action:hover {
            background: #667eea;
            color: white;
        }
        
        .timeline {
            position: relative;
            padding: 30px 0;
        }
        
        .timeline-item {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .timeline-icon {
            width: 40px;
            height: 40px;
            background: #e9ecef;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
        }
        
        .timeline-icon.active {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }
        
        .timeline-content {
            flex: 1;
        }
        
        .confetti {
            position: fixed;
            width: 10px;
            height: 10px;
            background: #f0f;
            position: absolute;
            animation: confetti-fall 3s linear infinite;
        }
        
        @keyframes confetti-fall {
            to {
                transform: translateY(100vh) rotate(360deg);
                opacity: 0;
            }
        }
    </style>
</head>
<body>
    <div class="success-container">
        <!-- Success Icon -->
        <div class="success-icon">
            <i class="bi bi-check-lg"></i>
        </div>
        
        <!-- Success Message -->
        <h1 class="success-title">Đặt hàng thành công!</h1>
        <p class="success-message">
            Cảm ơn bạn đã mua hàng tại UTESHOP. <br>
            Chúng tôi đã nhận được đơn hàng của bạn và sẽ xử lý trong thời gian sớm nhất.
        </p>
        
        <!-- Order Info -->
        <c:if test="${not empty order}">
            <div class="order-info-card">
                <h3 class="mb-4">Thông tin đơn hàng</h3>
                
                <div class="info-row">
                    <span class="info-label">Mã đơn hàng:</span>
                    <span class="info-value fw-bold">#${order.orderID}</span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Ngày đặt:</span>
                    <span class="info-value">
                        ${formattedOrderDate}
                    </span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Người nhận:</span>
                    <span class="info-value">${order.recipientName}</span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Số điện thoại:</span>
                    <span class="info-value">${order.phoneNumber}</span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Địa chỉ:</span>
                    <span class="info-value">
                        ${order.address.addressDetail}, ${order.address.ward}, 
                        ${order.address.district}, ${order.address.province}
                    </span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Phương thức thanh toán:</span>
                    <span class="info-value">${order.paymentMethod.name}</span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Trạng thái:</span>
                    <c:choose>
                        <c:when test="${order.orderStatus eq 'Đang chờ'}">
                            <span class="badge bg-warning text-dark" style="font-size: 0.9rem; padding: 0.5rem 1rem;">
                                <i class="fas fa-clock me-1"></i>Chờ xác nhận
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đang xử lý'}">
                            <span class="badge text-white" style="background-color: #fd7e14; font-size: 0.9rem; padding: 0.5rem 1rem;">
                                <i class="fas fa-hourglass-half me-1"></i>Đang xử lý
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đang giao'}">
                            <span class="badge bg-primary" style="font-size: 0.9rem; padding: 0.5rem 1rem;">
                                <i class="fas fa-shipping-fast me-1"></i>Đang giao
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đã giao'}">
                            <span class="badge bg-success" style="font-size: 0.9rem; padding: 0.5rem 1rem;">
                                <i class="fas fa-check-circle me-1"></i>Hoàn thành
                            </span>
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đã hủy'}">
                            <span class="badge bg-danger" style="font-size: 0.9rem; padding: 0.5rem 1rem;">
                                <i class="fas fa-times-circle me-1"></i>Đã hủy
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary" style="font-size: 0.9rem; padding: 0.5rem 1rem;">
                                ${order.orderStatus}
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Tạm tính:</span>
                    <span class="info-value">
                        <fmt:formatNumber value="${order.amount}" type="number" maxFractionDigits="0"/>₫
                    </span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Phí vận chuyển:</span>
                    <span class="info-value">
                        <fmt:formatNumber value="${order.shippingFee}" type="number" maxFractionDigits="0"/>₫
                    </span>
                </div>
                
                <c:if test="${order.totalDiscount > 0}">
                    <div class="info-row">
                        <span class="info-label">Giảm giá:</span>
                        <span class="info-value text-success">
                            -<fmt:formatNumber value="${order.totalDiscount}" type="number" maxFractionDigits="0"/>₫
                        </span>
                    </div>
                </c:if>
                
                <div class="info-row">
                    <span class="info-label">Tổng cộng:</span>
                    <span class="order-total">
                        <fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" 
                                        type="number" maxFractionDigits="0"/>₫
                    </span>
                </div>
            </div>
            
            <!-- Timeline -->
            <div class="order-info-card">
                <h3 class="mb-4">Trạng thái đơn hàng</h3>
                <div class="timeline">
                    <div class="timeline-item">
                        <div class="timeline-icon active">
                            <i class="bi bi-check"></i>
                        </div>
                        <div class="timeline-content">
                            <div class="fw-bold">Đơn hàng đã được đặt</div>
                            <div class="text-muted small">
                                ${formattedOrderDate}
                            </div>
                        </div>
                    </div>
                    
                    <div class="timeline-item">
                        <div class="timeline-icon">
                            <i class="bi bi-hourglass-split"></i>
                        </div>
                        <div class="timeline-content">
                            <div class="fw-bold">Đang xử lý</div>
                            <div class="text-muted small">Đơn hàng đang được xác nhận</div>
                        </div>
                    </div>
                    
                    <div class="timeline-item">
                        <div class="timeline-icon">
                            <i class="bi bi-truck"></i>
                        </div>
                        <div class="timeline-content">
                            <div class="fw-bold">Đang giao hàng</div>
                            <div class="text-muted small">Đơn hàng đang được vận chuyển</div>
                        </div>
                    </div>
                    
                    <div class="timeline-item">
                        <div class="timeline-icon">
                            <i class="bi bi-box-seam"></i>
                        </div>
                        <div class="timeline-content">
                            <div class="fw-bold">Đã giao hàng</div>
                            <div class="text-muted small">Bạn đã nhận được hàng</div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        
        <!-- Action Buttons -->
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/home" class="btn-action btn-primary-action">
                <i class="bi bi-house-fill me-2"></i>Tiếp tục mua sắm
            </a>
            <a href="${pageContext.request.contextPath}/user/profile" class="btn-action btn-secondary-action">
                <i class="bi bi-list-ul me-2"></i>Xem đơn hàng của tôi
            </a>
        </div>
        
        <!-- Additional Info -->
        <div class="mt-5 text-muted">
            <p>
                <i class="bi bi-info-circle me-2"></i>
                Bạn sẽ nhận được email xác nhận đơn hàng trong ít phút. <br>
                Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ hotline: <strong>1900-xxxx</strong>
            </p>
        </div>
    </div>
    
    <script>
        // Create confetti effect
        function createConfetti() {
            const colors = ['#f0f', '#0ff', '#ff0', '#f00', '#0f0', '#00f'];
            for (let i = 0; i < 50; i++) {
                const confetti = document.createElement('div');
                confetti.className = 'confetti';
                confetti.style.left = Math.random() * 100 + '%';
                confetti.style.background = colors[Math.floor(Math.random() * colors.length)];
                confetti.style.animationDelay = Math.random() * 3 + 's';
                confetti.style.animationDuration = Math.random() * 2 + 3 + 's';
                document.body.appendChild(confetti);
                
                setTimeout(() => confetti.remove(), 5000);
            }
        }
        
        // Trigger confetti on page load
        window.addEventListener('load', createConfetti);
    </script>
</body>
</html>

