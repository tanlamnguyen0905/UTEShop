<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container py-5">
	<div class="text-center mb-5">
		<h1 class="fw-bold text-gradient mb-2">
			<i class="fa-solid fa-lock me-2 text-primary"></i> Đổi mật khẩu
		</h1>
		<div class="mx-auto"
			style="width: 130px; height: 4px; background: linear-gradient(90deg, #6c63ff, #a26cf8, #ff8efb); border-radius: 3px;"></div>
	</div>

	<c:if test="${not empty success}">
		<div class="alert alert-success text-center shadow-sm">${success}</div>
	</c:if>
	<c:if test="${not empty error}">
		<div class="alert alert-danger text-center shadow-sm">${error}</div>
	</c:if>

	<div class="card shadow-lg border-0 rounded-4 p-4 mx-auto"
		style="max-width: 600px; background: linear-gradient(135deg, #f0f2ff 0%, #ffffff 100%);">

		<form id="changePwdForm"
			action="${pageContext.request.contextPath}/user/change-password"
			method="post" class="px-2">

			<div class="mb-3">
				<label class="form-label fw-semibold">Mật khẩu hiện tại</label> <input
					type="password" name="currentPassword" id="currentPassword"
					class="form-control" required>
			</div>

			<div class="mb-3">
				<label class="form-label fw-semibold">Mật khẩu mới</label> <input
					type="password" name="newPassword" id="newPassword"
					class="form-control" required>
			</div>

			<div class="mb-3">
				<label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
				<input type="password" id="confirmPassword" class="form-control"
					required>
				<div id="pwdError" class="text-danger small mt-1"
					style="display: none;">⚠️ Mật khẩu xác nhận không khớp!</div>
				<div id="sameError" class="text-danger small mt-1"
					style="display: none;">⚠️ Mật khẩu mới không được trùng mật
					khẩu hiện tại!</div>
			</div>

			<div class="form-check mb-3">
				<input type="checkbox" id="showPassword" class="form-check-input">
				<label for="showPassword" class="form-check-label">Hiện mật
					khẩu</label>
			</div>

			<div class="d-flex gap-3">
				<button type="submit" class="btn btn-primary px-4">
					<i class="fa-solid fa-key me-1"></i> Đổi mật khẩu
				</button>
				<a href="${pageContext.request.contextPath}/user/profile"
					class="btn btn-outline-secondary px-4"> <i
					class="fa-solid fa-arrow-left me-1"></i> Quay lại
				</a>
			</div>
		</form>
	</div>
</div>

<!-- ====== SCRIPT ====== -->
<script>
setTimeout(() => {
    const currentPwd = document.getElementById("currentPassword");
    const newPwd = document.getElementById("newPassword");
    const confirmPwd = document.getElementById("confirmPassword");
    const show = document.getElementById("showPassword");
    const error = document.getElementById("pwdError");
    const sameError = document.getElementById("sameError");
    const form = document.getElementById("changePwdForm");

    if (!show) return; // tránh lỗi khi chưa render xong
    // ✅ Hiện / ẩn mật khẩu	
    show.addEventListener("change", function () {
        const type = this.checked ? "text" : "password";
        currentPwd.type = type;
        newPwd.type = type;
        confirmPwd.type = type;
    });

    // ✅ Kiểm tra khớp mật khẩu mới
    confirmPwd.addEventListener("input", function () {
        error.style.display = confirmPwd.value !== newPwd.value ? "block" : "none";
    });

    // ✅ Kiểm tra mật khẩu mới khác mật khẩu cũ
    newPwd.addEventListener("input", function () {
        sameError.style.display = newPwd.value === currentPwd.value ? "block" : "none";
    });

    // ✅ Ngăn submit nếu có lỗi
    form.addEventListener("submit", function (e) {
        let hasError = false;

        if (confirmPwd.value !== newPwd.value) {
            error.style.display = "block";
            hasError = true;
        }
        if (newPwd.value === currentPwd.value) {
            sameError.style.display = "block";
            hasError = true;
        }

        if (hasError) e.preventDefault();
    });

    // ✅ Tự động quay về hồ sơ khi thành công
    const successMsg = document.querySelector(".alert-success");
    if (successMsg) {
        setTimeout(() => {
            window.location.href = "${pageContext.request.contextPath}/user/profile";
        }, 2000);
    }
}, 300); // đợi DOM và decorator load xong
</script>

<style>
.text-gradient {
	background: linear-gradient(90deg, #6c63ff, #a26cf8, #ff8efb);
	-webkit-background-clip: text;
	-webkit-text-fill-color: transparent;
}

.card {
	transition: transform .3s ease, box-shadow .3s ease;
}

.card:hover {
	transform: translateY(-4px);
	box-shadow: 0 10px 25px rgba(0, 0, 0, .1);
}

.alert {
	border-radius: 10px;
	max-width: 600px;
	margin: 0 auto 20px;
}
</style>
