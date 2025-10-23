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

#errorBox {
	display: none;
	background: #ffe0e0;
	border-left: 5px solid #d63031;
	color: #c0392b;
	padding: 10px;
	border-radius: 8px;
	margin-bottom: 15px;
	text-align: center;
	font-weight: 500;
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

					<!-- ====== FORM LOGIN ====== -->
					<form id="loginForm"
						action="${pageContext.request.contextPath}/auth/login"
						method="post" class="mt-4">
						<div id="errorBox">Sai tên đăng nhập hoặc mật khẩu!</div>

						<div class="mb-3">
							<label for="loginUsername" class="form-label">Tên đăng
								nhập</label> <input type="text" id="loginUsername" name="username"
								class="form-control" placeholder="Nhập tên đăng nhập" required>
						</div>

						<div class="mb-3">
							<label for="loginPassword" class="form-label">Mật khẩu</label>
							<div class="input-group">
								<input type="password" id="loginPassword" name="password"
									class="form-control" placeholder="Nhập mật khẩu" required>
								<button class="btn btn-outline-secondary" type="button"
									id="toggleLoginPassword">
									<i class="fa-solid fa-eye"></i>
								</button>
							</div>
						</div>

						<button type="submit" class="btn btn-login w-100">
							<i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Đăng nhập
						</button>

						<div class="text-center mt-3">
							<a href="#" data-bs-toggle="modal" data-bs-target="#forgotPasswordModal" data-bs-dismiss="modal">
    Quên mật khẩu?
</a><br> Chưa
							có tài khoản? <a href="#forgot-password" data-bs-toggle="modal"
								data-bs-target="#registerModal" data-bs-dismiss="modal">
								Đăng ký </a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- ====== SCRIPT ====== -->
<script>
document.addEventListener("DOMContentLoaded", () => {
    const togglePwd = document.getElementById("toggleLoginPassword");
    const pwdInput = document.getElementById("loginPassword");
    const form = document.getElementById("loginForm");
    const errorBox = document.getElementById("errorBox");
    let visible = false;

    // 👁 Hiện / ẩn mật khẩu
    togglePwd.addEventListener("click", () => {
        visible = !visible;
        pwdInput.type = visible ? "text" : "password";
        togglePwd.innerHTML = visible
            ? '<i class="fa-solid fa-eye-slash"></i>'
            : '<i class="fa-solid fa-eye"></i>';
    });

    // 🚀 Xử lý đăng nhập qua AJAX
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        errorBox.style.display = "none";

        const formData = new FormData(form);
        const res = await fetch(form.action, {
            method: "POST",
            body: formData
        });

        if (res.ok) {
            const data = await res.json();

            if (data.success) {
                // ✅ Lưu JWT token vào localStorage
                if (data.token) {
                    localStorage.setItem('authToken', data.token);
                    console.log('🔐 Token saved to localStorage');
                }
                
                // Đăng nhập thành công
                const loginModal = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
                loginModal.hide();

                // Thông báo toast
                const toast = document.createElement("div");
                toast.className = "toast align-items-center text-bg-success border-0 position-fixed bottom-0 end-0 m-3";
                toast.role = "alert";
                toast.innerHTML = `<div class="d-flex">
                    <div class="toast-body">🔐 Đăng nhập thành công! Xin chào ${data.username}.</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>`;
                document.body.appendChild(toast);
                new bootstrap.Toast(toast).show();

                // Check for redirect URL
                if (data.redirect) {
                    setTimeout(() => window.location.href = data.redirect, 1500);
                } else {
                    setTimeout(() => location.reload(), 1500);
                }
            } else {
                // ❌ Sai tài khoản / mật khẩu
                errorBox.textContent = data.error || "Sai tên đăng nhập hoặc mật khẩu!";
                errorBox.style.display = "block";
            }
        } else {
            errorBox.textContent = "Sai tên đăng nhập hoặc mật khẩu!";
            errorBox.style.display = "block";
        }
    });
});
</script>
