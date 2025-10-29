<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Reviews Section -->
<div class="reviews-section mt-5">
    <div class="row">
        <div class="col-12">
            <h3 class="mb-4">
                <i class="fas fa-star text-warning"></i>
                Đánh giá sản phẩm
                <span class="badge bg-primary ms-2">${totalReviews}</span>
            </h3>
            
            <!-- Rating Summary -->
            <div class="rating-summary-card mb-4">
                <div class="row">
                    <div class="col-md-4">
                        <div class="overall-rating text-center">
                            <div class="rating-score">
                                <fmt:formatNumber value="${averageRating}" type="number" maxFractionDigits="1" />
                            </div>
                            <div class="rating-stars-large">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fas fa-star ${i <= averageRating ? 'text-warning' : 'text-secondary'}"></i>
                                </c:forEach>
                            </div>
                            <div class="rating-text">Dựa trên ${totalReviews} đánh giá</div>
                        </div>
                    </div>
                    <div class="col-md-8">
                        <div class="rating-breakdown">
                            <c:forEach begin="5" end="1" step="-1" var="star">
                                <div class="rating-bar-item">
                                    <span class="star-label">${star} sao</span>
                                    <div class="progress">
                                        <div class="progress-bar bg-warning" 
                                             style="width: ${ratingDistribution[star] != null ? (ratingDistribution[star] / totalReviews * 100) : 0}%"></div>
                                    </div>
                                    <span class="count">${ratingDistribution[star] != null ? ratingDistribution[star] : 0}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Review Filters -->
            <div class="review-filters mb-4">
                <div class="d-flex flex-wrap gap-2">
                    <button class="btn btn-outline-primary filter-btn active" data-rating="all">
                        Tất cả (${totalReviews})
                    </button>
                    <c:forEach begin="5" end="1" step="-1" var="star">
                        <button class="btn btn-outline-primary filter-btn" data-rating="${star}">
                            ${star} sao (${ratingDistribution[star] != null ? ratingDistribution[star] : 0})
                        </button>
                    </c:forEach>
                </div>
            </div>

            <!-- Add Review Form -->
            <c:if test="${not empty sessionScope.user}">
                <div class="add-review-card mb-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-edit"></i>
                                Viết đánh giá của bạn
                            </h5>
                        </div>
                        <div class="card-body">
                            <form id="reviewForm" enctype="multipart/form-data">
                                <input type="hidden" name="productId" value="${productDTO.productID}">
                                
                                <!-- Rating Input -->
                                <div class="mb-3">
                                    <label class="form-label">Đánh giá của bạn:</label>
                                    <div class="rating-input">
                                        <input type="radio" id="star5" name="rating" value="5">
                                        <label for="star5" class="star-label">★</label>
                                        <input type="radio" id="star4" name="rating" value="4">
                                        <label for="star4" class="star-label">★</label>
                                        <input type="radio" id="star3" name="rating" value="3">
                                        <label for="star3" class="star-label">★</label>
                                        <input type="radio" id="star2" name="rating" value="2">
                                        <label for="star2" class="star-label">★</label>
                                        <input type="radio" id="star1" name="rating" value="1">
                                        <label for="star1" class="star-label">★</label>
                                    </div>
                                    <div class="rating-text-small mt-1">Chọn số sao để đánh giá</div>
                                </div>

                                <!-- Comment Input -->
                                <div class="mb-3">
                                    <label for="content" class="form-label">Nhận xét:</label>
                                    <textarea class="form-control" id="content" name="content" rows="4" 
                                              placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm này..."></textarea>
                                </div>

                                <!-- Image Upload -->
                                <div class="mb-3">
                                    <label for="reviewImage" class="form-label">Hình ảnh (tùy chọn):</label>
                                    <input type="file" class="form-control" id="reviewImage" name="image" 
                                           accept="image/*">
                                    <div class="form-text">Tải lên hình ảnh để minh họa cho đánh giá của bạn</div>
                                </div>

                                <!-- Submit Button -->
                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-paper-plane"></i>
                                        Gửi đánh giá
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Reviews List -->
            <div class="reviews-list">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5>Đánh giá khách hàng</h5>
                    <div class="sort-options">
                        <select class="form-select form-select-sm" id="sortReviews">
                            <option value="newest">Mới nhất</option>
                            <option value="oldest">Cũ nhất</option>
                            <option value="highest">Đánh giá cao nhất</option>
                            <option value="lowest">Đánh giá thấp nhất</option>
                        </select>
                    </div>
                </div>

                <div id="reviewsContainer">
                    <c:forEach items="${reviews}" var="review">
                        <div class="review-item" data-rating="${review.rating}">
                            <div class="card mb-3">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-start mb-3">
                                        <div class="user-info">
                                            <div class="d-flex align-items-center">
                                                <div class="user-avatar me-3">
                                                    <i class="fas fa-user-circle fa-2x text-muted"></i>
                                                </div>
                                                <div>
                                                    <h6 class="mb-0">${review.user.fullname}</h6>
                                                    <small class="text-muted">
                                                        <fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </small>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="review-rating">
                                            <div class="rating-stars">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <i class="fas fa-star ${i <= review.rating ? 'text-warning' : 'text-secondary'}"></i>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="review-content">
                                        <p class="mb-3">${review.content}</p>
                                        
                                        <c:if test="${not empty review.image}">
                                            <div class="review-image mb-3">
                                                <img src="${pageContext.request.contextPath}/image?fname=${review.image}" 
                                                     class="img-thumbnail" 
                                                     style="max-width: 200px; max-height: 200px; cursor: pointer;"
                                                     onclick="openImageModal('${pageContext.request.contextPath}/image?fname=${review.image}')"
                                                     alt="Review image">
                                            </div>
                                        </c:if>
                                    </div>

                                    <!-- Review Actions -->
                                    <c:if test="${not empty sessionScope.user && sessionScope.user.userID == review.user.userID}">
                                        <div class="review-actions">
                                            <hr class="my-2">
                                            <div class="d-flex gap-2">
                                                <button class="btn btn-sm btn-outline-primary edit-review-btn" 
                                                        data-review-id="${review.reviewID}"
                                                        data-content="${review.content}"
                                                        data-rating="${review.rating}">
                                                    <i class="fas fa-edit"></i> Sửa
                                                </button>
                                                <button class="btn btn-sm btn-outline-danger delete-review-btn" 
                                                        data-review-id="${review.reviewID}">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Review pagination">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage - 1}">Trước</a>
                            </li>
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage + 1}">Sau</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Image Modal -->
<div class="modal fade" id="imageModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Hình ảnh đánh giá</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-center">
                <img id="modalImage" src="" class="img-fluid" alt="Review image">
            </div>
        </div>
    </div>
</div>

<!-- Edit Review Modal -->
<div class="modal fade" id="editReviewModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Sửa đánh giá</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="editReviewForm">
                    <input type="hidden" id="editReviewId" name="reviewId">
                    
                    <div class="mb-3">
                        <label class="form-label">Đánh giá:</label>
                        <div class="rating-input">
                            <input type="radio" id="editStar5" name="editRating" value="5">
                            <label for="editStar5" class="star-label">★</label>
                            <input type="radio" id="editStar4" name="editRating" value="4">
                            <label for="editStar4" class="star-label">★</label>
                            <input type="radio" id="editStar3" name="editRating" value="3">
                            <label for="editStar3" class="star-label">★</label>
                            <input type="radio" id="editStar2" name="editRating" value="2">
                            <label for="editStar2" class="star-label">★</label>
                            <input type="radio" id="editStar1" name="editRating" value="1">
                            <label for="editStar1" class="star-label">★</label>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="editComment" class="form-label">Nhận xét:</label>
                        <textarea class="form-control" id="editComment" name="editComment" rows="4"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary" id="saveEditReview">Lưu thay đổi</button>
            </div>
        </div>
    </div>
</div>

<!-- CSS Styles -->
<style>
/* Rating Summary Card */
.rating-summary-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 15px;
    padding: 2rem;
    color: white;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.rating-score {
    font-size: 3rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
}

.rating-stars-large {
    font-size: 1.5rem;
    margin-bottom: 0.5rem;
}

.rating-text {
    font-size: 0.9rem;
    opacity: 0.9;
}

.rating-breakdown {
    padding-left: 1rem;
}

.rating-bar-item {
    display: flex;
    align-items: center;
    margin-bottom: 0.5rem;
}

.star-label {
    width: 60px;
    font-size: 0.9rem;
}

.progress {
    flex: 1;
    height: 8px;
    margin: 0 10px;
    background-color: rgba(255,255,255,0.3);
}

.count {
    width: 30px;
    text-align: right;
    font-size: 0.9rem;
}

/* Review Filters */
.review-filters .filter-btn {
    border-radius: 20px;
    padding: 0.5rem 1rem;
    transition: all 0.3s ease;
}

.review-filters .filter-btn.active {
    background-color: #007bff;
    color: white;
    border-color: #007bff;
}

.review-filters .filter-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* Add Review Card */
.add-review-card .card {
    border: none;
    box-shadow: 0 5px 15px rgba(0,0,0,0.08);
    border-radius: 15px;
}

.add-review-card .card-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: 15px 15px 0 0 !important;
}

/* Rating Input */
.rating-input {
    display: inline-flex;
    flex-direction: row-reverse;
    gap: 5px;
}

.rating-input input {
    display: none;
}

.rating-input label {
    cursor: pointer;
    color: #ddd;
    font-size: 2rem;
    transition: color 0.2s ease;
}

.rating-input input:checked ~ label,
.rating-input label:hover,
.rating-input label:hover ~ label {
    color: #ffc107;
}

.rating-text-small {
    font-size: 0.8rem;
    color: #6c757d;
}

/* Review Items */
.review-item {
    transition: all 0.3s ease;
}

.review-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.review-item .card {
    border: none;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
    border-radius: 15px;
    transition: all 0.3s ease;
}

.review-item .card:hover {
    box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.user-avatar {
    width: 50px;
    height: 50px;
}

.review-content p {
    line-height: 1.6;
    color: #495057;
}

.review-image img {
    border-radius: 10px;
    transition: transform 0.3s ease;
}

.review-image img:hover {
    transform: scale(1.05);
}

/* Review Actions */
.review-actions {
    margin-top: 1rem;
}

.review-actions .btn {
    border-radius: 20px;
    padding: 0.25rem 0.75rem;
    font-size: 0.8rem;
}

/* Pagination */
.pagination .page-link {
    border-radius: 20px;
    margin: 0 2px;
    border: none;
    color: #007bff;
}

.pagination .page-item.active .page-link {
    background-color: #007bff;
    color: white;
}

.pagination .page-link:hover {
    background-color: #e9ecef;
    color: #007bff;
}

/* Responsive */
@media (max-width: 768px) {
    .rating-summary-card {
        padding: 1rem;
    }
    
    .rating-score {
        font-size: 2rem;
    }
    
    .rating-stars-large {
        font-size: 1.2rem;
    }
    
    .rating-breakdown {
        padding-left: 0;
        margin-top: 1rem;
    }
    
    .review-filters {
        overflow-x: auto;
    }
    
    .review-filters .d-flex {
        flex-wrap: nowrap;
        min-width: max-content;
    }
}

/* Animation */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.review-item {
    animation: fadeInUp 0.6s ease-out;
}

/* Loading State */
.loading {
    opacity: 0.6;
    pointer-events: none;
}

.spinner-border-sm {
    width: 1rem;
    height: 1rem;
}
</style>

<!-- JavaScript -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Review form submission
    const reviewForm = document.getElementById('reviewForm');
    if (reviewForm) {
        reviewForm.addEventListener('submit', function(e) {
            e.preventDefault();
            submitReview();
        });
    }

    // Filter buttons
    const filterBtns = document.querySelectorAll('.filter-btn');
    filterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const rating = this.dataset.rating;
            filterReviews(rating);
            
            // Update active state
            filterBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Sort reviews
    const sortSelect = document.getElementById('sortReviews');
    if (sortSelect) {
        sortSelect.addEventListener('change', function() {
            sortReviews(this.value);
        });
    }

    // Edit review buttons
    const editBtns = document.querySelectorAll('.edit-review-btn');
    editBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const reviewId = this.dataset.reviewId;
            const content = this.dataset.content;
            const rating = this.dataset.rating;
            openEditModal(reviewId, content, rating);
        });
    });

    // Delete review buttons
    const deleteBtns = document.querySelectorAll('.delete-review-btn');
    deleteBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const reviewId = this.dataset.reviewId;
            deleteReview(reviewId);
        });
    });

    // Save edit review
    const saveEditBtn = document.getElementById('saveEditReview');
    if (saveEditBtn) {
        saveEditBtn.addEventListener('click', function() {
            saveEditReview();
        });
    }
});

function submitReview() {
    const form = document.getElementById('reviewForm');
    const formData = new FormData(form);
    
    // Show loading state
    const submitBtn = form.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang gửi...';
    submitBtn.disabled = true;

    fetch('${pageContext.request.contextPath}/review/add', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Thành công', 'Đánh giá của bạn đã được gửi!', 'success');
            form.reset();
            // Reload page to show new review
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        } else {
            showToast('Lỗi', data.message || 'Có lỗi xảy ra khi gửi đánh giá', 'danger');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Lỗi', 'Có lỗi xảy ra khi gửi đánh giá', 'danger');
    })
    .finally(() => {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    });
}

function filterReviews(rating) {
    const reviewItems = document.querySelectorAll('.review-item');
    
    reviewItems.forEach(item => {
        if (rating === 'all' || item.dataset.rating === rating) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}

function sortReviews(sortBy) {
    const container = document.getElementById('reviewsContainer');
    const reviewItems = Array.from(container.querySelectorAll('.review-item'));
    
    reviewItems.sort((a, b) => {
        switch(sortBy) {
            case 'newest':
                return 0; // Already sorted by newest
            case 'oldest':
                return 0; // Would need to reverse
            case 'highest':
                return parseInt(b.dataset.rating) - parseInt(a.dataset.rating);
            case 'lowest':
                return parseInt(a.dataset.rating) - parseInt(b.dataset.rating);
            default:
                return 0;
        }
    });
    
    // Clear container and re-append sorted items
    container.innerHTML = '';
    reviewItems.forEach(item => container.appendChild(item));
}

function openImageModal(imageSrc) {
    const modal = new bootstrap.Modal(document.getElementById('imageModal'));
    document.getElementById('modalImage').src = imageSrc;
    modal.show();
}

function openEditModal(reviewId, content, rating) {
    const modal = new bootstrap.Modal(document.getElementById('editReviewModal'));
    document.getElementById('editReviewId').value = reviewId;
    document.getElementById('editComment').value = content;
    
    // Set rating
    document.querySelectorAll('input[name="editRating"]').forEach(input => {
        input.checked = input.value === rating;
    });
    
    modal.show();
}

function saveEditReview() {
    const reviewId = document.getElementById('editReviewId').value;
    const content = document.getElementById('editComment').value;
    const rating = document.querySelector('input[name="editRating"]:checked').value;
    
    const formData = new FormData();
    formData.append('reviewId', reviewId);
    formData.append('content', content);
    formData.append('rating', rating);
    
    fetch('${pageContext.request.contextPath}/review/update', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Thành công', 'Đánh giá đã được cập nhật!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('editReviewModal')).hide();
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        } else {
            showToast('Lỗi', data.message || 'Có lỗi xảy ra khi cập nhật đánh giá', 'danger');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Lỗi', 'Có lỗi xảy ra khi cập nhật đánh giá', 'danger');
    });
}

function deleteReview(reviewId) {
    if (!confirm('Bạn có chắc chắn muốn xóa đánh giá này?')) {
        return;
    }
    
    fetch('${pageContext.request.contextPath}/review/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `reviewId=${reviewId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Thành công', 'Đánh giá đã được xóa!', 'success');
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        } else {
            showToast('Lỗi', data.message || 'Có lỗi xảy ra khi xóa đánh giá', 'danger');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Lỗi', 'Có lỗi xảy ra khi xóa đánh giá', 'danger');
    });
}

// Helper to show bootstrap toasts
function showToast(title, message, type = 'info') {
    const container = document.getElementById('toastContainer');
    if (!container) {
        console.error('❌ Không tìm thấy toastContainer');
        return;
    }

    const toastId = 'toast' + Date.now();
    const wrapper = document.createElement('div');
    wrapper.innerHTML = `
        <div id="${toastId}" class="toast align-items-center text-bg-${type} border-0 shadow-sm" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    <strong class="me-1">${title}</strong> ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>`;
    
    container.appendChild(wrapper);
    const toastEl = document.getElementById(toastId);

    if (!toastEl) {
        console.error('❌ Không thể tạo phần tử toast');
        return;
    }

    if (typeof bootstrap === 'undefined' || !bootstrap.Toast) {
        console.error('❌ Bootstrap JS chưa được tải!');
        return;
    }

    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();

    toastEl.addEventListener('hidden.bs.toast', () => wrapper.remove());
}
</script>
