<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng xuất</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .logout-container {
            text-align: center;
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }
        .spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #667eea;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 1s linear infinite;
            margin: 0 auto 20px;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        h2 {
            color: #333;
            margin: 0;
        }
        p {
            color: #666;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="logout-container">
        <div class="spinner"></div>
        <h2>Đang đăng xuất...</h2>
        <p>Vui lòng chờ trong giây lát</p>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/auth-utils.js"></script>
    <script>
        // Sử dụng AuthUtils để xóa token và thông tin xác thực
        if (window.AuthUtils) {
            AuthUtils.clearAuth();
            console.log('✅ Đã đăng xuất thành công');
        } else {
            // Fallback nếu AuthUtils chưa load
            localStorage.removeItem('authToken');
            localStorage.removeItem('username');
            localStorage.removeItem('role');
            console.log('✅ Token đã được xóa (fallback)');
        }
        
        // Redirect về trang chủ sau 1 giây
        setTimeout(() => {
            window.location.href = '${pageContext.request.contextPath}/';
        }, 1000);
    </script>
</body>
</html>

