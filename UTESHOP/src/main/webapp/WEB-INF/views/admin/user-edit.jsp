<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa người dùng - Admin</title>
    <style>
        .form-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .required {
            color: red;
        }
        .user-avatar-preview {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #0d6efd;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container-fluid px-4">
        <!-- Page header -->
        <h1 class="mt-4 mb-1">Chỉnh sửa người dùng</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/api/admin/dashboard">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/api/admin/users">Người dùng</a></li>
            <li class="breadcrumb-item active">Chỉnh sửa #${user.userID}</li>
        </ol>

        <!-- Error messages -->
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session" />
        </c:if>

        <div class="row">
            <div class="col-xl-8">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-user-edit me-2"></i>Thông tin người dùng
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/api/admin/user/update" method="post" id="updateForm">
                            <input type="hidden" name="userID" value="${user.userID}">
                            
                            <!-- Account Information (Read-only) -->
                            <div class="form-section">
                                <h5 class="mb-3"><i class="fas fa-user-lock me-2"></i>Thông tin tài khoản</h5>
                                
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Username</label>
                                        <input type="text" class="form-control" value="${user.username}" disabled>
                                        <div class="form-text">Username không thể thay đổi</div>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">ID</label>
                                        <input type="text" class="form-control" value="#${user.userID}" disabled>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="role" class="form-label">
                                            Vai trò <span class="required">*</span>
                                        </label>
                                        <select class="form-select" id="role" name="role" required>                                            
                                            <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>
                                                Quản trị viên (Admin)
                                            </option>
                                            <option value="MANAGER" ${user.role == 'MANAGER' ? 'selected' : ''}>
                                                Quản lý cửa hàng (Manager)
                                            </option>
                                            <option value="SHIPPER" ${user.role == 'SHIPPER' ? 'selected' : ''}>
                                                Shipper
                                            </option>
                                            <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>
                                                Khách hàng (User)
                                            </option>
                                        </select>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="status" class="form-label">
                                            Trạng thái <span class="required">*</span>
                                        </label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="ACTIVE" ${user.status == 'ACTIVE' || user.status == 'active' ? 'selected' : ''}>
                                                Hoạt động
                                            </option>
                                            <option value="LOCKED" ${user.status == 'LOCKED' ? 'selected' : ''}>
                                                Khóa
                                            </option>
                                        </select>
                                    </div>
                                </div>

                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle me-2"></i>
                                    Để thay đổi mật khẩu, vui lòng sử dụng chức năng reset password riêng.
                                </div>
                            </div>

                            <!-- Personal Information -->
                            <div class="form-section">
                                <h5 class="mb-3"><i class="fas fa-id-card me-2"></i>Thông tin cá nhân</h5>
                                
                                <div class="mb-3">
                                    <label for="fullname" class="form-label">
                                        Họ và tên <span class="required">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="fullname" name="fullname" 
                                           required maxlength="100"
                                           value="${user.fullname}">
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="email" class="form-label">
                                            Email <span class="required">*</span>
                                        </label>
                                        <input type="email" class="form-control" id="email" name="email" 
                                               required
                                               value="${user.email}">
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="phone" class="form-label">Số điện thoại</label>
                                        <input type="tel" class="form-control" id="phone" name="phone" 
                                               pattern="[0-9]{10,11}"
                                               value="${user.phone}">
                                        <div class="form-text">10-11 số</div>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="avatar" class="form-label">URL Avatar</label>
                                    <input type="text" class="form-control" id="avatar" name="avatar" 
                                           value="${user.avatar}">
                                    <div class="form-text">Đường dẫn đến ảnh đại diện</div>
                                </div>
                            </div>

                            <!-- Action buttons -->
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/api/admin/users" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Lưu thay đổi
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-xl-4">
                <!-- User Info Card -->
                <div class="card mb-3">
                    <div class="card-header bg-primary text-white">
                        <i class="fas fa-user me-2"></i>Thông tin người dùng
                    </div>
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <c:choose>
                                <c:when test="${not empty user.avatar}">
                                    <c:choose>
                                        <c:when test="${user.avatar.startsWith('http://') || user.avatar.startsWith('https://')}">
                                            <img src="${user.avatar}" 
                                                 class="user-avatar-preview" alt="${user.fullname}"
                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}${user.avatar != 'default-avatar.png' ? '/assets/uploads/avatar/'.concat(user.avatar) : '/assets/images/avatar/default-avatar.png'}" 
                                                 class="user-avatar-preview" alt="${user.fullname}"
                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                        </c:otherwise>
                                    </c:choose>
                                    <div style="width: 120px; height: 120px; border-radius: 50%; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: none; align-items: center; justify-content: center; margin: 0 auto; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                                        <i class="fas fa-user fa-3x text-white"></i>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div style="width: 120px; height: 120px; border-radius: 50%; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center; margin: 0 auto; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                                        <i class="fas fa-user fa-3x text-white"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <h5 class="mb-1">${user.fullname}</h5>
                        <p class="text-muted mb-2">@${user.username}</p>
                        
                        <div class="mb-2">
                            <c:choose>
                                <c:when test="${user.role == 'ADMIN'}">
                                    <span class="badge bg-danger">
                                        <i class="fas fa-user-shield me-1"></i>Admin
                                    </span>
                                </c:when>
                                <c:when test="${user.role == 'SHIPPER'}">
                                    <span class="badge bg-success">
                                        <i class="fas fa-shipping-fast me-1"></i>Shipper
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-info">
                                        <i class="fas fa-user me-1"></i>User
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <hr>
                        
                        <div class="text-start">
                            <p class="mb-1"><strong>Email:</strong> ${user.email}</p>
                            <p class="mb-1"><strong>SĐT:</strong> ${not empty user.phone ? user.phone : 'Chưa cập nhật'}</p>
                            <p class="mb-0"><strong>Ngày tạo:</strong>
                                <c:if test="${not empty user.createAt}">
                                    ${user.createAt.toString().substring(0, 10)}
                                </c:if>
                                <c:if test="${empty user.createAt}">
                                    -
                                </c:if>
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Help Card -->
                <div class="card">
                    <div class="card-header bg-warning">
                        <i class="fas fa-exclamation-triangle me-2"></i>Lưu ý
                    </div>
                    <div class="card-body">
                        <ul class="mb-0">
                            <li>Username không thể thay đổi sau khi tạo</li>
                            <li>Không thể xóa admin cuối cùng của hệ thống</li>
                            <li>Cẩn thận khi thay đổi vai trò người dùng</li>
                            <li>Khóa tài khoản sẽ ngăn người dùng đăng nhập</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

