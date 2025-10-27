<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom mb-3">
    <div class="container-fluid">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="d-none d-md-block">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/api/shipper/feed">
                        <i class="fas fa-home"></i> Shipper
                    </a>
                </li>
                <c:choose>
                    <c:when test="${fn:contains(pageContext.request.servletPath, '/feed')}">
                        <li class="breadcrumb-item active" aria-current="page">
                            <i class="fas fa-box-open"></i> Đơn hàng có sẵn
                        </li>
                    </c:when>
                    <c:when test="${fn:contains(pageContext.request.servletPath, '/my-deliveries')}">
                        <li class="breadcrumb-item active" aria-current="page">
                            <i class="fas fa-truck"></i> Đơn của tôi
                        </li>
                    </c:when>
                    <c:when test="${fn:contains(pageContext.request.servletPath, '/delivery-detail')}">
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/api/shipper/my-deliveries">
                                <i class="fas fa-truck"></i> Đơn của tôi
                            </a>
                        </li>
                        <li class="breadcrumb-item active" aria-current="page">
                            <i class="fas fa-info-circle"></i> Chi tiết đơn hàng
                        </li>
                    </c:when>
                </c:choose>
            </ol>
        </nav>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <!-- Notifications -->
                <li class="nav-item dropdown me-3">
                    <a class="nav-link position-relative" href="#" id="notificationDropdown" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-bell fa-lg"></i>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                            0
                        </span>
                    </a>
                </li>

                <!-- User menu -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                        <img src="${pageContext.request.contextPath}/assets/images/avatar/default-avatar.png" 
                             alt="Avatar" class="rounded-circle me-2" width="32" height="32">
                        <span>${sessionScope.currentUser.fullname}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><h6 class="dropdown-header">Tài khoản</h6></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile"><i class="fas fa-user me-2"></i>Hồ sơ</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/home"><i class="fas fa-home"></i>Trang chủ</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/logout"><i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Alert messages -->
<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="success" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="error" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.warning}">
    <div class="alert alert-warning alert-dismissible fade show" role="alert">
        <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.warning}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="warning" scope="session" />
</c:if>

