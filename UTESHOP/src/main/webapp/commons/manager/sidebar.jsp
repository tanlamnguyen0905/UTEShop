<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Sidebar for Manager -->
<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">

                <!-- CORE SECTION -->
                <div class="sb-sidenav-menu-heading">Core</div>
                <a class="nav-link active" href="${pageContext.request.contextPath}/api/manager/dashboard">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Dashboard
                </a>

                <!-- QUẢN LÝ SẢN PHẨM -->
                <div class="sb-sidenav-menu-heading">Quản lý sản phẩm</div>

                <!-- Products with submenu -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseProducts">
                    <div class="sb-nav-link-icon"><i class="fas fa-box"></i></div>
                    Sản phẩm
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseProducts">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/manager/products/searchpaginated">
                            <i class="fas fa-list me-1"></i> Danh sách sản phẩm
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/manager/products/saveOrUpdate">
                            <i class="fas fa-plus me-1"></i> Thêm sản phẩm
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/manager/products/export">
                            <i class="fas fa-file-excel me-1"></i> Xuất Excel
                        </a>
                    </nav>
                </div>


                <!-- QUẢN LÝ BÁN HÀNG -->
                <div class="sb-sidenav-menu-heading">Quản lý bán hàng</div>

                <!-- Orders -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseOrders">
                    <div class="sb-nav-link-icon"><i class="fas fa-shopping-cart"></i></div>
                    Đơn hàng
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseOrders">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/manager/orders">
                            <i class="fas fa-list me-1"></i> Danh sách đơn hàng
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/manager/orders/revenue">
                            <i class="fas fa-chart-line me-1"></i> Thống kê doanh thu
                        </a>
                    </nav>
                </div>

            </div>
        </div>

        <div class="sb-sidenav-footer">
            <div class="small">Đăng nhập:</div>
            <%
                String username = (String) session.getAttribute("username");
                if (username == null) {
                    username = "Manager";
                }
            %>
            <%= username %>
        </div>
    </nav>
</div>