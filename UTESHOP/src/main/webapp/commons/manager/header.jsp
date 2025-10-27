<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Top Navigation for Manager -->
<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark shadow">
    <!-- Logo / Brand -->
    <a class="navbar-brand ps-3 d-flex align-items-center" href="${pageContext.request.contextPath}/api/manager/dashboard">
        <i class="fas fa-store me-2"></i>
        <span class="fw-bold">UTESHOP Manager</span>
    </a>

    <!-- Sidebar Toggle -->
    <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0 text-white"
            id="sidebarToggle" href="#" title="Toggle Sidebar">
        <i class="fas fa-bars"></i>
    </button>

    <!-- Search bar -->
    <form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
        <div class="input-group">
            <input class="form-control" type="text" placeholder="Tìm kiếm..." aria-label="Search"
                   style="background-color: #495057; border: none; color: #fff;"/>
            <button class="btn btn-secondary" type="button">
                <i class="fas fa-search"></i>
            </button>
        </div>
    </form>

    <!-- Notifications -->
    <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle position-relative" id="notificationDropdown" href="#"
               role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="fas fa-bell fa-fw"></i>
                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                    3
                    <span class="visually-hidden">thông báo mới</span>
                </span>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="notificationDropdown" style="min-width: 300px;">
                <li class="dropdown-header bg-dark text-white">
                    <i class="fas fa-bell me-2"></i>Thông báo
                </li>
                <li><a class="dropdown-item py-2" href="#">
                    <i class="fas fa-shopping-cart text-success me-2"></i>
                    <small>Đơn hàng mới #12345</small>
                </a></li>
                <li><a class="dropdown-item py-2" href="#">
                    <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                    <small>Sản phẩm sắp hết hàng</small>
                </a></li>
                <li><a class="dropdown-item py-2" href="#">
                    <i class="fas fa-comment text-info me-2"></i>
                    <small>Đánh giá mới từ khách hàng</small>
                </a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-center text-primary fw-bold" href="#">
                    <i class="fas fa-external-link-alt me-2"></i>Xem tất cả
                </a></li>
            </ul>
        </li>
    </ul>

    <!-- User dropdown -->
    <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="userDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="fas fa-user fa-fw me-1"></i>
                <%  String username = (String) session.getAttribute("username");
                    if (username == null) {
                        username = "Manager";
                    }
                %>
                <%= username %>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="userDropdown" style="min-width: 220px;">
                <li class="dropdown-header bg-light">
                    <div class="text-center">
                        <img src="https://ui-avatars.com/api/?name=<%= username %>&background=6c757d&color=fff&size=64"
                             class="rounded-circle mb-2" width="64" height="64" alt="Avatar">
                        <div class="fw-bold"><%= username %></div>
                        <small class="text-muted">Manager</small>
                    </div>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/home">
                        <i class="fas fa-home fa-fw me-2 text-primary"></i>Trang chủ
                    </a>
                </li>
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/manager/profile">
                        <i class="fas fa-user-circle fa-fw me-2 text-info"></i>Hồ sơ cá nhân
                    </a>
                </li>
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/manager/change-password">
                        <i class="fas fa-key fa-fw me-2 text-warning"></i>Đổi mật khẩu
                    </a>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/auth/logout">
                        <i class="fas fa-sign-out-alt fa-fw me-2"></i>Đăng xuất
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</nav>

<style>
    /* Custom styling for navbar */
    .navbar-brand:hover {
        opacity: 0.9;
        transition: opacity 0.2s;
    }

    .dropdown-menu {
        border: none;
        animation: slideDown 0.3s ease;
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
        transition: all 0.2s;
        padding: 0.5rem 1rem;
    }

    .dropdown-item:hover {
        background-color: #f8f9fa;
        padding-left: 1.5rem;
    }

    .dropdown-item i {
        width: 20px;
        text-align: center;
    }

    .nav-link {
        transition: all 0.2s;
    }

    .nav-link:hover {
        opacity: 0.8;
    }

    /* Search input focus */
    .form-control:focus {
        background-color: #5a6268 !important;
        color: #fff !important;
        box-shadow: 0 0 0 0.2rem rgba(108, 117, 125, 0.25);
    }
</style>