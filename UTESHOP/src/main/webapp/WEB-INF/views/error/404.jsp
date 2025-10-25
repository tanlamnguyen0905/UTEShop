<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url value="/" var="URL"></c:url>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Not Found | UTESHOP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
            color: #4facfe;
            line-height: 1;
            margin-bottom: 1rem;
            text-shadow: 3px 3px 0 rgba(79, 172, 254, 0.1);
        }
        .error-icon {
            font-size: 5rem;
            color: #4facfe;
            margin-bottom: 1.5rem;
            animation: float 3s ease-in-out infinite;
        }
        @keyframes float {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-20px); }
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
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            border: none;
        }
        .btn-primary-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(79, 172, 254, 0.4);
            color: white;
        }
        .btn-secondary-custom {
            background: white;
            color: #4facfe;
            border: 2px solid #4facfe;
        }
        .btn-secondary-custom:hover {
            background: #4facfe;
            color: white;
            transform: translateY(-2px);
        }
        .search-box {
            max-width: 400px;
            margin: 2rem auto 0;
        }
        .search-box input {
            border-radius: 50px;
            padding: 12px 20px;
            border: 2px solid #e0e0e0;
            font-size: 1rem;
        }
        .search-box input:focus {
            border-color: #4facfe;
            box-shadow: 0 0 0 0.2rem rgba(79, 172, 254, 0.25);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-box">
            <div class="error-icon">
                <i class="bi bi-search"></i>
            </div>
            <div class="error-code">404</div>
            <h1 class="error-title">Không tìm thấy trang</h1>
            <p class="error-message">
                Trang bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.<br>
                Vui lòng kiểm tra lại URL hoặc quay về trang chủ.
            </p>
            <div class="mt-4">
                <a href="${URL}" class="btn-custom btn-primary-custom">
                    <i class="bi bi-house-door me-2"></i>Về trang chủ
                </a>
                <a href="javascript:history.back()" class="btn-custom btn-secondary-custom">
                    <i class="bi bi-arrow-left me-2"></i>Quay lại
                </a>
            </div>
            
            <!-- Search box -->
            <div class="search-box">
                <form action="${URL}web/filter" method="get">
                    <div class="input-group">
                        <input type="text" name="keyword" class="form-control" 
                               placeholder="Tìm kiếm sản phẩm..." aria-label="Search">
                        <button class="btn btn-primary-custom" type="submit">
                            <i class="bi bi-search"></i>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

