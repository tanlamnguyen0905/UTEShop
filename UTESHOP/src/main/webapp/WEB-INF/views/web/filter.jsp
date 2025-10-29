<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bộ lọc & Sản phẩm - UTESHOP</title>
    <style>
        .page-item.active .page-link {
            z-index: 3;
            color: #fff;
            background-color: #0d6efd;
            border-color: #0d6efd;
        }
        .pagination {
            --bs-pagination-padding-x: 0.75rem;
            --bs-pagination-padding-y: 0.375rem;
            --bs-pagination-font-size: 1rem;
            --bs-pagination-color: var(--bs-link-color);
            --bs-pagination-bg: #fff;
            --bs-pagination-border-width: 1px;
            --bs-pagination-border-color: #dee2e6;
            --bs-pagination-border-radius: 0.375rem;
            display: flex;
            padding-left: 0;
            list-style: none;
        }
    </style>
</head>

<body class="bg-light py-5">
    <div class="container">
        <div class="row">
            <!-- ===== CỘT TRÁI: BỘ LỌC ===== -->
            <div class="col-md-3 mb-4">
                <div class="bg-white p-3 rounded shadow-sm">
                    <!-- ✅ FIX 1: Thêm nút "Xóa tất cả filter" -->
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="mb-0">Bộ lọc</h5>
                        <div>
                            <span class="text-muted small me-2">${total} SP</span>
                            <a href="${pageContext.request.contextPath}/filter" 
                               class="btn btn-sm btn-outline-danger"
                               title="Xóa tất cả bộ lọc">
                                <i class="fas fa-times"></i>
                            </a>
                        </div>
                    </div>

                    <!-- Form filter -->
                    <form id="filterForm" method="get" action="${pageContext.request.contextPath}/filter">
                        <!-- ✅ FIX 2: CHỈ hidden cho keyword và sortBy (không hidden category/banner) -->
                        <c:if test="${not empty productFilter.keyword}">
                            <input type="hidden" name="keyword" value="${productFilter.keyword}">
                        </c:if>
                        <c:if test="${not empty productFilter.sortBy}">
                            <input type="hidden" name="sortBy" value="${productFilter.sortBy}">
                        </c:if>

                        <!-- Accordion Bootstrap cho bộ lọc -->
                        <div class="accordion" id="filterAccordion">
                            <!-- Khoảng giá -->
                            <div class="accordion-item">
                                <h6 class="accordion-header" id="headingPrice">
                                    <button class="accordion-button collapsed" type="button"
                                        data-bs-toggle="collapse" data-bs-target="#collapsePrice"
                                        aria-expanded="false" aria-controls="collapsePrice">
                                        Khoảng giá
                                    </button>
                                </h6>
                                <div id="collapsePrice" class="accordion-collapse collapse show"
                                    aria-labelledby="headingPrice" data-bs-parent="#filterAccordion">
                                    <div class="accordion-body">
                                        <div class="row g-2">
                                            <div class="col-6">
                                                <label class="form-label small">Từ (VNĐ)</label>
                                                <!-- ✅ FIX 3: Thêm validation HTML5 -->
                                                <input type="number" 
                                                       class="form-control form-control-sm"
                                                       name="minPrice" 
                                                       id="minPrice"
                                                       min="0"
                                                       step="1000"
                                                       value="${productFilter.minPrice != null ? productFilter.minPrice : ''}"
                                                       placeholder="0">
                                            </div>
                                            <div class="col-6">
                                                <label class="form-label small">Đến (VNĐ)</label>
                                                <input type="number" 
                                                       class="form-control form-control-sm"
                                                       name="maxPrice" 
                                                       id="maxPrice"
                                                       min="0"
                                                       step="1000"
                                                       value="${productFilter.maxPrice != null ? productFilter.maxPrice : ''}"
                                                       placeholder="10000000">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Danh mục -->
                            <div class="accordion-item">
                                <h6 class="accordion-header" id="headingCategory">
                                    <button class="accordion-button collapsed" type="button"
                                        data-bs-toggle="collapse" data-bs-target="#collapseCategory"
                                        aria-expanded="false" aria-controls="collapseCategory">
                                        Danh mục
                                    </button>
                                </h6>
                                <div id="collapseCategory" class="accordion-collapse collapse show"
                                    aria-labelledby="headingCategory" data-bs-parent="#filterAccordion">
                                    <div class="accordion-body">
                                        <!-- ✅ FIX 4: Thêm option "Tất cả" để bỏ chọn -->
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" 
                                                   name="category" id="categoryAll" value=""
                                                   ${empty productFilter.categoryId ? 'checked' : ''}>
                                            <label class="form-check-label" for="categoryAll">
                                                <strong>Tất cả danh mục</strong>
                                            </label>
                                        </div>
                                        <hr class="my-2">
                                        <c:forEach var="cate" items="${listCate}">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" 
                                                       name="category"
                                                       id="category${cate.categoryID}"
                                                       value="${cate.categoryID}"
                                                       ${productFilter.categoryId == cate.categoryID ? 'checked' : ''}>
                                                <label class="form-check-label" for="category${cate.categoryID}">
                                                    ${cate.categoryName}
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>

                            <!-- Banner -->
                            <div class="accordion-item">
                                <h6 class="accordion-header" id="headingBanner">
                                    <button class="accordion-button collapsed" type="button"
                                        data-bs-toggle="collapse" data-bs-target="#collapseBanner"
                                        aria-expanded="false" aria-controls="collapseBanner">
                                        Banner
                                    </button>
                                </h6>
                                <div id="collapseBanner" class="accordion-collapse collapse show"
                                    aria-labelledby="headingBanner" data-bs-parent="#filterAccordion">
                                    <div class="accordion-body">
                                        <!-- ✅ FIX 5: Thêm option "Tất cả" cho banner -->
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" 
                                                   name="banner" id="bannerAll" value=""
                                                   ${empty productFilter.bannerId ? 'checked' : ''}>
                                            <label class="form-check-label" for="bannerAll">
                                                <strong>Tất cả banner</strong>
                                            </label>
                                        </div>
                                        <hr class="my-2">
                                        <c:forEach var="banner" items="${listBanner}">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" 
                                                       name="banner"
                                                       id="banner${banner.bannerID}"
                                                       value="${banner.bannerID}"
                                                       ${productFilter.bannerId == banner.bannerID ? 'checked' : ''}>
                                                <label class="form-check-label" for="banner${banner.bannerID}">
                                                    ${banner.bannerName}
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Nút áp dụng filter -->
                        <div class="mt-3">
                            <button type="submit" class="btn btn-primary btn-sm w-100">
                                <i class="fas fa-filter me-1"></i>Áp dụng bộ lọc
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- ===== CỘT PHẢI: SẢN PHẨM ===== -->
            <div class="col-md-9">
                <!-- Thanh sắp xếp -->
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h4 class="mb-0">Sản phẩm</h4>
                        <!-- ✅ FIX 6: Hiển thị "X-Y trên tổng Z" thay vì "X trong Z" -->
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${total > 0}">
                                    <c:set var="startItem" value="${(currentPage - 1) * pageSize + 1}" />
                                    <c:set var="endItem" value="${currentPage * pageSize < total ? currentPage * pageSize : total}" />
                                    Hiển thị ${startItem} - ${endItem} trên tổng ${total} sản phẩm
                                </c:when>
                                <c:otherwise>
                                    Không có sản phẩm
                                </c:otherwise>
                            </c:choose>
                        </small>
                    </div>
                    <div class="d-flex align-items-center">
                        <label class="me-2 fw-semibold small">Sắp xếp:</label>
                        <div class="dropdown">
                            <button class="btn btn-outline-secondary btn-sm dropdown-toggle" type="button"
                                data-bs-toggle="dropdown" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${productFilter.sortBy == '0'}">Bán chạy</c:when>
                                    <c:when test="${productFilter.sortBy == '1'}">Mới nhất</c:when>
                                    <c:when test="${productFilter.sortBy == '2'}">Nhiều đánh giá</c:when>
                                    <c:when test="${productFilter.sortBy == '3'}">Yêu thích</c:when>
                                    <c:when test="${productFilter.sortBy == '4'}">Giá tăng dần</c:when>
                                    <c:when test="${productFilter.sortBy == '5'}">Giá giảm dần</c:when>
                                    <c:otherwise>Mới nhất</c:otherwise>
                                </c:choose>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item sort-link" data-sort="0" href="#">Bán chạy</a></li>
                                <li><a class="dropdown-item sort-link" data-sort="1" href="#">Mới nhất</a></li>
                                <li><a class="dropdown-item sort-link" data-sort="2" href="#">Nhiều đánh giá</a></li>
                                <li><a class="dropdown-item sort-link" data-sort="3" href="#">Yêu thích</a></li>
                                <li><a class="dropdown-item sort-link" data-sort="4" href="#">Giá tăng dần</a></li>
                                <li><a class="dropdown-item sort-link" data-sort="5" href="#">Giá giảm dần</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Lưới sản phẩm -->
                <c:choose>
                    <c:when test="${empty listPro}">
                        <div class="text-center py-5">
                            <div class="mb-3">
                                <i class="fas fa-search fa-3x text-muted"></i>
                            </div>
                            <h5 class="text-muted">Không tìm thấy sản phẩm nào</h5>
                            <p class="text-muted">Hãy thử điều chỉnh bộ lọc hoặc tìm kiếm với từ khóa khác</p>
                            <a href="${pageContext.request.contextPath}/filter" class="btn btn-primary">
                                Xem tất cả sản phẩm
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-4">
                            <c:forEach var="p" items="${listPro}">
                                <div class="col-6 col-md-4 col-lg-3">
                                    <%@ include file="product-card.jsp" %>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- ✅ FIX 7: Pagination thông minh (hiển thị window 5 trang) -->
                        <c:if test="${totalPages > 1}">
                            <c:set var="startPage" value="${currentPage - 2 > 1 ? currentPage - 2 : 1}" />
                            <c:set var="endPage" value="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}" />
                            
                            <nav aria-label="Page navigation" class="mt-4">
                                <ul class="pagination justify-content-center">
                                    <!-- First + Previous -->
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link pagination-link" 
                                               href="#" 
                                               data-page="1"
                                               title="Trang đầu">
                                                <i class="fas fa-angle-double-left"></i>
                                            </a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link pagination-link" 
                                               href="#" 
                                               data-page="${currentPage - 1}"
                                               title="Trang trước">
                                                <i class="fas fa-angle-left"></i>
                                            </a>
                                        </li>
                                    </c:if>

                                    <!-- Ellipsis before -->
                                    <c:if test="${startPage > 1}">
                                        <li class="page-item disabled">
                                            <span class="page-link">...</span>
                                        </li>
                                    </c:if>

                                    <!-- Page numbers -->
                                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link pagination-link" 
                                               href="#" 
                                               data-page="${i}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:forEach>

                                    <!-- Ellipsis after -->
                                    <c:if test="${endPage < totalPages}">
                                        <li class="page-item disabled">
                                            <span class="page-link">...</span>
                                        </li>
                                    </c:if>

                                    <!-- Next + Last -->
                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link pagination-link" 
                                               href="#" 
                                               data-page="${currentPage + 1}"
                                               title="Trang sau">
                                                <i class="fas fa-angle-right"></i>
                                            </a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link pagination-link" 
                                               href="#" 
                                               data-page="${totalPages}"
                                               title="Trang cuối">
                                                <i class="fas fa-angle-double-right"></i>
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- ✅ JAVASCRIPT ĐÃ SỬA HOÀN CHỈNH -->
    <script>
    (function() {
        'use strict';

        // ============== CONSTANTS ==============
        const CONTEXT_PATH = '${pageContext.request.contextPath}';
        
        // ============== FIX 8: Xử lý form submit ==============
        const filterForm = document.getElementById('filterForm');
        if (filterForm) {
            filterForm.addEventListener('submit', function(e) {
                const form = this;
                
                // Validate giá
                const minPrice = parseFloat(form.minPrice.value) || 0;
                const maxPrice = parseFloat(form.maxPrice.value) || Infinity;
                
                if (minPrice < 0 || maxPrice < 0) {
                    alert('Giá không thể là số âm!');
                    e.preventDefault();
                    return false;
                }
                
                if (minPrice > maxPrice && maxPrice !== Infinity) {
                    alert('Giá tối thiểu không thể lớn hơn giá tối đa!');
                    e.preventDefault();
                    return false;
                }
                
                // Remove empty price inputs
                ['minPrice', 'maxPrice'].forEach(function(name) {
                    const el = form.querySelector('[name="' + name + '"]');
                    if (el && !el.value.trim()) {
                        el.removeAttribute('name');
                    }
                });
                
                // Reset về trang 1 khi filter thay đổi
                let pageInput = form.querySelector('[name="currentPage"]');
                if (!pageInput) {
                    pageInput = document.createElement('input');
                    pageInput.type = 'hidden';
                    pageInput.name = 'currentPage';
                    form.appendChild(pageInput);
                }
                pageInput.value = '1';
            });
        }

        // ============== FIX 9: Xử lý dropdown sort (chỉ giữ params hợp lệ) ==============
        document.querySelectorAll('.sort-link').forEach(function(item) {
            item.addEventListener('click', function(e) {
                e.preventDefault();
                const sortVal = this.getAttribute('data-sort');
                
                // Build URL with valid params only
                const url = new URL(CONTEXT_PATH + '/filter', window.location.origin);
                const validParams = ['category', 'banner', 'brand', 'minPrice', 'maxPrice', 'keyword'];
                const currentParams = new URLSearchParams(window.location.search);
                
                // Copy valid params
                validParams.forEach(function(param) {
                    if (currentParams.has(param)) {
                        const value = currentParams.get(param);
                        if (value && value.trim()) {  // Only add non-empty values
                            url.searchParams.set(param, value);
                        }
                    }
                });
                
                // Set sortBy and reset page
                url.searchParams.set('sortBy', sortVal);
                url.searchParams.set('currentPage', '1');
                
                window.location.href = url.toString();
            });
        });

        // ============== FIX 10: Xử lý pagination (JavaScript) ==============
        function buildFilterUrl(page) {
            const url = new URL(CONTEXT_PATH + '/filter', window.location.origin);
            const params = new URLSearchParams(window.location.search);
            
            // Set page number
            params.set('currentPage', page);
            
            return url.pathname + '?' + params.toString();
        }

        // Attach pagination listeners
        document.querySelectorAll('.pagination-link').forEach(function(link) {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const page = this.getAttribute('data-page');
                if (page) {
                    window.location.href = buildFilterUrl(page);
                }
            });
        });

        // ============== SCROLL TO TOP KHI CHUYỂN TRANG ==============
        if (window.location.search.includes('currentPage=') && 
            !window.location.search.includes('currentPage=1')) {
            // Smooth scroll to product section
            const productSection = document.querySelector('.col-md-9');
            if (productSection) {
                productSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        }

    })();
    </script>

    <!-- Include Cart JS for quick add to cart functionality -->
    <script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
</body>
</html>