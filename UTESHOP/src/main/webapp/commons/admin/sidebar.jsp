<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Sidebar -->
<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">

        <div class="sb-sidenav-menu">
            <div class="nav">

                <div class="sb-sidenav-menu-heading">Core</div>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Dashboard
                </a>

                <div class="sb-sidenav-menu-heading">Quản lý</div>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                    <div class="sb-nav-link-icon"><i class="fas fa-users"></i></div>
                    Người dùng
                </a>

                <a class="nav-link" href="${pageContext.request.contextPath}/admin/products">
                    <div class="sb-nav-link-icon"><i class="fas fa-box"></i></div>
                    Sản phẩm
                </a>

                <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders">
                    <div class="sb-nav-link-icon"><i class="fas fa-shopping-cart"></i></div>
                    Đơn hàng
                </a>

                <a class="nav-link" href="${pageContext.request.contextPath}/admin/reports">
                    <div class="sb-nav-link-icon"><i class="fas fa-chart-bar"></i></div>
                    Báo cáo
                </a>
            </div>
        </div>

        <div class="sb-sidenav-footer
