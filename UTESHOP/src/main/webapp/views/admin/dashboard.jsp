<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Dashboard</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="mb-3">Bảng điều khiển</h1>

    <div class="alert alert-info">
        <strong>Chào mừng!</strong> Đây là trang Dashboard của Admin.
    </div>

    <div class="row">
        <div class="col-md-4">
            <div class="card border-primary mb-3">
                <div class="card-body text-center">
                    <h5 class="card-title">Tổng sản phẩm</h5>
                    <p class="card-text display-6 text-primary">128</p>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card border-success mb-3">
                <div class="card-body text-center">
                    <h5 class="card-title">Đơn hàng</h5>
                    <p class="card-text display-6 text-success">45</p>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card border-warning mb-3">
                <div class="card-body text-center">
                    <h5 class="card-title">Người dùng</h5>
                    <p class="card-text display-6 text-warning">23</p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
