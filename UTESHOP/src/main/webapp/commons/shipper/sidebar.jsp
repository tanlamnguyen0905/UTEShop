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
                <a class="nav-link ${param.page == 'feed' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/feed">
                    <i class="fas fa-box-open"></i>
                    Đơn hàng có sẵn
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${param.page == 'my-deliveries' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries">
                    <i class="fas fa-truck"></i>
                    Đơn của tôi
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries?status=Đang giao hàng">
                    <i class="fas fa-shipping-fast"></i>
                    Đang giao
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries?status=Đã giao hàng">
                    <i class="fas fa-check-circle"></i>
                    Đã hoàn thành
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" 
                   href="${pageContext.request.contextPath}/api/shipper/my-deliveries?status=Giao hàng thất bại">
                    <i class="fas fa-times-circle"></i>
                    Thất bại
                </a>
            </li>
        </ul>

        <hr class="bg-light my-3">

        <!-- Additional Links -->
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/user/profile">
                    <i class="fas fa-user"></i>
                    Hồ sơ
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/home">
                    <i class="fas fa-sign-out-alt"></i>
                    Trang chủ
                </a>
            </li>
        </ul>
    </div>
</nav>

