<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Dashboard</title>
    <style>
        .dashboard-card {
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: none;
            overflow: hidden;
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
        }
        
        .card-icon {
            width: 70px;
            height: 70px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
        }
        
        .stat-value {
            font-size: 2rem;
            font-weight: 700;
            margin: 10px 0;
        }
        
        .stat-label {
            color: #6c757d;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .trend-up {
            color: #28a745;
        }
        
        .section-title {
            font-weight: 600;
            margin-bottom: 20px;
            color: #2c3e50;
            position: relative;
            padding-bottom: 10px;
        }
        
        .section-title::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 50px;
            height: 3px;
            background: linear-gradient(to right, #007bff, #00d4ff);
        }
        
        .quick-action-btn {
            border-radius: 10px;
            padding: 15px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .quick-action-btn:hover {
            transform: scale(1.05);
        }
        
        .recent-item {
            padding: 15px;
            border-radius: 10px;
            background: #f8f9fa;
            margin-bottom: 10px;
            transition: background 0.3s ease;
        }
        
        .recent-item:hover {
            background: #e9ecef;
        }
        
        .product-img-small {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 8px;
        }
        
        .chart-container {
            position: relative;
            height: 300px;
        }
        
        .welcome-banner {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <!-- Welcome Banner -->
    <div class="welcome-banner">
        <h2 class="mb-2"><i class="fas fa-hand-wave"></i> Chào mừng trở lại, Admin!</h2>
        <p class="mb-0">Đây là tổng quan về hiệu suất cửa hàng của bạn hôm nay.</p>
    </div>

    <!-- Statistics Cards -->
    <div class="row mb-4">
        <!-- Total Products -->
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label">Tổng sản phẩm</div>
                            <div class="stat-value text-primary">${totalProducts}</div>
                            <small class="trend-up"><i class="fas fa-arrow-up"></i> 12% so với tháng trước</small>
                        </div>
                        <div class="card-icon bg-primary bg-opacity-10 text-primary">
                            <i class="fas fa-box"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Total Orders -->
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label">Đơn hàng</div>
                            <div class="stat-value text-success">${totalOrders}</div>
                            <small class="trend-up"><i class="fas fa-arrow-up"></i> 8% so với tháng trước</small>
                        </div>
                        <div class="card-icon bg-success bg-opacity-10 text-success">
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Total Users -->
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label">Người dùng</div>
                            <div class="stat-value text-info">${totalUsers}</div>
                            <small class="trend-up"><i class="fas fa-arrow-up"></i> 5% so với tháng trước</small>
                        </div>
                        <div class="card-icon bg-info bg-opacity-10 text-info">
                            <i class="fas fa-users"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Categories -->
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label">Danh mục</div>
                            <div class="stat-value text-danger">${totalCategories}</div>
                            <small class="text-muted"><i class="fas fa-minus"></i> Không đổi</small>
                        </div>
                        <div class="card-icon bg-danger bg-opacity-10 text-danger">
                            <i class="fas fa-th-large"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Charts and Recent Activity -->
    <div class="row mb-4">
        <!-- Sales Chart -->
        <div class="col-xl-8 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <h5 class="section-title">Biểu đồ doanh thu</h5>
                    <div class="chart-container">
                        <canvas id="salesChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Category Distribution -->
        <div class="col-xl-4 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <h5 class="section-title">Phân bố danh mục</h5>
                    <div class="chart-container">
                        <canvas id="categoryChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Recent Products and Best Sellers -->
    <div class="row mb-4">
        <!-- Recent Products -->
        <div class="col-xl-6 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="section-title mb-0">Sản phẩm mới nhất</h5>
                        <a href="${pageContext.request.contextPath}/admin/products/searchpaginated" class="btn btn-sm btn-outline-primary">
                            Xem tất cả <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty recentProducts}">
                            <c:forEach var="product" items="${recentProducts}">
                                <div class="recent-item d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${not empty product.images and not empty product.images[0]}">
                                            <img src="${pageContext.request.contextPath}/image?fname=${product.images[0].link}" 
                                                 class="product-img-small me-3" 
                                                 alt="${product.productName}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                 class="product-img-small me-3" 
                                                 alt="No image">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="flex-grow-1">
                                        <h6 class="mb-1">${product.productName}</h6>
                                        <small class="text-muted">
                                            <fmt:formatNumber value="${product.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                        </small>
                                    </div>
                                    <span class="badge bg-success">Mới</span>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i> Chưa có sản phẩm nào
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Best Sellers -->
        <div class="col-xl-6 mb-4">
            <div class="dashboard-card">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="section-title mb-0">Sản phẩm bán chạy</h5>
                        <a href="${pageContext.request.contextPath}/admin/products/searchpaginated" class="btn btn-sm btn-outline-success">
                            Xem tất cả <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty bestSellers}">
                            <c:forEach var="product" items="${bestSellers}" varStatus="status">
                                <div class="recent-item d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${not empty product.images and not empty product.images[0]}">
                                            <img src="${pageContext.request.contextPath}/image?fname=${product.images[0].link}" 
                                                 class="product-img-small me-3" 
                                                 alt="${product.productName}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                                                 class="product-img-small me-3" 
                                                 alt="No image">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="flex-grow-1">
                                        <h6 class="mb-1">${product.productName}</h6>
                                        <small class="text-muted">
                                            <fmt:formatNumber value="${product.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                        </small>
                                    </div>
                                    <span class="badge bg-warning text-dark">
                                        <i class="fas fa-fire"></i> #${status.index + 1}
                                    </span>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i> Chưa có dữ liệu bán hàng
                            </div>
                        </c:otherwise>
                    </c:choose>
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
                            <a href="${pageContext.request.contextPath}/admin/products/saveOrUpdate" class="btn btn-primary w-100 quick-action-btn">
                                <i class="fas fa-plus-circle fa-2x d-block mb-2"></i>
                                Thêm sản phẩm mới
                            </a>
                        </div>
                        <div class="col-md-3 mb-3">
                            <a href="${pageContext.request.contextPath}/admin/categories/searchpaginated" class="btn btn-success w-100 quick-action-btn">
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

<!-- Chart.js - Load in head -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
<script>
    // Wrap trong DOMContentLoaded để không conflict với Bootstrap scripts
    document.addEventListener('DOMContentLoaded', function() {
        // Đợi một chút để đảm bảo tất cả scripts đã load
        setTimeout(function() {
            // Sales Chart
            const salesCtx = document.getElementById('salesChart');
            if (salesCtx) {
                const salesChart = new Chart(salesCtx.getContext('2d'), {
                    type: 'line',
                    data: {
                        labels: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
                        datasets: [{
                            label: 'Doanh thu (triệu VNĐ)',
                            data: [12, 19, 15, 25, 22, 30, 28, 35, 32, 38, 42, 45],
                            borderColor: 'rgb(75, 192, 192)',
                            backgroundColor: 'rgba(75, 192, 192, 0.1)',
                            tension: 0.4,
                            fill: true
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                display: true,
                                position: 'top',
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            }

            // Category Chart
            const categoryCtx = document.getElementById('categoryChart');
            if (categoryCtx) {
                const categoryChart = new Chart(categoryCtx.getContext('2d'), {
                    type: 'doughnut',
                    data: {
                        labels: ['Điện thoại', 'Laptop', 'Tablet', 'Phụ kiện', 'Khác'],
                        datasets: [{
                            data: [35, 25, 15, 20, 5],
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.8)',
                                'rgba(54, 162, 235, 0.8)',
                                'rgba(255, 206, 86, 0.8)',
                                'rgba(75, 192, 192, 0.8)',
                                'rgba(153, 102, 255, 0.8)'
                            ],
                            borderWidth: 0
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'bottom',
                            }
                        }
                    }
                });
            }
        }, 100); // Delay 100ms để đảm bảo Bootstrap đã init xong
    });
</script>
</body>
</html>
