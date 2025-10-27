<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manager Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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

        .trend-down {
            color: #dc3545;
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

        .table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }

        .table td {
            vertical-align: middle;
        }

        .badge-status {
            font-size: 0.85em;
            padding: 0.4em 0.8em;
        }

        .badge-pending {
            background-color: #ffc107;
        }

        .badge-completed {
            background-color: #28a745;
        }

        .badge-cancelled {
            background-color: #dc3545;
        }

        .no-data {
            text-align: center;
            padding: 30px;
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="container-fluid px-4">
    <!-- Welcome Banner -->
    <div class="welcome-banner">
        <div class="row align-items-center">
            <div class="col-md-8">
                <h1 class="mb-2">Chào mừng quay lại, Manager!</h1>
                <p class="mb-0">Hôm nay là <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy" />. Hãy kiểm tra các chỉ số chính của cửa hàng.</p>
            </div>
            <div class="col-md-4 text-end">
                <i class="fas fa-store fa-3x opacity-75"></i>
            </div>
        </div>
    </div>

    <!-- Key Metrics -->
    <div class="row mb-4">
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card dashboard-card bg-primary text-white h-100">
                <div class="card-body">
                    <div class="d-flex align-items-center">
                        <div class="card-icon bg-white bg-opacity-20 rounded-circle me-3">
                            <i class="fas fa-shopping-cart text-white"></i>
                        </div>
                        <div class="flex-grow-1">
                            <div class="stat-label">Tổng đơn hàng</div>
                            <div class="stat-value">${totalOrders}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card dashboard-card bg-success text-white h-100">
                <div class="card-body">
                    <div class="d-flex align-items-center">
                        <div class="card-icon bg-white bg-opacity-20 rounded-circle me-3">
                            <i class="fas fa-dollar-sign text-white"></i>
                        </div>
                        <div class="flex-grow-1">
                            <div class="stat-label">Doanh thu tháng</div>
                            <div class="stat-value">₫<fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="0" /></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card dashboard-card bg-warning text-white h-100">
                <div class="card-body">
                    <div class="d-flex align-items-center">
                        <div class="card-icon bg-white bg-opacity-20 rounded-circle me-3">
                            <i class="fas fa-clock text-white"></i>
                        </div>
                        <div class="flex-grow-1">
                            <div class="stat-label">Đơn đang chờ</div>
                            <div class="stat-value">${pendingOrders}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card dashboard-card bg-info text-white h-100">
                <div class="card-body">
                    <div class="d-flex align-items-center">
                        <div class="card-icon bg-white bg-opacity-20 rounded-circle me-3">
                            <i class="fas fa-chart-line text-white"></i>
                        </div>
                        <div class="flex-grow-1">
                            <div class="stat-label">Tăng trưởng</div>
                            <div class="stat-value ${growthRate >= 0 ? 'trend-up' : 'trend-down'}">
                                ${growthRate >= 0 ? '+' : ''}<fmt:formatNumber value="${growthRate}" type="number" maxFractionDigits="1" />%
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Charts Section -->
    <div class="row mb-4">
        <div class="col-xl-12 mb-4">
            <div class="card dashboard-card">
                <div class="card-header">
                    <h5 class="card-title mb-0"><i class="fas fa-chart-line me-2"></i>Doanh thu 12 tháng gần nhất</h5>
                </div>
                <div class="card-body">
                    <div class="chart-container">
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Recent Orders -->
    <div class="row mb-4">
        <div class="col-12">
            <h5 class="section-title">Đơn hàng gần đây</h5>
            <div class="card dashboard-card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty recentOrders}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                    <tr>
                                        <th>Mã đơn</th>
                                        <th>Khách hàng</th>
                                        <th>Ngày đặt</th>
                                        <th>Trạng thái</th>
                                        <th>Tổng tiền</th>
                                        <th>Hành động</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="order" items="${recentOrders}" varStatus="status">
                                        <tr>
                                            <td>#${order.orderID}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty order.user and not empty order.user.fullname}">
                                                        ${order.user.fullname}
                                                    </c:when>
                                                    <c:when test="${not empty order.recipientName}">
                                                        ${order.recipientName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Khách hàng
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty order.orderDate}">
                                                        ${order.orderDate.dayOfMonth}/${order.orderDate.monthValue}/${order.orderDate.year}
                                                        ${order.orderDate.hour}:${order.orderDate.minute < 10 ? '0' : ''}${order.orderDate.minute}
                                                    </c:when>
                                                    <c:otherwise>N/A</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${order.orderStatus eq 'Đang chờ'}">
                                                        <span class="badge badge-pending badge-status">${order.orderStatus}</span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus eq 'Hoàn thành'}">
                                                        <span class="badge badge-completed badge-status">${order.orderStatus}</span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus eq 'Đã hủy'}">
                                                        <span class="badge badge-cancelled badge-status">${order.orderStatus}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-info badge-status">${order.orderStatus}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty order.amount}">
                                                        ₫<fmt:formatNumber value="${order.amount}" type="number" maxFractionDigits="0" />
                                                    </c:when>
                                                    <c:otherwise>₫0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/api/manager/orders/detail?id=${order.orderID}"
                                                   class="btn btn-sm btn-outline-primary">
                                                    <i class="fas fa-eye me-1"></i>Chi tiết
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="no-data">
                                <i class="fas fa-inbox fa-3x mb-3"></i>
                                <p>Chưa có đơn hàng nào</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="row">
        <div class="col-12">
            <h5 class="section-title">Hành động nhanh</h5>
            <div class="row">
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/api/manager/products/searchpaginated" class="btn quick-action-btn btn-primary w-100">
                        <i class="fas fa-box me-2"></i>Xem sản phẩm
                    </a>
                </div>
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/api/manager/orders" class="btn quick-action-btn btn-success w-100">
                        <i class="fas fa-shopping-cart me-2"></i>Xem đơn hàng
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Lấy dữ liệu từ controller
        const monthlyRevenues = [
            <c:forEach var="revenue" items="${monthlyRevenues}" varStatus="status">
            ${revenue.revenue}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        // Tạo labels cho 12 tháng
        const labels = [];
        const currentDate = new Date();
        for (let i = 11; i >= 0; i--) {
            const date = new Date(currentDate.getFullYear(), currentDate.getMonth() - i, 1);
            labels.push('Th' + (date.getMonth() + 1) + '/' + date.getFullYear());
        }

        // Revenue Chart
        const revenueCtx = document.getElementById('revenueChart');
        if (revenueCtx) {
            new Chart(revenueCtx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Doanh thu (VNĐ)',
                        data: monthlyRevenues,
                        borderColor: 'rgb(75, 192, 192)',
                        backgroundColor: 'rgba(75, 192, 192, 0.1)',
                        tension: 0.4,
                        fill: true,
                        borderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    label += new Intl.NumberFormat('vi-VN', {
                                        style: 'currency',
                                        currency: 'VND'
                                    }).format(context.parsed.y);
                                    return label;
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return new Intl.NumberFormat('vi-VN', {
                                        notation: 'compact',
                                        compactDisplay: 'short'
                                    }).format(value);
                                }
                            }
                        }
                    }
                }
            });
        }
    });
</script>
</body>
</html>