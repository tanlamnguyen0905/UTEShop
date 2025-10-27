<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url value="/" var="URL"></c:url>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Forbidden | UTESHOP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .error-container {
            text-align: center;
            padding: 2rem;
        }
        .error-box {
            background: white;
            border-radius: 20px;
            padding: 3rem 2rem;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            max-width: 600px;
            margin: 0 auto;
        }
        .error-code {
            font-size: 8rem;
            font-weight: bold;
            color: #f5576c;
            line-height: 1;
            margin-bottom: 1rem;
            text-shadow: 3px 3px 0 rgba(245, 87, 108, 0.1);
        }
        .error-icon {
            font-size: 5rem;
            color: #f5576c;
            margin-bottom: 1.5rem;
            animation: pulse 2s ease-in-out infinite;
        }
        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); }
        }
        .error-title {
            font-size: 2rem;
            font-weight: 600;
            color: #333;
            margin-bottom: 1rem;
        }
        .error-message {
            font-size: 1.1rem;
            color: #666;
            margin-bottom: 2rem;
            line-height: 1.6;
        }
        .btn-custom {
            padding: 12px 40px;
            font-size: 1rem;
            font-weight: 600;
            border-radius: 50px;
            text-decoration: none;
            transition: all 0.3s ease;
            margin: 0.5rem;
            display: inline-block;
        }
        .btn-primary-custom {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            border: none;
        }
        .btn-primary-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(245, 87, 108, 0.4);
            color: white;
        }
        .btn-secondary-custom {
            background: white;
            color: #f5576c;
            border: 2px solid #f5576c;
        }
        .btn-secondary-custom:hover {
            background: #f5576c;
            color: white;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-box">
            <div class="error-icon">
                <i class="bi bi-shield-x"></i>
            </div>
            <div class="error-code">403</div>
            <h1 class="error-title">Truy cập bị từ chối</h1>
            <p class="error-message">
                Bạn không có quyền truy cập vào trang này.<br>
                Vui lòng liên hệ quản trị viên nếu bạn cho rằng đây là lỗi.
            </p>
            <div class="mt-4">
                <a href="javascript:history.back()" class="btn-custom btn-primary-custom">
                    <i class="bi bi-arrow-left me-2"></i>Quay lại
                </a>
                <a href="${URL}" class="btn-custom btn-secondary-custom">
                    <i class="bi bi-house-door me-2"></i>Về trang chủ
                </a>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

