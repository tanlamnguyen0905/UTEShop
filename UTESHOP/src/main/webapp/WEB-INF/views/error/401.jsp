<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url value="/" var="URL"></c:url>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>401 - Unauthorized | UTESHOP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            color: #667eea;
            line-height: 1;
            margin-bottom: 1rem;
            text-shadow: 3px 3px 0 rgba(102, 126, 234, 0.1);
        }
        .error-icon {
            font-size: 5rem;
            color: #667eea;
            margin-bottom: 1.5rem;
            animation: shake 0.5s ease-in-out infinite alternate;
        }
        @keyframes shake {
            0% { transform: rotate(-5deg); }
            100% { transform: rotate(5deg); }
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
        }
        .btn-primary-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            color: white;
        }
        .btn-secondary-custom {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }
        .btn-secondary-custom:hover {
            background: #667eea;
            color: white;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-box">
            <div class="error-icon">
                <i class="bi bi-lock-fill"></i>
            </div>
            <div class="error-code">401</div>
            <h1 class="error-title">Xác thực thất bại</h1>
            <p class="error-message">
                Bạn cần đăng nhập để truy cập trang này.<br>
                Vui lòng đăng nhập để tiếp tục sử dụng dịch vụ.
            </p>
            <div class="mt-4">
                <a href="${URL}auth/login" class="btn-custom btn-primary-custom">
                    <i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập
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

