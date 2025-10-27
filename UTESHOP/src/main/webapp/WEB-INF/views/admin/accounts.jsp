<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý tài khoản</title>
<link
	href="${pageContext.request.contextPath}/assets/styles/accounts.css"
	rel="stylesheet" />
</head>
<body>
	<div class="container my-4">
		<h3 class="mb-4">Quản lý tài khoản</h3>

		<form id="searchForm" class="row g-3 align-items-center">
			<div class="col-md-4">
				<input type="text" id="searchInput" class="form-control"
					placeholder="Tìm kiếm thông tin">
			</div>

			<div class="col-md-3">
				<select id="roleSelect" class="form-select">
					<option value="">Tất cả vai trò</option>
					<c:forEach var="role" items="${requestScope.roles}">
						<option value="${role}">${role}</option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-3">
				<select id="statusSelect" class="form-select">
					<option value="">Tất cả trạng thái</option>
					<c:forEach var="status" items="${requestScope.statuses}">
						<option value="${status}">${status}</option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-2 text-end">
				<button type="button" id="btnAddUser" class="btn btn-primary w-100">Thêm
					tài khoản</button>
			</div>
		</form>

		<!-- Table Section -->
		<div class="mt-5">
			<h5 class="mb-3">Danh sách tài khoản</h5>
			<div id="message" class="text-center text-muted py-4">Đang tải
				thông tin...</div>

			<table id="accountsTable"
				class="table table-striped table-hover d-none align-middle">
				<thead class="table-dark">
					<tr>
						<th>Tên đăng nhập</th>
						<th>Họ tên</th>
						<th>Email</th>
						<th>SĐT</th>
						<th>Vai Trò</th>
						<th>Trạng Thái</th>
						<th class="text-center">Hành động</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<!-- Info Modal -->
	<div class="modal fade" id="acc_mdl_userModal" tabindex="-1"
		aria-labelledby="acc_mdl_userModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<form id="acc_mdl_userForm" class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="acc_mdl_userModalLabel">Chỉnh sửa tài
						khoản</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Đóng"></button>
				</div>
				<div class="modal-body row g-3">
					<input type="hidden" id="acc_mdl_userId">
					<div class="col-md-4 text-center">
						<img id="acc_mdl_avatarPreview" src="" alt="Avatar"
							class="img-thumbnail mb-2" style="max-width: 150px;"> <input
							type="file" id="avatarInput" class="form-control"
							accept="image/*">
					</div>
					<div class="col-md-8">
						<div class="mb-2">
							<label>Tên đăng nhập</label> <input type="text" id="acc_mdl_username"
								class="form-control" required>
						</div>
						<div class="mb-2">
							<label>Họ tên</label> <input type="text" id="acc_mdl_fullname"
								class="form-control">
						</div>
						<div class="mb-2">
							<label>Email</label> <input type="email" id="acc_mdl_email"
								class="form-control">
						</div>
						<div class="mb-2">
							<label>SĐT</label> <input type="text" id="acc_mdl_phone"
								class="form-control">
						</div>
						<div class="mb-2">
							<label>Mật khẩu</label> <input type="password" id="acc_mdl_password"
								class="form-control" placeholder="(không đổi)">
						</div>
						<div class="mb-2">
							<label>Nhập lại mật khẩu</label> <input type="password"
								id="acc_mdl_password2" class="form-control" placeholder="(nhập lại)">
						</div>
						<div class="mb-2">
							<label>Vai trò</label> <select id="acc_mdl_role" class="form-select">
								<c:forEach var="role" items="${requestScope.roles}">
									<option value="${role}">${role}</option>
								</c:forEach>
							</select>
						</div>
						<div class="mb-2">
							<label>Trạng thái</label> <select id="acc_mdl_status" class="form-select">
								<c:forEach var="status" items="${requestScope.statuses}">
									<option value="${status}">${status}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Hủy</button>
					<button type="submit" class="btn btn-primary">Lưu</button>
				</div>
			</form>
		</div>
	</div>

	<!-- Confirm Modal -->
	<div class="modal fade" id="acc_mdl_deleteModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header bg-danger text-white">
					<h5 class="modal-title">Xóa tài khoản</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Đóng"></button>
				</div>
				<div class="modal-body">
					<p>
						Bạn có chắc chắn muốn xóa tài khoản <strong id="deleteUsername"></strong>
						không?
					</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Hủy</button>
					<button type="button" id="confirmDeleteBtn" class="btn btn-danger">Xóa</button>
				</div>
			</div>
		</div>
	</div>

	<script>
		const contextPath = '${pageContext.request.contextPath}';
	</script>
	<script
		src="${pageContext.request.contextPath}/assets/scripts/accounts.js"></script>
</body>
</html>