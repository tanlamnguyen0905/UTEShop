<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="col-md-3 col-lg-2 d-md-block sidebar">
    <div class="position-sticky pt-3">
        <!-- Logo -->
        <div class="text-center mb-4">
            <h4 class="text-white fw-bold">
                <i class="fas fa-shipping-fast me-2"></i>
                SHIPPER
            </h4>
        </div>

        <!-- Navigation -->
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link ${param.page == 'dashboard' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/dashboard">
                    <i class="fas fa-tachometer-alt"></i>
                    Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${param.page == 'deliveries' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/deliveries">
                    <i class="fas fa-box"></i>
                    Đơn giao hàng
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" 
                   href="${pageContext.request.contextPath}/api/shipper/deliveries?status=Đang giao hàng">
                    <i class="fas fa-truck"></i>
                    Đang giao
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" 
                   href="${pageContext.request.contextPath}/api/shipper/deliveries?status=Đã giao hàng">
                    <i class="fas fa-check-circle"></i>
                    Đã hoàn thành
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" 
                   href="${pageContext.request.contextPath}/api/shipper/deliveries?status=Giao hàng thất bại">
                    <i class="fas fa-times-circle"></i>
                    Thất bại
                </a>
            </li>
        </ul>

        <hr class="bg-light my-3">

        <!-- Additional Links -->
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/profile">
                    <i class="fas fa-user"></i>
                    Hồ sơ
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/auth/logout">
                    <i class="fas fa-sign-out-alt"></i>
                    Đăng xuất
                </a>
            </li>
        </ul>
    </div>
</nav>

