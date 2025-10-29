<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Tổng quan - Dashboard</title>
    <link href="${pageContext.request.contextPath}/assets/styles/dashboard.css" rel="stylesheet"/>
</head>
<body>
<div class="container-fluid">
    <!-- Welcome Banner -->
    <div class="welcome-banner">
        <h2 class="mb-2"><i class="fas fa-hand-wave"></i> Chào mừng trở lại, Admin!</h2>
        <p class="mb-0">Đây là tổng quan về hiệu suất của hàng của bạn hôm nay.</p>
    </div>

    <!-- Statistics Cards (KPI) -->
    <div class="row g-3 mb-4" id="kpiRow">
        <!-- Will be populated by JavaScript -->
    </div>

    <!-- Charts Row -->
    <div class="row g-3 mb-4">
        <!-- Revenue Chart -->
        <div class="col-xl-8">
            <div class="dashboard-card">
                <div class="card-body">
                    <h5 class="section-title">Doanh thu 14 ngày gần nhất</h5>
                    <div class="chart-container">
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order Status Chart -->
        <div class="col-xl-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <h5 class="section-title">Trạng thái đơn hàng</h5>
                    <div class="chart-container">
                        <canvas id="statusChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Top Products and Customers -->
    <div class="row g-3 mb-4">
        <!-- Top Products -->
        <div class="col-xl-6">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="section-title mb-0">Top sản phẩm bán chạy</h5>
                        <a href="${pageContext.request.contextPath}/api/admin/products/searchpaginated" 
                           class="btn btn-sm btn-outline-primary">
                            Xem tất cả <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover table-sm mb-0 align-middle" id="topProductsTable">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Sản phẩm</th>
                                    <th class="text-end">SL</th>
                                    <th class="text-end">Doanh thu</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Will be populated by JavaScript -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Top Customers -->
        <div class="col-xl-6">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="section-title mb-0">Top khách hàng</h5>
                        <a href="#" class="btn btn-sm btn-outline-success">
                            Xem tất cả <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover table-sm mb-0 align-middle" id="topCustomersTable">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Khách hàng</th>
                                    <th class="text-end">Đơn</th>
                                    <th class="text-end">Chi tiêu</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Will be populated by JavaScript -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Category Sales Chart -->
    <div class="row g-3 mb-4">
        <div class="col-12">
            <div class="dashboard-card">
                <div class="card-body">
                    <h5 class="section-title">Doanh thu theo danh mục</h5>
                    <div class="chart-container" style="height: 350px;">
                        <canvas id="categoryChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="dashboard-card">
                <div class="card-body">
                    <h5 class="section-title">Thao tác nhanh</h5>
                    <div class="row">
                        <div class="col-md-3 mb-3">
                            <a href="${pageContext.request.contextPath}/api/admin/products/saveOrUpdate" 
                               class="btn btn-primary w-100 quick-action-btn">
                                <i class="fas fa-plus-circle fa-2x d-block mb-2"></i>
                                Thêm sản phẩm mới
                            </a>
                        </div>
                        <div class="col-md-3 mb-3">
                            <a href="${pageContext.request.contextPath}/api/admin/categories/searchpaginated" 
                               class="btn btn-success w-100 quick-action-btn">
                                <i class="fas fa-folder-plus fa-2x d-block mb-2"></i>
                                Quản lý danh mục
                            </a>
                        </div>
                        <div class="col-md-3 mb-3">
                            <a href="#" class="btn btn-warning w-100 quick-action-btn">
                                <i class="fas fa-clipboard-list fa-2x d-block mb-2"></i>
                                Xem đơn hàng
                            </a>
                        </div>
                        <div class="col-md-3 mb-3">
                            <a href="#" class="btn btn-info w-100 quick-action-btn">
                                <i class="fas fa-chart-line fa-2x d-block mb-2"></i>
                                Báo cáo chi tiết
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/scripts/dashboard.js"></script>
</body>
</html>