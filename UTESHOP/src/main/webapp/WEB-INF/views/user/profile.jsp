<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container py-5">
	<!-- ====== TIÊU ĐỀ ====== -->
	<div class="text-center mb-5">
		<h1 class="fw-bold text-gradient mb-2">
			<i class="fa-solid fa-id-card me-2 text-primary"></i> Hồ sơ cá nhân
		</h1>
		<div class="mx-auto"
			style="width: 120px; height: 4px; background: linear-gradient(90deg, #6c63ff, #a26cf8, #ff8efb); border-radius: 3px;"></div>
	</div>

	<!-- ====== THẺ THÔNG TIN ====== -->
	<div class="card shadow-lg border-0 rounded-4 p-4 mx-auto"
		style="max-width: 900px; background: linear-gradient(135deg, #f0f2ff 0%, #ffffff 100%);">

		<div class="row g-4 align-items-center">
			<!-- Avatar -->
			<div class="col-md-4 text-center">
				<div class="position-relative d-inline-block">
					<img
						src="${pageContext.request.contextPath}/uploads/avatar/${sessionScope.currentUser.avatar != null ? sessionScope.currentUser.avatar : 'default.png'}"
						alt="Avatar"
						class="rounded-circle shadow-sm border border-3 border-light"
						style="width: 180px; height: 180px; object-fit: cover; background-color: #f8f9fa;">
				</div>
			</div>

			<!-- Thông tin cá nhân -->
			<div class="col-md-8">
				<h2 class="fw-bold text-dark mb-1">${sessionScope.currentUser.fullname}</h2>
				<p class="text-muted mb-3">@${sessionScope.currentUser.username}</p>

				<div class="mb-2">
					<i class="fa-solid fa-envelope me-2 text-primary"></i> Email: <strong>${sessionScope.currentUser.email}</strong>
				</div>

				<div class="mb-2">
					<i class="fa-solid fa-phone me-2 text-success"></i> Điện thoại: <strong>${sessionScope.currentUser.phone}</strong>
				</div>

				<div class="mb-2">
					<i class="fa-solid fa-user-tag me-2 text-info"></i> Vai trò: <strong>${sessionScope.currentUser.role}</strong>
				</div>

				<div class="mb-3">
					<i class="fa-solid fa-circle me-2"
						style="color:${sessionScope.currentUser.status == 'ACTIVE' ? '#28a745' : '#dc3545'}"></i>
					Trạng thái: <strong>${sessionScope.currentUser.status}</strong>
				</div>

				<div class="d-flex gap-3 mt-4">
					<a href="${pageContext.request.contextPath}/user/update-profile" class="btn btn-outline-primary px-4"> <i
						class="fa-solid fa-pen-to-square me-1"></i> Chỉnh sửa hồ sơ
					</a>
					<a href="${pageContext.request.contextPath}/user/change-password" class="btn btn-outline-primary px-4"> <i
						class="fa-solid fa-pen-to-square me-1"></i> Đổi mật khẩu
					</a>
					 <a href="${pageContext.request.contextPath}/auth/logout"
						class="btn btn-outline-danger px-4"> <i
						class="fa-solid fa-right-from-bracket me-1"></i> Đăng xuất
					</a>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- ====== CSS ====== -->
<style>
.text-gradient {
	background: linear-gradient(90deg, #6c63ff, #a26cf8, #ff8efb);
	-webkit-background-clip: text;
	-webkit-text-fill-color: transparent;
}

.card {
	transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
	transform: translateY(-4px);
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}
</style>
