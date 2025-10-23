<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/auth/login.jsp" />
<jsp:include page="/WEB-INF/views/auth/register.jsp" />
<jsp:include page="/WEB-INF/views/auth/forgot-password.jsp" />
<jsp:include page="/WEB-INF/views/auth/reset-password.jsp" />

	<!-- ====== HEADER ====== -->
	<header class="border-bottom bg-white py-3 shadow-sm"
		style="position: sticky; top: 0; z-index: 1030; transition: backdrop-filter 0.3s ease; backdrop-filter: blur(5px);">
		<div class="container">
			<div class="row align-items-center">
				<!-- Logo -->
				<div class="col-auto">
					<a href="${pageContext.request.contextPath}/" class="d-flex align-items-center me-3"> <img
							src="${URL}assets/images/logo.png" alt="UTESHOP" style="height: 40px;">
					</a>
				</div>

				<!-- Navbar -->
				<div class="col d-flex justify-content-center">
					<nav class="nav main-nav">
						<a class="nav-link fw-bold text-dark" href="#">Sản Phẩm</a> <a
							class="nav-link fw-bold text-danger" href="#">Đang Sale</a> <a
							class="nav-link fw-bold text-dark" href="#">Quần áo</a> <a
							class="nav-link fw-bold text-dark" href="#">Phụ kiện</a>
					</nav>
				</div>

				<!-- Search + Icons -->
				<div class="col-auto ms-auto">
					<div class="d-flex align-items-center gap-3">
						<form class="d-flex align-items-center bg-light rounded-pill px-2" style="max-width: 260px;"
							method="get" action="${pageContext.request.contextPath}/filter">
							<input type="text" name="keyword" value="${param.keyword}"
								class="form-control border-0 bg-light rounded-pill" placeholder="Tìm kiếm sản phẩm..."
								style="box-shadow: none;">
							<button class="btn btn-link text-dark px-2" type="submit" title="Tìm kiếm">
								<i class="bi bi-search fs-5"></i>
							</button>
						</form>

						<!-- User dropdown -->
						<c:choose>
							<c:when test="${empty sessionScope.currentUser}">
								<div class="dropdown">
									<a href="#" class="btn btn-light rounded-circle" id="userDropdown"
										data-bs-toggle="dropdown" aria-expanded="false"> <i
											class="bi bi-person-fill fs-4 text-primary"></i>
									</a>
									<ul class="dropdown-menu dropdown-menu-end text-center"
										aria-labelledby="userDropdown">
										<li><a class="dropdown-item" href="#" data-bs-toggle="modal"
												data-bs-target="#loginModal"> <i
													class="fa-solid fa-right-to-bracket me-1"></i> Đăng nhập
											</a></li>
										<li><a class="dropdown-item" href="#" data-bs-toggle="modal"
												data-bs-target="#registerModal"> <i
													class="fa-solid fa-user-plus me-1"></i> Đăng ký
											</a></li>
									</ul>
								</div>
							</c:when>
							<c:otherwise>
								<div class="dropdown">
									<a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle"
										id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
										<!-- Avatar hoặc icon -->
										<c:choose>
											<c:when test="${not empty sessionScope.currentUser.avatar}">
												<img src="${sessionScope.currentUser.avatar}" alt="Avatar" 
													class="rounded-circle me-2" style="width: 35px; height: 35px; object-fit: cover;">
											</c:when>
											<c:otherwise>
												<div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center me-2"
													style="width: 35px; height: 35px; font-weight: bold;">
													${sessionScope.currentUser.fullname.substring(0,1).toUpperCase()}
												</div>
											</c:otherwise>
										</c:choose>
										<!-- Tên người dùng -->
										<span class="fw-semibold text-dark">${sessionScope.currentUser.fullname}</span>
									</a>

									<ul class="dropdown-menu dropdown-menu-end shadow-sm" style="min-width: 220px;"
										aria-labelledby="userDropdown">
										
										<!-- Header với role badge -->
										<li class="px-3 py-2 border-bottom">
											<div class="d-flex align-items-center">
												<div class="flex-grow-1">
													<div class="fw-bold text-dark small">${sessionScope.currentUser.fullname}</div>
													<div class="text-muted" style="font-size: 0.75rem;">${sessionScope.currentUser.email}</div>
												</div>
												<c:choose>
													<c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
														<span class="badge bg-danger">Admin</span>
													</c:when>
													<c:when test="${sessionScope.currentUser.role == 'MANAGER'}">
														<span class="badge bg-warning text-dark">Manager</span>
													</c:when>
													<c:when test="${sessionScope.currentUser.role == 'SHIPPER'}">
														<span class="badge bg-info">Shipper</span>
													</c:when>
													<c:otherwise>
														<span class="badge bg-secondary">User</span>
													</c:otherwise>
												</c:choose>
											</div>
										</li>

										<!-- Menu items chung -->
										<li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile">
											<i class="fa-solid fa-user me-2 text-primary"></i> Hồ sơ của tôi
										</a></li>

										<!-- Menu theo role -->
										<c:choose>
											<%-- ADMIN Menu --%>
											<c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/home/dashboard">
													<i class="fa-solid fa-gauge-high me-2 text-danger"></i> Trang quản trị
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/users">
													<i class="fa-solid fa-users-gear me-2 text-danger"></i> Quản lý người dùng
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/products">
													<i class="fa-solid fa-box me-2 text-danger"></i> Quản lý sản phẩm
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/orders">
													<i class="fa-solid fa-cart-shopping me-2 text-danger"></i> Quản lý đơn hàng
												</a></li>
											</c:when>

											<%-- MANAGER Menu --%>
											<c:when test="${sessionScope.currentUser.role == 'MANAGER'}">
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/manager/dashboard">
													<i class="fa-solid fa-chart-line me-2 text-warning"></i> Trang quản lý
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/manager/products">
													<i class="fa-solid fa-box me-2 text-warning"></i> Quản lý sản phẩm
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/manager/orders">
													<i class="fa-solid fa-clipboard-list me-2 text-warning"></i> Quản lý đơn hàng
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/manager/reports">
													<i class="fa-solid fa-file-chart-line me-2 text-warning"></i> Báo cáo
												</a></li>
											</c:when>

											<%-- SHIPPER Menu --%>
											<c:when test="${sessionScope.currentUser.role == 'SHIPPER'}">
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/shipper/dashboard">
													<i class="fa-solid fa-truck-fast me-2 text-info"></i> Trang vận chuyển
												</a></li>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/shipper/history">
													<i class="fa-solid fa-clock-rotate-left me-2 text-info"></i> Lịch sử giao hàng
												</a></li>
											</c:when>

											<%-- USER Menu --%>
											<c:otherwise>
												<li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/favorites">
													<i class="fa-solid fa-heart me-2 text-success"></i> Yêu thích
												</a></li>
											</c:otherwise>
										</c:choose>

										<li>
											<hr class="dropdown-divider">
										</li>

										<!-- Logout -->
										<li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/auth/logout">
											<i class="fa-solid fa-right-from-bracket me-2"></i> Đăng xuất
										</a></li>
									</ul>
								</div>
							</c:otherwise>
						</c:choose>

						<!-- Cart -->
						<div class="position-relative">
							<a href="${pageContext.request.contextPath}/cart" class="btn btn-light rounded-circle"> <i
									class="bi bi-bag-fill fs-4 text-dark"></i>
							</a>
							<!-- cart count badge -->
							<span id="cartCountBadge" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
								<c:out value="${sessionScope.cartCount != null ? sessionScope.cartCount : 0}"/>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</header>

	<!-- ====== CSS tùy chỉnh ====== -->
	<style>
		/* Hiệu ứng highlight khi hover */
		.main-nav .nav-link {
			position: relative;
			margin: 0 12px;
			color: #212529;
			transition: color 0.3s ease;
		}

		.main-nav .nav-link:hover {
			color: #d32f2f;
			/* đỏ khi hover */
		}

		/* Gạch chân chạy mượt dưới link khi hover */
		.main-nav .nav-link::after {
			content: "";
			position: absolute;
			left: 0;
			bottom: -6px;
			width: 0;
			height: 2px;
			background-color: #d32f2f;
			transition: width 0.3s ease;
		}

		.main-nav .nav-link:hover::after {
			width: 100%;
		}

		/* Gạch chân giữ nguyên cho menu đang active */
		.main-nav .nav-link.active {
			color: #d32f2f;
		}

		.main-nav .nav-link.active::after {
			width: 100%;
		}

		/* Dropdown menu styling */
		.dropdown-menu {
			border: none;
			box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
			border-radius: 12px;
			padding: 0.5rem 0;
			animation: fadeInDown 0.3s ease-in-out;
		}

		@keyframes fadeInDown {
			from {
				opacity: 0;
				transform: translateY(-10px);
			}
			to {
				opacity: 1;
				transform: translateY(0);
			}
		}

		.dropdown-item {
			padding: 0.6rem 1.2rem;
			transition: all 0.2s ease;
			border-left: 3px solid transparent;
		}

		.dropdown-item:hover {
			background-color: #f8f9fa;
			border-left: 3px solid #0d6efd;
			padding-left: 1.5rem;
		}

		.dropdown-item i {
			width: 20px;
			text-align: center;
		}

		/* Avatar circle hover effect */
		.rounded-circle {
			transition: transform 0.2s ease;
		}

		.dropdown:hover .rounded-circle {
			transform: scale(1.05);
		}

		/* Badge styling */
		.badge {
			font-size: 0.7rem;
			padding: 0.35em 0.65em;
			font-weight: 600;
		}
	</style>

	<!-- ====== Hiệu ứng mờ khi cuộn ====== -->
	<script>
		window.addEventListener('scroll', function () {
			const header = document.querySelector('header');
			if (window.scrollY > 20) {
				header.style.backdropFilter = 'blur(10px)';
				header.style.boxShadow = '0 2px 10px rgba(0,0,0,0.15)';
			} else {
				header.style.backdropFilter = 'blur(5px)';
				header.style.boxShadow = '0 2px 5px rgba(0,0,0,0.05)';
			}
		});
	</script>