<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- PROFILE HEADER -->
<div class="profile-header text-center text-white py-5 position-relative"
     style="background: linear-gradient(135deg, #007bff 0%, #ff4b5c 100%);
            border-bottom-left-radius: 40px;
            border-bottom-right-radius: 40px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);">

    <!-- Avatar -->
    <div class="d-flex justify-content-center mb-3" style="margin-top: -20px;">
        <div class="avatar-glow rounded-circle p-2 position-relative"
             style="background: linear-gradient(135deg, #007bff, #ff4b5c);
                    box-shadow: 0 0 20px rgba(255,75,92,0.5);">
            <img id="avatarImg" src="${pageContext.request.contextPath}${sessionScope.currentUser.avatar != null && sessionScope.currentUser.avatar != 'default-avatar.png' ? '/uploads/avatar/'.concat(sessionScope.currentUser.avatar) : '/assets/images/avatar/default-avatar.png'}"
                 alt="Avatar"
                 class="rounded-circle border border-4 border-white shadow"
                 style="width: 120px; height: 120px; object-fit: cover; background-color: #fff;">
            <button type="button" class="btn btn-light btn-sm position-absolute bottom-0 end-0 rounded-circle shadow"
                    style="width: 35px; height: 35px; padding: 0;" id="avatarUploadBtn">
                <i class="fas fa-camera text-primary"></i>
            </button>
            <input type="file" id="avatarInput" accept="image/*" style="display: none;">
        </div>
    </div>

    <!-- Info -->
    <h3 class="fw-bold mb-1">${sessionScope.currentUser.fullname != null ? sessionScope.currentUser.fullname : sessionScope.currentUser.username}</h3>
    <p class="mb-2">${sessionScope.currentUser.email}</p>

    <!-- Stats -->
    <div class="d-flex justify-content-center gap-5 mt-4">
        <div>
            <h5 class="fw-bold mb-0">0</h5>
            <span>Đơn hàng</span>
        </div>
        <div>
            <h5 class="fw-bold mb-0">0 ₫</h5>
            <span>Đã mua</span>
        </div>
        <div>
            <h5 class="fw-bold mb-0">Chưa cập nhật</h5>
            <span>Thành viên từ</span>
        </div>
    </div>
</div>

<!-- PROFILE CONTENT -->
<div class="container mt-5 pt-3">
    <div class="bg-white shadow-lg rounded-4 p-4">

        <!-- Tabs -->
        <ul class="nav nav-tabs justify-content-center border-0 mb-4 profile-tabs" id="profileTabs" role="tablist">
            <li class="nav-item">
                <a class="nav-link active fw-semibold border-0 rounded-pill px-4 py-2"
                   data-bs-toggle="tab" href="#info">
                   <i class="fas fa-user me-1"></i> Thông tin cá nhân
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link fw-semibold border-0 rounded-pill px-4 py-2"
                   data-bs-toggle="tab" href="#address">
                   <i class="fas fa-map-marker-alt me-1"></i> Địa chỉ
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link fw-semibold border-0 rounded-pill px-4 py-2"
                   data-bs-toggle="tab" href="#orders">
                   <i class="fas fa-box me-1"></i> Đơn hàng
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link fw-semibold border-0 rounded-pill px-4 py-2"
                   data-bs-toggle="tab" href="#settings">
                   <i class="fas fa-cog me-1"></i> Cài đặt
                </a>
            </li>
        </ul>

        <!-- Tab Content -->
        <div class="tab-content mt-4">

            <!-- TAB 1: Thông tin cá nhân -->
            <div class="tab-pane fade show active" id="info">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold text-gradient"><i class="fas fa-id-card me-2"></i>Thông tin cá nhân</h5>
                    <button class="btn btn-primary shadow-sm" id="editInfoBtn">
                        <i class="fas fa-edit me-1"></i> Chỉnh sửa thông tin
                    </button>
                </div>

                <!-- Success/Error Messages -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Display Mode -->
                <div id="infoDisplay">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Họ và tên:</p>
                            <p class="text-muted">${sessionScope.currentUser.fullname}</p>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Email:</p>
                            <p class="text-muted">${sessionScope.currentUser.email}</p>
                        </div>
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Số điện thoại:</p>
                            <p class="text-muted">${sessionScope.currentUser.phone != null ? sessionScope.currentUser.phone : 'Chưa cập nhật'}</p>
                        </div>
                    </div>
                </div>

                <!-- Edit Mode -->
                <div id="infoEdit" style="display: none;">
                    <form id="updateProfileForm" action="${pageContext.request.contextPath}/user/update-profile" method="post" enctype="multipart/form-data">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Họ và tên:</label>
                                <input type="text" class="form-control" name="fullname" value="${sessionScope.currentUser.fullname}">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Email:</label>
                                <input type="email" class="form-control" id="editEmail" name="email" value="${sessionScope.currentUser.email}" data-original-email="${sessionScope.currentUser.email}">
                                <div id="emailValidationMessage" class="mt-1" style="display: none;"></div>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Số điện thoại:</label>
                                <input type="tel" class="form-control" name="phone" value="${sessionScope.currentUser.phone}">
                            </div>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-success" id="submitProfileBtn">
                                <i class="fas fa-check me-1"></i> Xác nhận
                            </button>
                            <button type="button" class="btn btn-secondary" id="cancelEditBtn">
                                <i class="fas fa-times me-1"></i> Hủy
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- TAB 2: Địa chỉ -->
            <div class="tab-pane fade" id="address">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold text-gradient"><i class="fas fa-map-marker-alt me-2"></i>Địa chỉ giao hàng</h5>
                        <button class="btn btn-gradient shadow-sm" id="addAddressBtn">
                        <i class="fas fa-plus me-1"></i> Thêm địa chỉ mới
                        </button>
                </div>
                
                <!-- Success Message -->
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>${param.success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Address List -->
                <div id="addressListContainer">
                    <!-- Hiển thị danh sách địa chỉ bằng JSTL -->
                    <c:choose>
                        <c:when test="${not empty addresses}">
                            <div id="addressList">
                                <c:forEach var="addr" items="${addresses}" varStatus="status">
                                    <div class="card mb-3 address-card ${addr.isDefault ? 'border-primary' : ''}">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between align-items-start">
                                                <div class="flex-grow-1">
                                                    <div class="d-flex align-items-center gap-2 mb-2">
                                                        <h6 class="fw-bold text-primary mb-0">
                                                            <i class="fas fa-user me-2"></i>${addr.name}
                                                        </h6>
                                                        <c:if test="${addr.isDefault}">
                                                            <span class="badge bg-primary">Mặc định</span>
                                                        </c:if>
                                                    </div>
                                                    <p class="text-muted mb-1">
                                                        <i class="fas fa-phone me-2 text-secondary"></i>${addr.phone}
                                                    </p>
                                                    <p class="text-muted mb-1">
                                                        <i class="fas fa-location-arrow me-2 text-secondary"></i>
                                                        ${addr.addressDetail}
                                                    </p>
                                                    <p class="text-muted mb-0">
                                                        <i class="fas fa-map-marked me-2 text-secondary"></i>
                                                        ${addr.ward}, ${addr.district}, ${addr.province}
                                                    </p>
                                                </div>
                                                <div class="d-flex flex-column gap-1">
                                                    <button class="btn btn-sm btn-outline-primary" 
                                                            onclick="editAddress(${addr.addressID}, '${addr.name}', '${addr.phone}', '${addr.province}', '${addr.district}', '${addr.ward}', '${addr.addressDetail}', ${addr.isDefault})" 
                                                            title="Sửa">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <c:if test="${!addr.isDefault}">
                                                        <button class="btn btn-sm btn-outline-danger" 
                                                                onclick="deleteAddress(${addr.addressID})" 
                                                                title="Xóa">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="noAddressMessage">
                                <div class="card text-center py-5">
                                    <div class="card-body">
                                        <i class="fas fa-map-marker-alt fa-3x text-muted mb-3"></i>
                                        <h5 class="text-muted">Chưa có địa chỉ nào</h5>
                                        <p class="text-muted">Thêm địa chỉ giao hàng để đặt hàng nhanh chóng hơn!</p>
                                        <button class="btn btn-primary" onclick="document.getElementById('addAddressBtn').click()">
                                            <i class="fas fa-plus me-1"></i> Thêm địa chỉ đầu tiên
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Add/Edit Address Form -->
                <div id="addressFormContainer" style="display: none;">
                    <div class="card">
                        <div class="card-body">
                            <h6 class="fw-bold mb-3">
                                <i class="fas fa-map-marked-alt me-2"></i>
                                <span id="formTitle">Thêm địa chỉ mới</span>
                            </h6>
                            
                            <form id="addressForm">
                                <input type="hidden" id="addressId" value="">
                                
                                <!-- Tên người nhận -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-user me-1"></i>Tên người nhận:
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           placeholder="Nhập tên người nhận" required>
                                </div>

                                <!-- Số điện thoại -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-phone me-1"></i>Số điện thoại:
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           placeholder="Nhập số điện thoại" pattern="[0-9]{10,11}" required>
                                </div>

                                <!-- Tỉnh/Thành phố -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-city me-1"></i>Tỉnh/Thành phố:
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="province" name="province" 
                                           placeholder="Ví dụ: Thành phố Hồ Chí Minh, Hà Nội..." required>
                                </div>

                                <!-- Quận/Huyện -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-building me-1"></i>Quận/Huyện:
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="district" name="district" 
                                           placeholder="Ví dụ: Quận 1, Huyện Củ Chi..." required>
                                </div>

                                <!-- Phường/Xã -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-home me-1"></i>Phường/Xã:
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="ward" name="ward" 
                                           placeholder="Ví dụ: Phường Bến Nghé, Xã Tân An..." required>
                                </div>

                                <!-- Địa chỉ chi tiết -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-location-arrow me-1"></i>Địa chỉ chi tiết:
                                        <span class="text-danger">*</span>
                                    </label>
                                    <textarea class="form-control" id="addressDetail" name="addressDetail" 
                                              rows="2" placeholder="Số nhà, tên đường..." required></textarea>
                                </div>

                                <!-- Đặt làm mặc định -->
                                <div class="mb-3 form-check">
                                    <input type="checkbox" class="form-check-input" id="isDefault" name="isDefault" value="true">
                                    <label class="form-check-label" for="isDefault">
                                        Đặt làm địa chỉ mặc định
                                    </label>
                                </div>

                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-check me-1"></i> Lưu địa chỉ
                                    </button>
                                    <button type="button" class="btn btn-secondary" id="cancelAddressBtn">
                                        <i class="fas fa-times me-1"></i> Hủy
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- TAB 3: Đơn hàng -->
            <div class="tab-pane fade" id="orders">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold text-gradient"><i class="fas fa-box me-2"></i>Đơn hàng</h5>
                    <button class="btn btn-warning shadow-sm text-white" onclick="window.location.href='${pageContext.request.contextPath}/cart'">
                        <i class="fas fa-shopping-cart me-1"></i> Xem giỏ hàng
                    </button>
                </div>

                <!-- Order Statistics -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h5 class="text-primary">0</h5>
                                <small class="text-muted">Chờ xác nhận</small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h5 class="text-warning">0</h5>
                                <small class="text-muted">Đang giao</small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h5 class="text-success">0</h5>
                                <small class="text-muted">Đã giao</small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h5 class="text-danger">0</h5>
                                <small class="text-muted">Đã hủy</small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Order List -->
                <div class="card">
                    <div class="card-body">
                        <div class="text-center py-5">
                            <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">Chưa có đơn hàng nào</h5>
                            <p class="text-muted">Hãy mua sắm để tạo đơn hàng đầu tiên của bạn!</p>
                            <button class="btn btn-primary" onclick="window.location.href='${pageContext.request.contextPath}/home'">
                                <i class="fas fa-shopping-bag me-1"></i> Mua sắm ngay
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- TAB 4: Cài đặt -->
            <div class="tab-pane fade" id="settings">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold text-gradient"><i class="fas fa-cog me-2"></i>Cài đặt</h5>
                    <button class="btn btn-danger shadow-sm" data-bs-toggle="modal" data-bs-target="#deleteAccountModal">
                        <i class="fas fa-trash-alt me-1"></i> Xóa tài khoản
                    </button>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-body">
                                <h6 class="fw-bold"><i class="fas fa-lock me-2"></i>Bảo mật</h6>
                                <p class="text-muted mb-3">Quản lý mật khẩu và bảo mật tài khoản</p>
                                <button class="btn btn-outline-primary btn-sm" onclick="window.location.href='${pageContext.request.contextPath}/user/change-password'">
                                    <i class="fas fa-key me-1"></i> Đổi mật khẩu
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-body">
                                <h6 class="fw-bold"><i class="fas fa-bell me-2"></i>Thông báo</h6>
                                <p class="text-muted mb-3">Cài đặt nhận thông báo từ hệ thống</p>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="emailNotifications" checked>
                                    <label class="form-check-label" for="emailNotifications">
                                        Nhận email thông báo
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card">
                    <div class="card-body">
                        <h6 class="fw-bold text-danger"><i class="fas fa-exclamation-triangle me-2"></i>Vùng nguy hiểm</h6>
                        <p class="text-muted">Các hành động này không thể hoàn tác. Hãy cẩn thận!</p>
                        <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteAccountModal">
                            <i class="fas fa-trash-alt me-1"></i> Xóa tài khoản vĩnh viễn
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Delete Account Modal -->
<div class="modal fade" id="deleteAccountModal" tabindex="-1" aria-labelledby="deleteAccountModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title" id="deleteAccountModalLabel">
                    <i class="fas fa-exclamation-triangle me-2"></i>Xóa tài khoản
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/user/delete-account" method="post" 
                  onsubmit="return confirm('Bạn có chắc chắn muốn xóa tài khoản? Hành động này không thể hoàn tác!');">
                <div class="modal-body">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        <strong>Cảnh báo!</strong> Hành động này không thể hoàn tác.
                    </div>
                    <p>Bạn có chắc chắn muốn xóa tài khoản của mình? Tất cả dữ liệu sẽ bị mất vĩnh viễn:</p>
                    <ul>
                        <li>Thông tin cá nhân</li>
                        <li>Lịch sử đơn hàng</li>
                        <li>Địa chỉ giao hàng</li>
                        <li>Tất cả dữ liệu khác</li>
                    </ul>
                    
                    <div class="mb-3">
                        <label for="confirmDelete" class="form-label fw-semibold">
                            Nhập "XÓA TÀI KHOẢN" để xác nhận:
                            <span class="text-danger">*</span>
                        </label>
                        <input type="text" class="form-control" id="confirmDelete" name="confirmText"
                               placeholder="XÓA TÀI KHOẢN" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger" id="confirmDeleteBtn" disabled>
                        <i class="fas fa-trash-alt me-1"></i> Xóa tài khoản
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
        </div>
    </div>
</div>

<!-- Custom Styles -->
<style>
    .text-gradient {
        background: linear-gradient(135deg, #007bff, #ff4b5c);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }

    .avatar-glow:hover {
        transform: scale(1.05);
        transition: 0.3s ease;
        box-shadow: 0 0 25px rgba(255,75,92,0.6);
    }

    /* Tabs */
    .profile-tabs .nav-link {
        color: #333;
        transition: 0.3s;
        background-color: #f8f9fa;
    }
    .profile-tabs .nav-link:hover {
        background: linear-gradient(135deg, #007bff, #ff4b5c);
        color: #fff !important;
        transform: translateY(-2px);
        box-shadow: 0 3px 6px rgba(0,0,0,0.1);
    }
    .profile-tabs .nav-link.active {
        background: linear-gradient(135deg, #007bff, #ff4b5c);
        color: #fff !important;
        box-shadow: 0 3px 10px rgba(255,75,92,0.4);
    }

    /* Buttons */
    .btn-gradient {
        background: linear-gradient(135deg, #ff4b5c, #ff7096);
        border: none;
        color: white;
        transition: 0.3s;
    }
    .btn-gradient:hover {
        opacity: 0.9;
        transform: scale(1.03);
    }
    .btn-primary:hover, .btn-warning:hover, .btn-danger:hover {
        transform: scale(1.03);
        opacity: 0.9;
    }
</style>

<!-- JavaScript -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Avatar Upload
    const avatarUploadBtn = document.getElementById('avatarUploadBtn');
    const avatarInput = document.getElementById('avatarInput');
    const avatarImg = document.getElementById('avatarImg');

    avatarUploadBtn.addEventListener('click', () => {
        avatarInput.click();
    });

    avatarInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                avatarImg.src = e.target.result;
                // Here you can add AJAX to upload the avatar
                uploadAvatar(file);
            };
            reader.readAsDataURL(file);
        }
    });

    // Profile Edit Toggle
    const editInfoBtn = document.getElementById('editInfoBtn');
    const cancelEditBtn = document.getElementById('cancelEditBtn');
    const infoDisplay = document.getElementById('infoDisplay');
    const infoEdit = document.getElementById('infoEdit');

    editInfoBtn.addEventListener('click', () => {
        infoDisplay.style.display = 'none';
        infoEdit.style.display = 'block';
        editInfoBtn.style.display = 'none';
    });

    cancelEditBtn.addEventListener('click', () => {
        infoDisplay.style.display = 'block';
        infoEdit.style.display = 'none';
        editInfoBtn.style.display = 'block';
    });

    // Email Validation Realtime
    const editEmailInput = document.getElementById('editEmail');
    const emailValidationMessage = document.getElementById('emailValidationMessage');
    const submitProfileBtn = document.getElementById('submitProfileBtn');
    let emailCheckTimeout;

    if (editEmailInput) {
        editEmailInput.addEventListener('input', function() {
            clearTimeout(emailCheckTimeout);
            const newEmail = this.value.trim();
            const originalEmail = this.getAttribute('data-original-email');

            // Nếu email không thay đổi, không cần kiểm tra
            if (newEmail === originalEmail || newEmail === '') {
                emailValidationMessage.style.display = 'none';
                submitProfileBtn.disabled = false;
                return;
            }

            // Debounce - chờ 500ms sau khi user ngừng gõ
            emailCheckTimeout = setTimeout(async () => {
                emailValidationMessage.innerHTML = '<small class="text-info"><i class="fas fa-spinner fa-spin me-1"></i>Đang kiểm tra...</small>';
                emailValidationMessage.style.display = 'block';

                try {
                    const response = await fetch('${pageContext.request.contextPath}/auth/check-exist?email=' + encodeURIComponent(newEmail));
                    const data = await response.json();

                    if (data.success) {
                        emailValidationMessage.innerHTML = '<small class="text-success"><i class="fas fa-check-circle me-1"></i>Email khả dụng</small>';
                        submitProfileBtn.disabled = false;
                    } else {
                        emailValidationMessage.innerHTML = '<small class="text-danger"><i class="fas fa-times-circle me-1"></i>' + data.error + '</small>';
                        submitProfileBtn.disabled = true;
                    }
                } catch (error) {
                    emailValidationMessage.innerHTML = '<small class="text-warning"><i class="fas fa-exclamation-triangle me-1"></i>Không thể kiểm tra email</small>';
                    submitProfileBtn.disabled = false;
                }
            }, 500);
        });
    }

    // ==================== ADDRESS MANAGEMENT (AJAX) ====================
    
    // Hàm sửa địa chỉ
    window.editAddress = function(addressId, name, phone, province, district, ward, addressDetail, isDefault) {
        // Điền dữ liệu vào form
        document.getElementById('formTitle').textContent = 'Chỉnh sửa địa chỉ';
        document.getElementById('addressId').value = addressId;
        document.getElementById('name').value = name;
        document.getElementById('phone').value = phone;
        document.getElementById('province').value = province;
        document.getElementById('district').value = district;
        document.getElementById('ward').value = ward;
        document.getElementById('addressDetail').value = addressDetail;
        document.getElementById('isDefault').checked = isDefault;
        
        // Hiển thị form
        document.getElementById('addressListContainer').style.display = 'none';
        document.getElementById('addressFormContainer').style.display = 'block';
    };
    
    // Hàm xóa địa chỉ (AJAX)
    window.deleteAddress = async function(addressId) {
        if (!confirm('Bạn có chắc muốn xóa địa chỉ này?')) {
            return;
        }
        
        try {
            // ✅ Get token from localStorage
            const token = localStorage.getItem('authToken');
            
            const response = await fetch('${pageContext.request.contextPath}/api/user/addresses/' + addressId, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + token // ✅ Send token
                }
            });
            
            const result = await response.json();
            
            if (result.success) {
                // Success - reload page
                window.location.href = '${pageContext.request.contextPath}/user/profile?success=' + 
                    encodeURIComponent(result.message) + '#address';
            } else {
                alert('Lỗi: ' + result.error);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi xóa địa chỉ!');
        }
    };

    // ==================== ADDRESS FORM SUBMIT (AJAX) ====================
    
    const addressForm = document.getElementById('addressForm');
    if (addressForm) {
        addressForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const addressId = document.getElementById('addressId').value;
            const isEdit = addressId && addressId.trim() !== '';
            
            // Collect form data
            const formData = {
                name: document.getElementById('name').value,
                phone: document.getElementById('phone').value,
                province: document.getElementById('province').value,
                district: document.getElementById('district').value,
                ward: document.getElementById('ward').value,
                addressDetail: document.getElementById('addressDetail').value,
                isDefault: document.getElementById('isDefault').checked
            };
            
            try {
                let url = '${pageContext.request.contextPath}/api/user/addresses';
                let method = 'POST';
                
                if (isEdit) {
                    url += '/' + addressId;
                    method = 'PUT';
                }
                
                // ✅ Get token from localStorage
                const token = localStorage.getItem('authToken');
                
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token // ✅ Send token
                    },
                    body: JSON.stringify(formData)
                });
                
                const result = await response.json();
                
                if (result.success) {
                    // Success - reload page để hiển thị địa chỉ mới
                    window.location.href = '${pageContext.request.contextPath}/user/profile?success=' + 
                        encodeURIComponent(result.message) + '#address';
                } else {
                    alert('Lỗi: ' + result.error);
                }
                
            } catch (error) {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi lưu địa chỉ!');
            }
        });
    }
    
    // Hiển thị form thêm địa chỉ
    document.getElementById('addAddressBtn').addEventListener('click', () => {
        // Reset form
        const form = document.getElementById('addressForm');
        form.reset();
        document.getElementById('addressId').value = '';
        document.getElementById('formTitle').textContent = 'Thêm địa chỉ mới';
        
        // Show form
        document.getElementById('addressListContainer').style.display = 'none';
        document.getElementById('addressFormContainer').style.display = 'block';
    });

    // Nút hủy - quay lại danh sách địa chỉ
    const cancelBtn = document.getElementById('cancelAddressBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
            document.getElementById('addressFormContainer').style.display = 'none';
            document.getElementById('addressListContainer').style.display = 'block';
        });
    }

    // Tự động chuyển đến tab address nếu có hash #address trong URL
    if (window.location.hash === '#address') {
        const triggerEl = document.querySelector('a[href="#address"]');
        if (triggerEl) {
            const tab = new bootstrap.Tab(triggerEl);
            tab.show();
        }
    }

    // Delete Account Confirmation - Đơn giản hóa
    const confirmDeleteInput = document.getElementById('confirmDelete');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

    // Enable button chỉ khi nhập đúng "XÓA TÀI KHOẢN"
    if (confirmDeleteInput && confirmDeleteBtn) {
        confirmDeleteInput.addEventListener('input', function() {
            confirmDeleteBtn.disabled = (this.value !== 'XÓA TÀI KHOẢN');
        });

        // Reset khi đóng modal
        const deleteAccountModal = document.getElementById('deleteAccountModal');
        if (deleteAccountModal) {
            deleteAccountModal.addEventListener('hidden.bs.modal', function() {
                confirmDeleteInput.value = '';
                confirmDeleteBtn.disabled = true;
            });
        }
    }
});

// Helper Functions
function uploadAvatar(file) {
    const formData = new FormData();
    formData.append('avatar', file);
    
    fetch('${pageContext.request.contextPath}/user/update-profile', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Cập nhật avatar thành công!', 'success');
        } else {
            showToast('Có lỗi xảy ra khi cập nhật avatar!', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Có lỗi xảy ra!', 'error');
    });
}

function showToast(message, type) {
    const toast = document.createElement('div');
    const bgClass = type === 'success' ? 'success' : 'danger';
    toast.className = `toast align-items-center text-bg-${bgClass} border-0 position-fixed bottom-0 end-0 m-3`;
    toast.role = 'alert';
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    document.body.appendChild(toast);
    new bootstrap.Toast(toast).show();
}
</script>