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
    <c:url value="/image?fname=${productImage}" var="imgUrl" />
    
    <div class="position-relative">
        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}" 
           class="text-decoration-none">
            <img src="${imgUrl}" class="card-img-top rounded-4" alt="${p.productName}"
                 style="height: 250px; object-fit: cover;">
        </a>
        
        <!-- Quick Add to Cart Button -->
        <button class="btn btn-primary btn-sm position-absolute bottom-0 end-0 m-2 btn-quick-add"
                onclick="addToCart(${p.productID}, 1)"
                title="Thêm vào giỏ hàng"
                style="opacity: 0; transition: opacity 0.3s;">
            <i class="bi bi-cart-plus"></i>
        </button>
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
    </div>
</div>

<style>
    .product-card:hover .btn-quick-add {
        opacity: 1 !important;
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
        transform: translateY(-5px);
        transition: transform 0.3s ease;
    }
</style>
