<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
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

        .register-card {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
            padding: 35px 40px;
            max-width: 720px;
            width: 100%;
        }

        h3 {
            text-align: center;
            color: #6c5ce7;
            font-weight: 600;
            margin-bottom: 25px;
        }

        label {
            font-weight: 500;
        }

        .form-control, .form-select {
            border-radius: 10px;
            padding: 10px 12px;
        }

        .form-control:focus, .form-select:focus {
            border-color: #6c5ce7;
            box-shadow: 0 0 6px rgba(108,92,231,0.4);
        }

        .btn-register {
            background-color: #6c5ce7;
            color: white;
            font-weight: 600;
            padding: 10px 0;
            border-radius: 10px;
            transition: 0.3s;
        }

        .btn-register:hover {
            background-color: #5941d8;
        }

        .text-muted a {
            text-decoration: none;
            color: #6c5ce7;
            font-weight: 500;
        }

        .text-muted a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="register-card">
	<!-- Header -->
	<div class="text-center mb-3">
	    <!-- Logo trái -->
	    <div class="text-start">
	        <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
	             alt="Logo" style="height:60px;">
	    </div>

	    <!-- Tiêu đề căn giữa, thấp hơn logo -->
	    <h3 class="mt-2 text-primary fw-bold">
	        <i class="fa-solid fa-user-plus me-2"></i> Đăng ký
	    </h3>
	</div>

    <form action="${pageContext.request.contextPath}/user/register" method="post" enctype="multipart/form-data">
        <div class="row g-3">
            <div class="col-md-6">
                <label for="fullName" class="form-label">Họ và tên</label>
                <input type="text" id="fullName" name="fullName" class="form-control" placeholder="Nguyễn Văn A" required>
            </div>

            <div class="col-md-6">
                <label for="dob" class="form-label">Ngày sinh</label>
                <input type="date" id="dob" name="dob" class="form-control" required>
            </div>

            <div class="col-md-6">
                <label for="email" class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="example@gmail.com" required>
            </div>

            <div class="col-md-6">
                <label for="phone" class="form-label">Số điện thoại</label>
                <input type="text" id="phone" name="phone" class="form-control" placeholder="0123456789" required>
            </div>

            <div class="col-md-6">
                <label for="sex" class="form-label">Giới tính</label>
                <select id="sex" name="sex" class="form-select" required>
                    <option value="">-- Chọn giới tính --</option>
                    <option value="Male">Nam</option>
                    <option value="Female">Nữ</option>
                    <option value="Other">Khác</option>
                </select>
            </div>

            <div class="col-md-6">
                <label for="avatar" class="form-label">Ảnh đại diện</label>
                <input type="file" id="avatar" name="avatar" class="form-control" accept="image/*">
            </div>

            <div class="col-md-6">
                <label for="username" class="form-label">Tên đăng nhập</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="username" required>
            </div>

            <!-- Mật khẩu và xác nhận mật khẩu cùng hàng -->
			<div class="col-12">
				<div class="row g-3">
				        <div class="col-md-6">
				            <label for="password" class="form-label">Mật khẩu</label>
				            <input type="password" id="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
				        </div>
				        <div class="col-md-6">
				            <label for="confirmPassword" class="form-label">Xác nhận</label>
				            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Nhập lại" required>
				        </div>
				    </div>
				    <!-- Thông báo lỗi -->
				    <div id="passwordError" class="text-danger mt-1" style="display:none;">
				        Mật khẩu và xác nhận mật khẩu không khớp!
				    </div>
			    <!-- Checkbox hiển thị mật khẩu -->
			    <div class="form-check mt-2">
			        <input type="checkbox" class="form-check-input" id="showPassword" onclick="togglePassword()">
			        <label for="showPassword" class="form-check-label">Hiện mật khẩu</label>
			    </div>
			</div>
        </div>

        <button type="submit" class="btn btn-register w-100 mt-4">
            <i class="fa-solid fa-paper-plane me-1"></i>Đăng ký ngay
        </button>

        <div class="text-center text-muted mt-3">
            Đã có tài khoản? <a href="${pageContext.request.contextPath}/user/login">Đăng nhập</a>
        </div>
    </form>
</div>

<script>
    function togglePassword() {
        let pwd = document.getElementById('password');
        let cpwd = document.getElementById('confirmPassword');
        let type = pwd.type === 'password' ? 'text' : 'password';
        pwd.type = type;
        cpwd.type = type;
    }

    const pwd = document.getElementById("password");
    const cpwd = document.getElementById("confirmPassword");
    const errorBox = document.getElementById("passwordError");
    const form = document.querySelector("form");

    // Kiểm tra realtime khi gõ confirm password
    cpwd.addEventListener("input", function () {
        if (cpwd.value !== pwd.value) {
            errorBox.style.display = "block";
        } else {
            errorBox.style.display = "none";
        }
    });

    // Kiểm tra lần cuối trước khi submit
    form.addEventListener("submit", function (e) {
        if (pwd.value !== cpwd.value) {
            e.preventDefault(); // chặn submit
            errorBox.style.display = "block";
        }
    });
</script>

</body>
</html>
