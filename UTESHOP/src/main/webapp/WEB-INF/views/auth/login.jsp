<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!-- ====== LOGIN MODAL ====== -->
<div class="modal fade" id="loginModal" tabindex="-1"
	aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content p-0 rounded-4 shadow-lg">

			<div class="modal-body p-0">
				<style>
#loginModal .modal-content {
	background: linear-gradient(135deg, #8EC5FC 0%, #E0C3FC 100%);
	font-family: "Segoe UI", sans-serif;
	border-radius: 16px;
}

.login-card {
	background: #fff;
	border-radius: 16px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
	padding: 35px 40px;
	max-width: 450px;
	width: 100%;
	margin: auto;
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

				<div class="login-card">

					<!-- Logo và tiêu đề -->
					<div class="text-start">
						<img
							src="${pageContext.request.contextPath}/assets/images/logo.png"
							alt="Logo" style="height: 60px;">
					</div>

					<h3 class="text-center mt-2">
						<i class="fa-solid fa-right-to-bracket me-2"></i> Đăng nhập
					</h3>

					<form action="${pageContext.request.contextPath}/auth/login"
						method="post" class="mt-4">
						<div class="mb-3">
							<label for="username" class="form-label">Tên đăng nhập</label> <input
								type="text" id="username" name="username" class="form-control"
								required>
						</div>

						<div class="mb-3">
							<label for="password" class="form-label">Mật khẩu</label> <input
								type="password" id="password" name="password"
								class="form-control" required>
						</div>

						<button type="submit" class="btn btn-login w-100">
							<i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Đăng nhập
						</button>

						<div class="text-center mt-3">
							<a href="#" class="text-muted">Quên mật khẩu?</a><br> Chưa
							có tài khoản? <a href="#" data-bs-toggle="modal"
								data-bs-target="#registerModal" data-bs-dismiss="modal">Đăng
								ký</a>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
</div>
