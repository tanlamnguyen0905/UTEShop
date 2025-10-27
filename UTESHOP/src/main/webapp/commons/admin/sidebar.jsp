<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="currentPath" value="${pageContext.request.requestURI}" />

<!-- Sidebar -->
<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">

                <!-- DASHBOARD -->
                <div class="sb-sidenav-menu-heading">Tổng quan</div>
                <a class="nav-link ${currentPath.contains('/dashboard') ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/admin/dashboard">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Dashboard
                </a>

                <!-- QUẢN LÝ SẢN PHẨM -->
                <div class="sb-sidenav-menu-heading">Quản lý sản phẩm</div>

                <!-- Products with submenu -->
                <a class="nav-link collapsed ${currentPath.contains('/products') ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#collapseProducts">
                    <div class="sb-nav-link-icon"><i class="fas fa-box"></i></div>
                    Sản phẩm
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse ${currentPath.contains('/products') ? 'show' : ''}" id="collapseProducts">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link ${currentPath.contains('/products/searchpaginated') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/products/searchpaginated">
                            <i class="fas fa-list me-2"></i>Danh sách sản phẩm
                        </a>
                        <a class="nav-link ${currentPath.contains('/products/saveOrUpdate') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate">
                            <i class="fas fa-plus me-2"></i>Thêm sản phẩm
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/products/export">
                            <i class="fas fa-file-excel me-2"></i>Xuất Excel
                        </a>
                    </nav>
                </div>

                <!-- Categories with submenu -->
                <a class="nav-link collapsed ${currentPath.contains('/categories') ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#collapseCategories">
                    <div class="sb-nav-link-icon"><i class="fas fa-th-large"></i></div>
                    Danh mục
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse ${currentPath.contains('/categories') ? 'show' : ''}" id="collapseCategories">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link ${currentPath.contains('/categories/searchpaginated') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/categories/searchpaginated">
                            <i class="fas fa-list me-2"></i>Danh sách danh mục
                        </a>
                        <a class="nav-link ${currentPath.contains('/categories/saveOrUpdate') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/categories/saveOrUpdate">
                            <i class="fas fa-plus me-2"></i>Thêm danh mục
                        </a>
                    </nav>
                </div>

                <!-- Brands with submenu -->
                <a class="nav-link collapsed ${currentPath.contains('/brands') ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#collapseBrands">
                    <div class="sb-nav-link-icon"><i class="fas fa-tags"></i></div>
                    Thương hiệu
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse ${currentPath.contains('/brands') ? 'show' : ''}" id="collapseBrands">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link ${currentPath.contains('/brands/searchpaginated') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/brands/searchpaginated">
                            <i class="fas fa-list me-2"></i>Danh sách thương hiệu
                        </a>
                        <a class="nav-link ${currentPath.contains('/brands/saveOrUpdate') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/brands/saveOrUpdate">
                            <i class="fas fa-plus me-2"></i>Thêm thương hiệu
                        </a>
                    </nav>
                </div>

                <!-- QUẢN LÝ BÁN HÀNG -->
                <div class="sb-sidenav-menu-heading">Quản lý bán hàng</div>

                <!-- Orders -->
                <a class="nav-link ${currentPath.contains('/orders') ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/admin/orders">
                    <div class="sb-nav-link-icon"><i class="fas fa-shopping-cart"></i></div>
                    Đơn hàng
                </a>

                <!-- Voucher -->
                <a class="nav-link collapsed ${currentPath.contains('/voucher') ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#collapseVoucher">
                    <div class="sb-nav-link-icon"><i class="fas fa-ticket-alt"></i></div>
                    Mã giảm giá
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse ${currentPath.contains('/voucher') ? 'show' : ''}" id="collapseVoucher">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link ${currentPath.contains('/voucher/searchpaginated') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/voucher/searchpaginated">
                            <i class="fas fa-list me-2"></i>Danh sách voucher
                        </a>
                        <a class="nav-link ${currentPath.contains('/voucher/saveOrUpdate') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/voucher/saveOrUpdate">
                            <i class="fas fa-plus me-2"></i>Thêm voucher
                        </a>
                    </nav>
                </div>

                <!-- Review -->
                <a class="nav-link collapsed ${currentPath.contains('/review') ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#collapseReview">
                    <div class="sb-nav-link-icon"><i class="fas fa-star"></i></div>
                    Đánh giá
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse ${currentPath.contains('/review') ? 'show' : ''}" id="collapseReview">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link ${currentPath.contains('/review/searchpaginated') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/api/admin/review/searchpaginated">
                            <i class="fas fa-list me-2"></i>Danh sách đánh giá
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/review/exportExcel">
                            <i class="fas fa-file-excel me-2"></i>Xuất đánh giá
                        </a>
                    </nav>
                </div>

                <!-- QUẢN LÝ NGƯỜI DÙNG & HỆ THỐNG -->
                <div class="sb-sidenav-menu-heading">Quản lý hệ thống</div>

                <!-- Users -->
                <a class="nav-link ${currentPath.contains('/users') || currentPath.contains('/user/') ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/admin/users">
                    <div class="sb-nav-link-icon"><i class="fas fa-users"></i></div>
                    Người dùng
                </a>

                <!-- Chat -->
                <a class="nav-link ${currentPath.contains('/chat') ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/admin/chat">
                    <div class="sb-nav-link-icon"><i class="fas fa-comments"></i></div>
                    Chat hỗ trợ
                    <c:if test="${not empty unreadChatCount && unreadChatCount > 0}">
                        <span class="badge bg-danger ms-auto">${unreadChatCount}</span>
                    </c:if>
                </a>

                <!-- Banners -->
                <a class="nav-link ${currentPath.contains('/banner') ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/admin/banner">
                    <div class="sb-nav-link-icon"><i class="fas fa-images"></i></div>
                    Banner quảng cáo
                </a>

                <!-- Settings -->
                <a class="nav-link ${currentPath.contains('/settings') ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/home">
                    <div class="sb-nav-link-icon"><i class="fa-solid fa-house"></i>
</i></div>
                    Trang chủ
                </a>

            </div>
        </div>
    </nav>
</div>

<style>
    /* Active state for nav links */
    .sb-sidenav .nav-link.active {
        background: linear-gradient(90deg, rgba(102, 126, 234, 0.1) 0%, transparent 100%);
        border-left: 3px solid #667eea;
        color: #667eea !important;
    }
    
    .sb-sidenav .nav-link.active .sb-nav-link-icon {
        color: #667eea;
    }
    
    /* Nested nav active state */
    .sb-sidenav-menu-nested .nav-link.active {
        background: rgba(102, 126, 234, 0.1);
        color: #667eea !important;
        font-weight: 600;
    }
    
    /* Hover effect */
    .sb-sidenav .nav-link:hover {
        background: rgba(255, 255, 255, 0.05);
        padding-left: 1.25rem;
        transition: all 0.2s ease;
    }
    
    .sb-sidenav-menu-nested .nav-link:hover {
        padding-left: 2.5rem;
    }
    
    /* Icon styling */
    .sb-nav-link-icon {
        width: 1.5rem;
        text-align: center;
        margin-right: 0.5rem;
    }
    
    /* Badge styling */
    .sb-sidenav .badge {
        font-size: 0.65rem;
        padding: 0.25rem 0.5rem;
    }
    
    /* Footer styling */
    .sb-sidenav-footer {
        background: rgba(0, 0, 0, 0.2);
        padding: 1rem;
    }
    
    /* Menu heading */
    .sb-sidenav-menu-heading {
        padding: 1rem 1rem 0.5rem;
        font-size: 0.75rem;
        font-weight: 800;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: rgba(255, 255, 255, 0.4);
    }
    
    /* Collapse arrow animation */
    .sb-sidenav-collapse-arrow {
        transition: transform 0.2s ease;
    }
    
    .nav-link:not(.collapsed) .sb-sidenav-collapse-arrow {
        transform: rotate(180deg);
    }
</style>
