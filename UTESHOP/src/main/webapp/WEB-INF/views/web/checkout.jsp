<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - UTESHOP</title>
    <style>
        .checkout-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 15px;
        }
        
        .checkout-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.08);
            margin-bottom: 20px;
            overflow: hidden;
        }
        
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            font-size: 1.25rem;
            font-weight: 600;
        }
        
        .card-body {
            padding: 25px;
        }
        
        .address-item {
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
        }
        
        .address-item:hover {
            border-color: #667eea;
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
        }
        
        .address-item.selected {
            border-color: #667eea;
            background-color: #f8f9ff;
        }
        
        .address-item input[type="radio"] {
            position: absolute;
            top: 15px;
            right: 15px;
        }
        
        .payment-method {
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
        }
        
        .payment-method:hover {
            border-color: #667eea;
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
        }
        
        .payment-method.selected {
            border-color: #667eea;
            background-color: #f8f9ff;
        }
        
        .payment-icon {
            width: 50px;
            height: 50px;
            margin-right: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
        }
        
        .order-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            font-size: 0.95rem;
        }
        
        .summary-row.total {
            font-size: 1.25rem;
            font-weight: 700;
            padding-top: 15px;
            border-top: 2px solid #dee2e6;
            margin-top: 10px;
            color: #e74c3c;
        }
        
        .product-item {
            display: flex;
            padding: 15px 0;
            border-bottom: 1px solid #e9ecef;
        }
        
        .product-item:last-child {
            border-bottom: none;
        }
        
        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
            margin-right: 15px;
        }
        
        .product-info {
            flex: 1;
        }
        
        .btn-checkout {
            width: 100%;
            padding: 15px;
            font-size: 1.1rem;
            font-weight: 600;
            border-radius: 8px;
            border: none;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .btn-checkout:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }
        
        .btn-checkout:disabled {
            background: #ccc;
            cursor: not-allowed;
            transform: none;
        }
        
        .badge-default {
            background: #28a745;
            color: white;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.75rem;
            margin-left: 8px;
        }
        
        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        
        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            font-size: 0.95rem;
            transition: border-color 0.3s ease;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .breadcrumb {
            background: none;
            padding: 20px 0;
            margin: 0;
        }
        
        .breadcrumb-item + .breadcrumb-item::before {
            content: "›";
            padding: 0 8px;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="checkout-container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a></li>
                <li class="breadcrumb-item active" aria-current="page">Thanh toán</li>
            </ol>
        </nav>
        
        <h1 class="mb-4">Thanh toán đơn hàng</h1>
        
        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/checkout/process" method="POST" id="checkoutForm">
            <div class="row">
                <!-- Left Column -->
                <div class="col-lg-7">
                    <!-- Địa chỉ giao hàng -->
                    <div class="checkout-card">
                        <div class="card-header">
                            <i class="bi bi-geo-alt-fill me-2"></i>Địa chỉ giao hàng
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty addresses}">
                                    <c:forEach var="address" items="${addresses}" varStatus="status">
                                        <div class="address-item ${address.addressID == defaultAddress.addressID ? 'selected' : ''}" 
                                             onclick="selectAddress(this, ${address.addressID})">
                                            <input type="radio" name="addressId" value="${address.addressID}" 
                                                   ${address.addressID == defaultAddress.addressID ? 'checked' : ''} required>
                                            <div class="fw-bold mb-2">
                                                ${address.name}
                                                <span class="text-muted">| ${address.phone}</span>
                                                <c:if test="${address.isDefault}">
                                                    <span class="badge-default">Mặc định</span>
                                                </c:if>
                                            </div>
                                            <div class="text-muted">
                                                ${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-warning">
                                        Bạn chưa có địa chỉ giao hàng. 
                                        <a href="${pageContext.request.contextPath}/account/addresses">Thêm địa chỉ mới</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    
                    <!-- Phương thức thanh toán -->
                    <div class="checkout-card">
                        <div class="card-header">
                            <i class="bi bi-credit-card-fill me-2"></i>Phương thức thanh toán
                        </div>
                        <div class="card-body">
                            <c:forEach var="method" items="${paymentMethods}" varStatus="status">
                                <div class="payment-method ${status.first ? 'selected' : ''}" 
                                     onclick="selectPayment(this, ${method.id})">
                                    <div class="payment-icon">
                                        <c:choose>
                                            <c:when test="${method.name.contains('COD') or method.name.contains('tiền mặt')}">
                                                <i class="bi bi-cash-coin text-success"></i>
                                            </c:when>
                                            <c:when test="${method.name.contains('Banking') or method.name.contains('chuyển khoản')}">
                                                <i class="bi bi-bank text-primary"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-wallet2 text-info"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div>
                                        <input type="radio" name="paymentMethodId" value="${method.id}" 
                                               ${status.first ? 'checked' : ''} required>
                                        <div class="fw-bold">${method.name}</div>
                                        <div class="text-muted small">${method.description}</div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <!-- Ghi chú -->
                    <div class="checkout-card">
                        <div class="card-header">
                            <i class="bi bi-chat-left-text-fill me-2"></i>Ghi chú đơn hàng
                        </div>
                        <div class="card-body">
                            <textarea name="notes" class="form-control" rows="4" 
                                      placeholder="Ghi chú về đơn hàng, ví dụ: thời gian hay chỉ dẫn địa điểm giao hàng chi tiết hơn..."></textarea>
                        </div>
                    </div>
                </div>
                
                <!-- Right Column - Order Summary -->
                <div class="col-lg-5">
                    <div class="checkout-card sticky-top" style="top: 20px;">
                        <div class="card-header">
                            <i class="bi bi-cart-check-fill me-2"></i>Đơn hàng của bạn
                        </div>
                        <div class="card-body">
                            <!-- Product List -->
                            <div class="mb-3">
                                <c:forEach var="item" items="${cart.cartDetails}">
                                    <div class="product-item">
                                        <c:choose>
                                            <c:when test="${not empty item.product.images and not empty item.product.images[0]}">
                                                <img src="${pageContext.request.contextPath}/image?fname=${item.product.images[0].dirImage}" 
                                                     class="product-image" alt="${item.product.productName}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                     class="product-image" alt="No image">
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="product-info">
                                            <div class="fw-bold mb-1">${item.product.productName}</div>
                                            <div class="text-muted small">Số lượng: ${item.quantity}</div>
                                            <div class="text-danger fw-bold">
                                                <fmt:formatNumber value="${item.product.unitPrice}" type="number" maxFractionDigits="0"/>₫
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            
                            <!-- Order Summary -->
                            <div class="order-summary">
                                <div class="summary-row">
                                    <span>Tạm tính:</span>
                                    <span class="fw-bold">
                                        <fmt:formatNumber value="${cart.totalPrice}" type="number" maxFractionDigits="0"/>₫
                                    </span>
                                </div>
                                <div class="summary-row">
                                    <span>Phí vận chuyển:</span>
                                    <span class="fw-bold">25,000₫</span>
                                </div>
                                <div class="summary-row">
                                    <span>Giảm giá:</span>
                                    <span class="fw-bold text-success">0₫</span>
                                </div>
                                <div class="summary-row total">
                                    <span>Tổng cộng:</span>
                                    <span>
                                        <fmt:formatNumber value="${cart.totalPrice + 25000}" type="number" maxFractionDigits="0"/>₫
                                    </span>
                                </div>
                            </div>
                            
                            <!-- Checkout Button -->
                            <button type="submit" class="btn-checkout mt-4" id="btnCheckout">
                                <i class="bi bi-check-circle-fill me-2"></i>Đặt hàng
                            </button>
                            
                            <div class="text-center mt-3">
                                <small class="text-muted">
                                    <i class="bi bi-shield-check me-1"></i>
                                    Thông tin của bạn được bảo mật
                                </small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
    
    <script>
        function selectAddress(element, addressId) {
            // Remove selected class from all
            document.querySelectorAll('.address-item').forEach(item => {
                item.classList.remove('selected');
            });
            
            // Add selected class
            element.classList.add('selected');
            
            // Check radio
            element.querySelector('input[type="radio"]').checked = true;
        }
        
        function selectPayment(element, paymentId) {
            // Remove selected class from all
            document.querySelectorAll('.payment-method').forEach(item => {
                item.classList.remove('selected');
            });
            
            // Add selected class
            element.classList.add('selected');
            
            // Check radio
            element.querySelector('input[type="radio"]').checked = true;
        }
        
        // Form validation
        document.getElementById('checkoutForm').addEventListener('submit', function(e) {
            const addressSelected = document.querySelector('input[name="addressId"]:checked');
            const paymentSelected = document.querySelector('input[name="paymentMethodId"]:checked');
            
            if (!addressSelected) {
                e.preventDefault();
                alert('Vui lòng chọn địa chỉ giao hàng!');
                return false;
            }
            
            if (!paymentSelected) {
                e.preventDefault();
                alert('Vui lòng chọn phương thức thanh toán!');
                return false;
            }
            
            // Disable button to prevent double submit
            const btn = document.getElementById('btnCheckout');
            btn.disabled = true;
            btn.innerHTML = '<i class="bi bi-hourglass-split me-2"></i>Đang xử lý...';
        });
    </script>
</body>
</html>

