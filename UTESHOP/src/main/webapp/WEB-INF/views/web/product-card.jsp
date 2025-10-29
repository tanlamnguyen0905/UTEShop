<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Product Card Component - Reusable product card with quick add to cart --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="card border-0 shadow-sm rounded-4 h-100 product-card">
    <c:choose>
        <c:when test="${not empty p.images and not empty p.images[0]}">
            <c:set var="productImage" value="${p.images[0]}" />
        </c:when>
        <c:otherwise>
            <c:set var="productImage" value="images/logo.png" />
        </c:otherwise>
    </c:choose>
    <c:url value="/assets/uploads/product/${productImage}" var="imgUrl" />
    
    <div class="position-relative">
        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}" 
           class="text-decoration-none">
            <img src="${imgUrl}" class="card-img-top rounded-4" alt="${p.productName}"
                 style="height: 250px; object-fit: cover;">
        </a>
    </div>
    
    <div class="card-body p-3">
        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}" 
           class="text-decoration-none">
            <h6 class="text-dark mb-2 product-name">${p.productName}</h6>
        </a>
        
        <!-- Price -->
        <div class="mb-2">
            <span class="fw-bold text-danger fs-5">
                <fmt:formatNumber value="${p.unitPrice}" type="number" maxFractionDigits="0" />₫
            </span>
        </div>
        
        <!-- Rating and Review Count -->
        <div class="mb-2">
            <div class="d-flex align-items-center">
                <!-- Display 5 stars -->
                <c:forEach begin="1" end="5" var="star">
                    <c:choose>
                        <c:when test="${p.rating >= star}">
                            <!-- Full star -->
                            <i class="bi bi-star-fill text-warning"></i>
                        </c:when>
                        <c:when test="${p.rating >= star - 0.5 && p.rating < star}">
                            <!-- Half star -->
                            <i class="bi bi-star-half text-warning"></i>
                        </c:when>
                        <c:otherwise>
                            <!-- Empty star -->
                            <i class="bi bi-star text-warning"></i>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                
                <!-- Rating Number and Review Count -->
                <c:if test="${p.rating > 0}">
                    <span class="ms-1 text-muted small">
                        <fmt:formatNumber value="${p.rating}" type="number" maxFractionDigits="1" />
                        (<fmt:formatNumber value="${p.reviewCount}" type="number" />)
                    </span>
                </c:if>
                <c:if test="${p.rating == 0 || empty p.rating}">
                    <span class="ms-1 text-muted small">(Chưa có đánh giá)</span>
                </c:if>
            </div>
        </div>
        
        <!-- Sold Count -->
        <div class="mb-2">
            <span class="text-muted small">
                <i class="bi bi-bag-check"></i>
                Đã bán: <fmt:formatNumber value="${p.soldCount}" type="number" />
            </span>
        </div>
        
        <!-- Stock status -->
        <c:if test="${p.stockQuantity <= 0}">
            <span class="badge bg-secondary mt-1">Hết hàng</span>
        </c:if>
        
        <!-- Action Buttons -->
        <div class="mt-3">
            <c:choose>
                <c:when test="${p.stockQuantity > 0}">
                    <!-- Nút Giỏ hàng - Full width -->
                    <button class="btn btn-outline-primary btn-sm w-100 mb-2" 
                            onclick="addToCart(${p.productID}, 1)"
                            title="Thêm vào giỏ hàng">
                        <i class="bi bi-cart-plus me-1"></i>Thêm vào giỏ hàng
                    </button>
                    
                    <!-- Nút Mua ngay - Full width, nổi bật -->
                    <button class="btn btn-buy-now btn-sm w-100" 
                            onclick="buyNow(${p.productID})"
                            title="Mua ngay">
                        <i class="bi bi-lightning-charge-fill me-1"></i>Mua ngay
                    </button>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-secondary btn-sm w-100" disabled>
                        <i class="bi bi-x-circle me-1"></i>Hết hàng
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<style>
    /* Product Card - White background nổi bật */
    .product-card {
        background: #ffffff;
        border: 1px solid rgba(0, 0, 0, 0.06) !important;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;
    }
    
    .product-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, rgba(102, 126, 234, 0.02) 0%, rgba(118, 75, 162, 0.02) 100%);
        opacity: 0;
        transition: opacity 0.3s ease;
        pointer-events: none;
    }
    
    .product-card:hover::before {
        opacity: 1;
    }
    
    .product-card .product-name {
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        min-height: 3em;
    }
    
    .product-card:hover {
        transform: translateY(-8px);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        box-shadow: 0 12px 24px rgba(102, 126, 234, 0.15), 
                    0 6px 12px rgba(0, 0, 0, 0.1) !important;
        border-color: rgba(102, 126, 234, 0.2) !important;
    }
    
    .product-card .card-img-top {
        background: #f8f9fa;
        transition: transform 0.3s ease;
    }
    
    .product-card:hover .card-img-top {
        transform: scale(1.05);
    }
    
    /* Nút Mua ngay - Gradient cam/đỏ nổi bật */
    .btn-buy-now {
        background: linear-gradient(135deg, #ff6b6b 0%, #ff8e53 100%);
        color: white;
        border: none;
        font-weight: 600;
        transition: all 0.3s ease;
        box-shadow: 0 2px 8px rgba(255, 107, 107, 0.3);
    }
    
    .btn-buy-now:hover {
        background: linear-gradient(135deg, #ff5252 0%, #ff7043 100%);
        color: white;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(255, 107, 107, 0.5);
    }
    
    .btn-buy-now:active {
        transform: translateY(0);
    }
</style>

<script>
    function buyNow(productId) {
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                // Đã đăng nhập - thêm vào giỏ và chuyển checkout
                addToCart(productId, 1, function(success) {
                    if (success) {
                        window.location.href = '${pageContext.request.contextPath}/checkout';
                    }
                });
            </c:when>
            <c:otherwise>
                // Chưa đăng nhập - lưu ý định mua ngay và hiện modal login
                sessionStorage.setItem('buyNowProductId', productId);
                sessionStorage.setItem('redirectAfterLogin', 'checkout');
                showLoginModal();
            </c:otherwise>
        </c:choose>
    }
    
    // Kiểm tra khi trang load - xử lý sau khi login thành công
    <c:if test="${not empty sessionScope.currentUser}">
        document.addEventListener('DOMContentLoaded', function() {
            const buyNowProductId = sessionStorage.getItem('buyNowProductId');
            const redirectAfterLogin = sessionStorage.getItem('redirectAfterLogin');
            
            if (buyNowProductId && redirectAfterLogin === 'checkout') {
                // Người dùng vừa login và muốn mua ngay
                sessionStorage.removeItem('buyNowProductId');
                sessionStorage.removeItem('redirectAfterLogin');
                
                // Thêm vào giỏ hàng và chuyển đến checkout
                addToCart(parseInt(buyNowProductId), 1, function(success) {
                    if (success) {
                        setTimeout(function() {
                            window.location.href = '${pageContext.request.contextPath}/checkout';
                        }, 500);
                    }
                });
            }
        });
    </c:if>
</script>
