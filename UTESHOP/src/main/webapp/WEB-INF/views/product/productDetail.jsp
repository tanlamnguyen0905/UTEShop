<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>${product.productName}</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
	rel="stylesheet">
<style>
body {
	background-color: #f8f9fa;
	font-family: "Segoe UI", sans-serif;
}

.product-container {
	max-width: 1200px;
	margin: 60px auto;
	background: #fff;
	border-radius: 12px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	overflow: hidden;
	padding: 40px;
}

.main-img {
	width: 100%;
	border-radius: 12px;
}

.price {
	color: #e63946;
	font-size: 1.8rem;
	font-weight: 700;
}

.discount {
	color: #28a745;
	font-weight: 600;
}

.rating i {
	color: #f4b400;
}

.btn-add {
	background-color: #6c5ce7;
	border: none;
	padding: 12px 0;
	color: #fff;
	font-weight: 600;
	border-radius: 10px;
	width: 100%;
	transition: all 0.3s;
}

.btn-add:hover {
	background-color: #5941d8;
}
</style>
</head>
<body>

	<div class="container product-container">
		<div class="row">
			<!-- Cột ảnh sản phẩm -->
			<div class="col-md-6">
				<img src="${pageContext.request.contextPath}${product.image}"
					alt="${product.productName}" class="main-img mb-3">
			</div>

			<!-- Cột thông tin -->
			<div class="col-md-6">
				<h2 class="fw-bold mb-3">${product.productName}</h2>

				<p class="text-muted mb-1">
					Thương hiệu: <strong>${product.brand.brandName}</strong><br>
					Danh mục: <strong>${product.category.categoryName}</strong>
				</p>

				<div class="rating mb-2">
					<c:forEach begin="1" end="5" var="i">
						<i class="fa-solid fa-star${i <= product.rating ? '' : '-o'}"></i>
					</c:forEach>
					<span class="text-muted">(${product.reviewCount} đánh giá)</span>
				</div>

				<p class="price">
					${product.price}₫
					<c:if test="${product.discount > 0}">
						<span class="discount">(-${product.discount}%)</span>
					</c:if>
				</p>

				<p class="text-muted">${product.description}</p>

				<p>
					<strong>Đã bán:</strong> ${product.soldCount} | <strong>Tồn
						kho:</strong> ${product.stockQuantity}
				</p>

				<div class="mt-4">
					<button class="btn btn-add">
						<i class="fa-solid fa-cart-plus me-2"></i>Thêm vào giỏ hàng
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- ===================== PHẦN MÔ TẢ CHI TIẾT ===================== -->
	<div class="container mt-5 mb-5">
		<h3 class="text-center fw-bold mb-4">MÔ TẢ SẢN PHẨM</h3>

		<!-- Hàng icon đặc trưng -->
		<div class="row text-center mb-4">
			<div class="col-md-4">
				<i class="fa-solid fa-arrows-rotate fa-3x text-primary mb-2"></i>
				<p class="fw-semibold">Co giãn</p>
			</div>
			<div class="col-md-4">
				<i class="fa-solid fa-wind fa-3x text-primary mb-2"></i>
				<p class="fw-semibold">Thoáng mát</p>
			</div>
			<div class="col-md-4">
				<i class="fa-solid fa-shirt fa-3x text-primary mb-2"></i>
				<p class="fw-semibold">Thoải mái</p>
			</div>
		</div>

		<!-- Thông tin chi tiết sản phẩm -->
		<div class="row align-items-start">
			<div class="col-md-6">
				<table class="table table-borderless">
					<tbody>
						<tr>
							<th class="text-secondary">MÃ SẢN PHẨM</th>
							<td>SOZ893</td>
						</tr>
						<tr>
							<th class="text-secondary">CHẤT LIỆU</th>
							<td>100% Polyester</td>
						</tr>
						<tr>
							<th class="text-secondary">KIỂU DÁNG</th>
							<td>Short</td>
						</tr>
						<tr>
							<th class="text-secondary">PHÙ HỢP</th>
							<td>Tập thể dục thể thao</td>
						</tr>
						<tr>
							<th class="text-secondary">TÍNH NĂNG</th>
							<td>Thấm hút và nhanh khô</td>
						</tr>
						<tr>
							<th class="text-secondary">BẢO QUẢN</th>
							<td>Giặt máy nhẹ – Sấy mức thấp – Không dùng chất tẩy –
								Không giặt khô</td>
						</tr>
					</tbody>
				</table>
				<p class="fst-italic text-muted">* Proudly Made In Vietnam</p>
			</div>

			<div class="col-md-6 text-center">
				<img
					src="${pageContext.request.contextPath}/assets/images/product_desc1.jpg"
					class="img-fluid rounded mb-3" alt="Ảnh mô tả 1"> <img
					src="${pageContext.request.contextPath}/assets/images/product_desc2.jpg"
					class="img-fluid rounded" alt="Ảnh mô tả 2">
			</div>
		</div>

		<!-- Ảnh mô tả chi tiết -->
		<div class="row mt-5 text-center">
			<div class="col-md-3">
				<img
					src="${pageContext.request.contextPath}/assets/images/detail1.jpg"
					class="img-fluid rounded mb-2">
				<p class="fw-semibold">Vải dệt mềm mại</p>
				<p class="text-muted small">Polyester kỹ thuật cao – mềm mịn,
					nhẹ, bền vượt trội</p>
			</div>
			<div class="col-md-3">
				<img
					src="${pageContext.request.contextPath}/assets/images/detail2.jpg"
					class="img-fluid rounded mb-2">
				<p class="fw-semibold">Thoải mái, linh hoạt</p>
				<p class="text-muted small">Dáng regular fit, cử động linh hoạt
					tối đa</p>
			</div>
			<div class="col-md-3">
				<img
					src="${pageContext.request.contextPath}/assets/images/detail3.jpg"
					class="img-fluid rounded mb-2">
				<p class="fw-semibold">Túi tiện lợi hai bên</p>
				<p class="text-muted small">Giúp mang theo đồ dùng cá nhân dễ
					dàng</p>
			</div>
			<div class="col-md-3">
				<img
					src="${pageContext.request.contextPath}/assets/images/detail4.jpg"
					class="img-fluid rounded mb-2">
				<p class="fw-semibold">Đa năng & thoải mái</p>
				<p class="text-muted small">Sẵn sàng cho mọi hoạt động thể chất</p>
			</div>
		</div>

		<!-- Đoạn mô tả dài -->
		<div class="mt-5">
			<h5 class="fw-bold">Quần Thể Thao Nam 7" Ultra Shorts - Khô
				Thoáng Vượt Trội, Tự Do Luyện Tập</h5>
			<p class="text-muted">Được thiết kế để phá vỡ mọi giới hạn, Quần
				Thể Thao Nam 7" Ultra Shorts là lựa chọn lý tưởng cho mọi buổi tập.
				Sản phẩm sử dụng chất liệu Polyester Woven Plain, giúp mặt vải mềm
				mịn, nhẹ và bền bỉ, hạn chế xước do ma sát khi vận động. Công nghệ
				Quick-Dry giúp thấm hút nhanh, khô thoáng, và giữ cho cơ thể luôn dễ
				chịu trong mọi điều kiện thời tiết.</p>

			<h6 class="fw-bold mt-4">Thiết Kế Co Giãn Tối Ưu Vận Động</h6>
			<ul class="text-muted">
				<li>Vải co giãn 4 chiều, di chuyển linh hoạt trong mọi hoạt
					động.</li>
				<li>Lưng chun co giãn, ôm vừa vặn mà không gây khó chịu.</li>
				<li>Túi hai bên tiện dụng, để đồ cá nhân nhỏ gọn.</li>
				<li>Logo Coolmate in sắc nét, khẳng định thương hiệu Việt Nam.</li>
			</ul>

			<h6 class="fw-bold mt-4">Đồng Hành Trong Mọi Buổi Tập</h6>
			<p class="text-muted">Dù là chạy bộ, tập gym hay chơi thể thao,
				quần Ultra Short luôn mang lại sự thoải mái tối đa và phong cách
				năng động.</p>
		</div>
	</div>

	<!-- Slide gợi ý sản phẩm -->

	<!-- ===================== PHẦN ĐÁNH GIÁ SẢN PHẨM ===================== -->
	<div class="container mt-5 mb-5">
		<h3 class="fw-bold mb-4">ĐÁNH GIÁ SẢN PHẨM</h3>

		<div class="row">
			<!-- Cột trái: Bộ lọc -->
			<div class="col-md-3 border-end">
				<div class="mb-3">
					<input type="text" class="form-control"
						placeholder="🔍 Tìm kiếm đánh giá...">
				</div>
				<p class="fw-semibold">Phân loại xếp hạng</p>
				<div class="mb-2">
					<c:forEach var="i" begin="1" end="5">
						<c:set var="value" value="${6 - i}" />
						<div class="form-check mb-1">
							<input class="form-check-input" type="radio" name="ratingFilter"
								id="star${value}" value="${value}"> <label
								class="form-check-label" for="star${value}"> <!-- Sao đầy -->
								<c:forEach var="s" begin="1" end="${value}">
									<i class="fa-solid fa-star text-warning"></i>
								</c:forEach> <!-- Sao rỗng --> <c:forEach var="e" begin="1"
									end="${5 - value}">
									<i class="fa-regular fa-star text-warning"></i>
								</c:forEach>
							</label>
						</div>
					</c:forEach>
				</div>
				<p class="small text-muted mt-3">
					<i class="fa-solid fa-circle-check text-success me-1"></i> Các
					review đều đến từ khách hàng đã mua hàng tại UTESHOP
				</p>
				<div class="mt-3">
					<p class="fw-semibold mb-1">Lọc phản hồi</p>
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="replied">
						<label class="form-check-label" for="replied">Đã phản hồi</label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="hasImage">
						<label class="form-check-label" for="hasImage">Có hình ảnh</label>
					</div>
				</div>
			</div>

			<!-- Cột phải: Tổng điểm và danh sách review -->
			<div class="col-md-9 ps-4">
				<!-- Tổng điểm -->
				<div
					class="d-flex justify-content-between align-items-center mb-4 p-3 bg-light rounded">
					<div>
						<h1 class="fw-bold mb-0">${product.rating}</h1>
						<div class="rating">
							<c:forEach begin="1" end="5" var="i">
								<i
									class="fa-solid fa-star${i <= product.rating ? ' text-warning' : ' text-muted'}"></i>
							</c:forEach>
						</div>
						<small class="text-muted">Dựa trên ${product.reviewCount}
							đánh giá</small>
					</div>
					<div>
						<p class="fw-semibold mb-2">Phù hợp với cơ thể</p>
						<div class="d-flex align-items-center mb-1">
							<span class="me-2 small">Chật</span>
							<div class="progress flex-grow-1" style="height: 6px;">
								<div class="progress-bar bg-dark" style="width: 10%;"></div>
							</div>
							<span class="ms-2 small text-muted">10%</span>
						</div>
						<div class="d-flex align-items-center mb-1">
							<span class="me-2 small">Đúng kích thước</span>
							<div class="progress flex-grow-1" style="height: 6px;">
								<div class="progress-bar bg-dark" style="width: 85%;"></div>
							</div>
							<span class="ms-2 small text-muted">85%</span>
						</div>
						<div class="d-flex align-items-center mb-1">
							<span class="me-2 small">Rộng</span>
							<div class="progress flex-grow-1" style="height: 6px;">
								<div class="progress-bar bg-dark" style="width: 5%;"></div>
							</div>
							<span class="ms-2 small text-muted">5%</span>
						</div>
					</div>
				</div>

				<!-- Danh sách đánh giá -->
				<div class="review-item border rounded p-3 mb-3">
					<div class="d-flex justify-content-between">
						<p class="fw-semibold mb-1">
							tuoixinh156 <span class="text-muted small">• 25/09/2025</span>
						</p>
						<span class="small text-muted">Đánh giá từ <i
							class="fa-brands fa-shopify"></i> Shopee
						</span>
					</div>
					<p>
						<i class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-regular fa-star text-warning"></i>
					</p>
					<p class="text-muted small mb-1">Kích thước: 3XL — Màu sắc: Đen</p>
					<p>Quần mình mua 1 lần rồi nên thấy ok. Chất liệu giỏ mặc thoải
						mái.</p>
					<div class="mt-2">
						<img
							src="${pageContext.request.contextPath}/assets/images/review1.jpg"
							class="rounded me-2" style="height: 100px;"> <img
							src="${pageContext.request.contextPath}/assets/images/review2.jpg"
							class="rounded" style="height: 100px;">
					</div>
				</div>

				<div class="review-item border rounded p-3 mb-3">
					<div class="d-flex justify-content-between">
						<p class="fw-semibold mb-1">
							cong_den <span class="text-muted small">• 25/09/2025</span>
						</p>
						<span class="small text-muted">Đánh giá từ Shopee</span>
					</div>
					<p>
						<i class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i>
					</p>
					<p class="text-muted small mb-1">Kích thước: 3XL — Màu sắc: Đen</p>
					<p>Sản phẩm tốt, mặc hơi nóng chút.</p>
				</div>

				<div class="review-item border rounded p-3 mb-3">
					<div class="d-flex justify-content-between">
						<p class="fw-semibold mb-1">
							ngoccoc1986 <span class="text-muted small">• 19/09/2025</span>
						</p>
						<span class="small text-muted">Đánh giá từ Shopee</span>
					</div>
					<p>
						<i class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-solid fa-star text-warning"></i> <i
							class="fa-regular fa-star text-warning"></i>
					</p>
					<p class="text-muted small mb-1">Kích thước: M — Màu sắc: Xám
						đậm</p>
					<p>Quần chất mỏng nhẹ, phù hợp giá tiền, sẽ ủng hộ tiếp.</p>
					<div class="mt-2">
						<img
							src="${pageContext.request.contextPath}/assets/images/review3.jpg"
							class="rounded" style="height: 100px;">
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
