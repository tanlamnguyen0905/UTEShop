<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!-- ====== REGISTER MODAL ====== -->
<div class="modal fade" id="registerModal" tabindex="-1"
	aria-labelledby="registerModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg">
		<div class="modal-content p-0 rounded-4 shadow-lg">

			<div class="modal-body p-0">
				<style>
#registerModal .modal-content {
	background: linear-gradient(135deg, #8EC5FC 0%, #E0C3FC 100%);
	font-family: "Segoe UI", sans-serif;
	border-radius: 16px;
}

.register-card {
	background: #fff;
	border-radius: 16px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
	padding: 35px 40px;
	max-width: 720px;
	width: 100%;
	margin: auto;
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
	box-shadow: 0 0 6px rgba(108, 92, 231, 0.4);
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

				<div class="register-card">
					<div class="text-center mb-3">
						<div class="text-start">
							<img
								src="${pageContext.request.contextPath}/assets/images/logo.png"
								alt="Logo" style="height: 60px;">
						</div>
						<h3 class="mt-2 text-primary fw-bold">
							<i class="fa-solid fa-user-plus me-2"></i> Đăng ký
						</h3>
					</div>

					<form action="${pageContext.request.contextPath}/auth/register"
						method="post" enctype="multipart/form-data">
						<div class="row g-3">
							<div class="col-md-6">
								<label for="fullname" class="form-label">Họ và tên</label> <input
									type="text" id="fullname" name="fullname" class="form-control"
									placeholder="Nguyễn Văn A" required>
							</div>

							<div class="col-md-6">
								<label for="email" class="form-label">Email</label> <input
									type="email" id="email" name="email" class="form-control"
									placeholder="example@gmail.com" required>
							</div>

							<div class="col-md-6">
								<label for="phone" class="form-label">Số điện thoại</label> <input
									type="text" id="phone" name="phone" class="form-control"
									placeholder="0123456789" required>
							</div>

							<div class="col-md-6">
								<label for="avatar" class="form-label">Ảnh đại diện</label> <input
									type="file" id="avatar" name="avatar" class="form-control"
									accept="image/*">
							</div>

							<div class="col-md-6">
								<label for="username" class="form-label">Tên đăng nhập</label> <input
									type="text" id="username" name="username" class="form-control"
									placeholder="username" required>
							</div>

							<div class="col-12">
								<div class="row g-3">
									<div class="col-md-6">
										<label for="password" class="form-label">Mật khẩu</label> <input
											type="password" id="password" name="password"
											class="form-control" placeholder="Nhập mật khẩu" required>
									</div>
									<div class="col-md-6">
										<label for="confirmPassword" class="form-label">Xác
											nhận</label> <input type="password" id="confirmPassword"
											name="confirmPassword" class="form-control"
											placeholder="Nhập lại mật khẩu" required>
									</div>
								</div>
								<div id="passwordError" class="text-danger mt-1"
									style="display: none;">Mật khẩu và xác nhận mật khẩu
									không khớp!</div>
								<div class="form-check mt-2">
									<input type="checkbox" class="form-check-input"
										id="showPassword" onclick="togglePassword()"> <label
										for="showPassword" class="form-check-label">Hiện mật
										khẩu</label>
								</div>
							</div>
						</div>

						<button type="submit" class="btn btn-register w-100 mt-4">
							<i class="fa-solid fa-paper-plane me-1"></i>Đăng ký ngay
						</button>

						<div class="text-center text-muted mt-3">
							Đã có tài khoản? <a href="#" data-bs-toggle="modal"
								data-bs-target="#loginModal" data-bs-dismiss="modal">Đăng
								nhập</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	function togglePassword() {
		const pwd = document.getElementById('password');
		const cpwd = document.getElementById('confirmPassword');
		const type = pwd.type === 'password' ? 'text' : 'password';
		pwd.type = type;
		cpwd.type = type;
	}

	const pwd = document.getElementById("password");
	const cpwd = document.getElementById("confirmPassword");
	const errorBox = document.getElementById("passwordError");
	const form = document.querySelector("form");

	cpwd.addEventListener("input", function() {
		errorBox.style.display = (cpwd.value !== pwd.value) ? "block" : "none";
	});

	form.addEventListener("submit", function(e) {
		if (pwd.value !== cpwd.value) {
			e.preventDefault();
			errorBox.style.display = "block";
		}
	});
</script>
