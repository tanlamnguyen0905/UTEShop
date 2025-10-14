<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- ====== HEADER ====== -->
<jsp:include page="/WEB-INF/views/auth/login.jsp" />
<jsp:include page="/WEB-INF/views/auth/register.jsp" />

<header class="border-bottom bg-white py-3">
	<div class="container">
		<div class="row align-items-center">
			<!-- Logo -->
			<div class="col-auto">
				<a href="#" class="d-flex align-items-center me-3"> <img
					src="${URL}assets/images/logo.png" alt="UTESHOP"
					style="height: 40px;">
				</a>
			</div>

			<!-- Navbar -->
			<div class="col d-flex justify-content-center">
				<nav class="nav">
					<a class="nav-link fw-bold text-dark" href="#">Sản Phẩm</a> <a
						class="nav-link fw-bold text-danger" href="#">Đang Sale</a> <a
						class="nav-link fw-bold text-dark" href="#">Quần áo</a> <a
						class="nav-link fw-bold text-dark" href="#">Phụ kiện</a>
					<!-- <a class="nav-link fw-bold text-danger" href="#">SALE <sup class="text-danger">-50%</sup></a>
                        <a class="nav-link fw-bold text-dark" href="#">C&S</a> -->
				</nav>
			</div>

			<!-- Search + Icons -->
			<div class="col-auto ms-auto">
				<div class="d-flex align-items-center gap-3">
					<form class="d-flex align-items-center bg-light rounded-pill px-2"
						style="max-width: 260px;">
						<input type="text"
							class="form-control border-0 bg-light rounded-pill"
							placeholder="Tìm kiếm..." style="box-shadow: none;">
						<button class="btn btn-link text-dark px-2" type="submit">
							<i class="bi bi-search fs-5"></i>
						</button>
					</form>

					<!-- Persion -->
					<!-- Dropdown người dùng -->
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
					<!-- Cart -->
					<div class="position-relative">
						<a href="#" class="btn btn-light rounded-circle"><i
							class="bi bi-bag-fill fs-4 text-dark"></i></a> <span
							class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">20</span>
					</div>
					<!-- end cart -->
				</div>
			</div>
		</div>
	</div>
</header>
