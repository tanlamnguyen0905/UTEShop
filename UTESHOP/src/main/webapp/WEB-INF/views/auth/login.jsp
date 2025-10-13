<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #8EC5FC 0%, #E0C3FC 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: "Segoe UI", sans-serif;
        }
        .login-card {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
            padding: 35px 40px;
            max-width: 450px;
            width: 100%;
        }
        h3 {
            color: #6c5ce7;
            font-weight: 600;
        }
        .btn-login {
            background-color: #6c5ce7;
            color: white;
            font-weight: 600;
            border-radius: 10px;
            transition: 0.3s;
        }
        .btn-login:hover {
            background-color: #5941d8;
        }
    </style>
</head>
<body>
<div class="login-card">

    <!-- Logo trái + tiêu đề giữa -->
    <div class="text-start">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
             alt="Logo" style="height:60px;">
    </div>
    <h3 class="text-center mt-2">
        <i class="fa-solid fa-right-to-bracket me-2"></i> Đăng nhập
    </h3>

    <form action="${pageContext.request.contextPath}/user/login" method="post" class="mt-4">
        <div class="mb-3">
            <label for="username" class="form-label">Tên đăng nhập</label>
            <input type="text" id="username" name="username" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Mật khẩu</label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-login w-100">
            <i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Đăng nhập
        </button>

        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/user/forgot-password" class="text-muted">Quên mật khẩu?</a><br>
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/user/register">Đăng ký</a>
        </div>
    </form>
</div>
</body>
</html>
