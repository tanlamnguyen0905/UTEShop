<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Tổng quan</title>
    <link href="${pageContext.request.contextPath}/assets/styles/dashboard.css" rel="stylesheet"/>
</head>
<body>
<div class="container-fluid my-4">
    <h3 class="mb-4">Tổng quan</h3>

    <div class="row g-3 mb-3" id="kpiRow">
        
    </div>

    <div class="row g-3 mb-3">
        <div class="col-lg-8">
            <div class="card h-100">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span class="fw-semibold">Doanh thu 14 ngày gần nhất</span>
                </div>
                <div class="card-body">
                    <canvas id="revenueChart" height="140"></canvas>
                </div>
            </div>
        </div>
        <div class="col-lg-4">
            <div class="card h-100">
                <div class="card-header fw-semibold">Trạng thái đơn hàng</div>
                <div class="card-body">
                    <canvas id="statusChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-3 mb-3">
        <div class="col-xl-6">
            <div class="card h-100">
                <div class="card-header fw-semibold">Top sản phẩm</div>
                <div class="table-responsive">
                    <table class="table table-hover table-sm mb-0 align-middle" id="topProductsTable">
                        <thead><tr><th>#</th><th>Sản phẩm</th><th class="text-end">SL</th><th class="text-end">Doanh thu</th></tr></thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-xl-6">
            <div class="card h-100">
                <div class="card-header fw-semibold">Top khách hàng</div>
                <div class="table-responsive">
                    <table class="table table-hover table-sm mb-0 align-middle" id="topCustomersTable">
                        <thead><tr><th>#</th><th>Khách hàng</th><th class="text-end">Đơn</th><th class="text-end">Chi tiêu</th></tr></thead>
                        <tbody>
                        
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-3 mb-3">
        <div class="col-12">
            <div class="card">
                <div class="card-header fw-semibold">Doanh thu theo danh mục</div>
                <div class="card-body">
                    <canvas id="categoryChart" height="110"></canvas>
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
