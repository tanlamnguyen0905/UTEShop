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
        
        /* Voucher Styles */
        .voucher-input-group {
            display: flex;
            gap: 10px;
            margin-bottom: 15px;
        }
        
        .btn-apply-voucher {
            padding: 12px 24px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            white-space: nowrap;
        }
        
        .btn-apply-voucher:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        
        .btn-apply-voucher:disabled {
            background: #ccc;
            cursor: not-allowed;
            transform: none;
        }
        
        .applied-voucher {
            margin-top: 15px;
        }
        
        .voucher-success {
            display: flex;
            align-items: center;
            gap: 12px;
            background: linear-gradient(135deg, #d4f4dd 0%, #e8f8ed 100%);
            border: 2px solid #28a745;
            border-radius: 10px;
            padding: 15px;
            animation: slideIn 0.3s ease;
        }
        
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .voucher-icon {
            color: #28a745;
            font-size: 1.8rem;
        }
        
        .voucher-details {
            flex: 1;
        }
        
        .voucher-code-display {
            font-weight: 700;
            color: #155724;
            font-size: 1.1rem;
            margin-bottom: 4px;
        }
        
        .voucher-description {
            color: #155724;
            font-size: 0.9rem;
            margin-bottom: 4px;
        }
        
        .voucher-discount {
            color: #28a745;
            font-weight: 600;
            font-size: 0.95rem;
        }
        
        .btn-remove-voucher {
            background: transparent;
            border: none;
            color: #dc3545;
            font-size: 1.2rem;
            cursor: pointer;
            padding: 5px 10px;
            transition: all 0.2s;
        }
        
        .btn-remove-voucher:hover {
            color: #bb2d3b;
            transform: scale(1.2);
        }
        
        .voucher-error {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 12px;
            border-radius: 8px;
            margin-top: 10px;
            animation: shake 0.5s ease;
        }
        
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }
        
        /* Button toggle danh sách voucher */
        .btn-toggle-voucher {
            width: 100%;
            background: transparent;
            border: 2px dashed #667eea;
            color: #667eea;
            padding: 12px 20px;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .btn-toggle-voucher:hover {
            background: #f8f9ff;
            border-color: #764ba2;
            color: #764ba2;
        }
        
        .btn-toggle-voucher.active {
            background: #f8f9ff;
            border-color: #667eea;
        }
        
        /* Danh sách voucher */
        .voucher-list {
            margin-top: 15px;
            max-height: 400px;
            overflow-y: auto;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 10px;
            background: #f8f9fa;
        }
        
        .voucher-list-loading {
            text-align: center;
            padding: 20px;
            color: #6c757d;
        }
        
        .voucher-list-item {
            background: white;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .voucher-list-item:hover {
            border-color: #667eea;
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
            transform: translateY(-2px);
        }
        
        .voucher-list-item:last-child {
            margin-bottom: 0;
        }
        
        .voucher-item-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8px;
        }
        
        .voucher-item-code {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 6px 12px;
            border-radius: 6px;
            font-weight: 700;
            font-size: 0.95rem;
            letter-spacing: 0.5px;
        }
        
        .voucher-item-discount {
            background: #28a745;
            color: white;
            padding: 4px 10px;
            border-radius: 12px;
            font-weight: 600;
            font-size: 0.85rem;
        }
        
        .voucher-item-desc {
            color: #495057;
            font-size: 0.9rem;
            margin-bottom: 8px;
        }
        
        .voucher-item-footer {
            display: flex;
            justify-content: space-between;
            font-size: 0.8rem;
            color: #6c757d;
            padding-top: 8px;
            border-top: 1px dashed #dee2e6;
        }
        
        .voucher-item-max {
            color: #dc3545;
            font-weight: 600;
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
                                        <a href="${pageContext.request.contextPath}/user/profile">Thêm địa chỉ mới</a>
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
                    
                    <!-- Mã giảm giá -->
                    <div class="checkout-card">
                        <div class="card-header">
                            <i class="bi bi-ticket-perforated-fill me-2"></i>Mã giảm giá
                        </div>
                        <div class="card-body">
                            <div class="voucher-input-group">
                                <input type="text" 
                                       id="voucherCodeInput" 
                                       class="form-control" 
                                       placeholder="Nhập mã giảm giá"
                                       style="flex: 1;">
                                <button type="button" 
                                        class="btn-apply-voucher" 
                                        onclick="applyVoucher()">
                                    Áp dụng
                                </button>
                            </div>
                            
                            <!-- Danh sách mã khả dụng -->
                            <div class="mt-3">
                                <button type="button" class="btn-toggle-voucher" onclick="toggleVoucherList()">
                                    <i class="bi bi-ticket-detailed me-2"></i>
                                    <span id="toggleText">Xem mã khả dụng</span>
                                    <i class="bi bi-chevron-down ms-2" id="toggleIcon"></i>
                                </button>
                                
                                <div id="voucherList" class="voucher-list" style="display: none;">
                                    <div class="voucher-list-loading">
                                        Đang tải...
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Applied Voucher Display -->
                            <div id="appliedVoucherInfo" class="applied-voucher" style="display: none;">
                                <div class="voucher-success">
                                    <div class="voucher-icon">
                                        <i class="bi bi-check-circle-fill"></i>
                                    </div>
                                    <div class="voucher-details">
                                        <div class="voucher-code-display" id="appliedVoucherCode"></div>
                                        <div class="voucher-description" id="appliedVoucherDescription"></div>
                                        <div class="voucher-discount" id="appliedVoucherDiscount"></div>
                                    </div>
                                    <button type="button" class="btn-remove-voucher" onclick="removeVoucher()">
                                        <i class="bi bi-x-lg"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <div id="voucherError" class="voucher-error" style="display: none;"></div>
                            
                            <!-- Hidden field for voucher ID -->
                            <input type="hidden" name="userCouponId" id="voucherIdField" value="">
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
                                    <span class="fw-bold" id="subtotalAmount">
                                        <fmt:formatNumber value="${cart.totalPrice}" type="number" maxFractionDigits="0"/>₫
                                    </span>
                                </div>
                                <div class="summary-row">
                                    <span>Phí vận chuyển:</span>
                                    <span class="fw-bold" id="shippingAmount">25,000₫</span>
                                </div>
                                <div class="summary-row" id="discountRow">
                                    <span>Giảm giá:</span>
                                    <span class="fw-bold text-success" id="discountAmount">0₫</span>
                                </div>
                                <div class="summary-row total">
                                    <span>Tổng cộng:</span>
                                    <span id="finalTotal">
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
        // Store original prices
        const originalSubtotal = ${cart.totalPrice};
        const shippingFee = 25000;
        let currentDiscount = 0;
        let appliedVoucherData = null;
        
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
        
        // Format number as Vietnamese currency
        function formatCurrency(amount) {
            return new Intl.NumberFormat('vi-VN').format(Math.round(amount)) + '₫';
        }
        
        // Toggle voucher list
        let vouchersLoaded = false;
        function toggleVoucherList() {
            const list = document.getElementById('voucherList');
            const btn = document.querySelector('.btn-toggle-voucher');
            const text = document.getElementById('toggleText');
            const icon = document.getElementById('toggleIcon');
            
            if (list.style.display === 'none') {
                // Show list
                list.style.display = 'block';
                btn.classList.add('active');
                text.textContent = 'Ẩn danh sách';
                icon.classList.remove('bi-chevron-down');
                icon.classList.add('bi-chevron-up');
                
                // Load vouchers if not loaded yet
                if (!vouchersLoaded) {
                    loadVouchers();
                }
            } else {
                // Hide list
                list.style.display = 'none';
                btn.classList.remove('active');
                text.textContent = 'Xem mã khả dụng';
                icon.classList.remove('bi-chevron-up');
                icon.classList.add('bi-chevron-down');
            }
        }
        
        // Load vouchers
        function loadVouchers() {
            const list = document.getElementById('voucherList');
            
            fetch('${pageContext.request.contextPath}/api/vouchers/available')
                .then(r => r.json())
                .then(data => {
                    if (data.success && data.vouchers && data.vouchers.length > 0) {
                        displayVoucherList(data.vouchers);
                        vouchersLoaded = true;
                    } else {
                        list.innerHTML = '<div class="voucher-list-loading">Không có mã giảm giá khả dụng</div>';
                    }
                })
                .catch(err => {
                    console.error('Error loading vouchers:', err);
                    list.innerHTML = '<div class="voucher-list-loading">Lỗi khi tải danh sách</div>';
                });
        }
        
        // Display voucher list
        function displayVoucherList(vouchers) {
            const list = document.getElementById('voucherList');
            let html = '';
            
            vouchers.forEach(v => {
                const code = v.codeVoucher || 'N/A';
                const desc = v.description || 'Mã giảm giá';
                const percent = v.discountPercent || 0;
                const maxDiscount = v.maxDiscountAmount;
                
                let dateStr = 'N/A';
                if (v.endDate) {
                    try {
                        dateStr = new Date(v.endDate).toLocaleDateString('vi-VN');
                    } catch(e) {
                        dateStr = 'N/A';
                    }
                }
                
                const maxText = maxDiscount ? formatCurrency(maxDiscount) : 'Không giới hạn';
                
                html += '<div class="voucher-list-item" onclick="selectVoucher(\'' + code + '\')">' +
                    '<div class="voucher-item-header">' +
                        '<span class="voucher-item-code">' + code + '</span>' +
                        '<span class="voucher-item-discount">-' + percent + '%</span>' +
                    '</div>' +
                    '<div class="voucher-item-desc">' + desc + '</div>' +
                    '<div class="voucher-item-footer">' +
                        '<span class="voucher-item-max">Max: ' + maxText + '</span>' +
                        '<span>HSD: ' + dateStr + '</span>' +
                    '</div>' +
                '</div>';
            });
            
            list.innerHTML = html;
        }
        
        // Select voucher from list
        function selectVoucher(code) {
            // Remove old voucher if exists
            if (appliedVoucherData) {
                removeVoucherQuiet();
            }
            
            document.getElementById('voucherCodeInput').value = code;
            toggleVoucherList(); // Hide list
            applyVoucher();
        }
        
        // Apply voucher
        function applyVoucher() {
            const voucherCode = document.getElementById('voucherCodeInput').value.trim();
            
            if (!voucherCode) {
                showVoucherError('Vui lòng nhập mã giảm giá');
                return;
            }
            
            // Remove old voucher if exists
            if (appliedVoucherData) {
                removeVoucherQuiet();
            }
            
            // Get button element safely
            const btn = document.querySelector('.btn-apply-voucher');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Đang xử lý...';
            }
            
            // Clear previous error
            hideVoucherError();
            
            // Call API to validate voucher
            fetch('${pageContext.request.contextPath}/api/voucher/validate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'voucherCode=' + encodeURIComponent(voucherCode)
            })
            .then(response => response.json())
            .then(data => {
                if (btn) {
                    btn.disabled = false;
                    btn.innerHTML = 'Áp dụng';
                }
                
                if (data.success) {
                    appliedVoucherData = data;
                    showAppliedVoucher(data);
                    updateOrderSummary(data.discountAmount);
                    
                    // Set voucher ID to hidden field
                    document.getElementById('voucherIdField').value = data.voucherId;
                    
                    // Clear input
                    document.getElementById('voucherCodeInput').value = '';
                } else {
                    showVoucherError(data.error);
                }
            })
            .catch(error => {
                if (btn) {
                    btn.disabled = false;
                    btn.innerHTML = 'Áp dụng';
                }
                console.error('Error:', error);
                showVoucherError('Có lỗi xảy ra khi áp dụng mã giảm giá');
            });
        }
        
        // Remove voucher with confirmation
        function removeVoucher() {
            if (confirm('Bạn có chắc muốn xóa mã giảm giá đã áp dụng?')) {
                removeVoucherQuiet();
            }
        }
        
        // Remove voucher without confirmation (internal use)
        function removeVoucherQuiet() {
            // Hide applied voucher display
            document.getElementById('appliedVoucherInfo').style.display = 'none';
            
            // Reset discount
            currentDiscount = 0;
            appliedVoucherData = null;
            updateOrderSummary(0);
            
            // Clear voucher ID
            document.getElementById('voucherIdField').value = '';
            
            // Enable input again
            const input = document.getElementById('voucherCodeInput');
            input.disabled = false;
            input.value = '';
            
            // Hide any errors
            hideVoucherError();
        }
        
        // Show applied voucher
        function showAppliedVoucher(data) {
            document.getElementById('appliedVoucherCode').textContent = data.voucherCode;
            document.getElementById('appliedVoucherDescription').textContent = data.voucherDescription || '';
            
            let discountText = 'Giảm ' + data.discountPercent + '%';
            if (data.maxDiscountAmount && data.maxDiscountAmount > 0) {
                discountText += ' (tối đa ' + formatCurrency(data.maxDiscountAmount) + ')';
            }
            document.getElementById('appliedVoucherDiscount').textContent = discountText;
            
            document.getElementById('appliedVoucherInfo').style.display = 'block';
            
            // Disable input when voucher is applied
            document.getElementById('voucherCodeInput').disabled = true;
        }
        
        // Update order summary
        function updateOrderSummary(discountAmount) {
            currentDiscount = discountAmount;
            
            // Update discount display
            const discountRow = document.getElementById('discountRow');
            const discountAmountEl = document.getElementById('discountAmount');
            
            if (discountAmount > 0) {
                discountAmountEl.textContent = '- ' + formatCurrency(discountAmount);
                discountRow.style.display = 'flex';
            } else {
                discountAmountEl.textContent = '0₫';
            }
            
            // Update final total
            const total = originalSubtotal + shippingFee - discountAmount;
            document.getElementById('finalTotal').textContent = formatCurrency(Math.max(0, total));
        }
        
        // Show voucher error
        function showVoucherError(message) {
            const errorEl = document.getElementById('voucherError');
            errorEl.textContent = message;
            errorEl.style.display = 'block';
            
            // Auto hide after 5 seconds
            setTimeout(() => {
                hideVoucherError();
            }, 5000);
        }
        
        // Hide voucher error
        function hideVoucherError() {
            document.getElementById('voucherError').style.display = 'none';
        }
        
        // Allow Enter key to apply voucher
        document.getElementById('voucherCodeInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                applyVoucher();
            }
        });
        
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

