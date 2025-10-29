<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- KIỂM TRA BANNER TỒN TẠI --%>
<c:if test="${empty banner}">
  <c:redirect url="${pageContext.request.contextPath}/api/admin/banner">
    <c:param name="error" value="Banner không tồn tại"/>
  </c:redirect>
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý Banner | UTESHOP</title>

  <style>
    body { 
      background-color: #f5f7fa; 
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .banner-container { 
      max-width: 1300px; 
      margin: 40px auto; 
      padding: 0 15px; 
    }
    .card { 
      border-radius: 12px; 
      overflow: hidden; 
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }
    .card-header { 
      font-weight: 600; 
      letter-spacing: 0.3px; 
      padding: 1rem 1.25rem;
    }
    .product-row {
      border: 1px solid #eaeaea;
      border-radius: 10px;
      padding: 10px 15px;
      margin-bottom: 10px;
      display: flex;
      align-items: center;
      background: #fff;
      transition: all 0.2s ease;
    }
    .product-row:hover {
      transform: translateY(-2px);
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }
    .product-row img {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      object-fit: cover;
      margin-right: 15px;
      flex-shrink: 0;
      border: 1px solid #f0f0f0;
    }
    .search-box {
      position: relative;
    }
    .search-box input {
      border-radius: 10px;
      border: 1px solid #ccc;
      padding: 0.5rem 45px 0.5rem 1rem;
      transition: all 0.2s ease;
      width: 100%;
    }
    .search-box input:focus {
      box-shadow: 0 0 0 3px rgba(13,110,253,0.25);
      border-color: #0d6efd;
      outline: none;
    }
    .search-result {
      border: 1px solid #eaeaea;
      border-radius: 8px;
      padding: 10px;
      margin-bottom: 8px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: #fff;
      transition: all 0.15s ease;
    }
    .search-result:hover {
      background-color: #f8f9fa;
      border-color: #0d6efd;
    }
    .search-result img {
      width: 50px;
      height: 50px;
      border-radius: 6px;
      object-fit: cover;
      margin-right: 12px;
      flex-shrink: 0;
      border: 1px solid #f0f0f0;
    }
    .search-result-info {
      flex: 1;
      min-width: 0;
    }
    .search-result-info .name {
      font-weight: 600;
      font-size: 0.95rem;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: #212529;
    }
    .spinner-border {
      width: 1.2rem;
      height: 1.2rem;
      border-width: 2px;
    }
    #loadingSpinner {
      position: absolute;
      right: 12px;
      top: 50%;
      transform: translateY(-50%);
    }
    .toast { 
      border-radius: 10px !important; 
      box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
    }
    #toastContainer { 
      position: fixed; 
      top: 1rem; 
      right: 1rem; 
      z-index: 11000;
      max-width: 350px;
    }
    .btn:disabled {
      cursor: not-allowed;
      opacity: 0.6;
    }
    .empty-state {
      text-align: center;
      padding: 3rem 1rem;
      color: #6c757d;
    }
    .empty-state i {
      font-size: 3rem;
      margin-bottom: 1rem;
      opacity: 0.5;
    }
    .badge {
      font-weight: 600;
      padding: 0.35em 0.65em;
    }
    .min-w-0 {
      min-width: 0;
    }
    .flex-grow-1 {
      flex-grow: 1;
    }
    .flex-shrink-0 {
      flex-shrink: 0;
    }
  </style>
</head>

<body>
<div class="banner-container">
  <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
    <div>
      <h3 class="fw-bold text-primary mb-1">
        <i class="fa-solid fa-rectangle-ad me-2"></i>Quản lý Banner
      </h3>
      <div class="text-muted">
        Banner: <strong><c:out value="${banner.bannerName}" default="(Chưa có tên)"/></strong>
      </div>
    </div>
    <a href="${pageContext.request.contextPath}/api/admin/banner" class="btn btn-outline-secondary btn-sm">
      <i class="fa fa-arrow-left me-1"></i>Quay lại danh sách
    </a>
  </div>

  <div class="row g-4">
    <!-- LEFT: Danh sách sản phẩm trong banner -->
    <div class="col-lg-7">
      <div class="card shadow-sm border-0">
        <div class="card-header bg-primary text-white">
          <i class="fa-solid fa-boxes me-1"></i> 
          Sản phẩm trong Banner 
          <span class="badge bg-light text-primary ms-2">
            ${fn:length(banner.products)}
          </span>
        </div>
        <div class="card-body bg-light" id="productListContainer">
          <c:choose>
            <c:when test="${empty banner.products}">
              <div class="empty-state">
                <i class="fa-regular fa-face-frown"></i><br>
                <div class="fw-semibold">Chưa có sản phẩm nào</div>
                <small class="text-muted">Hãy thêm sản phẩm từ ô bên phải</small>
              </div>
            </c:when>
            <c:otherwise>
              <c:forEach var="p" items="${banner.products}">
                <div class="product-row" data-product-id="${p.productID}">
                  <c:choose>
                    <c:when test="${not empty p.images and fn:length(p.images) > 0}">
                      <img src="${pageContext.request.contextPath}/assets/uploads/category/${p.images[0]}" 
                           alt="${p.productName}"
                           onerror="this.src='${pageContext.request.contextPath}/assets/images/logo.png'">
                    </c:when>
                    <c:otherwise>
                      <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                           alt="No image">
                    </c:otherwise>
                  </c:choose>
                  <div class="flex-grow-1 min-w-0">
                    <div class="fw-semibold text-truncate">
                      <c:out value="${p.productName}"/>
                    </div>
                    <small class="text-muted">
                      <c:out value="${p.brand}" default=""/>
                    </small>
                  </div>
                  <button class="btn btn-sm btn-outline-danger remove-btn flex-shrink-0"
                          data-banner-id="${banner.bannerID}"
                          data-product-id="${p.productID}">
                    <i class="fa fa-trash"></i>
                  </button>
                </div>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>

    <!-- RIGHT: Thêm sản phẩm -->
    <div class="col-lg-5">
      <div class="card shadow-sm border-0">
        <div class="card-header bg-success text-white">
          <i class="fa-solid fa-plus me-1"></i> Thêm sản phẩm
        </div>
        <div class="card-body bg-light">
          <div class="search-box mb-3">
            <input type="text" 
                   id="searchKeyword" 
                   class="form-control" 
                   placeholder="Nhập tên sản phẩm để tìm kiếm..."
                   autocomplete="off"
                   aria-label="Tìm kiếm sản phẩm">
            <div id="loadingSpinner" class="d-none">
              <div class="spinner-border text-success" role="status">
                <span class="visually-hidden">Đang tải...</span>
              </div>
            </div>
          </div>
          <div id="searchResults" role="region" aria-live="polite"></div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Toast Container -->
<div id="toastContainer" aria-live="polite" aria-atomic="true"></div>

<%-- ===== JAVASCRIPT CONFIG DATA (SAFE APPROACH) ===== --%>
<script id="app-config" type="application/json">
{
  "contextPath": "<c:out value='${pageContext.request.contextPath}' escapeXml='false'/>",
  "bannerId": <c:out value='${banner.bannerID}' default='0'/>,
  "bannerName": "<c:out value='${banner.bannerName}' escapeXml='true'/>"
}
</script>

<script>
(function() {
  'use strict';
  
  // ============== SAFE CONFIG LOADING ==============
  let CONTEXT_PATH = '';
  let BANNER_ID = 0;
  
  try {
    const configEl = document.getElementById('app-config');
    if (configEl) {
      const config = JSON.parse(configEl.textContent);
      CONTEXT_PATH = config.contextPath || '';
      BANNER_ID = parseInt(config.bannerId, 10) || 0;
    }
  } catch (e) {
    console.error('Failed to parse config:', e);
  }

  // ============== CONSTANTS ==============
  const API_ADD = CONTEXT_PATH + '/api/admin/banner/addProduct';
  const API_REMOVE = CONTEXT_PATH + '/api/admin/banner/removeProduct';
  const API_SEARCH = CONTEXT_PATH + '/api/admin/product/search';
  const DEFAULT_IMAGE = CONTEXT_PATH + '/assets/images/logo.png';

  const DEBOUNCE_DELAY = 400;

  // ============== UTILITY FUNCTIONS ==============
  function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

  function showToast(msg, type) {
    type = type || 'info';
    const container = document.getElementById('toastContainer');
    if (!container) return;
    
    const toastId = 'toast-' + Date.now();
    const iconMap = {
      'success': 'fa-check-circle',
      'danger': 'fa-exclamation-circle',
      'warning': 'fa-exclamation-triangle',
      'info': 'fa-info-circle'
    };
    
    const icon = iconMap[type] || iconMap.info;
    
    const toastHtml = [
      '<div id="' + toastId + '" class="toast align-items-center text-bg-' + type + ' border-0 shadow-sm" role="alert">',
      '  <div class="d-flex">',
      '    <div class="toast-body">',
      '      <i class="fa ' + icon + ' me-2"></i>' + escapeHtml(msg),
      '    </div>',
      '    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Đóng"></button>',
      '  </div>',
      '</div>'
    ].join('');
    
    container.insertAdjacentHTML('beforeend', toastHtml);
    
    const toastEl = document.getElementById(toastId);
    if (toastEl && typeof bootstrap !== 'undefined') {
      const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
      toast.show();
      
      toastEl.addEventListener('hidden.bs.toast', function() {
        toastEl.remove();
      });
    }
  }

  function disableButton(button, text) {
    if (!button) return;
    button.disabled = true;
    button.dataset.originalHtml = button.innerHTML;
    button.innerHTML = text || '<span class="spinner-border spinner-border-sm" role="status"></span>';
  }

  function enableButton(button) {
    if (!button) return;
    button.disabled = false;
    if (button.dataset.originalHtml) {
      button.innerHTML = button.dataset.originalHtml;
      delete button.dataset.originalHtml;
    }
  }

  // ============== API FUNCTIONS ==============
  function removeFromBanner(button, bannerId, productId) {
    const productRow = button.closest('.product-row');
    const productName = productRow ? productRow.querySelector('.fw-semibold').textContent.trim() : 'sản phẩm';
    
    if (!confirm('Bạn có chắc muốn xóa "' + productName + '" khỏi banner?')) {
      return;
    }

    disableButton(button);

    fetch(API_REMOVE, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify({ bannerId: bannerId, productId: productId })
    })
    .then(function(response) {
      if (!response.ok) {
        throw new Error('HTTP error! status: ' + response.status);
      }
      return response.json();
    })
    .then(function(data) {
      if (data.success) {
        showToast(data.message || 'Đã xóa sản phẩm thành công', 'success');
        
        if (productRow) {
          productRow.style.transition = 'all 0.3s ease';
          productRow.style.opacity = '0';
          productRow.style.transform = 'translateX(-20px)';
          
          setTimeout(function() {
            productRow.remove();
            
            const container = document.getElementById('productListContainer');
            if (container) {
              const rows = container.querySelectorAll('.product-row');
              if (rows.length === 0) {
                container.innerHTML = [
                  '<div class="empty-state">',
                  '  <i class="fa-regular fa-face-frown"></i><br>',
                  '  <div class="fw-semibold">Chưa có sản phẩm nào</div>',
                  '  <small class="text-muted">Hãy thêm sản phẩm từ ô bên phải</small>',
                  '</div>'
                ].join('');
              }
            }
          }, 300);
        }
      } else {
        showToast(data.error || 'Không thể xóa sản phẩm', 'danger');
        enableButton(button);
      }
    })
    .catch(function(error) {
      console.error('Error removing product:', error);
      showToast('Lỗi kết nối máy chủ. Vui lòng thử lại.', 'danger');
      enableButton(button);
    });
  }

  function addToBanner(button, bannerId, productId) {
    disableButton(button);

    fetch(API_ADD, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify({ bannerId: bannerId, productId: productId })
    })
    .then(function(response) {
      if (!response.ok) {
        throw new Error('HTTP error! status: ' + response.status);
      }
      return response.json();
    })
    .then(function(data) {
      if (data.success) {
        showToast(data.message || 'Đã thêm sản phẩm thành công', 'success');
        
        button.innerHTML = '<i class="fa fa-check"></i> Đã thêm';
        button.classList.remove('btn-outline-primary');
        button.classList.add('btn-secondary');
        button.disabled = true;
        
        setTimeout(function() {
          window.location.reload();
        }, 1000);
      } else {
        showToast(data.error || 'Không thể thêm sản phẩm', 'danger');
        enableButton(button);
      }
    })
    .catch(function(error) {
      console.error('Error adding product:', error);
      showToast('Lỗi kết nối máy chủ. Vui lòng thử lại.', 'danger');
      enableButton(button);
    });
  }

  // ============== SEARCH FUNCTIONALITY ==============
  let searchTimeout;
  const searchInput = document.getElementById('searchKeyword');
  const searchResults = document.getElementById('searchResults');
  const spinner = document.getElementById('loadingSpinner');

  function performSearch(keyword) {
    if (!searchResults || !spinner) return;

    spinner.classList.remove('d-none');
    searchResults.innerHTML = '';

    fetch(API_SEARCH + '?kw=' + encodeURIComponent(keyword))
      .then(function(response) {
        if (!response.ok) {
          throw new Error('HTTP error! status: ' + response.status);
        }
        return response.json();
      })
      .then(function(data) {
        if (!Array.isArray(data)) {
          throw new Error('Invalid response format');
        }

        if (data.length === 0) {
          searchResults.innerHTML = [
            '<div class="text-center text-muted py-3">',
            '  <i class="fa fa-search fa-2x mb-2 opacity-50"></i><br>',
            '  <small>Không tìm thấy sản phẩm phù hợp</small>',
            '</div>'
          ].join('');
          return;
        }

        const resultsHtml = data.map(function(product) {
          const productName = escapeHtml(product.productName || 'Sản phẩm');
          const brand = escapeHtml(product.brand || '');
          
          let imageUrl = DEFAULT_IMAGE;
          if (product.images && Array.isArray(product.images) && product.images.length > 0) {
            imageUrl = CONTEXT_PATH + '/assets/uploads/banner/' + encodeURIComponent(product.images[0]);
          }

          return [
            '<div class="search-result">',
            '  <div class="d-flex align-items-center flex-grow-1 min-w-0">',
            '    <img src="' + imageUrl + '" alt="' + productName + '" onerror="this.src=\'' + DEFAULT_IMAGE + '\'">',
            '    <div class="search-result-info">',
            '      <div class="name">' + productName + '</div>',
            brand ? '<small class="text-muted">' + brand + '</small>' : '',
            '    </div>',
            '  </div>',
            '  <button class="btn btn-sm btn-outline-primary add-btn flex-shrink-0" data-product-id="' + product.productID + '">',
            '    <i class="fa fa-plus"></i> Thêm',
            '  </button>',
            '</div>'
          ].join('');
        }).join('');

        searchResults.innerHTML = resultsHtml;

        searchResults.querySelectorAll('.add-btn').forEach(function(btn) {
          btn.addEventListener('click', function(e) {
            e.preventDefault();
            const productId = parseInt(this.dataset.productId, 10);
            
            if (isNaN(productId)) {
              showToast('ID sản phẩm không hợp lệ', 'danger');
              return;
            }
            
            addToBanner(this, BANNER_ID, productId);
          });
        });
      })
      .catch(function(error) {
        console.error('Search error:', error);
        searchResults.innerHTML = [
          '<div class="alert alert-danger alert-sm mb-0">',
          '  <i class="fa fa-exclamation-triangle me-1"></i>',
          '  Lỗi khi tìm kiếm. Vui lòng thử lại.',
          '</div>'
        ].join('');
      })
      .finally(function() {
        spinner.classList.add('d-none');
      });
  }

  // ============== EVENT LISTENERS ==============
  
  if (searchInput) {
    searchInput.addEventListener('input', function(e) {
      const keyword = e.target.value.trim();

      clearTimeout(searchTimeout);

      if (keyword.length < 2) {
        if (searchResults) searchResults.innerHTML = '';
        if (spinner) spinner.classList.add('d-none');
        return;
      }

      searchTimeout = setTimeout(function() {
        performSearch(keyword);
      }, DEBOUNCE_DELAY);
    });
  }

  document.addEventListener('click', function(e) {
    const removeBtn = e.target.closest('.remove-btn');
    if (removeBtn) {
      e.preventDefault();
      
      const bannerId = parseInt(removeBtn.dataset.bannerId, 10);
      const productId = parseInt(removeBtn.dataset.productId, 10);
      
      if (isNaN(bannerId) || isNaN(productId)) {
        showToast('Dữ liệu không hợp lệ', 'danger');
        return;
      }
      
      removeFromBanner(removeBtn, bannerId, productId);
    }
  });

  // ============== INITIALIZATION ==============
  
  if (BANNER_ID === 0 || isNaN(BANNER_ID)) {
    showToast('Banner ID không hợp lệ', 'warning');
    if (searchInput) {
      searchInput.disabled = true;
      searchInput.placeholder = 'Không thể tìm kiếm - Banner không hợp lệ';
    }
  }

  if (typeof bootstrap === 'undefined') {
    console.warn('Bootstrap JavaScript is not loaded. Toast notifications may not work.');
  }

})();
</script>
</body>
</html>