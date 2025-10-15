<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Danh mục sản phẩm</title>
</head>

<body class="bg-light py-5">

	<div class="container">



		<!-- ---------------------------------------------Begin Slide -->
		<!-- ===== HERO SLIDE ===== -->
		<div id="coolmateCarousel" class="carousel slide"
			data-bs-ride="carousel">

			<!-- Nút chấm chỉ báo (dưới cùng) -->
			<div class="carousel-indicators">
				<button type="button" data-bs-target="#coolmateCarousel"
					data-bs-slide-to="0" class="active" aria-current="true"
					aria-label="Slide 1"></button>
				<button type="button" data-bs-target="#coolmateCarousel"
					data-bs-slide-to="1" aria-label="Slide 2"></button>
				<button type="button" data-bs-target="#coolmateCarousel"
					data-bs-slide-to="2" aria-label="Slide 3"></button>
			</div>

			<!-- Slide nội dung -->
			<div class="carousel-inner">

				<!-- Slide 1 -->
				<c:forEach var="banner" items="${listBanner}">
				<div class="carousel-item active">
					<c:url value="/image?fname=${banner.bannerImage}" var="imgUrl"></c:url>
					<img src="${imgUrl}" class="d-block w-100" alt="Coolmate banner 1">
				</div>
				</c:forEach>

			</div>

			<!-- Nút điều hướng -->
			<button class="carousel-control-prev" type="button"
				data-bs-target="#coolmateCarousel" data-bs-slide="prev">
				<span class="carousel-control-prev-icon" aria-hidden="true"></span>
				<span class="visually-hidden">Trước</span>
			</button>
			<button class="carousel-control-next" type="button"
				data-bs-target="#coolmateCarousel" data-bs-slide="next">
				<span class="carousel-control-next-icon" aria-hidden="true"></span>
				<span class="visually-hidden">Sau</span>
			</button>
		</div>

		<!-------begin Category -->

		<div class="row g-4 text-center py-5">
			<div class="col-12 mb-4 text-start">
				<button type="button"
					class="btn rounded-pill px-4 fw-bold text-white"
					style="background-color: black;">Phân loại</button>
			</div>
			<!-- Lặp qua danh sách listCate -->
			<c:forEach var="cate" items="${listCate}">
				<div class="col-6 col-md-4 col-lg-2">
					<div
						class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white h-100">
						<c:url value="/image?fname=${cate.image}" var="imgUrl"></c:url>
						<%-- <td><img height="150" width="200" src="${imgUrl}" alt="${book.title}" /></td> --%>
						<a href="/UTESHOP/filter?category=${cate.categoryID}"
							class="text-decoration-none"> <img src="${imgUrl}"
							class="card-img-top rounded-4" alt="${cate.categoryName}"
							style="height: 300px; object-fit: cover;">
							<div class="card-body p-2">
								<h6 class="text-dark text-uppercase">${cate.categoryName}</h6>
							</div>
						</a>
					</div>
				</div>
			</c:forEach>
		</div>

		<!-- End Category -->

		<!-- Begin Sản phẩm bán chạy -->

		<div class="row g-4 text-center py-5">
			<div class="row align-items-center">
				<!-- Cột trái -->

				<div class="col">
					<h3 class="text-start mb-0">SẢN PHẨM BÁN CHẠY</h3>
				</div>

				<!-- Cột phải -->
				<div class="col-auto">
					<a href="#" class="text-decoration-none">
						<p
							class="text-end text-dark mb-0 fw-semibold text-decoration-underline">Xem
							thêm</p>
					</a>
				</div>
			</div>
			<!-- CHỉ nên hiện thị 6 cái sau đó muôn xem thêm thì ấn xem thêm -->
			<c:forEach var="p" items="${listBestSell}">
				<div class="col-6 col-md-4 col-lg-2">
					<div
						class="card border-0 shadow-sm rou3nded-4 overflow-hidden bg-white h-100">

						<c:choose>
							<c:when test="${not empty p.images}">
								<c:url value="/image?fname=${p.images[0].dirImage}" var="imgUrl"></c:url>
								<a href="#" class="text-decoration-none"> <img
									src="${imgUrl}" alt="${p.productName}"
									class="card-img-top rounded-4"
									style="height: 300px; object-fit: cover;">
									<div class="card-body p-2">
										<h6 class="text-dark text-uppercase">${p.productName}</h6>
										<h6 class="text-dark text-uppercase">${p.unitPrice}VND</h6>
									</div>
								</a>
							</c:when>
							<c:otherwise>
								<c:url value="/image?fname=logo.png" var="imgUrl"></c:url>
								<a href="#" class="text-decoration-none"> <img
									src="${imgUrl}" alt="${p.productName}"
									class="card-img-top rounded-4"
									style="height: 300px; object-fit: cover;">
									<div class="card-body p-2">
										<h6 class="text-dark text-uppercase">${p.productName}</h6>
										<h6 class="text-dark text-uppercase">${p.unitPrice}VND</h6>
									</div>
								</a>
							</c:otherwise>
						</c:choose>



					</div>
				</div>
<<<<<<< HEAD
			</c:forEach>



=======
			</div>
>>>>>>> origin/trung
		</div>

		<!-- Begin Sản phẩm Tieu bieu -->

		<div class="row g-4 text-center py-5">
			<div class="row align-items-center">
				<!-- Cột trái -->
				<div class="col">
					<h3 class="text-start mb-0">Sản phẩm tieu bieu</h3>
				</div>

				<!-- Cột phải -->
				<div class="col-auto">
					<a href="#" class="text-decoration-none">
						<p
							class="text-end text-dark mb-0 fw-semibold text-decoration-underline">Xem
							thêm</p>
					</a>
				</div>
			</div>

			<!-- CHỉ nên hiện thị 6 cái sau đó muôn xem thêm thì ấn xem thêm -->
			<div class="col-6 col-md-4 col-lg-2">
				<div
					class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white h-100">
					<a href="#"><img src="logo.png" class="card-img-top rounded-4"
						alt="Áo thun" style="height: 400;">
						<div class="card-body p-2">
							<h6 class="card-title text-uppercase fw-semibold mb-0">Áo
								thun</h6>
						</div> </a>

				</div>
			</div>

		</div>

		<!-- --san pham tieu bieu tuy bien them hoac xoa -->

		<div class="row g-4 text-center py-1">
			<a><img src="image.png" class="img-fluid py-5" alt="banner"></a>

			<!-- CHỉ nên hiện thị 6 cái sau đó muôn xem thêm thì ấn xem thêm -->
			<div class="row align-items-center">
				<!-- Cột trái -->
				<div class="col">
					<h3 class="text-start mb-0">Sản phẩm tieu bieu</h3>
				</div>

				<!-- Cột phải -->
				<div class="col-auto">
					<a href="#" class="text-decoration-none">
						<p
							class="text-end text-dark mb-0 fw-semibold text-decoration-underline">Xem
							thêm</p>
					</a>
				</div>
			</div>

			<!-- CHỉ nên hiện thị 6 cái sau đó muôn xem thêm thì ấn xem thêm -->
			<div class="col-6 col-md-4 col-lg-2">
				<div
					class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white h-100">
					<a href="#"><img src="logo.png" class="card-img-top rounded-4"
						alt="Áo thun" style="height: 400;">
						<div class="card-body p-2">
							<h6 class="card-title text-uppercase fw-semibold mb-0">Áo
								thun</h6>
						</div> </a>

				</div>
			</div>

		</div>
	</div>
</body>

</html>