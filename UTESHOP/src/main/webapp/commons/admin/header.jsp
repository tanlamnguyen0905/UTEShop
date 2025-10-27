<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Top Navigation -->
<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark shadow-sm">
    <!-- Logo / Brand -->
    <a class="navbar-brand ps-3 d-flex align-items-center" href="${pageContext.request.contextPath}/api/admin/dashboard">
        <i class="fas fa-store me-2"></i>
        <span class="fw-bold">UTESHOP Admin</span>
    </a>

    <!-- Sidebar Toggle -->
    <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0 text-white"
            id="sidebarToggle" title="Toggle Sidebar">
        <i class="fas fa-bars"></i>
    </button>

    <!-- Spacer -->
    <div class="ms-auto"></div>

    <!-- Quick Actions -->
    <ul class="navbar-nav ms-0 me-3 d-none d-lg-flex">
        <!-- Chat -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/chat" title="Chat">
                <i class="fas fa-comments fa-fw"></i>
            </a>
        </li>
        
        <!-- Notifications -->
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle position-relative" id="notificationDropdown" href="#"
               role="button" data-bs-toggle="dropdown" aria-expanded="false" title="Thông báo">
                <i class="fas fa-bell fa-fw"></i>
                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem;">
                    3
                </span>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow animated-dropdown" style="min-width: 320px;">
                <li class="dropdown-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <span><i class="fas fa-bell me-2"></i>Thông báo</span>
                    <span class="badge bg-light text-primary">3</span>
                </li>
                <li><a class="dropdown-item py-2 border-bottom" href="${pageContext.request.contextPath}/api/admin/orders">
                    <div class="d-flex align-items-center">
                        <div class="flex-shrink-0">
                            <i class="fas fa-shopping-cart text-success fa-lg"></i>
                        </div>
                        <div class="flex-grow-1 ms-3">
                            <div class="small fw-bold">Đơn hàng mới</div>
                        </div>
                    </div>
                </a></li>
                <li><a class="dropdown-item py-2 border-bottom" href="${pageContext.request.contextPath}/api/admin/products/searchpaginated">
                    <div class="d-flex align-items-center">
                        <div class="flex-shrink-0">
                            <i class="fas fa-exclamation-triangle text-warning fa-lg"></i>
                        </div>
                        <div class="flex-grow-1 ms-3">
                            <div class="small fw-bold">Sản phẩm sắp hết hàng</div>
                        </div>
                    </div>
                </a></li>
                <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/api/admin/review/searchpaginated">
                    <div class="d-flex align-items-center">
                        <div class="flex-shrink-0">
                            <i class="fas fa-star text-warning fa-lg"></i>
                        </div>
                        <div class="flex-grow-1 ms-3">
                            <div class="small fw-bold">Đánh giá mới từ khách hàng</div>
                        </div>
                    </div>
                </a></li>
               
            </ul>
        </li>
    </ul>

    <!-- User dropdown -->
    <ul class="navbar-nav ms-0 me-3 me-lg-4">
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle d-flex align-items-center" id="userDropdown" href="#"
               role="button" data-bs-toggle="dropdown" aria-expanded="false">
               <c:set var="currentUser" value="${sessionScope.currentUser}" />
               <c:set var="displayName" value="${not empty currentUser.fullname ? currentUser.fullname : (not empty currentUser.username ? currentUser.username : 'Admin')}" />
               <c:set var="avatarName" value="${not empty currentUser.fullname ? currentUser.fullname : (not empty currentUser.username ? currentUser.username : 'Admin')}" />
               <c:set var="userRole" value="${not empty currentUser.role ? currentUser.role : 'Admin'}" />
               
                <img src="https://ui-avatars.com/api/?name=${avatarName}&background=4e73df&color=fff&size=32" 
                     class="rounded-circle me-2" width="32" height="32" alt="Avatar">
                <span class="d-none d-lg-inline">${displayName}</span>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow animated-dropdown" style="min-width: 240px;">
                <li class="dropdown-header bg-gradient-primary text-white">
                    <div class="text-center py-2">
                        <img src="https://ui-avatars.com/api/?name=${avatarName}&background=4e73df&color=fff&size=64" 
                             class="rounded-circle mb-2 border border-3 border-white" width="64" height="64" alt="Avatar">
                        <div class="fw-bold">${displayName}</div>
                        <small class="opacity-75">
                            <c:choose>
                                <c:when test="${userRole eq 'ADMIN'}">
                                    <i class="fas fa-user-shield me-1"></i>Administrator
                                </c:when>
                                <c:when test="${userRole eq 'MANAGER'}">
                                    <i class="fas fa-user-tie me-1"></i>Manager
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-user me-1"></i>${userRole}
                                </c:otherwise>
                            </c:choose>
                        </small>
                    </div>
                </li>
                <li><hr class="dropdown-divider my-0"></li>
                <li>
                    <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/api/admin/dashboard">
                        <i class="fas fa-tachometer-alt fa-fw me-2 text-primary"></i>Dashboard
                    </a>
                </li>
                <li>
                    <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/home">
                        <i class="fa fa-house text-secondary"></i> Trang chủ
                    </a>
                </li>
                <li><hr class="dropdown-divider my-0"></li>
                <li>
                    <a class="dropdown-item py-2 text-danger" href="${pageContext.request.contextPath}/auth/logout">
                        <i class="fas fa-sign-out-alt fa-fw me-2"></i>Đăng xuất
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</nav>

<style>
    .bg-gradient-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    .navbar-brand {
        transition: all 0.3s ease;
    }
    
    .navbar-brand:hover {
        opacity: 0.9;
        transform: scale(1.02);
    }
    
    .animated-dropdown {
        border: none;
        animation: slideDown 0.2s ease;
    }
    
    @keyframes slideDown {
        from {
            opacity: 0;
            transform: translateY(-10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .dropdown-item {
        transition: all 0.2s ease;
        padding: 0.5rem 1rem;
    }
    
    .dropdown-item:hover {
        background-color: #f8f9fa;
        padding-left: 1.25rem;
    }
    
    .dropdown-item i.fa-fw {
        width: 20px;
        text-align: center;
    }
    
    .nav-link {
        transition: all 0.2s ease;
    }
    
    .nav-link:hover {
        opacity: 0.8;
        transform: translateY(-1px);
    }
    
    #sidebarToggle {
        transition: all 0.2s ease;
    }
    
    #sidebarToggle:hover {
        transform: scale(1.1);
    }
</style>
