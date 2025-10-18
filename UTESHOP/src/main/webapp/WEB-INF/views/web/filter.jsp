<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Bộ lọc & Sản phẩm - Bootstrap</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>

<body class="bg-light py-5">

	<div class="container">
		<div class="row">
			<!-- ===== CỘT TRÁI: BỘ LỌC ===== -->
			<div class="col-md-3 mb-4">
				<div class="bg-white p-3 rounded shadow-sm">
					<div class="d-flex justify-content-between align-items-center mb-3">
						<h5 class="mb-0">Bộ lọc</h5>
						<span class="text-muted small">78 kết quả</span>
					</div>

					<!-- Accordion Bootstrap cho bộ lọc -->
					<div class="accordion" id="filterAccordion">
						<!-- Phù hợp với -->
						<div class="accordion-item">
							<h6 class="accordion-header" id="headingFit">
								<button class="accordion-button collapsed" type="button"
									data-bs-toggle="collapse" data-bs-target="#collapseFit"
									aria-expanded="false" aria-controls="collapseFit">Phù
									hợp với</button>
							</h6>
							<div id="collapseFit" class="accordion-collapse collapse show"
								aria-labelledby="headingFit" data-bs-parent="#filterAccordion">
								<div class="accordion-body">
									<div class="form-check">
										<input class="form-check-input" type="radio" name="fit"
											id="fit1"> <label class="form-check-label" for="fit1">Mặc
											hàng ngày</label>
									</div>
									<div class="form-check">
										<input class="form-check-input" type="radio" name="fit"
											id="fit2"> <label class="form-check-label" for="fit2">Mặc
											ở nhà</label>
									</div>
									<div class="form-check">
										<input class="form-check-input" type="radio" name="fit"
											id="fit3"> <label class="form-check-label" for="fit3">Thể
											thao</label>
									</div>
								</div>
							</div>
						</div>

						<!-- Kích cỡ -->
						<div class="accordion-item">
							<h6 class="accordion-header" id="headingSize">
								<button class="accordion-button collapsed" type="button"
									data-bs-toggle="collapse" data-bs-target="#collapseSize"
									aria-expanded="false" aria-controls="collapseSize">
									Kích cỡ</button>
							</h6>
							<div id="collapseSize" class="accordion-collapse collapse show"
								aria-labelledby="headingSize" data-bs-parent="#filterAccordion">
								<div class="form-check">
									<input class="form-check-input" type="checkbox" value=""
										id="checkDefault"> <label class="form-check-label"
										for="checkDefault"> Default checkbox </label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="checkbox" value=""
										id="checkChecked" checked> <label
										class="form-check-label" for="checkChecked"> Checked
										checkbox </label>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- ===== CỘT PHẢI: SẢN PHẨM ===== -->
			<div class="col-md-9">
				<!-- Thanh sắp xếp -->
				<div class="d-flex justify-content-end align-items-center mb-3">
					<label class="me-2 fw-semibold">Sắp xếp theo:</label>
					<div class="dropdown">
						<button class="btn btn-outline-secondary dropdown-toggle"
							type="button" data-bs-toggle="dropdown" aria-expanded="false">
							Bán chạy</button>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="#">Bán chạy</a></li>
							<li><a class="dropdown-item" href="#">Giá tăng dần</a></li>
							<li><a class="dropdown-item" href="#">Giá giảm dần</a></li>
							<li><a class="dropdown-item" href="#">Mới nhất</a></li>
						</ul>
					</div>
				</div>

				<!-- Lưới sản phẩm -->
				<div class="row g-4">

					<div class="row align-items-center py-5">
						<c:forEach var="p" items="${listPro}">
							<div class="col-6 col-md-4 col-lg-2">
								<div
									class="card border-0 shadow-sm rou3nded-4 overflow-hidden bg-white h-90">

									<c:choose>
										<c:when test="${not empty p.images}">
											<c:url value="/image?fname=${p.images[0].dirImage}"
												var="imgUrl"></c:url>
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
						</c:forEach>

					</div>

				</div>
			</div>
			<!-- end col-md-9 -->
		</div>
		<!-- end row -->
	</div>
	<!-- end container -->

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>