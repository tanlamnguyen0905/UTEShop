<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Sidebar -->
<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">

                <!-- CORE SECTION -->
                <div class="sb-sidenav-menu-heading">Core</div>
                <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/dashboard">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/products/searchpaginated">
                            <i class="fas fa-list me-1"></i> Danh sách sản phẩm
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate">
                            <i class="fas fa-plus me-1"></i> Thêm sản phẩm
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/products/export">
                            <i class="fas fa-file-excel me-1"></i> Xuất Excel
                        </a>
                    </nav>
                </div>

                <!-- Categories with submenu -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseCategories">
                    <div class="sb-nav-link-icon"><i class="fas fa-th-large"></i></div>
                    Danh mục
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseCategories">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/categories/searchpaginated">
                            <i class="fas fa-list me-1"></i> Danh sách danh mục
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/categories/saveOrUpdate">
                            <i class="fas fa-plus me-1"></i> Thêm danh mục
                        </a>
                    </nav>
                </div>

                <!-- Brands with submenu -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseBrands">
                    <div class="sb-nav-link-icon"><i class="fas fa-tags"></i></div>
                    Thương hiệu
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseBrands">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/brands/searchpaginated">
                            <i class="fas fa-list me-1"></i> Danh sách thương hiệu
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/brands/saveOrUpdate">
                            <i class="fas fa-plus me-1"></i> Thêm thương hiệu
                        </a>
                    </nav>
                </div>

                <!-- QUẢN LÝ BÁN HÀNG -->
                <div class="sb-sidenav-menu-heading">Quản lý bán hàng</div>

                <!-- Orders -->
                <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/orders">
                    <div class="sb-nav-link-icon"><i class="fas fa-shopping-cart"></i></div>
                    Đơn hàng
                </a>

                <!-- Voucher -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseUserCoupon">
                    <div class="sb-nav-link-icon"><i class="fas fa-gift"></i></div>
                    Mã Giảm Giá
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseUserCoupon">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/voucher/searchpaginated">
                            <i class="fas fa-list me-1"></i> Danh sách Voucher
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/voucher/saveOrUpdate">
                            <i class="fas fa-plus me-1"></i> Thêm Voucher
                        </a>
                    </nav>
                </div>


                <!-- Danh Gia -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseReview">
                    <div class="sb-nav-link-icon"><i class="fas fa-gift"></i></div>
                    Đánh Giá
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseReview">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/review/searchpaginated">
                            <i class="fas fa-list me-1"></i> Danh sách đánh giá
                        </a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/review/exportExcel">
                            <i class="fas fa-plus me-1"></i> Xuất đánh giá
                        </a>
                    </nav>
                </div>

                <!-- QUẢN LÝ NGƯỜI DÙNG -->
                <div class="sb-sidenav-menu-heading">Quản lý người dùng</div>

                <!-- Users -->
                <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/users">
                    <div class="sb-nav-link-icon"><i class="fas fa-users"></i></div>
                    Người dùng
                </a>

                <!-- Banners -->
                <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/banner">
                    <div class="sb-nav-link-icon"><i class="fas fa-images"></i></div>
                    Banner quảng cáo
                </a>


                <!-- Reports with submenu -->
                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseReports">
                    <div class="sb-nav-link-icon"><i class="fas fa-chart-bar"></i></div>
                    Báo cáo
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseReports">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="#">
                            <i class="fas fa-dollar-sign me-1"></i> Báo cáo doanh thu
                        </a>
                        <a class="nav-link" href="#">
                            <i class="fas fa-box me-1"></i> Báo cáo sản phẩm
                        </a>
                        <a class="nav-link" href="#">
                            <i class="fas fa-user-friends me-1"></i> Báo cáo khách hàng
                        </a>
                    </nav>
                </div>

                <!-- CÀI ĐẶT -->
                <div class="sb-sidenav-menu-heading">Hệ thống</div>

                <!-- Settings -->
                <a class="nav-link" href="${pageContext.request.contextPath}/api/admin/settings">
                    <div class="sb-nav-link-icon"><i class="fas fa-cog"></i></div>
                    Cài đặt
                </a>

            </div>
        </div>

        <div class="sb-sidenav-footer">
            <div class="small">Đăng nhập:</div>
            <%
                String username = (String) session.getAttribute("username");
                if (username == null) {
                    username = "Admin";
                }
            %>
            <%= username %>
        </div>
    </nav>
</div>