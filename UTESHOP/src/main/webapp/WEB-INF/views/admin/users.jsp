<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý người dùng - Admin</title>
    <link href="${pageContext.request.contextPath}/assets/styles/users.css" rel="stylesheet"/>
</head>
<body>
    <div class="container-fluid px-4">
        <!-- Page header -->
        <div class="d-flex justify-content-between align-items-center mt-4 mb-4">
            <div>
                <h1 class="h3 mb-0">Quản lý người dùng</h1>
                <ol class="breadcrumb mb-0 mt-2">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/api/admin/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active">Người dùng</li>
                </ol>
            </div>
        </div>

        <!-- Success/Error messages -->
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="success" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session" />
        </c:if>

        <!-- Statistics -->
        <div class="row mb-4">
            <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-3">
                <div class="stats-card gradient-blue">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <div class="stat-label text-white-50">Người dùng</div>
                                <div class="stat-value text-white">${totalUsers}</div>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-users"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-3">
                <div class="stats-card gradient-red">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <div class="stat-label text-white-50">Admin</div>
                                <div class="stat-value text-white">${adminCount}</div>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-user-shield"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-3">
                <div class="stats-card gradient-purple">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <div class="stat-label text-white-50">Manager</div>
                                <div class="stat-value text-white">${managerCount}</div>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-user-tie"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-3">
                <div class="stats-card gradient-green">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <div class="stat-label text-white-50">Shipper</div>
                                <div class="stat-value text-white">${shipperCount}</div>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-shipping-fast"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-3">
                <div class="stats-card gradient-orange">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <div class="stat-label text-white-50">Khách hàng</div>
                                <div class="stat-value text-white">${userCount}</div>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-user"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Search and Filter Section -->
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-search me-2"></i>Tìm kiếm người dùng
            </div>
            <div class="card-body">
                <form id="searchForm" class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label class="form-label">Từ khóa</label>
                        <input type="text" id="searchInput" class="form-control"
                            placeholder="Tìm theo username, email, họ tên...">
                    </div>

                    <div class="col-md-3">
                        <label class="form-label">Vai trò</label>
                        <select id="roleSelect" class="form-select">
                            <option value="">-- Chọn vai trò --</option>
                            <option value="ADMIN">ADMIN</option>
                            <option value="MANAGER">MANAGER</option>
                            <option value="SHIPPER">SHIPPER</option>
                            <option value="USER">USER</option>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label">Trạng thái</label>
                        <select id="statusSelect" class="form-select status-select">
                            <option value="">-- Chọn trạng thái --</option>
                            <option value="ACTIVE" class="status-active">ACTIVE</option>
                            <option value="LOCKED" class="status-locked">LOCKED</option>
                            <option value="PENDING" class="status-pending">PENDING</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <button type="button" id="btnAddUser" class="btn btn-primary w-100">
                            <i class="fas fa-plus me-2"></i>Thêm mới
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Users table -->
        <div class="card">
            <div class="card-header">
                <i class="fas fa-table me-2"></i>
                Danh sách người dùng
            </div>
            <div class="card-body">
                <div id="message" class="text-center text-muted py-4">Đang tải thông tin...</div>
                
                <div id="usersTableContainer" class="d-none">
                    <div class="table-responsive">
                        <table class="table table-hover table-striped align-middle" id="usersTable">
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Họ tên</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>
                                    <th>Vai trò</th>
                                    <th>Trạng thái</th>
                                    <th>Ngày tạo</th>
                                    <th class="text-center">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Populated by JavaScript -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add User Modal -->
    <div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <form id="addUserForm" method="post" action="${pageContext.request.contextPath}/api/admin/user/insert" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addUserModalLabel"><i class="fas fa-user-lock me-2"></i>Thêm tài khoản mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
				<div class="modal-body">
					<div class="row">
						<div class="mb-3">
							<label class="form-label">Tên đăng nhập <span
								class="text-danger">*</span></label> <input type="text" name="username"
								class="form-control" required>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 mb-3">
							<label class="form-label">Mật khẩu <span
								class="text-danger">*</span></label> <input type="password"
								name="password" id="addPassword" class="form-control" required>
						</div>
						<div class="col-md-6 mb-3">
							<label class="form-label">Nhập lại mật khẩu <span
								class="text-danger">*</span></label> <input type="password"
								name="password2" id="addPassword2" class="form-control" required>
						</div>

						<div class="row">
							<div class="col-md-6 mb-3">
								<label class="form-label">Vai trò <span
									class="text-danger">*</span></label> <select name="role"
									class="form-select" required>
									<option value="">-- Chọn vai trò --</option>
									<option value="ADMIN">ADMIN</option>
									<option value="MANAGER">MANAGER</option>
									<option value="SHIPPER">SHIPPER</option>
									<option value="USER">USER</option>
								</select>
							</div>
							<div class="col-md-6 mb-3">
								<label class="form-label">Trạng thái <span
									class="text-danger">*</span></label> <select name="status"
									class="form-select" required>
									<option value="">-- Chọn trạng thái --</option>
									<option value="ACTIVE">ACTIVE</option>
									<option value="LOCKED">LOCKED</option>
									<option value="PENDING">PENDING</option>
								</select>
							</div>
						</div>
						<div class="row">
							<div class="mb-3">
								<label class="form-label">Họ tên <span
									class="text-danger">*</span></label> <input type="text" name="fullname"
									class="form-control" required>
							</div>
						</div>
						<div class="row">
							<div class="mb-3">
								<label class="form-label">Email <span
									class="text-danger">*</span></label> <input type="email" name="email"
									class="form-control" required>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-2"></i>Lưu
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Delete confirmation modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle me-2"></i>Xác nhận xóa
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p class="mb-0">Bạn có chắc chắn muốn xóa người dùng <strong id="deleteUsername"></strong>?</p>
                    <p class="text-danger mb-0 mt-2">Hành động này không thể hoàn tác!</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <form id="deleteForm" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash me-2"></i>Xóa
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        const contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/assets/scripts/users.js"></script>
</body>
</html>