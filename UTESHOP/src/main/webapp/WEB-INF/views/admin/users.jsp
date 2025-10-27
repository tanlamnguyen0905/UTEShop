<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý người dùng - Admin</title>
    <style>
        .stats-card {
            border-left: 4px solid;
            transition: transform 0.2s;
        }
        .stats-card:hover {
            transform: translateY(-5px);
        }
        .stats-card.total { border-left-color: #007bff; }
        .stats-card.admin { border-left-color: #dc3545; }
        .stats-card.shipper { border-left-color: #28a745; }
        .stats-card.user { border-left-color: #ffc107; }
    </style>
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
            <div class="col-md-3">
                <div class="card stats-card total">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-1">Tổng người dùng</h6>
                                <h3 class="mb-0">${totalUsers}</h3>
                            </div>
                            <div class="text-primary">
                                <i class="fas fa-users fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card admin">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-1">Admin</h6>
                                <h3 class="mb-0">${adminCount}</h3>
                            </div>
                            <div class="text-danger">
                                <i class="fas fa-user-shield fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card shipper">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-1">Shipper</h6>
                                <h3 class="mb-0">${shipperCount}</h3>
                            </div>
                            <div class="text-success">
                                <i class="fas fa-shipping-fast fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card user">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted mb-1">Khách hàng</h6>
                                <h3 class="mb-0">${userCount}</h3>
                            </div>
                            <div class="text-warning">
                                <i class="fas fa-user fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Users table -->
        <div class="card">
            <div class="card-header">
                <i class="fas fa-table me-2"></i>
                Danh sách người dùng
            </div>
            <div class="card-body">
                <c:if test="${empty users}">
                    <div class="alert alert-info mb-0">
                        <i class="fas fa-info-circle me-2"></i>
                        Chưa có người dùng nào trong hệ thống
                    </div>
                </c:if>

                <c:if test="${not empty users}">
                    <div class="table-responsive">
                        <table class="table table-hover table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Họ tên</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>
                                    <th>Vai trò</th>
                                    <th>Trạng thái</th>
                                    <th>Ngày tạo</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr>
                                        <td><strong>#${user.userID}</strong></td>
                                        <td>${user.username}</td>
                                        <td>${user.fullname}</td>
                                        <td>${user.email}</td>
                                        <td>${not empty user.phone ? user.phone : '-'}</td>
                                        <td>
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
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.status == 'ACTIVE' || user.status == 'active'}">
                                                    <span class="badge bg-success">Hoạt động</span>
                                                </c:when>
                                                <c:when test="${user.status == 'LOCKED'}">
                                                    <span class="badge bg-danger">Khóa</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${user.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${not empty user.createAt}">
                                                ${user.createAt.toString().replace('T', ' ').substring(0, 16)}
                                            </c:if>
                                            <c:if test="${empty user.createAt}">
                                                -
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/api/admin/user/edit?id=${user.userID}" 
                                               class="btn btn-sm btn-warning" title="Chỉnh sửa">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <button type="button" class="btn btn-sm btn-danger" 
                                                    onclick="confirmDelete(${user.userID}, '${user.username}')"
                                                    title="Xóa">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
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
        function confirmDelete(userId, username) {
            document.getElementById('deleteUsername').textContent = username;
            document.getElementById('deleteForm').action = '${pageContext.request.contextPath}/api/admin/user/delete?id=' + userId;
            var modal = new bootstrap.Modal(document.getElementById('deleteModal'));
            modal.show();
        }
    </script>
</body>
</html>

