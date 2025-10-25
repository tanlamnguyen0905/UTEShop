<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    // Create DateTimeFormatter for use in page
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    pageContext.setAttribute("dateFormatter", dateFormatter);
%>

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
    <h3 class="fw-bold mb-1" style="color: #fff; text-shadow: 2px 2px 8px rgba(0,0,0,0.3);">
        ${sessionScope.currentUser.fullname != null ? sessionScope.currentUser.fullname : sessionScope.currentUser.username}
    </h3>
    <p class="mb-2" style="color: #fff; text-shadow: 1px 1px 4px rgba(0,0,0,0.2);">
        ${sessionScope.currentUser.email}
    </p>

    <!-- Stats -->
    <c:set var="totalSpent" value="0" />
    <c:forEach var="order" items="${orders}">
        <c:set var="totalSpent" value="${totalSpent + order.amount}" />
    </c:forEach>
    
    <div class="d-flex justify-content-center gap-5 mt-4">
        <div class="text-center px-4 py-2 rounded-3" style="background: rgba(255,255,255,0.2); backdrop-filter: blur(10px);">
            <h4 class="fw-bold mb-1">${not empty orders ? orders.size() : 0}</h4>
            <span class="small">Đơn hàng</span>
        </div>
        <div class="text-center px-4 py-2 rounded-3" style="background: rgba(255,255,255,0.2); backdrop-filter: blur(10px);">
            <h4 class="fw-bold mb-1"><fmt:formatNumber value="${totalSpent}" type="number" maxFractionDigits="0" /> ₫</h4>
            <span class="small">Tổng chi tiêu</span>
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
                                                    <button class="btn btn-sm btn-outline-primary edit-addr-btn" 
                                                            data-id="${addr.addressID}"
                                                            data-name="${addr.name}"
                                                            data-phone="${addr.phone}"
                                                            data-province="${addr.province}"
                                                            data-district="${addr.district}"
                                                            data-ward="${addr.ward}"
                                                            data-detail="${addr.addressDetail}"
                                                            data-default="${addr.isDefault}"
                                                            title="Sửa">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <c:if test="${!addr.isDefault}">
                                                        <button class="btn btn-sm btn-outline-danger delete-addr-btn" 
                                                                data-id="${addr.addressID}"
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
                                    <input type="tel" class="form-control" id="inputPhone" name="inputPhone" 
                                           placeholder="Nhập số điện thoại (VD: 0912345678)" 
                                           minlength="10" maxlength="15" required>
                                    <small class="text-muted">Nhập số điện thoại từ 10-15 chữ số</small>
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
                <c:set var="pendingCount" value="0" />
                <c:set var="confirmedCount" value="0" />
                <c:set var="shippingCount" value="0" />
                <c:set var="completedCount" value="0" />
                <c:set var="canceledCount" value="0" />
                <c:forEach var="order" items="${orders}">
                    <c:choose>
                        <c:when test="${order.orderStatus eq 'Đang chờ'}">
                            <c:set var="pendingCount" value="${pendingCount + 1}" />
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đã xác nhận'}">
                            <c:set var="confirmedCount" value="${confirmedCount + 1}" />
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đang giao hàng' || order.orderStatus eq 'Đang giao'}">
                            <c:set var="shippingCount" value="${shippingCount + 1}" />
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đã giao hàng' || order.orderStatus eq 'Đã giao'}">
                            <c:set var="completedCount" value="${completedCount + 1}" />
                        </c:when>
                        <c:when test="${order.orderStatus eq 'Đã hủy'}">
                            <c:set var="canceledCount" value="${canceledCount + 1}" />
                        </c:when>
                    </c:choose>
                </c:forEach>
                
                <div class="row mb-4 g-3">
                    <!-- Chờ xác nhận - Vàng -->
                    <div class="col">
                        <div class="card text-center border-0 shadow-sm">
                            <div class="card-body">
                                <h5 class="text-warning fw-bold mb-1">${pendingCount}</h5>
                                <small class="text-muted">Chờ xác nhận</small>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Đã xác nhận - Xanh lá nhạt -->
                    <div class="col">
                        <div class="card text-center border-0 shadow-sm">
                            <div class="card-body">
                                <h5 class="fw-bold mb-1" style="color: #198754;">${confirmedCount}</h5>
                                <small class="text-muted">Đã xác nhận</small>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Đang giao - Xanh dương -->
                    <div class="col">
                        <div class="card text-center border-0 shadow-sm">
                            <div class="card-body">
                                <h5 class="text-info fw-bold mb-1">${shippingCount}</h5>
                                <small class="text-muted">Đang giao</small>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Hoàn thành - Xanh dương đậm -->
                    <div class="col">
                        <div class="card text-center border-0 shadow-sm">
                            <div class="card-body">
                                <h5 class="text-primary fw-bold mb-1">${completedCount}</h5>
                                <small class="text-muted">Hoàn thành</small>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Đã hủy - Đỏ -->
                    <div class="col">
                        <div class="card text-center border-0 shadow-sm">
                            <div class="card-body">
                                <h5 class="text-danger fw-bold mb-1">${canceledCount}</h5>
                                <small class="text-muted">Đã hủy</small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Order List -->
                <c:choose>
                    <c:when test="${empty orders}">
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
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="order" items="${orders}">
                            <div class="card mb-3 shadow-sm">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <div>
                                            <h6 class="mb-1">
                                                <i class="fas fa-receipt me-2"></i>Đơn hàng #${order.orderID}
                                            </h6>
                                            <small class="text-muted">
                                                <i class="fas fa-clock me-1"></i>
                                                ${order.orderDate.format(dateFormatter)}
                                            </small>
                                        </div>
                                        <div class="d-flex align-items-center gap-2">
                                            <c:choose>
                                                <c:when test="${order.orderStatus eq 'Đang chờ'}">
                                                    <span class="badge bg-warning text-dark" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                                        <i class="fas fa-clock me-1"></i>Chờ xác nhận
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus eq 'Đã xác nhận'}">
                                                    <span class="badge bg-success" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                                        <i class="fas fa-check me-1"></i>Đã xác nhận
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus eq 'Đang giao hàng' || order.orderStatus eq 'Đang giao'}">
                                                    <span class="badge bg-info" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                                        <i class="fas fa-shipping-fast me-1"></i>Đang giao
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus eq 'Đã giao hàng' || order.orderStatus eq 'Đã giao'}">
                                                    <span class="badge bg-primary" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                                        <i class="fas fa-check-circle me-1"></i>Hoàn thành
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.orderStatus eq 'Đã hủy'}">
                                                    <span class="badge bg-danger" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                                        <i class="fas fa-times-circle me-1"></i>Đã hủy
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                                        ${order.orderStatus}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                            
                                            <!-- Nút đánh giá cho đơn hàng đã hoàn thành -->
                                            <c:if test="${order.orderStatus eq 'Đã giao hàng' || order.orderStatus eq 'Đã giao'}">
                                                <button class="btn btn-sm btn-outline-warning shadow-sm" 
                                                        onclick="openReviewModal(${order.orderID})"
                                                        title="Đánh giá sản phẩm"
                                                        style="font-weight: 500;">
                                                    <i class="fas fa-star me-1"></i>Đánh giá
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                    
                                    <!-- Bố cục mới: Sản phẩm bên trái, Thông tin bên phải -->
                                    <div class="border-top pt-3">
                                        <div class="row">
                                            <!-- Danh sách sản phẩm - Bên trái -->
                                            <div class="col-md-7">
                                                <h6 class="mb-3"><i class="fas fa-box-open me-2"></i>Sản phẩm</h6>
                                                <div class="products-list" style="max-height: 300px; overflow-y: auto;">
                                                    <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                                                        <div class="d-flex align-items-center mb-3 p-2 border rounded">
                                                            <c:choose>
                                                                <c:when test="${not empty detail.product.images and not empty detail.product.images[0]}">
                                                                    <img src="${pageContext.request.contextPath}/image?fname=${detail.product.images[0].dirImage}"
                                                                         alt="${detail.product.productName}"
                                                                         style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;"
                                                                         class="me-3">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                                                                         alt="No image"
                                                                         style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;"
                                                                         class="me-3">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <div class="flex-grow-1">
                                                                <div class="fw-semibold">${detail.product.productName}</div>
                                                                <small class="text-muted">
                                                                    <fmt:formatNumber value="${detail.unitPrice}" type="number" maxFractionDigits="0" />₫ x ${detail.quantity}
                                                                </small>
                                                                <div class="text-primary fw-bold mt-1">
                                                                    <fmt:formatNumber value="${detail.unitPrice * detail.quantity}" type="number" maxFractionDigits="0" />₫
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                            
                                            <!-- Thông tin đơn hàng - Bên phải -->
                                            <div class="col-md-5">
                                                <div class="border rounded p-3 bg-light">
                                                    <h6 class="mb-3 fw-bold"><i class="fas fa-info-circle me-2"></i>Thông tin đơn hàng</h6>
                                                    
                                                    <div class="mb-2">
                                                        <small class="text-muted d-block">Người nhận</small>
                                                        <div class="fw-semibold">
                                                            <i class="fas fa-user me-1 text-primary"></i>${order.recipientName}
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="mb-2">
                                                        <small class="text-muted d-block">Số điện thoại</small>
                                                        <div class="fw-semibold">
                                                            <i class="fas fa-phone me-1 text-success"></i>${order.phoneNumber}
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="mb-2">
                                                        <small class="text-muted d-block">Địa chỉ giao hàng</small>
                                                        <div class="fw-semibold">
                                                            <i class="fas fa-map-marker-alt me-1 text-danger"></i>
                                                            ${order.address.addressDetail}, ${order.address.ward}, ${order.address.district}, ${order.address.province}
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="mb-2">
                                                        <small class="text-muted d-block">Phương thức thanh toán</small>
                                                        <div class="fw-semibold">
                                                            <i class="fas fa-credit-card me-1 text-info"></i>${order.paymentMethod.name}
                                                        </div>
                                                    </div>
                                                    
                                                    <hr class="my-3">
                                                    
                                                    <div class="d-flex justify-content-between mb-1">
                                                        <span>Tạm tính:</span>
                                                        <span><fmt:formatNumber value="${order.amount}" type="number" maxFractionDigits="0" />₫</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-1">
                                                        <span>Phí vận chuyển:</span>
                                                        <span><fmt:formatNumber value="${order.shippingFee}" type="number" maxFractionDigits="0" />₫</span>
                                                    </div>
                                                    <c:if test="${order.totalDiscount > 0}">
                                                        <div class="d-flex justify-content-between mb-1 text-success">
                                                            <span>Giảm giá:</span>
                                                            <span>-<fmt:formatNumber value="${order.totalDiscount}" type="number" maxFractionDigits="0" />₫</span>
                                                        </div>
                                                    </c:if>
                                                    
                                                    <hr class="my-2">
                                                    
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <span class="fw-bold fs-5">Tổng cộng:</span>
                                                        <span class="text-danger fw-bold fs-4">
                                                            <fmt:formatNumber value="${order.amount + order.shippingFee - order.totalDiscount}" type="number" maxFractionDigits="0" />₫
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
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

<!-- Review Modal -->
<div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
                <h5 class="modal-title" id="reviewModalLabel">
                    <i class="fas fa-star me-2"></i>Đánh giá sản phẩm
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="reviewProductsList">
                    <!-- Danh sách sản phẩm sẽ được load ở đây -->
                    <div class="text-center py-4">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                        <p class="mt-2 text-muted">Đang tải sản phẩm...</p>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="fas fa-times me-1"></i>Đóng
                </button>
                <button type="button" class="btn btn-primary" onclick="submitReviews()">
                    <i class="fas fa-paper-plane me-1"></i>Gửi đánh giá
                </button>
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

    /* Products List Scrollbar */
    .products-list {
        scrollbar-width: thin;
        scrollbar-color: #007bff #f0f0f0;
    }
    .products-list::-webkit-scrollbar {
        width: 6px;
    }
    .products-list::-webkit-scrollbar-track {
        background: #f0f0f0;
        border-radius: 10px;
    }
    .products-list::-webkit-scrollbar-thumb {
        background: #007bff;
        border-radius: 10px;
    }
    .products-list::-webkit-scrollbar-thumb:hover {
        background: #0056b3;
    }

    /* Product Item Hover Effect */
    .products-list .d-flex.border {
        transition: all 0.3s ease;
    }
    .products-list .d-flex.border:hover {
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        transform: translateX(5px);
        border-color: #007bff !important;
    }

    /* Review Modal */
    .review-product-item {
        border: 1px solid #dee2e6;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 15px;
        transition: all 0.3s ease;
    }
    .review-product-item:hover {
        box-shadow: 0 3px 10px rgba(0,0,0,0.1);
    }
    .star-rating {
        display: flex;
        gap: 5px;
        font-size: 1.5rem;
    }
    .star-rating i {
        cursor: pointer;
        color: #ddd;
        transition: color 0.2s;
    }
    .star-rating i.active,
    .star-rating i:hover {
        color: #ffc107;
    }
    .review-textarea {
        resize: vertical;
        min-height: 80px;
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
    
    // Attach event listeners to edit/delete buttons
    document.addEventListener('click', function(e) {
        // Edit button
        if (e.target.closest('.edit-addr-btn')) {
            const btn = e.target.closest('.edit-addr-btn');
            editAddress(
                btn.dataset.id,
                btn.dataset.name,
                btn.dataset.phone,
                btn.dataset.province,
                btn.dataset.district,
                btn.dataset.ward,
                btn.dataset.detail,
                btn.dataset.default === 'true'
            );
        }
        // Delete button
        if (e.target.closest('.delete-addr-btn')) {
            const btn = e.target.closest('.delete-addr-btn');
            deleteAddress(btn.dataset.id);
        }
    });
    
    // Hàm load danh sách địa chỉ từ API (không cần reload trang)
    async function loadAddresses() {
        console.log('Loading addresses from API...');
        
        try {
            const token = localStorage.getItem('authToken');
            const response = await fetch('${pageContext.request.contextPath}/api/user/addresses', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            });
            
            const result = await response.json();
            
            if (result.success && result.data) {
                console.log('Loaded addresses:', result.data.length);
                renderAddresses(result.data);
            } else {
                console.error('Failed to load addresses:', result.error);
            }
        } catch (error) {
            console.error('Error loading addresses:', error);
        }
    }
    
    // Hàm render danh sách địa chỉ
    function renderAddresses(addresses) {
        const addressListDiv = document.getElementById('addressList');
        
        if (!addresses || addresses.length === 0) {
            addressListDiv.innerHTML = 
                '<div class="card text-center py-5">' +
                    '<div class="card-body">' +
                        '<i class="fas fa-map-marker-alt fa-3x text-muted mb-3"></i>' +
                        '<h5 class="text-muted">Chưa có địa chỉ nào</h5>' +
                        '<p class="text-muted">Thêm địa chỉ giao hàng để đặt hàng nhanh chóng hơn!</p>' +
                        '<button class="btn btn-primary" onclick="document.getElementById(\'addAddressBtn\').click()">' +
                            '<i class="fas fa-plus me-1"></i> Thêm địa chỉ đầu tiên' +
                        '</button>' +
                    '</div>' +
                '</div>';
            return;
        }
        
        let html = '';
        addresses.forEach(function(addr) {
            const isDefault = addr.isDefault === true;
            const borderClass = isDefault ? 'border-primary' : '';
            const defaultBadge = isDefault ? '<span class="badge bg-primary">Mặc định</span>' : '';
            
            html += '<div class="card mb-3 address-card ' + borderClass + '">' +
                '<div class="card-body">' +
                    '<div class="d-flex justify-content-between align-items-start">' +
                        '<div class="flex-grow-1">' +
                            '<div class="d-flex align-items-center gap-2 mb-2">' +
                                '<h6 class="fw-bold text-primary mb-0">' +
                                    '<i class="fas fa-user me-2"></i>' + escapeHtml(addr.name || '') +
                                '</h6>' +
                                defaultBadge +
                            '</div>' +
                            '<p class="text-muted mb-1">' +
                                '<i class="fas fa-phone me-2 text-secondary"></i>' + escapeHtml(addr.phone || '') +
                            '</p>' +
                            '<p class="text-muted mb-1">' +
                                '<i class="fas fa-location-arrow me-2 text-secondary"></i>' +
                                escapeHtml(addr.addressDetail || '') +
                            '</p>' +
                            '<p class="text-muted mb-0">' +
                                '<i class="fas fa-map-marked me-2 text-secondary"></i>' +
                                escapeHtml(addr.ward || '') + ', ' + escapeHtml(addr.district || '') + ', ' + escapeHtml(addr.province || '') +
                            '</p>' +
                        '</div>' +
                        '<div class="d-flex flex-column gap-1">' +
                            '<button class="btn btn-sm btn-outline-primary" ' +
                                    'onclick="editAddress(' + addr.addressID + ', \'' + escapeHtml(addr.name || '') + '\', \'' + escapeHtml(addr.phone || '') + '\', \'' + escapeHtml(addr.province || '') + '\', \'' + escapeHtml(addr.district || '') + '\', \'' + escapeHtml(addr.ward || '') + '\', \'' + escapeHtml(addr.addressDetail || '') + '\', ' + isDefault + ')" ' +
                                    'title="Sửa">' +
                                '<i class="fas fa-edit"></i>' +
                            '</button>';
            
            if (!isDefault) {
                html += '<button class="btn btn-sm btn-outline-danger" ' +
                            'onclick="deleteAddress(' + addr.addressID + ')" ' +
                            'title="Xóa">' +
                            '<i class="fas fa-trash"></i>' +
                        '</button>';
            }
            
            html += '</div>' +
                    '</div>' +
                '</div>' +
            '</div>';
        });
        
        addressListDiv.innerHTML = html;
    }
    
    // Helper function để escape HTML
    function escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    // Hàm sửa địa chỉ
    window.editAddress = function(addressId, name, phone, province, district, ward, addressDetail, isDefault) {
        console.log('Editing address:', addressId, {name, phone, province});
        
        try {
            // Điền dữ liệu vào form
            document.getElementById('formTitle').textContent = 'Chỉnh sửa địa chỉ';
            document.getElementById('addressId').value = addressId || '';
            document.getElementById('name').value = name || '';
            document.getElementById('inputPhone').value = phone || '';
            document.getElementById('province').value = province || '';
            document.getElementById('district').value = district || '';
            document.getElementById('ward').value = ward || '';
            document.getElementById('addressDetail').value = addressDetail || '';
            document.getElementById('isDefault').checked = isDefault === true || isDefault === 'true';
            
            // Hiển thị form
            document.getElementById('addressListContainer').style.display = 'none';
            document.getElementById('addressFormContainer').style.display = 'block';
            
            // Scroll to form
            document.getElementById('addressFormContainer').scrollIntoView({ behavior: 'smooth', block: 'start' });
        } catch (error) {
            console.error('Error in editAddress:', error);
            alert('Có lỗi khi mở form sửa địa chỉ!');
        }
    };
    
    // Hàm xóa địa chỉ (AJAX)
    window.deleteAddress = async function(addressId) {
        console.log('Deleting address:', addressId);
        
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
                console.log('Address deleted successfully');
                showToast(result.message || 'Xóa địa chỉ thành công!', 'success');
                
                // Reload danh sách địa chỉ không cần reload trang
                await loadAddresses();
            } else {
                alert('Lỗi: ' + result.error);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi xóa địa chỉ!');
        }
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
        // Remove any existing listeners to prevent duplicates
        addressForm.replaceWith(addressForm.cloneNode(true));
        const newAddressForm = document.getElementById('addressForm');
        
        newAddressForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            // Disable submit button to prevent double submission
            const submitBtn = e.target.querySelector('button[type="submit"]');
            const originalBtnText = submitBtn.innerHTML;
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i> Đang xử lý...';
            
            const addressId = document.getElementById('addressId').value;
            const isEdit = addressId && addressId.trim() !== '';
            
            // Collect form data
            const formData = (function() {
                const name = document.getElementById('name').value || '';
                const phone = document.getElementById('inputPhone').value || '';
                // Keep original phone string but also provide digits-only for stricter systems
                // const phone = phoneRaw.trim();
                const province = document.getElementById('province').value || '';
                const district = document.getElementById('district').value || '';
                const ward = document.getElementById('ward').value || '';
                const addressDetail = document.getElementById('addressDetail').value || '';
                const isDefault = !!document.getElementById('isDefault').checked;

                // Debug: log payload to console before sending
                const payload = { name, phone, province, district, ward, addressDetail, isDefault };
                console.log('Sending address payload:', payload);
                return payload;
            })();
             
             console.log('=== FORM SUBMIT DEBUG ===');
             console.log('Is Edit:', isEdit);
             console.log('Form Data:', formData);
             console.log('Phone value:', formData.phone);
             console.log('Phone length:', formData.phone?.length);
             console.log('=========================');
            
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
                    // Success - không reload, chỉ cập nhật danh sách
                    console.log('Address saved successfully:', result.data);
                    showToast(result.message || 'Lưu địa chỉ thành công!', 'success');
                    
                    // Reset form
                    newAddressForm.reset();
                    document.getElementById('addressId').value = '';
                    
                    // Hide form, show list
                    document.getElementById('addressFormContainer').style.display = 'none';
                    document.getElementById('addressListContainer').style.display = 'block';
                    
                    // Reload danh sách địa chỉ không cần reload trang
                    await loadAddresses();
                    
                    // Re-enable button
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = originalBtnText;
                } else {
                    alert('Lỗi: ' + result.error);
                    // Re-enable button on error
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = originalBtnText;
                }
                
            } catch (error) {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi lưu địa chỉ!');
                // Re-enable button on error
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalBtnText;
            }
        });
    }
    
    // Hiển thị form thêm địa chỉ - Use onclick to prevent duplicate listeners
    const addAddressBtn = document.getElementById('addAddressBtn');
    if (addAddressBtn) {
        // Remove old listener by cloning
        const newAddBtn = addAddressBtn.cloneNode(true);
        addAddressBtn.parentNode.replaceChild(newAddBtn, addAddressBtn);
        
        newAddBtn.addEventListener('click', () => {
            console.log('Opening add address form');
            // Reset form completely
            const form = document.getElementById('addressForm');
            if (form) {
                form.reset();
                document.getElementById('addressId').value = '';
                document.getElementById('formTitle').textContent = 'Thêm địa chỉ mới';
                
                // Clear all inputs explicitly
                document.getElementById('name').value = '';
                document.getElementById('inputPhone').value = '';
                document.getElementById('province').value = '';
                document.getElementById('district').value = '';
                document.getElementById('ward').value = '';
                document.getElementById('addressDetail').value = '';
                document.getElementById('isDefault').checked = false;
                
                // Show form
                document.getElementById('addressListContainer').style.display = 'none';
                document.getElementById('addressFormContainer').style.display = 'block';
            }
        });
    }

    // Nút hủy - quay lại danh sách địa chỉ
    const cancelBtn = document.getElementById('cancelAddressBtn');
    if (cancelBtn) {
        // Remove old listener by cloning
        const newCancelBtn = cancelBtn.cloneNode(true);
        cancelBtn.parentNode.replaceChild(newCancelBtn, cancelBtn);
        
        newCancelBtn.addEventListener('click', () => {
            console.log('Canceling address form');
            // Reset form
            const form = document.getElementById('addressForm');
            if (form) {
                form.reset();
                document.getElementById('addressId').value = '';
            }
            
            // Hide form, show list
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
    
    // ==================== PHONE VALIDATION (REAL-TIME) ====================
    
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function(e) {
            const value = e.target.value;
            
            // Remove non-digits for validation (but keep original value)
            const digitsOnly = value.replace(/\D/g, '');
            
            console.log('Phone input changed:', value, '| Digits:', digitsOnly);
            
            // Visual feedback
            if (digitsOnly.length >= 10 && digitsOnly.length <= 15) {
                phoneInput.classList.remove('is-invalid');
                phoneInput.classList.add('is-valid');
            } else if (digitsOnly.length > 0) {
                phoneInput.classList.remove('is-valid');
                phoneInput.classList.add('is-invalid');
            } else {
                phoneInput.classList.remove('is-valid', 'is-invalid');
            }
        });
        
        // Validation on blur
        phoneInput.addEventListener('blur', function(e) {
            const value = e.target.value;
            const digitsOnly = value.replace(/\D/g, '');
            
            if (digitsOnly.length < 10) {
                console.warn('⚠️ Phone too short:', digitsOnly.length, 'digits');
            } else if (digitsOnly.length > 15) {
                console.warn('⚠️ Phone too long:', digitsOnly.length, 'digits');
            } else {
                console.log('✅ Phone valid:', digitsOnly);
            }
        });
    }
    
    console.log('✅ Phone validation initialized');

    // ==================== REVIEW FUNCTIONALITY ====================
    
    window.currentOrderId = null;
    window.reviewData = [];
    
    // Mở modal đánh giá
    window.openReviewModal = async function(orderId) {
        console.log('Opening review modal for order:', orderId);
        window.currentOrderId = orderId;
        
        // Reset review data
        window.reviewData = [];
        
        // Show modal
        const modal = new bootstrap.Modal(document.getElementById('reviewModal'));
        modal.show();
        
        // Load products from order
        await loadOrderProducts(orderId);
    };
    
    // Load danh sách sản phẩm từ đơn hàng
    async function loadOrderProducts(orderId) {
        const container = document.getElementById('reviewProductsList');
        
        try {
            // Tìm đơn hàng trong danh sách orders hiện tại
            const orders = ${not empty orders ? '[' : '[]'}
            <c:forEach var="order" items="${orders}" varStatus="status">
                {
                    orderID: ${order.orderID},
                    orderDetails: [
                        <c:forEach var="detail" items="${order.orderDetails}" varStatus="detailStatus">
                        {
                            productId: ${detail.product.productID},
                            productName: '${detail.product.productName}'.replace(/'/g, "\\'"),
                            productImage: '${not empty detail.product.images and not empty detail.product.images[0] ? detail.product.images[0].dirImage : ""}',
                            quantity: ${detail.quantity}
                        }${!detailStatus.last ? ',' : ''}
                        </c:forEach>
                    ]
                }${!status.last ? ',' : ''}
            </c:forEach>
            ${not empty orders ? ']' : ''};
            
            const order = orders.find(o => o.orderID == orderId);
            
            if (!order || !order.orderDetails) {
                container.innerHTML = '<div class="alert alert-warning">Không tìm thấy sản phẩm trong đơn hàng này.</div>';
                return;
            }
            
            // Render products
            let html = '';
            order.orderDetails.forEach((product, index) => {
                html += `
                    <div class="review-product-item" data-product-id="${product.productId}">
                        <div class="d-flex align-items-start">
                            <img src="${product.productImage ? '${pageContext.request.contextPath}/image?fname=' + product.productImage : '${pageContext.request.contextPath}/assets/images/logo.png'}"
                                 alt="${product.productName}"
                                 style="width: 80px; height: 80px; object-fit: cover; border-radius: 8px;"
                                 class="me-3">
                            <div class="flex-grow-1">
                                <h6 class="mb-2">${product.productName}</h6>
                                
                                <div class="mb-2">
                                    <label class="form-label fw-semibold small mb-1">Đánh giá của bạn:</label>
                                    <div class="star-rating" data-product-id="${product.productId}">
                                        <i class="fas fa-star" data-rating="1"></i>
                                        <i class="fas fa-star" data-rating="2"></i>
                                        <i class="fas fa-star" data-rating="3"></i>
                                        <i class="fas fa-star" data-rating="4"></i>
                                        <i class="fas fa-star" data-rating="5"></i>
                                    </div>
                                </div>
                                
                                <div>
                                    <label class="form-label fw-semibold small mb-1">Nhận xét:</label>
                                    <textarea class="form-control review-textarea" 
                                              data-product-id="${product.productId}"
                                              placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm này..."></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
            });
            
            container.innerHTML = html;
            
            // Attach star rating event listeners
            document.querySelectorAll('.star-rating').forEach(ratingDiv => {
                const stars = ratingDiv.querySelectorAll('i');
                stars.forEach(star => {
                    star.addEventListener('click', function() {
                        const rating = parseInt(this.dataset.rating);
                        const productId = ratingDiv.dataset.productId;
                        
                        // Update visual
                        stars.forEach((s, idx) => {
                            if (idx < rating) {
                                s.classList.add('active');
                            } else {
                                s.classList.remove('active');
                            }
                        });
                        
                        // Store rating
                        const existingReview = window.reviewData.find(r => r.productId == productId);
                        if (existingReview) {
                            existingReview.rating = rating;
                        } else {
                            window.reviewData.push({
                                productId: productId,
                                rating: rating,
                                comment: ''
                            });
                        }
                    });
                    
                    // Hover effect
                    star.addEventListener('mouseenter', function() {
                        const rating = parseInt(this.dataset.rating);
                        stars.forEach((s, idx) => {
                            if (idx < rating) {
                                s.style.color = '#ffc107';
                            }
                        });
                    });
                    
                    star.addEventListener('mouseleave', function() {
                        stars.forEach(s => {
                            if (!s.classList.contains('active')) {
                                s.style.color = '#ddd';
                            }
                        });
                    });
                });
            });
            
        } catch (error) {
            console.error('Error loading order products:', error);
            container.innerHTML = '<div class="alert alert-danger">Có lỗi xảy ra khi tải sản phẩm.</div>';
        }
    }
    
    // Gửi đánh giá
    window.submitReviews = async function() {
        if (!window.currentOrderId) {
            alert('Không tìm thấy thông tin đơn hàng!');
            return;
        }
        
        // Collect all reviews
        const reviews = [];
        document.querySelectorAll('.review-product-item').forEach(item => {
            const productId = item.dataset.productId;
            const ratingDiv = item.querySelector('.star-rating');
            const activeStars = ratingDiv.querySelectorAll('i.active').length;
            const textarea = item.querySelector('textarea');
            const comment = textarea.value.trim();
            
            if (activeStars > 0) {
                reviews.push({
                    productId: parseInt(productId),
                    orderId: window.currentOrderId,
                    rating: activeStars,
                    comment: comment
                });
            }
        });
        
        if (reviews.length === 0) {
            alert('Vui lòng đánh giá ít nhất một sản phẩm!');
            return;
        }
        
        try {
            // Send reviews to server
            const response = await fetch('${pageContext.request.contextPath}/review/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reviews)
            });
            
            const result = await response.json();
            
            if (result.success) {
                showToast('Cảm ơn bạn đã đánh giá!', 'success');
                // Close modal
                bootstrap.Modal.getInstance(document.getElementById('reviewModal')).hide();
                // Reset data
                window.currentOrderId = null;
                window.reviewData = [];
            } else {
                alert('Lỗi: ' + (result.error || 'Không thể gửi đánh giá'));
            }
        } catch (error) {
            console.error('Error submitting reviews:', error);
            alert('Có lỗi xảy ra khi gửi đánh giá!');
        }
    };

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