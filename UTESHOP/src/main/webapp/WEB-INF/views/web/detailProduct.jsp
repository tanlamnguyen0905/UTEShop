<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="container py-5">

    <div class="row g-5">
        <!-- 🖼️ Ảnh sản phẩm -->
        <div class="col-lg-6">
            <div class="product-gallery text-center">
                <c:set var="mainImage" value="${empty productDTO.images ? 'logo.png' : productDTO.images[0]}" />
                <img id="mainImage" 
                     src="${pageContext.request.contextPath}/image?fname=${mainImage}" 
                     alt="${productDTO.productName}" 
                     class="img-fluid rounded shadow-sm mb-3 main-product-img" 
                     onerror="this.src='${pageContext.request.contextPath}/assets/images/default.png';">

                <div class="d-flex flex-wrap justify-content-center gap-2">
                    <c:forEach items="${productDTO.images}" var="imageUrl">
                        <img src="${pageContext.request.contextPath}/image?fname=${imageUrl}"
                             class="thumbnail-img ${imageUrl eq mainImage ? 'active' : ''}"
                             onclick="changeMainImage('${pageContext.request.contextPath}/image?fname=${imageUrl}', this)"
                             alt="Thumbnail"
                             loading="lazy">
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- 🧾 Thông tin sản phẩm -->
        <div class="col-lg-6">
            <h1 class="fw-bold mb-3">${productDTO.productName}</h1>

            <!-- ⭐ Đánh giá -->
            <div class="d-flex align-items-center mb-3">
                <div>
                    <c:forEach begin="1" end="5" var="i">
                        <i class="fas fa-star ${i <= averageRating ? 'text-warning' : 'text-muted'}"></i>
                    </c:forEach>
                    <span class="ms-2 small text-muted">(${reviewCount} đánh giá)</span>
                </div>
                <div class="ms-auto fw-semibold">${averageRating}/5</div>
            </div>

            <!-- 💰 Giá -->
            <div class="price h3 text-danger mb-4">
                <fmt:formatNumber value="${productDTO.unitPrice}" type="currency" currencySymbol="₫"/>
            </div>

            <!-- 🛒 Form giỏ hàng -->
            <form id="addToCartForm" class="mb-4">
                <input type="hidden" name="productId" value="${productDTO.productID}">
                <div class="mb-3 d-flex align-items-center gap-2">
                    <label for="quantity" class="fw-semibold">Số lượng:</label>
                    <input type="number" id="quantity" name="quantity" value="1" min="1" max="${productDTO.stockQuantity}" class="form-control w-auto">
                </div>
                <div class="d-flex gap-2">
                    <button type="button" onclick="addToCart()" class="btn btn-primary flex-fill">
                        <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
                    </button>
                    <button type="button" onclick="addToWishlist(${productDTO.productID})" id="wishlistBtn"
                            class="btn ${isFavorite ? 'btn-danger' : 'btn-outline-danger'}">
                        <i id="wishlistIcon" class="fas fa-heart ${isFavorite ? 'text-white' : ''}"></i>
                        <span id="wishlistText">${isFavorite ? 'Đã yêu thích' : 'Yêu thích'}</span>
                    </button>
                </div>
            </form>

            <!-- 📋 Chi tiết sản phẩm -->
            <div class="border-top pt-3">
                <h4 class="fw-semibold mb-3">Thông tin sản phẩm</h4>
                <p>${productDTO.description}</p>
                <table class="table table-sm table-bordered align-middle">
                    <c:if test="${not empty productDTO.brand}">
                        <tr><th>Thương hiệu</th><td>${productDTO.brand}</td></tr>
                    </c:if>
                    <tr><th>Tình trạng</th><td>${productDTO.stockQuantity > 0 ? 'Còn hàng' : 'Hết hàng'}</td></tr>
                    <tr><th>Danh mục</th><td>${productDTO.category}</td></tr>
                </table>
            </div>
        </div>
    </div>

    <!-- 💬 Đánh giá -->
    <div class="mt-5">
        <h3 class="fw-semibold mb-4">Đánh giá sản phẩm</h3>

        <div class="review-list">
            <c:forEach items="${reviews}" var="review">
                <div class="border-bottom pb-3 mb-3">
                    <div class="d-flex justify-content-between">
                        <div>
                            <strong>${review.user.fullname}</strong>
                            <div>
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fas fa-star ${i <= review.rating ? 'text-warning' : 'text-muted'}"></i>
                                </c:forEach>
                            </div>
                        </div>
                        <small class="text-muted"><fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy"/></small>
                    </div>
                    <p class="mb-0 mt-2">${review.content}</p>
                </div>
            </c:forEach>
        </div>

        <c:if test="${not empty sessionScope.currentUser}">
            <div class="mt-4">
                <h4 class="fw-semibold">Thêm đánh giá của bạn</h4>
                <form id="reviewForm" class="mt-3">
                    <input type="hidden" name="productId" value="${productDTO.productID}">
                    <div class="rating-input mb-3">
                        <c:forEach begin="5" end="1" step="-1" var="i">
                            <input type="radio" id="star${i}" name="rating" value="${i}">
                            <label for="star${i}" title="${i} sao"><i class="fas fa-star"></i></label>
                        </c:forEach>
                    </div>
                    <textarea name="comment" class="form-control mb-3" rows="3" placeholder="Viết nhận xét của bạn..." required></textarea>
                    <button type="button" onclick="submitReview()" class="btn btn-primary">Gửi đánh giá</button>
                </form>
            </div>
        </c:if>
    </div>
</div>

<!-- 🌈 CSS -->
<style>
.main-product-img { transition: transform 0.3s ease; cursor: zoom-in; }
.main-product-img:hover { transform: scale(1.05); }
.thumbnail-img { width: 80px; height: 80px; object-fit: cover; border: 2px solid transparent; border-radius: 6px; cursor: pointer; transition: transform 0.2s; }
.thumbnail-img:hover { transform: scale(1.1); border-color: #007bff; }
.thumbnail-img.active { border-color: #007bff; }
.rating-input { display: inline-flex; flex-direction: row-reverse; }
.rating-input input { display: none; }
.rating-input label { color: #ccc; font-size: 24px; cursor: pointer; }
.rating-input input:checked ~ label,
.rating-input label:hover,
.rating-input label:hover ~ label { color: #ffc107; }
</style>

<!-- ⚙️ JS -->
<script>
function changeMainImage(url, el) {
    document.getElementById("mainImage").src = url;
    document.querySelectorAll(".thumbnail-img").forEach(i => i.classList.remove("active"));
    el.classList.add("active");
}

function checkLogin() {
    if (${empty sessionScope.currentUser}) {
        showToast("Thông báo", "Vui lòng đăng nhập để tiếp tục", "warning");
        setTimeout(() => {
            window.location.href = '${pageContext.request.contextPath}/auth/login?redirect=' + encodeURIComponent(window.location.href);
        }, 1000);
        return false;
    }
    return true;
}

async function postRequest(url, data) {
    try {
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams(data)
        });
        return await res.json();
    } catch {
        showToast("Lỗi", "Không thể kết nối đến máy chủ", "danger");
        return { success: false };
    }
}

async function addToCart() {
    if (!checkLogin()) return;
    const data = await postRequest('${pageContext.request.contextPath}/cart/add', {
        productId: document.querySelector('input[name="productId"]').value,
        quantity: document.getElementById('quantity').value
    });
    if (data.success) handleAddToCartSuccess(data);
    else showToast("Lỗi", data.message || "Không thể thêm giỏ hàng", "danger");
}

async function addToWishlist(productId) {
    if (!checkLogin()) return;
    const data = await postRequest('${pageContext.request.contextPath}/wishlist/add', { productId });
    if (data.success) {
        const btn = document.getElementById('wishlistBtn');
        const icon = document.getElementById('wishlistIcon');
        const text = document.getElementById('wishlistText');
        const added = data.action === "added";
        btn.classList.toggle("btn-danger", added);
        btn.classList.toggle("btn-outline-danger", !added);
        icon.classList.toggle("text-white", added);
        text.textContent = added ? "Đã yêu thích" : "Yêu thích";
        showToast("Thành công", added ? "Đã thêm vào danh sách yêu thích" : "Đã gỡ khỏi danh sách", added ? "success" : "info");
    } else showToast("Lỗi", data.message || "Có lỗi xảy ra", "danger");
}

async function submitReview() {
    const form = document.getElementById("reviewForm");
    const data = Object.fromEntries(new FormData(form).entries());
    const res = await postRequest('${pageContext.request.contextPath}/review/add', data);
    if (res.success) {
        showToast("Cảm ơn", "Cảm ơn bạn đã đánh giá!", "success");
        setTimeout(() => location.reload(), 800);
    } else showToast("Lỗi", res.message || "Không gửi được đánh giá", "danger");
}

function handleAddToCartSuccess(data) {
    if (data.cartCount) updateCartBadge(data.cartCount);
    showToast("Thành công", "Đã thêm vào giỏ hàng!", "success");
}
function updateCartBadge(count) {
    const badge = document.getElementById("cartCountBadge");
    if (badge) badge.textContent = count;
}
function showToast(title, message, type="info") {
    const toast = document.createElement("div");
    toast.className = `toast align-items-center text-bg-${type} border-0 show`;
    toast.innerHTML = `
      <div class="d-flex">
        <div class="toast-body"><strong>${title}</strong> ${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
      </div>`;
    const container = document.getElementById("toastContainer");
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
}
</script>

<div id="toastContainer" class="position-fixed top-0 end-0 p-3" style="z-index: 11000;"></div>
