<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container py-5">
	<!-- ====== TIÊU ĐỀ ====== -->
	<div class="text-center mb-5">
		<h1 class="fw-bold text-gradient mb-2">
			<i class="fa-solid fa-user-pen me-2 text-primary"></i> Chỉnh sửa hồ
			sơ
		</h1>
		<div class="mx-auto"
			style="width: 140px; height: 4px; background: linear-gradient(90deg, #6c63ff, #a26cf8, #ff8efb); border-radius: 3px;"></div>
	</div>

	<!-- ====== FORM CHỈNH SỬA ====== -->
	<div class="card shadow-lg border-0 rounded-4 p-4 mx-auto"
		style="max-width: 800px; background: linear-gradient(135deg, #f7f8ff 0%, #ffffff 100%);">

		<form action="${pageContext.request.contextPath}/user/update-profile"
			method="post" enctype="multipart/form-data">

			<!-- Avatar -->
			<div class="text-center mb-4">
				<img
					src="${pageContext.request.contextPath}${sessionScope.currentUser.avatar != null && sessionScope.currentUser.avatar != 'default-avatar.png' ? '/assets/uploads/avatar/'.concat(sessionScope.currentUser.avatar) : '/assets/images/avatar/default-avatar.png'}"
					alt="Avatar" class="rounded-circle shadow-sm mb-3"
					style="width: 150px; height: 150px; object-fit: cover; background-color: #f8f9fa;">
				<div>
					<label class="form-label fw-semibold text-primary">Đổi ảnh
						đại diện:</label> <input type="file" class="form-control w-50 mx-auto"
						name="avatar" accept="image/*">
				</div>
			</div>

			<!-- Thông tin -->
			<div class="row g-3">
				<div class="col-md-6">
					<label class="form-label">Họ và tên</label> <input type="text"
						class="form-control" name="fullname"
						value="${sessionScope.currentUser.fullname}" required>
				</div>

				<div class="col-md-6">
					<label class="form-label">Tên đăng nhập</label> <input type="text"
						class="form-control" name="username"
						value="${sessionScope.currentUser.username}" readonly>
				</div>

				<div class="col-md-6">
					<label class="form-label">Email</label> <input type="email"
						class="form-control" name="email"
						value="${sessionScope.currentUser.email}" required>
				</div>

				<div class="col-md-6">
					<label class="form-label">Số điện thoại</label> <input type="text"
						class="form-control" name="phone"
						value="${sessionScope.currentUser.phone}">
				</div>
			</div>

			<div class="text-center mt-4">
				<button type="submit" class="btn btn-primary px-4 me-2">
					<i class="fa-solid fa-floppy-disk me-1"></i> Lưu thay đổi
				</button>
				<a href="${pageContext.request.contextPath}/user/profile"
					class="btn btn-outline-secondary px-4"> <i
					class="fa-solid fa-arrow-left me-1"></i> Quay lại
				</a>
			</div>
		</form>
	</div>
</div>

<!-- ====== CSS ====== -->
<style>
.text-gradient {
	background: linear-gradient(90deg, #6c63ff, #a26cf8, #ff8efb);
	-webkit-background-clip: text;
	-webkit-text-fill-color: transparent;
}

.form-control:focus {
	border-color: #6c63ff;
	box-shadow: 0 0 5px rgba(108, 99, 255, 0.4);
}
</style>
