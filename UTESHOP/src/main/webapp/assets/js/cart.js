/**
 * Cart functionality for UTESHOP
 */

// Thêm sản phẩm vào giỏ hàng
function addToCart(productId, quantity = 1, callback = null) {
    // Kiểm tra user đã đăng nhập chưa
    // Bạn có thể thêm logic kiểm tra session ở đây

    fetch(getContextPath() + '/api/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include', // Include cookies in cross-origin requests
        body: JSON.stringify({
            productId: parseInt(productId),
            quantity: parseInt(quantity)
        })
    })
        .then(response => {
            if (response.status === 401) {
                // Chưa đăng nhập, hiển thị modal
                showLoginModal();
                if (callback) callback(false);
                throw new Error('Unauthorized');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                showNotification(data.message, 'success');
                updateCartCount(data.itemCount);

                // Có thể trigger event để các component khác cập nhật
                window.dispatchEvent(new CustomEvent('cartUpdated', {
                    detail: { itemCount: data.itemCount }
                }));
                
                // Gọi callback nếu có
                if (callback) callback(true);
            } else {
                showNotification(data.error || 'Có lỗi xảy ra', 'error');
                if (callback) callback(false);
            }
        })
        .catch(error => {
            if (error.message !== 'Unauthorized') {
                console.error('Error:', error);
                showNotification('Không thể thêm sản phẩm vào giỏ hàng', 'error');
                if (callback) callback(false);
            }
        });
}

// Cập nhật số lượng sản phẩm trong giỏ hàng
function updateCartQuantity(cartDetailId, quantity) {
    fetch(getContextPath() + '/api/cart/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({
            cartDetailId: cartDetailId,
            quantity: quantity
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Không hiển thị thông báo, chỉ reload
                updateCartCount(data.itemCount);
                if (window.location.pathname.includes('/cart')) {
                    location.reload();
                }
            } else {
                // Chỉ hiển thị thông báo khi có lỗi
                showNotification(data.error || 'Có lỗi xảy ra', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Không thể cập nhật số lượng', 'error');
        });
}

// Xóa sản phẩm khỏi giỏ hàng
function removeFromCart(cartDetailId) {
    // Xóa trực tiếp không cần confirm
    fetch(getContextPath() + '/api/cart/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({
            cartDetailId: cartDetailId
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Không hiển thị thông báo, chỉ reload
                updateCartCount(data.itemCount);
                if (window.location.pathname.includes('/cart')) {
                    location.reload();
                }
            } else {
                // Chỉ hiển thị thông báo khi có lỗi
                showNotification(data.error || 'Không thể xóa sản phẩm', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Không thể xóa sản phẩm', 'error');
        });
}

// Xóa tất cả sản phẩm trong giỏ hàng
function clearCart() {
    if (!confirm('Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng?')) {
        return;
    }

    fetch(getContextPath() + '/api/cart/clear', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Không hiển thị thông báo, chỉ reload
                updateCartCount(0);
                location.reload();
            } else {
                // Chỉ hiển thị thông báo khi có lỗi
                showNotification(data.error || 'Không thể xóa giỏ hàng', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Không thể xóa giỏ hàng', 'error');
        });
}

// Lấy số lượng sản phẩm trong giỏ hàng
function getCartCount() {
    fetch(getContextPath() + '/api/cart/count', {
        credentials: 'include'
    })
        .then(response => {
            if (response.status === 401) {
                // User chưa đăng nhập, hiển thị 0
                updateCartCount(0);
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (data && data.success) {
                updateCartCount(data.count);
            } else if (data && !data.success) {
                updateCartCount(0);
            }
        })
        .catch(error => {
            console.error('Error loading cart count:', error);
            // Không hiển thị lỗi cho user, chỉ set về 0
            updateCartCount(0);
        });
}

// Cập nhật số lượng hiển thị trên icon giỏ hàng
function updateCartCount(count) {
    const cartBadges = document.querySelectorAll('.cart-count, .cart-badge, #cart-count');
    cartBadges.forEach(badge => {
        badge.textContent = count || 0;
        // Luôn hiển thị badge, có thể thay đổi màu khi count = 0
        badge.style.display = 'inline-block';
        if (count > 0) {
            badge.classList.remove('bg-secondary');
            badge.classList.add('bg-danger');
        } else {
            badge.classList.remove('bg-danger');
            badge.classList.add('bg-secondary');
        }
    });
}

// Hiển thị thông báo
function showNotification(message, type = 'info') {
    // Tạo notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px; max-width: 500px;';
    notification.setAttribute('role', 'alert');

    // Icon dựa trên type
    let icon = '';
    switch (type) {
        case 'success':
            icon = '<i class="fas fa-check-circle me-2"></i>';
            break;
        case 'error':
        case 'danger':
            icon = '<i class="fas fa-exclamation-circle me-2"></i>';
            break;
        case 'warning':
            icon = '<i class="fas fa-exclamation-triangle me-2"></i>';
            break;
        default:
            icon = '<i class="fas fa-info-circle me-2"></i>';
    }

    notification.innerHTML = `
        ${icon}${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;

    document.body.appendChild(notification);

    // Tự động ẩn sau 4 giây
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 150);
    }, 4000);
}

// Helper function để lấy context path
function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '';
}

// Khởi tạo khi trang load
document.addEventListener('DOMContentLoaded', function () {
    // Lấy số lượng giỏ hàng khi load trang
    getCartCount();

    // Thêm event listener cho các nút "Thêm vào giỏ hàng"
    const addToCartButtons = document.querySelectorAll('.btn-add-to-cart, [data-action="add-to-cart"]');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const productId = this.getAttribute('data-product-id');
            const quantity = this.getAttribute('data-quantity') || 1;

            if (productId) {
                addToCart(parseInt(productId), parseInt(quantity));
            }
        });
    });
});

// Hiển thị modal đăng nhập
function showLoginModal() {
    const loginModal = document.getElementById('loginModal');
    if (loginModal) {
        const modal = new bootstrap.Modal(loginModal);
        modal.show();
        showNotification('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng', 'warning');
    } else {
        // Fallback: redirect nếu không tìm thấy modal
        showNotification('Vui lòng đăng nhập', 'warning');
        setTimeout(() => {
            window.location.href = getContextPath() + '/?showLogin=true';
        }, 1500);
    }
}

// Export functions để có thể gọi từ global scope
window.addToCart = addToCart;
window.updateCartQuantity = updateCartQuantity;
window.removeFromCart = removeFromCart;
window.clearCart = clearCart;
window.getCartCount = getCartCount;
window.updateCartCount = updateCartCount;
window.showLoginModal = showLoginModal;

