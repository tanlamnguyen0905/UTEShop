<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <style>
        body {
            background: linear-gradient(135deg, #8EC5FC 0%, #E0C3FC 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: "Segoe UI", sans-serif;
        }
        .forgot-card {
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
        .btn-forgot {
            background-color: #6c5ce7;
            color: white;
            font-weight: 600;
            border-radius: 10px;
            transition: 0.3s;
        }
        .btn-forgot:hover {
            background-color: #5941d8;
        }
    </style>
</head>
<body>
<div class="forgot-card">

    <!-- Logo trái + tiêu đề giữa -->
    <div class="text-start">
        <img src="${URL}assets/images/logo.png" 
             alt="Logo" style="height:60px;">
    </div>
    <h3 class="text-center mt-2">
        <i class="fa-solid fa-key me-2"></i> Quên mật khẩu
    </h3>

    <form action="${pageContext.request.contextPath}/user/forgot-password" method="post" class="mt-4">
        <div class="mb-3">
            <label for="email" class="form-label">Nhập email đăng ký</label>
            <input type="email" id="email" name="email" class="form-control" placeholder="example@gmail.com" required>
        </div>
        <button type="submit" class="btn btn-forgot w-100">
            <i class="fa-solid fa-paper-plane me-1"></i> Gửi yêu cầu
        </button>

        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/user/login" class="text-muted">Quay lại đăng nhập</a>
        </div>
    </form>
</div>
</body>
</html>
