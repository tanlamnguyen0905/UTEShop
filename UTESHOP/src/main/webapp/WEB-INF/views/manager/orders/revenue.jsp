<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Thống kê Doanh Thu - Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .table th { background-color: #f8f9fa; font-weight: 600; }
        .card { margin-bottom: 20px; }
        .alert { border-radius: 8px; }
        .stats-card { transition: transform 0.2s; }
        .stats-card:hover { transform: scale(1.02); }
    </style>
</head>
<body>
<div class="container-fluid px-4">
    <h1 class="mt-4 mb-4 fw-bold text-primary">
        <i class="fas fa-chart-line me-2"></i>Thống kê Doanh Thu
    </h1>

    <!-- Error -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-triangle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Filter Form -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-filter me-2"></i>Lọc Theo Thời Gian
        </div>
        <div class="card-body">
            <form method="POST" class="row g-3">
                <div class="col-md-3">
                    <label class="form-label">Từ ngày:</label>
                    <!-- Fix: Sử dụng string từ Controller, fallback default nếu null -->
                    <input type="date" name="fromDate" class="form-control" value="${fromDateStr != null ? fromDateStr : '2025-10-01'}">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Đến ngày:</label>
                    <input type="date" name="toDate" class="form-control" value="${toDateStr != null ? toDateStr : '2025-10-27'}">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search me-2"></i>Lọc
                    </button>
                </div>
                <div class="col-md-4 d-flex align-items-end">
                    <span class="text-muted">Khoảng thời gian: ${fromDisplay != null ? fromDisplay : '01/10/2025'} - ${toDisplay != null ? toDisplay : '27/10/2025'}</span>
                </div>
            </form>
        </div>
    </div>

    <!-- Tổng hợp Stats -->
    <c:if test="${not empty totalStats && totalStats.revenue > 0}">
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card text-center bg-primary text-white stats-card">
                    <div class="card-body">
                        <i class="fas fa-dollar-sign fa-2x mb-2"></i>
                        <h5>Tổng Doanh Thu</h5>
                        <h2>₫<fmt:formatNumber value="${totalStats.revenue}" type="number" maxFractionDigits="0" groupingUsed="true" /></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-center bg-success text-white stats-card">
                    <div class="card-body">
                        <i class="fas fa-shopping-cart fa-2x mb-2"></i>
                        <h5>Số Đơn Hàng</h5>
                        <h2><fmt:formatNumber value="${totalStats.orderCount}" type="number" /></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-center bg-info text-white stats-card">
                    <div class="card-body">
                        <i class="fas fa-chart-bar fa-2x mb-2"></i>
                        <h5>Doanh Thu TB/Đơn</h5>
                        <h2>₫<fmt:formatNumber value="${totalStats.avgRevenue}" type="number" maxFractionDigits="0" groupingUsed="true" /></h2>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${empty totalStats || totalStats.revenue == 0}">
        <div class="alert alert-warning">
            <i class="fas fa-info-circle me-2"></i>Không có dữ liệu doanh thu trong khoảng thời gian này.
        </div>
    </c:if>

    <!-- Biểu đồ Doanh Thu Theo Ngày -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-chart-line me-2"></i>Biểu Đồ Doanh Thu Theo Ngày
        </div>
        <div class="card-body">
            <canvas id="revenueChart" width="800" height="400"></canvas>
        </div>
    </div>

    <!-- Bảng Chi Tiết -->
    <div class="card">
        <div class="card-header">
            <i class="fas fa-table me-2"></i>Chi Tiết Doanh Thu Theo Ngày
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Ngày</th>
                        <th>Doanh Thu (₫)</th>
                        <th>Thay Đổi So Với Ngày Trước (%)</th>
                        <th>Số Đơn Hàng</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="prevRevenue" value="0" />
                    <c:forEach var="stat" items="${dailyRevenues}" varStatus="status">
                        <c:choose>
                            <c:when test="${status.first}">
                                <c:set var="changePercent" value="0" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="changePercent" value="${((stat.revenue - prevRevenue) / prevRevenue * 100)}" />
                            </c:otherwise>
                        </c:choose>
                        <c:set var="prevRevenue" value="${stat.revenue}" />
                        <tr>
                            <!-- Fix: Format LocalDate an toàn bằng string nếu cần, nhưng EL hỗ trợ toString() cho display -->
                            <td>${stat.date}</td>
                            <td><fmt:formatNumber value="${stat.revenue}" type="number" maxFractionDigits="0" groupingUsed="true" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${changePercent > 0}">
                                        <span class="text-success">+<fmt:formatNumber value="${changePercent}" type="number" maxFractionDigits="1" />%</span>
                                    </c:when>
                                    <c:when test="${changePercent < 0}">
                                        <span class="text-danger"><fmt:formatNumber value="${changePercent}" type="number" maxFractionDigits="1" />%</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">0%</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>1</td>  <!-- Mock; có thể query thêm từ service nếu cần -->
                        </tr>
                    </c:forEach>
                    <c:if test="${empty dailyRevenues}">
                        <tr>
                            <td colspan="4" class="text-center text-muted">Không có dữ liệu chi tiết.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Dữ liệu cho biểu đồ từ JSP (JSON-like) - Sử dụng EL cho LocalDate
    const labels = [
        <c:forEach var="stat" items="${dailyRevenues}" varStatus="status">
        '${stat.date}'<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
    const data = [
        <c:forEach var="stat" items="${dailyRevenues}" varStatus="status">
        ${stat.revenue}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Vẽ biểu đồ line với Chart.js
    const ctx = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh Thu (₫)',
                data: data,
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                tension: 0.1,
                fill: true
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return '₫' + value.toLocaleString('vi-VN');
                        }
                    }
                },
                x: {
                    ticks: {
                        maxRotation: 45
                    }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return 'Doanh thu: ₫' + context.parsed.y.toLocaleString('vi-VN');
                        }
                    }
                }
            }
        }
    });
</script>
</body>
</html>