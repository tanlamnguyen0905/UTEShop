<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Main Content -->
<div class="container mt-5 mb-5">
    <div class="row">
        <!-- Product Images Section -->
        <div class="col-md-6">
            <div class="product-gallery">
                <c:set var="mainImage" value="${empty productDTO.images ? 'logo.png' : productDTO.images[0]}" />
                <img src="${pageContext.request.contextPath}/image?fname=${mainImage}" id="mainImage" class="img-fluid mb-3" alt="${productDTO.productName}">
                <div class="thumbnail-images d-flex flex-wrap">
                    <c:forEach items="${productDTO.images}" var="imageUrl">
                        <img src="${pageContext.request.contextPath}/image?fname=${imageUrl}" 
                             onclick="changeMainImage('${pageContext.request.contextPath}/image?fname=${imageUrl}')" 
                             class="thumbnail ${imageUrl eq mainImage ? 'active' : ''}" 
                             alt="Product thumbnail">
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Product Info Section -->
        <div class="col-md-6">
            <h1 class="mb-3">${productDTO.productName}</h1>
            
            <!-- Rating Summary -->
            <div class="rating-summary mb-3">
                <div class="rating-stars">
                    <c:forEach begin="1" end="5" var="i">
                        <i class="fas fa-star ${i <= averageRating ? 'text-warning' : 'text-secondary'}"></i>
                    </c:forEach>
                    <span class="ml-2">(${reviewCount} đánh giá)</span>
                </div>
                <div class="average-rating">
                    <span class="h4">${averageRating}/5</span>
                </div>
            </div>

            <!-- Price -->
            <div class="price-tag mb-4">
                <fmt:formatNumber value="${productDTO.unitPrice}" type="currency" currencySymbol="₫"/>
                <%-- <c:if test="${productDTO.discountPrice > 0}">
                    <span class="original-price text-muted ml-2">
                        <del><fmt:formatNumber value="${productDTO.originalPrice}" type="currency" currencySymbol="₫"/></del>
                    </span>
                </c:if> --%>
            </div>

            <!-- Add to Cart Form -->
            <form id="addToCartForm" class="mb-4">
                <input type="hidden" name="productId" value="${productDTO.productID}">
                <div class="form-group">
                    <label for="quantity">Số lượng:</label>
                    <input type="number" 
                           class="form-control quantity-input" 
                           id="quantity" 
                           name="quantity" 
                           value="1" 
                           min="1" 
                           max="${productDTO.stockQuantity}">
                </div>
                <div class="action-buttons">
                    <button type="button" id="addCartBtn" onclick="addToCart()" class="btn btn-primary">
                        <i class="fas fa-shopping-cart"></i> Thêm vào giỏ hàng
                    </button>
                    <button type="button" id="wishlistBtn" onclick="addToWishlist(${product.id})" class="btn ${isFavorite ? 'btn-danger' : 'btn-outline-danger'}">
                        <i id="wishlistIcon" class="fas ${isFavorite ? 'fa-heart' : 'fa-heart'} ${isFavorite ? 'text-white' : ''}"></i>
                        <span id="wishlistText">${isFavorite ? 'Đã yêu thích' : 'Yêu thích'}</span>
                    </button>
                </div>
            </form>

            <!-- Product Details -->
            <div class="product-details mt-4">
                <h4>Thông tin sản phẩm</h4>
                <p>${productDTO.description}</p>
                <table class="table table-bordered">
                    <c:if test="${not empty productDTO.brand}">
                        <tr>
                            <td>Thương hiệu</td>
                            <td>${productDTO.brand}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>Tình trạng</td>
                        <td>${productDTO.stockQuantity > 0 ? 'Còn hàng' : 'Hết hàng'}</td>
                    </tr>
                    <tr>
                        <td>Danh mục</td>
                        <td>${productDTO.category}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <!-- Reviews Section -->
    <div class="reviews-section mt-5">
        <h3>Đánh giá sản phẩm</h3>
        
        <!-- Review List -->
        <div class="review-list mt-4">
            <c:forEach items="${reviews}" var="review">
                <div class="review-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="user-info">
                            <h5 class="mb-0">${review.user.fullname}</h5>
                            <div class="rating-stars">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fas fa-star ${i <= review.rating ? 'text-warning' : 'text-secondary'}"></i>
                                </c:forEach>
                            </div>
                        </div>
                        <small class="text-muted">
                            <fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy"/>
                        </small>
                    </div>
                    <p class="mt-2 mb-0">${review.content}</p>
                </div>
            </c:forEach>
        </div>

        <!-- Add Review Form -->
        <c:if test="${not empty sessionScope.user}">
            <div class="add-review-form mt-4">
                <h4>Thêm đánh giá của bạn</h4>
                <form id="reviewForm" class="mt-3">
                    <input type="hidden" name="productId" value="${product.productID}">
                    <div class="form-group">
                        <label>Đánh giá:</label>
                        <div class="rating-input">
                            <c:forEach begin="5" end="1" step="-1" var="i">
                                <input type="radio" id="star${i}" name="rating" value="${i}">
                                <label for="star${i}"><i class="fas fa-star"></i></label>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="comment">Nhận xét của bạn:</label>
                        <textarea class="form-control" 
                                  id="comment" 
                                  name="comment" 
                                  rows="3" 
                                  required></textarea>
                    </div>
                    <button type="button" onclick="submitReview()" class="btn btn-primary">
                        Gửi đánh giá
                    </button>
                </form>
            </div>
        </c:if>
    </div>
</div>

<!-- CSS -->
<style>
    .product-gallery img {
        max-width: 100%;
        height: auto;
    }
    .thumbnail-images img {
        width: 80px;
        height: 80px;
        object-fit: cover;
        cursor: pointer;
        margin: 5px;
        border: 2px solid transparent;
    }
    .thumbnail-images img.active {
        border-color: #007bff;
    }
    .rating-stars i {
        color: #ffc107;
    }
    .rating-input {
        display: inline-flex;
        flex-direction: row-reverse;
    }
    .rating-input input {
        display: none;
    }
    .rating-input label {
        cursor: pointer;
        color: #ddd;
        font-size: 24px;
        padding: 0 2px;
    }
    .rating-input input:checked ~ label,
    .rating-input label:hover,
    .rating-input label:hover ~ label {
        color: #ffc107;
    }
    .review-item {
        border-bottom: 1px solid #eee;
        padding: 15px 0;
    }
    .quantity-input {
        width: 100px;
    }
    .price-tag {
        font-size: 24px;
        color: #dc3545;
        font-weight: bold;
    }
    .action-buttons .btn {
        margin-right: 10px;
    }
</style>

<!-- JavaScript -->
<script>
function changeMainImage(imageUrl) {
    document.getElementById('mainImage').src = imageUrl;
    document.querySelectorAll('.thumbnail').forEach(thumb => {
        thumb.classList.remove('active');
        if(thumb.src === imageUrl) {
            thumb.classList.add('active');
        }
    });
}

function addToCart() {
    const quantity = document.getElementById('quantity').value;
    const productId = document.querySelector('input[name="productId"]').value;

    fetch('${pageContext.request.contextPath}/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}&quantity=${quantity}`
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            handleAddToCartSuccess(data);
        } else {
            showToast('Lỗi', data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng', 'danger');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Lỗi', 'Có lỗi xảy ra khi thêm vào giỏ hàng', 'danger');
    });
}

function addToWishlist(productId) {
    fetch('${pageContext.request.contextPath}/wishlist/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}`
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            // Toggle UI state
            const wishlistBtn = document.getElementById('wishlistBtn');
            const wishlistIcon = document.getElementById('wishlistIcon');
            const wishlistText = document.getElementById('wishlistText');
            if(data.action === 'added') {
                wishlistBtn.classList.remove('btn-outline-danger');
                wishlistBtn.classList.add('btn-danger');
                wishlistIcon.classList.add('text-white');
                wishlistText.textContent = 'Đã yêu thích';
                showToast('Thành công', 'Đã thêm vào danh sách yêu thích', 'success');
            } else if(data.action === 'removed') {
                wishlistBtn.classList.remove('btn-danger');
                wishlistBtn.classList.add('btn-outline-danger');
                wishlistIcon.classList.remove('text-white');
                wishlistText.textContent = 'Yêu thích';
                showToast('Đã gỡ', 'Đã gỡ khỏi danh sách yêu thích', 'info');
            } else {
                showToast('Thành công', data.message || 'Hành động yêu thích hoàn tất', 'success');
            }
        } else {
            showToast('Lưu ý', data.message || 'Vui lòng đăng nhập để thêm vào danh sách yêu thích', 'warning');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Lỗi', 'Có lỗi xảy ra khi thêm vào danh sách yêu thích', 'danger');
    });
}

function submitReview() {
    const form = document.getElementById('reviewForm');
    const formData = new FormData(form);

    fetch('${pageContext.request.contextPath}/review/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            showToast('Cảm ơn', 'Cảm ơn bạn đã đánh giá sản phẩm!', 'success');
            setTimeout(() => location.reload(), 800);
        } else {
            alert(data.message || 'Có lỗi xảy ra khi gửi đánh giá');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi gửi đánh giá');
    });
}

// Helper to show bootstrap toasts (requires bootstrap 5)
function showToast(title, message, type = 'info') {
    // Create toast element
    const toastId = 'toast' + Date.now();
    const wrapper = document.createElement('div');
    wrapper.innerHTML = `\
        <div id="${toastId}" class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">\
            <div class="d-flex">\
                <div class="toast-body">\
                    <strong class="me-1">${title}</strong> ${message}\
                </div>\
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>\
            </div>\
        </div>`;
    const container = document.getElementById('toastContainer');
    container.appendChild(wrapper);
    const toastEl = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();
    // Remove after hidden
    toastEl.addEventListener('hidden.bs.toast', () => wrapper.remove());
}

// Update cart badge helper
function updateCartBadge(count) {
    const badge = document.getElementById('cartCountBadge');
    if(badge) {
        badge.textContent = count;
    }
}

// After add to cart success, update header badge if returned
function handleAddToCartSuccess(data) {
    if(data.cartCount !== undefined) {
        updateCartBadge(data.cartCount);
    }
    showToast('Thành công', 'Sản phẩm đã được thêm vào giỏ hàng!', 'success');
}
</script>

<!-- Toast container -->
<div id="toastContainer" class="position-fixed top-0 end-0 p-3" style="z-index: 11000;"></div>
