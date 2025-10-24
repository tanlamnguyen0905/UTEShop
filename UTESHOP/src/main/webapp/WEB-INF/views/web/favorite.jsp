<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html>
<head>
    <title>Yêu thích - UTESHOP</title>
    
    <style>
        body {
            background-color: #f8f9fa;
        }
        
        .favorite-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 15px;
        }
        
        .favorite-item {
            background: white;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 15px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
        }
        
        .favorite-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.12);
        }
        
        .product-image {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 10px;
            border: 1px solid #e9ecef;
        }
        
        .quantity-control {
            display: flex;
            align-items: center;
            gap: 8px;
            justify-content: center;
        }
        
        .quantity-control button {
            width: 32px;
            height: 32px;
            border-radius: 6px;
            border: 1px solid #dee2e6;
            background: white;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
        }
        
        .quantity-control button:hover:not(:disabled) {
            background: #0d6efd;
            border-color: #0d6efd;
            color: white;
        }
        
        .quantity-control button:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }
        
        .quantity-control input {
            width: 60px;
            text-align: center;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            padding: 6px;
            font-weight: 500;
        }
        
        .favorite-summary {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            position: sticky;
            top: 100px;
        }
        
        .price-original {
            text-decoration: line-through;
            color: #6c757d;
            font-size: 0.85rem;
        }
        
        .price-discount {
            color: #dc3545;
            font-weight: 600;
            font-size: 1.1rem;
        }
        
        .empty-favorite {
            text-align: center;
            padding: 80px 20px;
        }
        
        .empty-favorite i {
            font-size: 6rem;
            color: #dee2e6;
            margin-bottom: 20px;
        }
        
        .btn-remove {
            color: #dc3545;
            cursor: pointer;
            transition: all 0.2s;
            font-size: 1.2rem;
        }
        
        .btn-remove:hover {
            color: #bb2d3b;
            transform: scale(1.2);
        }
        
        .favorite-header {
            background: white;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        
        .product-name {
            font-size: 1rem;
            font-weight: 600;
            color: #212529;
            margin-bottom: 4px;
        }
        
        .product-name:hover {
            color: #0d6efd;
        }
        
        .summary-row {
            padding: 12px 0;
            border-bottom: 1px dashed #e9ecef;
        }
        
        .summary-row:last-child {
            border-bottom: none;
        }
    </style>
</head>

<body>
<div class="favorite-container">
    <div class="row g-4">
        <!-- favorite Items -->
        <div class="col-lg-8">
            <!-- Header -->
            <div class="favorite-header">
                <div class="d-flex justify-content-between align-items-center">
                    <h3 class="mb-0">
                        <i class="fas fa-shopping-favorite me-2 text-primary"></i>
                        Sản phẩm yêu thích của bạn
                        <c:if test="${not empty favoriteItems && favoriteItems.size() > 0}">
                            <span class="badge bg-primary ms-2">${itemCount} sản phẩm</span>
                        </c:if>
                    </h3>
                    <c:if test="${not empty favoriteItems && favoriteItems.size() > 0}">
                        <button class="btn btn-outline-danger btn-sm" onclick="clearfavorite()">
                            <i class="fas fa-trash me-2"></i>Xóa tất cả
                        </button>
                    </c:if>
                </div>
            </div>

            <c:choose>
                <c:when test="${empty favoriteItems || favoriteItems.size() == 0}">
                    <!-- Empty favorite -->
                    <div class="favorite-item">
                        <div class="empty-favorite">
                            <i class="fas fa-shopping-favorite"></i>
                            <h4 class="mb-3">Sản phẩm yêu thích của bạn đang trống</h4>
                            <p class="text-muted mb-4">Hãy thêm sản phẩm vào yêu thích để tiếp tục mua sắm</p>
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                                <i class="fas fa-arrow-left me-2"></i>Tiếp tục xem
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- favorite Items List -->
                    <c:forEach var="item" items="${favoriteItems}">
                        <div class="favorite-item" data-favorite-detail-id="${item.favoriteDetailID}">
                            <div class="row align-items-center g-3">
                                <!-- Product Image -->
                                <div class="col-auto">
                                    <c:choose>
                                        <c:when test="${not empty item.product.images && item.product.images.size() > 0}">
                                            <img src="${pageContext.request.contextPath}/image?fname=${item.product.images[0].dirImage}" 
                                                 alt="${item.product.productName}" 
                                                 class="product-image">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                 alt="No image" 
                                                 class="product-image">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                
                                <!-- Product Info -->
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/product/${item.product.productID}" 
                                       class="product-name text-decoration-none d-block">
                                        ${item.product.productName}
                                    </a>
                                    <c:if test="${not empty item.product.brand}">
                                        <p class="text-muted small mb-2">
                                            <i class="fas fa-tag me-1"></i>${item.product.brand.brandName}
                                        </p>
                                    </c:if>
                                    <small class="text-muted">
                                        <i class="fas fa-box me-1"></i>Còn ${item.product.stockQuantity} sản phẩm
                                    </small>
                                </div>
                                
                                <!-- Price -->
                                <div class="col-auto text-end">
                                    <div class="fw-bold text-danger">
                                        <fmt:formatNumber value="${item.product.unitPrice}" type="number" groupingUsed="true"/>₫
                                    </div>
                                </div>
                                
                                <!-- Quantity Control -->
                                <div class="col-auto">
                                    <div class="quantity-control">
                                        <button onclick="updateQuantity(${item.favoriteDetailID}, ${item.quantity - 1})"
                                                ${item.quantity <= 1 ? 'disabled' : ''}>
                                            <i class="fas fa-minus"></i>
                                        </button>
                                        <input type="number" 
                                               value="${item.quantity}" 
                                               min="1" 
                                               max="${item.product.stockQuantity}"
                                               class="form-control"
                                               readonly>
                                        <button onclick="updateQuantity(${item.favoriteDetailID}, ${item.quantity + 1})"
                                                ${item.quantity >= item.product.stockQuantity ? 'disabled' : ''}>
                                            <i class="fas fa-plus"></i>
                                        </button>
                                    </div>
                                </div>
                                
                                <!-- Subtotal -->
                                <div class="col-auto">
                                    <div class="fw-bold text-danger">
                                        <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="number" groupingUsed="true"/>₫
                                    </div>
                                </div>
                                
                                <!-- Remove Button -->
                                <div class="col-auto">
                                    <i class="fas fa-trash btn-remove" 
                                       onclick="removeFromfavorite(${item.favoriteDetailID})"
                                       title="Xóa sản phẩm"></i>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</div>

<script>


// Remove from favorite
function removeFromfavorite(favoriteDetailId) {
    // Xóa trực tiếp không cần confirm
    fetch('${pageContext.request.contextPath}/api/favorite/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({
            favoriteDetailId: favoriteDetailId
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Không hiển thị thông báo, chỉ reload và cập nhật badge
            if (window.updatefavoriteCount) {
                window.updatefavoriteCount(data.itemCount);
            }
            location.reload();
        } else {
            // Chỉ hiển thị thông báo khi có lỗi
            if (window.showNotification) {
                window.showNotification(data.error, 'danger');
            } else {
                alert(data.error);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        if (window.showNotification) {
            window.showNotification('Có lỗi xảy ra!', 'error');
        } else {
            alert('Có lỗi xảy ra!');
        }
    });
}

// Clear favorite
function clearfavorite() {
    if (!confirm('Bạn có chắc muốn xóa tất cả sản phẩm trong yêu thích không?')) {
        return;
    }
    
    fetch('${pageContext.request.contextPath}/api/favorite/clear', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Không hiển thị thông báo, chỉ reload và cập nhật badge
            if (window.updatefavoriteCount) {
                window.updatefavoriteCount(0);
            }
            location.reload();
        } else {
            // Chỉ hiển thị thông báo khi có lỗi
            if (window.showNotification) {
                window.showNotification(data.error, 'danger');
            } else {
                alert(data.error);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        if (window.showNotification) {
            window.showNotification('Có lỗi xảy ra!', 'error');
        } else {
            alert('Có lỗi xảy ra!');
        }
    });
}
</script>

</body>
</html>
