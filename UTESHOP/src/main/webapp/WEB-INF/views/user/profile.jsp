<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
            <img id="avatarImg" src="${user.avatar != null ? user.avatar : '/images/default-avatar.png'}"
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
    <h3 class="fw-bold mb-1">${user.fullname != null ? user.fullname : user.username}</h3>
    <p class="mb-2">${user.email}</p>

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

                <!-- Display Mode -->
                <div id="infoDisplay">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Họ và tên:</p>
                            <p class="text-muted">${user.fullname}</p>
                        </div>
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Ngày sinh:</p>
                            <p class="text-muted">Chưa cập nhật</p>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Email:</p>
                            <p class="text-muted">${user.email}</p>
                        </div>
                        <div class="col-md-6">
                            <p class="fw-semibold text-dark mb-1">Số điện thoại:</p>
                            <p class="text-muted">${user.phone != null ? user.phone : 'Chưa cập nhật'}</p>
                        </div>
                    </div>
                </div>

                <!-- Edit Mode -->
                <div id="infoEdit" style="display: none;">
                    <form id="updateProfileForm" action="${pageContext.request.contextPath}/user/update-profile" method="post" enctype="multipart/form-data">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Họ và tên:</label>
                                <input type="text" class="form-control" name="fullname" value="${user.fullname}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Ngày sinh:</label>
                                <input type="date" class="form-control" name="birthday">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Email:</label>
                                <input type="email" class="form-control" name="email" value="${user.email}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Số điện thoại:</label>
                                <input type="tel" class="form-control" name="phone" value="${user.phone}">
                            </div>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-success">
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
                    <h5 class="fw-bold text-gradient"><i class="fas fa-map-marker-alt me-2"></i>Địa chỉ</h5>
                    <div class="d-flex gap-2">
                        <button class="btn btn-gradient shadow-sm" id="addAddressBtn">
                            <i class="fas fa-plus me-1"></i> Thêm địa chỉ
                        </button>
                        <button class="btn btn-outline-primary shadow-sm" id="editAddressBtn">
                            <i class="fas fa-pen me-1"></i> Chỉnh sửa
                        </button>
                    </div>
                </div>

                <!-- Address List -->
                <div id="addressList">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start">
                                <div>
                                    <h6 class="fw-bold">Địa chỉ mặc định</h6>
                                    <p class="text-muted mb-1">123 Đường ABC, Phường XYZ, Quận 1, TP.HCM</p>
                                    <p class="text-muted mb-0">Số điện thoại: 0123456789</p>
                                </div>
                                <div class="d-flex gap-1">
                                    <button class="btn btn-sm btn-outline-primary" onclick="editAddress(1)">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger" onclick="deleteAddress(1)">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Add/Edit Address Form -->
                <div id="addressForm" style="display: none;">
                    <form id="addressFormData" class="card">
                        <div class="card-body">
                            <h6 class="fw-bold mb-3">Thông tin địa chỉ</h6>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Họ tên người nhận:</label>
                                    <input type="text" class="form-control" name="recipientName" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Số điện thoại:</label>
                                    <input type="tel" class="form-control" name="phone" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-8">
                                    <label class="form-label">Địa chỉ chi tiết:</label>
                                    <input type="text" class="form-control" name="address" required>
                                </div>
                            </div>
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="checkbox" name="isDefault">
                                <label class="form-check-label">Đặt làm địa chỉ mặc định</label>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-check me-1"></i> Lưu địa chỉ
                                </button>
                                <button type="button" class="btn btn-secondary" id="cancelAddressBtn">
                                    <i class="fas fa-times me-1"></i> Hủy
                                </button>
                            </div>
                        </div>
                    </form>
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
                    <label for="confirmDelete" class="form-label">Nhập "XÓA TÀI KHOẢN" để xác nhận:</label>
                    <input type="text" class="form-control" id="confirmDelete" placeholder="XÓA TÀI KHOẢN">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger" id="confirmDeleteBtn" disabled>
                    <i class="fas fa-trash-alt me-1"></i> Xóa tài khoản
                </button>
            </div>
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

    // Address Management
    const addAddressBtn = document.getElementById('addAddressBtn');
    const editAddressBtn = document.getElementById('editAddressBtn');
    const cancelAddressBtn = document.getElementById('cancelAddressBtn');
    const addressList = document.getElementById('addressList');
    const addressForm = document.getElementById('addressForm');

    addAddressBtn.addEventListener('click', () => {
        addressList.style.display = 'none';
        addressForm.style.display = 'block';
        addAddressBtn.style.display = 'none';
        editAddressBtn.style.display = 'none';
    });

    editAddressBtn.addEventListener('click', () => {
        addressList.style.display = 'none';
        addressForm.style.display = 'block';
        addAddressBtn.style.display = 'none';
        editAddressBtn.style.display = 'none';
    });

    cancelAddressBtn.addEventListener('click', () => {
        addressList.style.display = 'block';
        addressForm.style.display = 'none';
        addAddressBtn.style.display = 'block';
        editAddressBtn.style.display = 'block';
    });

    // Delete Account Confirmation
    const confirmDeleteInput = document.getElementById('confirmDelete');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

    confirmDeleteInput.addEventListener('input', function() {
        if (this.value === 'XÓA TÀI KHOẢN') {
            confirmDeleteBtn.disabled = false;
        } else {
            confirmDeleteBtn.disabled = true;
        }
    });

    confirmDeleteBtn.addEventListener('click', function() {
        if (confirm('Bạn có chắc chắn muốn xóa tài khoản? Hành động này không thể hoàn tác!')) {
            // Here you can add AJAX to delete the account
            deleteAccount();
        }
    });
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

function editAddress(addressId) {
    // Show edit form with address data
    document.getElementById('addressForm').style.display = 'block';
    document.getElementById('addressList').style.display = 'none';
}

function deleteAddress(addressId) {
    if (confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')) {
        // Here you can add AJAX to delete the address
        showToast('Đã xóa địa chỉ!', 'success');
    }
}

function deleteAccount() {
    fetch('${pageContext.request.contextPath}/user/delete-account', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Tài khoản đã được xóa!', 'success');
            setTimeout(() => {
                window.location.href = '${pageContext.request.contextPath}/home';
            }, 2000);
        } else {
            showToast('Có lỗi xảy ra khi xóa tài khoản!', 'error');
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
