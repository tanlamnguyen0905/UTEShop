<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ute.entities.Review" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Main Content -->
<div class="product-detail-wrapper">
    <div class="container py-5">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home"><i class="fas fa-home me-1"></i>Trang chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/shop">Sản phẩm</a></li>
                <li class="breadcrumb-item active" aria-current="page">${productDTO.productName}</li>
            </ol>
        </nav>

        <!-- Product Detail Card -->
        <div class="product-detail-card">
            <div class="row g-4">
                <!-- Product Gallery -->
                <div class="col-lg-5">
                    <div class="product-gallery-wrapper">
                        <div class="main-image-container">
                            <c:set var="mainImage" value="${empty productDTO.images ? 'logo.png' : productDTO.images[0]}" />
                            <img src="${pageContext.request.contextPath}/image?fname=${mainImage}" 
                                 id="mainImage" 
                                 class="main-product-image" 
                                 alt="${productDTO.productName}">
                            <c:if test="${productDTO.stockQuantity <= 0}">
                                <div class="out-of-stock-badge">
                                    <span>Hết hàng</span>
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="thumbnail-gallery">
                            <c:forEach items="${productDTO.images}" var="imageUrl" varStatus="status">
                                <div class="thumbnail-item ${status.index == 0 ? 'active' : ''}" 
                                     onclick="changeMainImage('${pageContext.request.contextPath}/image?fname=${imageUrl}', this)">
                                    <img src="${pageContext.request.contextPath}/image?fname=${imageUrl}" alt="Thumbnail">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <!-- Product Info -->
                <div class="col-lg-7">
                    <div class="product-info-wrapper">
                        <!-- Title -->
                        <h1 class="product-title">${productDTO.productName}</h1>
                        
                        <!-- Rating & Stats -->
                        <div class="product-stats">
                            <div class="rating-section">
                                <div class="stars-display">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fas fa-star ${i <= productDTO.rating ? 'filled' : ''}"></i>
                                    </c:forEach>
                                </div>
                                <span class="rating-text">
                                    <strong><fmt:formatNumber value="${productDTO.rating}" type="number" maxFractionDigits="1" /></strong>
                                    (<fmt:formatNumber value="${productDTO.reviewCount}" type="number" /> đánh giá)
                                </span>
                            </div>
                            <span class="separator">|</span>
                            <div class="sold-count">
                                <i class="fas fa-box-open me-1"></i>
                                Đã bán: <strong><fmt:formatNumber value="${productDTO.soldCount}" type="number" /></strong>
                            </div>
                        </div>

                        <!-- Price -->
                        <div class="price-section">
                            <div class="current-price">
                                <fmt:formatNumber value="${productDTO.unitPrice}" type="number" maxFractionDigits="0" />₫
                            </div>
                        </div>

                        <!-- Product Details Table -->
                        <div class="product-meta">
                            <div class="meta-item">
                                <span class="meta-label"><i class="fas fa-tag me-2"></i>Thương hiệu:</span>
                                <span class="meta-value">${productDTO.brand}</span>
                            </div>
                            <div class="meta-item">
                                <span class="meta-label"><i class="fas fa-layer-group me-2"></i>Danh mục:</span>
                                <span class="meta-value">${productDTO.category}</span>
                            </div>
                            <div class="meta-item">
                                <span class="meta-label"><i class="fas fa-box me-2"></i>Tình trạng:</span>
                                <span class="meta-value ${productDTO.stockQuantity > 0 ? 'text-success' : 'text-danger'}">
                                    <i class="fas fa-circle me-1" style="font-size: 8px;"></i>
                                    ${productDTO.stockQuantity > 0 ? 'Còn hàng' : 'Hết hàng'}
                                </span>
                            </div>
                        </div>

                        <!-- Add to Cart Form -->
                        <form id="addToCartForm" class="purchase-section">
                            <input type="hidden" name="productId" value="${productDTO.productID}">
                            
                            <div class="quantity-selector">
                                <label class="form-label fw-semibold">Số lượng:</label>
                                <div class="quantity-control">
                                    <button type="button" class="qty-btn" onclick="decreaseQty()">
                                        <i class="fas fa-minus"></i>
                                    </button>
                                    <input type="number" 
                                           class="qty-input" 
                                           id="quantity" 
                                           name="quantity" 
                                           value="1" 
                                           min="1" 
                                           max="${productDTO.stockQuantity}"
                                           readonly>
                                    <button type="button" class="qty-btn" onclick="increaseQty()">
                                        <i class="fas fa-plus"></i>
                                    </button>
                                </div>
                                <span class="stock-info text-muted">
                                    (Còn ${productDTO.stockQuantity} sản phẩm)
                                </span>
                            </div>

                            <div class="action-buttons-group">
                                <button type="button" 
                                        class="btn btn-add-cart" 
                                        onclick="handleAddToCartClick()"
                                        ${productDTO.stockQuantity <= 0 ? 'disabled' : ''}>
                                    <i class="fas fa-shopping-cart me-2"></i>
                                    Thêm vào giỏ hàng
                                </button>
                                
                                <button type="button" 
                                        class="btn btn-wishlist ${isFavorite ? 'active' : ''}" 
                                        id="wishlistBtn"
                                        onclick="addToWishlist(${productDTO.productID})">
                                    <i class="fas fa-heart" id="wishlistIcon"></i>
                                </button>
                            </div>
                        </form>

                        <!-- Features -->
                        <div class="product-features">
                            <div class="feature-item">
                                <i class="fas fa-shipping-fast"></i>
                                <span>Miễn phí vận chuyển</span>
                            </div>
                            <div class="feature-item">
                                <i class="fas fa-undo-alt"></i>
                                <span>Đổi trả trong 7 ngày</span>
                            </div>
                            <div class="feature-item">
                                <i class="fas fa-shield-alt"></i>
                                <span>Bảo hành chính hãng</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Product Description & Reviews -->
        <div class="row mt-4">
            <div class="col-12">
                <!-- Tabs Navigation -->
                <ul class="nav nav-tabs detail-tabs" role="tablist">
                    <li class="nav-item">
                        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#description">
                            <i class="fas fa-info-circle me-2"></i>Mô tả sản phẩm
                        </button>
                    </li>
                    <li class="nav-item">
                        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#reviews">
                            <i class="fas fa-star me-2"></i>Đánh giá (<fmt:formatNumber value="${productDTO.reviewCount}" type="number" />)
                        </button>
                    </li>
                </ul>

                <!-- Tabs Content -->
                <div class="tab-content detail-tab-content">
                    <!-- Description Tab -->
                    <div class="tab-pane fade show active" id="description">
                        <div class="description-content">
                            <h4 class="mb-3">Thông tin chi tiết</h4>
                            <p>${productDTO.description}</p>
                        </div>
                    </div>

                    <!-- Reviews Tab -->
                    <div class="tab-pane fade" id="reviews">
                        <div class="reviews-wrapper">
                            <h4 class="mb-4">Đánh giá từ khách hàng</h4>
                            
                            <c:choose>
                                <c:when test="${not empty reviews}">
                                    <div class="reviews-list">
                                        <c:forEach items="${reviews}" var="review">
                                            <div class="review-card">
                                                <div class="review-header">
                                                    <div class="reviewer-info">
                                                        <div class="reviewer-avatar">
                                                            <i class="fas fa-user-circle"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="reviewer-name">${review.user.fullname}</h6>
                                                            <div class="review-rating">
                                                                <c:forEach begin="1" end="5" var="i">
                                                                    <i class="fas fa-star ${i <= review.rating ? 'filled' : ''}"></i>
                                                                </c:forEach>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="review-date">
                                                        <i class="far fa-clock me-1"></i>
                                                        <%
                                                            Review currentReview = (Review) pageContext.getAttribute("review");
                                                            if (currentReview != null && currentReview.getCreatedAt() != null) {
                                                                out.print(currentReview.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                                
                                                <div class="review-content">
                                                    <p>${review.content}</p>
                                                    <c:if test="${not empty review.image && review.image != 'logo.png'}">
                                                        <div class="review-image">
                                                            <img src="${pageContext.request.contextPath}/image?fname=${review.image}" 
                                                                 alt="Review image">
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="empty-reviews">
                                        <i class="fas fa-comments fa-3x mb-3"></i>
                                        <p>Chưa có đánh giá nào cho sản phẩm này</p>
                                        <small class="text-muted">Hãy là người đầu tiên đánh giá sản phẩm này!</small>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Toast Container -->
<div id="toastContainer" class="position-fixed top-0 end-0 p-3" style="z-index: 11000;"></div>

<!-- Include Cart JS -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>

<!-- Styles -->
<style>
    /* Product Detail Wrapper */
    .product-detail-wrapper {
        background: #fff;
        min-height: 100vh;
    }
    
    .breadcrumb {
        background: transparent;
        padding: 0;
        margin: 0;
    }
    
    .breadcrumb-item a {
        color: #667eea;
        text-decoration: none;
        transition: color 0.3s;
    }
    
    .breadcrumb-item a:hover {
        color: #764ba2;
    }
    
    /* Product Detail Card */
    .product-detail-card {
        background: #fff;
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
    }
    
    /* Product Gallery */
    .product-gallery-wrapper {
        position: sticky;
        top: 100px;
    }
    
    .main-image-container {
        position: relative;
        background: #f8f9fa;
        border-radius: 15px;
        overflow: hidden;
        margin-bottom: 15px;
    }
    
    .main-product-image {
        width: 100%;
        height: auto;
        max-height: 500px;
        object-fit: contain;
        padding: 20px;
    }
    
    .out-of-stock-badge {
        position: absolute;
        top: 20px;
        right: 20px;
        background: rgba(220, 53, 69, 0.9);
        color: white;
        padding: 8px 20px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 14px;
    }
    
    .thumbnail-gallery {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
        gap: 10px;
    }
    
    .thumbnail-item {
        border: 2px solid #e9ecef;
        border-radius: 10px;
        overflow: hidden;
        cursor: pointer;
        transition: all 0.3s;
        aspect-ratio: 1;
    }
    
    .thumbnail-item:hover {
        border-color: #667eea;
        transform: translateY(-3px);
    }
    
    .thumbnail-item.active {
        border-color: #667eea;
        box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
    }
    
    .thumbnail-item img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    
    /* Product Info */
    .product-info-wrapper {
        padding-left: 20px;
    }
    
    .product-title {
        font-size: 28px;
        font-weight: 700;
        color: #2d3436;
        margin-bottom: 15px;
        line-height: 1.4;
    }
    
    .product-stats {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
        padding: 15px 0;
        border-top: 1px solid #e9ecef;
        border-bottom: 1px solid #e9ecef;
    }
    
    .rating-section {
        display: flex;
        align-items: center;
        gap: 8px;
    }
    
    .stars-display i {
        color: #ddd;
        font-size: 18px;
    }
    
    .stars-display i.filled {
        color: #ffc107;
    }
    
    .rating-text {
        color: #6c757d;
        font-size: 14px;
    }
    
    .separator {
        color: #dee2e6;
    }
    
    .sold-count {
        color: #6c757d;
        font-size: 14px;
    }
    
    /* Price */
    .price-section {
        background: linear-gradient(135deg, #fff5f5 0%, #ffe0e0 100%);
        padding: 20px;
        border-radius: 12px;
        margin-bottom: 25px;
    }
    
    .current-price {
        font-size: 32px;
        font-weight: 700;
        color: #dc3545;
    }
    
    /* Product Meta */
    .product-meta {
        background: #f8f9fa;
        border-radius: 12px;
        padding: 20px;
        margin-bottom: 25px;
    }
    
    .meta-item {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px solid #e9ecef;
    }
    
    .meta-item:last-child {
        border-bottom: none;
    }
    
    .meta-label {
        color: #6c757d;
        font-weight: 500;
    }
    
    .meta-value {
        font-weight: 600;
        color: #2d3436;
    }
    
    /* Purchase Section */
    .purchase-section {
        margin-bottom: 25px;
    }
    
    .quantity-selector {
        margin-bottom: 20px;
    }
    
    .quantity-control {
        display: inline-flex;
        align-items: center;
        gap: 0;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        overflow: hidden;
        margin-right: 15px;
    }
    
    .qty-btn {
        background: #fff;
        border: none;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s;
        color: #667eea;
    }
    
    .qty-btn:hover {
        background: #667eea;
        color: white;
    }
    
    .qty-input {
        border: none;
        border-left: 1px solid #e9ecef;
        border-right: 1px solid #e9ecef;
        width: 60px;
        height: 40px;
        text-align: center;
        font-weight: 600;
        font-size: 16px;
    }
    
    .stock-info {
        font-size: 13px;
    }
    
    /* Action Buttons */
    .action-buttons-group {
        display: flex;
        gap: 15px;
    }
    
    .btn-add-cart {
        flex: 1;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        border: none;
        padding: 15px 30px;
        border-radius: 12px;
        font-weight: 600;
        font-size: 16px;
        transition: all 0.3s;
    }
    
    .btn-add-cart:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        color: white;
    }
    
    .btn-add-cart:disabled {
        background: #6c757d;
        cursor: not-allowed;
    }
    
    .btn-wishlist {
        width: 55px;
        height: 55px;
        border: 2px solid #dc3545;
        background: white;
        color: #dc3545;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        transition: all 0.3s;
        cursor: pointer;
    }
    
    .btn-wishlist:hover {
        background: #dc3545;
        color: white;
        transform: scale(1.05);
    }
    
    .btn-wishlist.active {
        background: #dc3545;
        color: white;
    }
    
    /* Features */
    .product-features {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 15px;
        padding: 20px;
        background: #f8f9fa;
        border-radius: 12px;
    }
    
    .feature-item {
        display: flex;
        align-items: center;
        gap: 10px;
        color: #667eea;
        font-size: 14px;
    }
    
    .feature-item i {
        font-size: 20px;
    }
    
    /* Tabs */
    .detail-tabs {
        border-bottom: 2px solid #e9ecef;
        margin-bottom: 0;
    }
    
    .detail-tabs .nav-link {
        border: none;
        color: #6c757d;
        font-weight: 600;
        padding: 15px 25px;
        border-bottom: 3px solid transparent;
        transition: all 0.3s;
    }
    
    .detail-tabs .nav-link:hover {
        color: #667eea;
        border-bottom-color: #667eea;
    }
    
    .detail-tabs .nav-link.active {
        color: #667eea;
        background: transparent;
        border-bottom-color: #667eea;
    }
    
    .detail-tab-content {
        background: white;
        border-radius: 0 0 15px 15px;
        padding: 30px;
    }
    
    /* Reviews */
    .reviews-wrapper h4 {
        color: #2d3436;
        font-weight: 700;
    }
    
    .reviews-list {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }
    
    .review-card {
        border: 1px solid #e9ecef;
        border-radius: 12px;
        padding: 20px;
        transition: all 0.3s;
    }
    
    .review-card:hover {
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
    }
    
    .review-header {
        display: flex;
        justify-content: space-between;
        align-items: start;
        margin-bottom: 15px;
    }
    
    .reviewer-info {
        display: flex;
        gap: 15px;
        align-items: center;
    }
    
    .reviewer-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 24px;
    }
    
    .reviewer-name {
        font-weight: 600;
        color: #2d3436;
        margin-bottom: 5px;
    }
    
    .review-rating i {
        font-size: 14px;
        color: #ddd;
    }
    
    .review-rating i.filled {
        color: #ffc107;
    }
    
    .review-date {
        color: #6c757d;
        font-size: 13px;
    }
    
    .review-content p {
        color: #495057;
        line-height: 1.6;
        margin-bottom: 10px;
    }
    
    .review-image img {
        max-width: 200px;
        border-radius: 8px;
        margin-top: 10px;
    }
    
    .empty-reviews {
        text-align: center;
        padding: 60px 20px;
        color: #6c757d;
    }
    
    .empty-reviews i {
        color: #dee2e6;
    }
    
    /* Responsive */
    @media (max-width: 991px) {
        .product-detail-card {
            padding: 20px;
        }
        
        .product-info-wrapper {
            padding-left: 0;
            margin-top: 20px;
        }
        
        .product-title {
            font-size: 24px;
        }
        
        .current-price {
            font-size: 28px;
        }
        
        .product-gallery-wrapper {
            position: relative;
            top: 0;
        }
    }
    
    @media (max-width: 576px) {
        .action-buttons-group {
            flex-direction: column;
        }
        
        .btn-wishlist {
            width: 100%;
        }
        
        .product-features {
            grid-template-columns: 1fr;
        }
    }
</style>

<!-- JavaScript -->
<script>
// Change main image
function changeMainImage(imageUrl, thumbnail) {
    document.getElementById('mainImage').src = imageUrl;
    document.querySelectorAll('.thumbnail-item').forEach(item => {
        item.classList.remove('active');
    });
    thumbnail.classList.add('active');
}

// Quantity controls
function increaseQty() {
    const input = document.getElementById('quantity');
    const max = parseInt(input.max);
    const current = parseInt(input.value);
    if (current < max) {
        input.value = current + 1;
    }
}

function decreaseQty() {
    const input = document.getElementById('quantity');
    const min = parseInt(input.min);
    const current = parseInt(input.value);
    if (current > min) {
        input.value = current - 1;
    }
}

// Add to cart
function handleAddToCartClick() {
    const quantity = parseInt(document.getElementById('quantity').value) || 1;
    const productId = parseInt(document.querySelector('input[name="productId"]').value);
    
    if (typeof addToCart === 'function') {
        addToCart(productId, quantity);
    } else {
        console.error('Function addToCart from cart.js is not loaded');
        showToast('Lỗi', 'Không thể tải chức năng giỏ hàng', 'danger');
    }
}

// Add to wishlist
function addToWishlist(productId) {
    fetch('${pageContext.request.contextPath}/wishlist/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'productId=' + productId
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            const wishlistBtn = document.getElementById('wishlistBtn');
            if(data.action === 'added') {
                wishlistBtn.classList.add('active');
                showToast('Thành công', 'Đã thêm vào danh sách yêu thích', 'success');
            } else if(data.action === 'removed') {
                wishlistBtn.classList.remove('active');
                showToast('Đã gỡ', 'Đã gỡ khỏi danh sách yêu thích', 'info');
            }
        } else {
            showToast('Lưu ý', data.message || 'Vui lòng đăng nhập để thêm vào danh sách yêu thích', 'warning');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

// Show toast
function showToast(title, message, type = 'info') {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toastId = 'toast' + Date.now();
    const wrapper = document.createElement('div');
    wrapper.innerHTML = `
        <div id="${toastId}" class="toast align-items-center text-bg-${type} border-0 shadow-sm" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <strong class="me-1">${title}</strong> ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>`;
    
    container.appendChild(wrapper);
    const toastEl = document.getElementById(toastId);

    if (toastEl && typeof bootstrap !== 'undefined' && bootstrap.Toast) {
        const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
        toast.show();
        toastEl.addEventListener('hidden.bs.toast', () => wrapper.remove());
    }
}
</script>
