<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="modal fade" id="resetPasswordModal" tabindex="-1"
	aria-labelledby="resetPasswordModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content p-0 rounded-4 shadow-lg">
			<div class="modal-body p-0">
				<style>
#resetPasswordModal .modal-content {
	background: linear-gradient(135deg, #8EC5FC 0%, #E0C3FC 100%);
	font-family: "Segoe UI", sans-serif;
	border-radius: 16px;
}

.reset-card {
	background: #fff;
	border-radius: 16px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
	padding: 35px 40px;
	max-width: 480px;
	width: 100%;
	margin: auto;
}

.reset-card h3 {
	color: #6c5ce7;
	font-weight: 600;
}

.btn-reset {
	background-color: #6c5ce7;
	color: #fff;
	font-weight: 600;
	border-radius: 10px;
	transition: 0.3s;
}

.btn-reset:hover {
	background-color: #5941d8;
}

#resetErrorBox {
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

#resetSuccessBox {
	display: none;
	background: #e9f7ef;
	border-left: 5px solid #27ae60;
	color: #1e8449;
	padding: 10px;
	border-radius: 8px;
	margin-bottom: 15px;
	text-align: center;
	font-weight: 500;
}
</style>

				<div class="reset-card">
					<div id="resetErrorBox"></div>
					<div id="resetSuccessBox"></div>

					<div class="text-start">
						<img
							src="${pageContext.request.contextPath}/assets/images/logo.png"
							alt="Logo" style="height: 60px;">
					</div>
					<h3 class="text-center mt-2">
						<i class="fa-solid fa-lock me-2"></i> Đặt lại mật khẩu
					</h3>

					<form id="resetForm"
						action="${pageContext.request.contextPath}/auth/reset-password"
						method="post" class="mt-4">
						<div class="mb-3">
							<label for="email" class="form-label">Email đăng ký</label> <input
								type="email" id="email" name="email" class="form-control"
								placeholder="example@gmail.com" required />
						</div>

						<div class="mb-3">
							<label for="otp" class="form-label">Mã OTP</label> <input
								type="text" id="otp" name="otp" class="form-control"
								maxlength="6" placeholder="Nhập mã OTP" required />
						</div>

						<div class="mb-3">
							<label for="newPassword" class="form-label">Mật khẩu mới</label>
							<input type="password" id="newPassword" name="newPassword"
								class="form-control" minlength="6"
								placeholder="Nhập mật khẩu mới" required />
						</div>

						<div class="mb-3">
							<label for="confirmPassword" class="form-label">Xác nhận
								mật khẩu mới</label> <input type="password" id="confirmPassword"
								name="confirmPassword" class="form-control" minlength="6"
								placeholder="Nhập lại mật khẩu mới" required />
						</div>

						<button type="submit" class="btn btn-reset w-100">
							<i class="fa-solid fa-check me-1"></i> Cập nhật mật khẩu
						</button>

						<div class="text-center mt-3">
							<a href="#" class="text-muted" data-bs-toggle="modal"
								data-bs-target="#loginModal" data-bs-dismiss="modal">← Quay
								lại đăng nhập</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("resetForm");
    const errorBox = document.getElementById("resetErrorBox");
    const successBox = document.getElementById("resetSuccessBox");
    const emailInput = document.getElementById("email");

    if (!form) return;

    // ✅ Khi mở modal reset → tự điền email từ modal forgot (nếu đã lưu)
    const savedEmail = sessionStorage.getItem("forgotEmail");
    if (savedEmail) {
        emailInput.value = savedEmail;
        emailInput.readOnly = true;
        emailInput.classList.add("bg-light");
        emailInput.style.cursor = "not-allowed";
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        errorBox.style.display = "none";
        successBox.style.display = "none";
        errorBox.textContent = "";
        successBox.textContent = "";

        try {
            const formData = new FormData(form);
            const res = await fetch(form.action, { method: "POST", body: formData });
            const contentType = res.headers.get("content-type") || "";

            if (contentType.includes("application/json")) {
                const data = await res.json();

                if (data.success) {
                    successBox.textContent = data.message || "Đổi mật khẩu thành công!";
                    successBox.style.display = "block";

                    // Sau 1s đóng modal reset và mở loginModal
                    setTimeout(() => {
                        const resetModal = bootstrap.Modal.getInstance(document.getElementById("resetPasswordModal"));
                        if (resetModal) resetModal.hide();

                        // ✅ Khởi tạo đối tượng modal đăng nhập trước khi gọi show()
                        const loginModalEl = document.getElementById("loginModal");
                        const loginModal = new bootstrap.Modal(loginModalEl);
                        loginModal.show();
                    }, 1000);
                } else {
                    errorBox.textContent = data.error || "Có lỗi xảy ra. Vui lòng thử lại.";
                    errorBox.style.display = "block";
                }
            } else {
                errorBox.textContent = "Không nhận được phản hồi hợp lệ từ máy chủ.";
                errorBox.style.display = "block";
            }
        } catch (err) {
            console.error("Lỗi:", err);
            errorBox.textContent = "Có lỗi xảy ra. Vui lòng thử lại.";
            errorBox.style.display = "block";
        }
    });
});
</script>
