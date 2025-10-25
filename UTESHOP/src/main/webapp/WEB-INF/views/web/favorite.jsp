<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Sản phẩm yêu thích | UTESHOP</title>

  <!-- Bootstrap CSS (nếu layout tổng chưa nhúng) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet" />

  <style>
    body { background-color: #f7f8fa; }
    .page-header { background: #fff; border-radius: 14px; padding: 18px 22px; box-shadow: 0 2px 16px rgba(16,24,40,.06); }
    .fav-card { background:#fff; border: 1px solid #edf0f3; border-radius: 14px; box-shadow: 0 1px 10px rgba(16,24,40,.05); }
    .fav-item + .fav-item { border-top: 1px dashed #e9ecef; }
    .fav-image { width: 92px; height: 92px; border-radius: 10px; object-fit: cover; border: 1px solid #eef1f5; }
    .product-title { font-weight: 600; color: #212529; text-decoration: none; }
    .product-title:hover { color: #0d6efd; }
    .price { color: #dc3545; font-weight: 700; }
    .brand, .stock { font-size: .875rem; }
    .action-icon { cursor: pointer; transition: transform .15s ease; }
    .action-icon:hover { transform: scale(1.06); }
    .empty-state { text-align:center; padding: 64px 16px; background:#fff; border-radius:14px; border: 1px dashed #e9edf2; }
    .empty-state i { font-size: 64px; color:#c7d0da; }
    .sticky-sidebar { position: sticky; top: 88px; }
    /* Toast container */
    #toastContainer { position: fixed; top: 12px; right: 12px; z-index: 11000; }
  </style>
</head>

<body>
<div class="container my-4 my-md-5">
  <!-- Header -->
  <div class="page-header d-flex align-items-center justify-content-between mb-4">
    <div class="d-flex align-items-center gap-3">
      <div class="bg-danger-subtle rounded-circle d-flex align-items-center justify-content-center" style="width:44px;height:44px;">
        <i class="fa-solid fa-heart text-danger"></i>
      </div>
      <div>
        <h1 class="h5 mb-1">Sản phẩm yêu thích</h1>
        <c:choose>
          <c:when test="${not empty favoriteItems}">
            <div class="text-muted small">Bạn đang lưu <strong>${itemCount}</strong> sản phẩm</div>
          </c:when>
          <c:otherwise>
            <div class="text-muted small">Danh sách yêu thích đang trống</div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div>
      <a class="btn btn-outline-secondary btn-sm" href="${pageContext.request.contextPath}/">
        <i class="fa-solid fa-arrow-left me-1"></i> Tiếp tục xem hàng
      </a>
    </div>
  </div>

  <div class="row g-4">
    <!-- Danh sách yêu thích -->
    <div class="col-lg-8">
      <c:choose>
        <c:when test="${empty favoriteItems}">
          <div class="empty-state">
            <i class="fa-regular fa-heart mb-3"></i>
            <h3 class="h5 mb-2">Bạn chưa có sản phẩm yêu thích</h3>
            <p class="text-muted mb-4">Nhấn biểu tượng <i class="fa-solid fa-heart text-danger"></i> ở trang chi tiết sản phẩm để thêm vào danh sách.</p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/"><i class="fa-solid fa-store me-2"></i>Khám phá sản phẩm</a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="fav-card">
            <div class="p-3 border-bottom d-flex align-items-center justify-content-between">
              <div class="small text-muted">
                Tổng: <strong>${itemCount}</strong> sản phẩm
              </div>
              <button class="btn btn-outline-danger btn-sm" id="btnClearAll" type="button">
                <i class="fa-solid fa-trash me-1"></i> Xóa tất cả
              </button>
            </div>

            <!-- Items -->
            <div class="list-group list-group-flush">
              <c:forEach var="item" items="${favoriteItems}">
                <div class="list-group-item fav-item py-3">
                  <div class="d-flex align-items-center gap-3">
                    <!-- Image -->
                    <a class="flex-shrink-0" href="${pageContext.request.contextPath}/detailProduct?productID=${item.productID}">
                      <c:choose>
                        <c:when test="${not empty item.images && item.images.size() > 0}">
                          <img class="fav-image" src="${pageContext.request.contextPath}/image?fname=${item.images[0]}" alt="${item.productName}" />
                        </c:when>
                        <c:otherwise>
                          <img class="fav-image" src="${pageContext.request.contextPath}/assets/images/logo.png" alt="No image" />
                        </c:otherwise>
                      </c:choose>
                    </a>

                    <!-- Info -->
                    <div class="flex-grow-1">
                      <a class="product-title d-block mb-1" href="${pageContext.request.contextPath}/detailProduct?productID=${item.productID}">
                        ${item.productName}
                      </a>
                      <div class="text-muted brand mb-1">
                        <c:if test="${not empty item.brand}"><i class="fa-solid fa-tag me-1"></i>${item.brand}</c:if>
                      </div>
                      <div class="text-muted stock"><i class="fa-solid fa-box me-1"></i>Còn ${item.stockQuantity} sản phẩm</div>
                    </div>

                    <!-- Price & Actions -->
                    <div class="text-end">
                      <div class="price mb-2"><fmt:formatNumber value="${item.unitPrice}" type="number" groupingUsed="true" />₫</div>
                      <div class="d-flex align-items-center justify-content-end gap-2">
                        <button class="btn btn-sm btn-outline-primary" type="button" data-pid="${item.productID}" onclick="moveToCart(${item.productID})">
                          <i class="fa-solid fa-cart-plus me-1"></i> Thêm vào giỏ
                        </button>
                        <button class="btn btn-sm btn-outline-danger" type="button" aria-label="Xóa" onclick="removeFavorite(${item.productID})">
                          <i class="fa-solid fa-trash"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Sidebar gợi ý / ghi chú (tùy chọn) -->
    <div class="col-lg-4">
      <div class="fav-card p-3 sticky-sidebar">
        <h6 class="mb-3">Mẹo mua sắm</h6>
        <ul class="list-unstyled small text-muted mb-0">
          <li class="mb-2"><i class="fa-regular fa-lightbulb me-2 text-warning"></i>Dùng bộ lọc ở trang danh sách để tìm nhanh sản phẩm tương tự.</li>
          <li class="mb-2"><i class="fa-regular fa-clock me-2 text-primary"></i>Sản phẩm yêu thích có thể hết hàng—hãy thêm vào giỏ sớm.</li>
          <li><i class="fa-regular fa-heart me-2 text-danger"></i>Danh sách yêu thích được đồng bộ khi bạn đăng nhập.</li>
        </ul>
      </div>
    </div>
  </div>
</div>

<!-- Toast container -->
<div id="toastContainer"></div>

<!-- Optional: cart.js nếu dùng moveToCart -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>

<!-- Bootstrap JS (nếu layout tổng chưa nhúng) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  // ===== Toast helper (an toàn cho JSP) =====
  function showToast(title, message, type = 'info') {
    const container = document.getElementById('toastContainer');
    if (!container) return;
    const toastId = 't' + Date.now();
    const wrap = document.createElement('div');
    wrap.innerHTML = `
      <div id="${toastId}" class="toast align-items-center text-bg-${type} border-0 shadow-sm" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body"><strong class="me-1">${title}</strong> ${message}</div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
      </div>`;
    container.appendChild(wrap);
    const el = document.getElementById(toastId);
    if (!el || typeof bootstrap === 'undefined' || !bootstrap.Toast) return;
    const t = new bootstrap.Toast(el, { delay: 2800 });
    t.show();
    el.addEventListener('hidden.bs.toast', () => wrap.remove());
  }

  // ===== API endpoints =====
  const API_REMOVE = '${pageContext.request.contextPath}/api/favorite/remove';
  const API_CLEAR  = '${pageContext.request.contextPath}/api/favorite/clear';

  // ===== Actions =====
  async function removeFavorite(productID) {
    try {
      const res = await fetch(API_REMOVE, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ productID: productID})
      });
      const data = await res.json();
      if (data.success) {
        showToast('Đã xóa', 'Đã gỡ khỏi yêu thích', 'success');
        // Xóa phần tử khỏi DOM để mượt mà hơn thay vì reload toàn trang
      } else {
        showToast('Lỗi', data.error || 'Không thể xóa sản phẩm', 'danger');
      }
    } catch (e) {
      console.error(e);
      showToast('Lỗi', 'Không thể kết nối máy chủ', 'danger');
    }
  }

  async function clearAllFavorites() {
    try {
      const res = await fetch(API_CLEAR, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
      });
      const data = await res.json();
      if (data.success) {
        showToast('Đã xóa', 'Đã xóa khỏi danh sách yêu thích', 'success');
        setTimeout(() => location.reload(), 600);
      } else {
        showToast('Lỗi', data.error || 'Không thể xóa danh sách', 'danger');
      }
    } catch (e) {
      console.error(e);
      showToast('Lỗi', 'Không thể kết nối máy chủ', 'danger');
    }
  }

  // Gắn sự kiện cho nút Clear All
  document.getElementById('btnClearAll')?.addEventListener('click', () => {
    if (confirm('Bạn có chắc muốn xóa tất cả sản phẩm trong yêu thích?')) {
      clearAllFavorites();
    }
  });

  // Tùy chọn: thêm nhanh vào giỏ hàng từ yêu thích (nếu có cart.js)
  async function moveToCart(productId) {
    try {
      if (typeof window.addToCart === 'function') {
        await window.addToCart(productId, 1); // mặc định 1 sản phẩm
        showToast('Thành công', 'Đã thêm vào giỏ hàng', 'success');
      } else {
        // fallback gọi API nếu dự án bạn có endpoint
        // fetch('${pageContext.request.contextPath}/api/cart/add', { ... })
        showToast('Thông báo', 'Chức năng giỏ hàng chưa sẵn sàng', 'warning');
      }
    } catch (e) {
      console.error(e);
      showToast('Lỗi', 'Không thể thêm vào giỏ', 'danger');
    }
  }
</script>
</body>
</html>