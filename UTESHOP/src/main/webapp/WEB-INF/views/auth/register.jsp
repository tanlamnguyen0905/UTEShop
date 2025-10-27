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

.form-control {
	border-radius: 10px;
	padding: 10px 12px;
}

.form-control:focus {
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

/* Hộp lỗi ẩn mặc định + hiệu ứng fade-in */
#registerErrorBox {
	display: none;
	animation: fadeIn 0.3s ease;
}

@
keyframes fadeIn {from { opacity:0;
	transform: translateY(-3px);
}

to {
	opacity: 1;
	transform: translateY(0);
}
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
										<label for="registerPassword" class="form-label">Mật
											khẩu</label> <input type="password" id="registerPassword"
											name="password" class="form-control"
											placeholder="Nhập mật khẩu" required>
									</div>
									<div class="col-md-6">
										<label for="registerConfirmPassword" class="form-label">Xác
											nhận</label> <input type="password" id="registerConfirmPassword"
											name="confirmPassword" class="form-control"
											placeholder="Nhập lại mật khẩu" required>
									</div>
								</div>

								<div id="passwordError" class="text-danger mt-1"
									style="display: none;">Mật khẩu và xác nhận mật khẩu
									không khớp!</div>

								<div class="form-check mt-2">
									<input type="checkbox" class="form-check-input"
										id="showPassword"> <label for="showPassword"
										class="form-check-label">Hiện mật khẩu</label>
								</div>
								<!-- ===== Ô nhập OTP & Nút nhận mã ===== -->
								<div class="row align-items-end g-3 mt-2">
									<div class="col-md-6">
										<label for="otp" class="form-label">Mã xác thực (OTP)</label>
										<input type="text" id="otp" name="otp" maxlength="6"
											class="form-control text-center fw-bold"
											placeholder="_ _ _ _ _ _" required>
									</div>
									<div class="col-md-6 text-center">
										<button type="button" id="sendOtpBtn"
											class="btn btn-outline-primary w-100">
											<i class="fa-solid fa-envelope-circle-check me-1"></i> Nhận
											mã OTP
										</button>
									</div>
								</div>
								<div id="otpMessage" class="alert mt-3 py-2"
									style="display: none;"></div>
							</div>
						</div>

						<div id="registerErrorBox" class="alert alert-danger mt-3 py-2"
							style="display: none;"></div>

						<button type="submit" class="btn btn-register w-100 mt-3">
							<i class="fa-solid fa-paper-plane me-1"></i> Đăng ký ngay
						</button>

						<div class="text-center text-muted mt-3">
							Đã có tài khoản? <a href="#" data-bs-toggle="modal"
								data-bs-target="#loginModal" data-bs-dismiss="modal"> Đăng
								nhập </a>
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
  const form = document.querySelector("#registerModal form");
  const pwd = document.getElementById("registerPassword");
  const cpwd = document.getElementById("registerConfirmPassword");
  const showPwd = document.getElementById("showPassword");
  const passwordError = document.getElementById("passwordError");
  const alertBox = document.getElementById("registerErrorBox");
  const sendOtpBtn = document.getElementById("sendOtpBtn");
  const otpMsg = document.getElementById("otpMessage");

  // 👁 Hiện / ẩn mật khẩu
  showPwd.addEventListener("change", () => {
    const type = showPwd.checked ? "text" : "password";
    pwd.type = cpwd.type = type;
  });

  // ⚠️ Kiểm tra khớp mật khẩu realtime
  cpwd.addEventListener("input", () => {
    passwordError.style.display = (cpwd.value !== pwd.value) ? "block" : "none";
  });

  // 📨 Gửi mã OTP qua email
  sendOtpBtn.addEventListener("click", async () => {
    const email = document.getElementById("email").value.trim();
    const username = document.getElementById("username").value.trim();

    if (!email) {
      otpMsg.className = "alert alert-warning py-2";
      otpMsg.textContent = "⚠️ Vui lòng nhập email trước khi nhận mã OTP!";
      otpMsg.style.display = "block";
      return;
    }

    if (!username) {
      otpMsg.className = "alert alert-warning py-2";
      otpMsg.textContent = "⚠️ Vui lòng nhập tên đăng nhập trước khi nhận mã OTP!";
      otpMsg.style.display = "block";
      return;
    }

    // Disable nút gửi trong vài giây để tránh spam
    sendOtpBtn.disabled = true;
    otpMsg.className = "alert alert-info py-2";
    otpMsg.textContent = "⏳ Đang kiểm tra thông tin...";
    otpMsg.style.display = "block";

    try {
      // Không dùng để tránh lỗi EL
      const url = form.action + "?sendOtp=true&email=" + encodeURIComponent(email) + "&username=" + encodeURIComponent(username);
      const res = await fetch(url);
      const data = await res.json();

      if (res.ok && data.success) {
        otpMsg.className = "alert alert-success py-2";
        otpMsg.textContent = "📩 Mã OTP đã được gửi đến email của bạn!";
        // Kích hoạt lại nút sau 60 giây để cho phép gửi lại OTP
        setTimeout(() => sendOtpBtn.disabled = false, 60000);
      } else {
        otpMsg.className = "alert alert-danger py-2";
        otpMsg.textContent = data.error || "❌ Gửi OTP thất bại. Thử lại sau!";
        sendOtpBtn.disabled = false; // Cho phép gửi lại ngay nếu lỗi
      }
    } catch (err) {
      otpMsg.className = "alert alert-danger py-2";
      otpMsg.textContent = "🚫 Không thể kết nối đến máy chủ!";
      sendOtpBtn.disabled = false;
    } finally {
      otpMsg.style.display = "block";
    }
  });

  // 🚀 Submit form đăng ký + OTP
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    alertBox.style.display = "none";

    if (pwd.value !== cpwd.value) {
      passwordError.style.display = "block";
      return;
    }

    const formData = new FormData(form);

    try {
      const res = await fetch(form.action, { method: "POST", body: formData });
      const data = await res.json();

      if (res.ok && data.success) {
    	  // ✅ Thông báo đăng ký thành công
    	  otpMsg.className = "alert alert-success py-2";
    	  otpMsg.textContent = "✅ Đăng ký & xác thực thành công! Đang mở trang đăng nhập...";
    	  otpMsg.style.display = "block";

    	  // 🔄 Sau 2 giây: ẩn modal đăng ký và mở modal đăng nhập
    	  setTimeout(() => {
    	    // Ẩn modal đăng ký
    	    const regModal = bootstrap.Modal.getInstance(document.getElementById("registerModal"));
    	    if (regModal) regModal.hide();

    	    // Mở modal đăng nhập
    	    const loginModalEl = document.getElementById("loginModal");
    	    if (loginModalEl) {
    	      const loginModal = new bootstrap.Modal(loginModalEl);
    	      loginModal.show();
    	    }
    	  }, 2000);
    	  
    	} else {
    	  alertBox.textContent = data.error || "Đăng ký thất bại!";
    	  alertBox.style.display = "block";
    	}
    } catch (err) {
      alertBox.textContent = "Không thể kết nối đến máy chủ!";
      alertBox.style.display = "block";
    }
  });
});
</script>
