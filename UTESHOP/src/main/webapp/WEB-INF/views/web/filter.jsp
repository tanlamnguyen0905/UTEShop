<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="jakarta.tags.core" %>
		<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
			<!DOCTYPE html>
			<html>

			<head>
				<meta charset="UTF-8">
				<meta name="viewport" content="width=device-width, initial-scale=1.0">
				<title>Bộ lọc & Sản phẩm - UTESHOP</title>
			</head>

			<body class="bg-light py-5">

				<div class="container">
					<div class="row">
						<!-- ===== CỘT TRÁI: BỘ LỌC ===== -->
						<div class="col-md-3 mb-4">
							<div class="bg-white p-3 rounded shadow-sm">
								<div class="d-flex justify-content-between align-items-center mb-3">
									<h5 class="mb-0">Bộ lọc</h5>
									<span class="text-muted small">${total} sản phẩm</span>
								</div>

								<!-- Form filter -->
								<form id="filterForm" method="get" action="${pageContext.request.contextPath}/filter">
									<!-- Ẩn các tham số hiện tại -->
									<c:if test="${productFilter.categoryId != null}">
										<input type="hidden" name="category" value="${productFilter.categoryId}">
									</c:if>
									<c:if test="${productFilter.bannerId != null}">
										<input type="hidden" name="banner" value="${productFilter.bannerId}">
									</c:if>
									<c:if test="${productFilter.brandId != null}">
										<input type="hidden" name="brand" value="${productFilter.brandId}">
									</c:if>
									<c:if test="${productFilter.keyword != null}">
										<input type="hidden" name="keyword" value="${productFilter.keyword}">
									</c:if>
									<!-- preserve current sorting when applying filters -->
									<c:if test="${productFilter.sortBy != null}">
										<input type="hidden" name="sortBy" value="${productFilter.sortBy}">
									</c:if>

									<!-- Accordion Bootstrap cho bộ lọc -->
									<div class="accordion" id="filterAccordion">
										<!-- Khoảng giá -->
										<div class="accordion-item">
											<h6 class="accordion-header" id="headingPrice">
												<button class="accordion-button collapsed" type="button"
													data-bs-toggle="collapse" data-bs-target="#collapsePrice"
													aria-expanded="false" aria-controls="collapsePrice">
													Khoảng giá</button>
											</h6>
											<div id="collapsePrice" class="accordion-collapse collapse show"
												aria-labelledby="headingPrice" data-bs-parent="#filterAccordion">
												<div class="accordion-body">
													<div class="row g-2">
														<div class="col-6">
															<label class="form-label small">Từ (VNĐ)</label>
															<input type="number" class="form-control form-control-sm"
																name="minPrice" id="minPrice"
																value="${productFilter.minPrice != null ? productFilter.minPrice : ''}"
																placeholder="0">
														</div>
														<div class="col-6">
															<label class="form-label small">Đến (VNĐ)</label>
															<input type="number" class="form-control form-control-sm"
																name="maxPrice" id="maxPrice"
																value="${productFilter.maxPrice != null ? productFilter.maxPrice : ''}"
																placeholder="10000000">
														</div>
													</div>
												</div>
											</div>
										</div>

										<!-- Danh mục -->
										<div class="accordion-item">
											<h6 class="accordion-header" id="headingCategory">
												<button class="accordion-button collapsed" type="button"
													data-bs-toggle="collapse" data-bs-target="#collapseCategory"
													aria-expanded="false" aria-controls="collapseCategory">
													Danh mục</button>
											</h6>
											<div id="collapseCategory" class="accordion-collapse collapse show"
												aria-labelledby="headingCategory" data-bs-parent="#filterAccordion">
												<div class="accordion-body">
													<c:forEach var="cate" items="${listCate}">
														<div class="form-check">
															<input class="form-check-input" type="radio" name="category"
																id="category${cate.categoryID}"
																value="${cate.categoryID}"
																${productFilter.categoryId==cate.categoryID ? 'checked'
																: '' }>
															<label class="form-check-label"
																for="category${cate.categoryID}">
																${cate.categoryName}
															</label>
														</div>
													</c:forEach>
												</div>
											</div>
										</div>

										<!-- Banner -->
										<div class="accordion-item">
											<h6 class="accordion-header" id="headingBanner">
												<button class="accordion-button collapsed" type="button"
													data-bs-toggle="collapse" data-bs-target="#collapseBanner"
													aria-expanded="false" aria-controls="collapseBanner">
													Banner</button>
											</h6>
											<div id="collapseBanner" class="accordion-collapse collapse show"
												aria-labelledby="headingBanner" data-bs-parent="#filterAccordion">
												<div class="accordion-body">
													<c:forEach var="banner" items="${listBanner}">
														<div class="form-check">
															<input class="form-check-input" type="radio" name="banner"
																id="banner${banner.bannerID}" value="${banner.bannerID}"
																${productFilter.bannerId==banner.bannerID ? 'checked'
																: '' }>
															<label class="form-check-label"
																for="banner${banner.bannerID}">
																${banner.bannerName}
															</label>
														</div>
													</c:forEach>
												</div>
											</div>
										</div>
									</div>

									<!-- Nút áp dụng filter -->
									<div class="mt-3">
										<button type="submit" class="btn btn-primary btn-sm w-100">Áp dụng bộ
											lọc</button>
									</div>
								</form>
							</div>
						</div>

						<!-- ===== CỘT PHẢI: SẢN PHẨM ===== -->
						<div class="col-md-9">
							<!-- Thanh sắp xếp -->
							<div class="d-flex justify-content-between align-items-center mb-3">
								<div>
									<h4 class="mb-0">Sản phẩm</h4>
									<small class="text-muted">Hiển thị ${listPro.size()} trong ${total} sản phẩm</small>
								</div>
								<div class="d-flex align-items-center">
									<label class="me-2 fw-semibold">Sắp xếp theo:</label>
									<div class="dropdown">
										<button class="btn btn-outline-secondary dropdown-toggle" type="button"
											data-bs-toggle="dropdown" aria-expanded="false">
											<c:choose>
												<c:when test="${productFilter.sortBy == '0'}">Bán chạy</c:when>
												<c:when test="${productFilter.sortBy == '1'}">Mới nhất</c:when>
												<c:when test="${productFilter.sortBy == '2'}">Nhiều đánh giá</c:when>
												<c:when test="${productFilter.sortBy == '3'}">Yêu thích</c:when>
												<c:when test="${productFilter.sortBy == '4'}">Giá tăng dần</c:when>
												<c:when test="${productFilter.sortBy == '5'}">Giá giảm dần</c:when>
												<c:otherwise>Mới nhất</c:otherwise>
											</c:choose>
										</button>
										<ul class="dropdown-menu">
											<li><a class="dropdown-item sort-link" data-sort="0" href="#">Bán chạy</a>
											</li>
											<li><a class="dropdown-item sort-link" data-sort="1" href="#">Mới nhất</a>
											</li>
											<li><a class="dropdown-item sort-link" data-sort="2" href="#">Nhiều đánh
													giá</a></li>
											<li><a class="dropdown-item sort-link" data-sort="3" href="#">Yêu thích</a>
											</li>
											<li><a class="dropdown-item sort-link" data-sort="4" href="#">Giá tăng
													dần</a></li>
											<li><a class="dropdown-item sort-link" data-sort="5" href="#">Giá giảm
													dần</a></li>
										</ul>
									</div>
								</div>
							</div>

							<!-- Lưới sản phẩm -->
							<c:choose>
								<c:when test="${empty listPro}">
									<div class="text-center py-5">
										<div class="mb-3">
											<i class="fas fa-search fa-3x text-muted"></i>
										</div>
										<h5 class="text-muted">Không tìm thấy sản phẩm nào</h5>
										<p class="text-muted">Hãy thử điều chỉnh bộ lọc hoặc tìm kiếm với từ khóa khác
										</p>
										<a href="${pageContext.request.contextPath}/filter" class="btn btn-primary">Xem
											tất cả sản phẩm</a>
									</div>
								</c:when>
								<c:otherwise>
									<div class="row g-4">
										<c:forEach var="p" items="${listPro}">
											<div class="col-6 col-md-4 col-lg-3">
												<%@ include file="product-card.jsp" %>
											</div>
										</c:forEach>
									</div>

									<!-- Thanh phân trang -->
									<c:if test="${totalPages > 1}">
										<nav aria-label="Page navigation" class="mt-4">
											<ul class="pagination justify-content-center">
												<!-- Prev -->
												<c:if test="${currentPage > 1}">
													<c:url var="prevUrl" value="/filter">
														<c:if test="${productFilter.categoryId != null}">
															<c:param name="category"
																value="${productFilter.categoryId}" />
														</c:if>
														<c:if test="${productFilter.bannerId != null}">
															<c:param name="banner" value="${productFilter.bannerId}" />
														</c:if>
														<c:if test="${productFilter.brandId != null}">
															<c:param name="brand" value="${productFilter.brandId}" />
														</c:if>
														<c:if test="${productFilter.minPrice != null}">
															<c:param name="minPrice"
																value="${productFilter.minPrice}" />
														</c:if>
														<c:if test="${productFilter.maxPrice != null}">
															<c:param name="maxPrice"
																value="${productFilter.maxPrice}" />
														</c:if>
														<c:if test="${productFilter.keyword != null}">
															<c:param name="keyword" value="${productFilter.keyword}" />
														</c:if>
														<c:if test="${productFilter.sortBy != null}">
															<c:param name="sortBy" value="${productFilter.sortBy}" />
														</c:if>
														<c:param name="currentPage" value="${currentPage - 1}" />
													</c:url>
													<li class="page-item">
														<a class="page-link" href="${prevUrl}">«</a>
													</li>
												</c:if>

												<!-- Page numbers -->
												<c:forEach var="i" begin="1" end="${totalPages}">
													<c:url var="pageUrl" value="/filter">
														<c:if test="${productFilter.categoryId != null}">
															<c:param name="category"
																value="${productFilter.categoryId}" />
														</c:if>
														<c:if test="${productFilter.bannerId != null}">
															<c:param name="banner" value="${productFilter.bannerId}" />
														</c:if>
														<c:if test="${productFilter.brandId != null}">
															<c:param name="brand" value="${productFilter.brandId}" />
														</c:if>
														<c:if test="${productFilter.minPrice != null}">
															<c:param name="minPrice"
																value="${productFilter.minPrice}" />
														</c:if>
														<c:if test="${productFilter.maxPrice != null}">
															<c:param name="maxPrice"
																value="${productFilter.maxPrice}" />
														</c:if>
														<c:if test="${productFilter.keyword != null}">
															<c:param name="keyword" value="${productFilter.keyword}" />
														</c:if>
														<c:if test="${productFilter.sortBy != null}">
															<c:param name="sortBy" value="${productFilter.sortBy}" />
														</c:if>
														<c:param name="currentPage" value="${i}" />
													</c:url>
													<li class="page-item ${i == currentPage ? 'active' : ''}">
														<a class="page-link" href="${pageUrl}">${i}</a>
													</li>
												</c:forEach>

												<!-- Next -->
												<c:if test="${currentPage < totalPages}">
													<c:url var="nextUrl" value="/filter">
														<c:if test="${productFilter.categoryId != null}">
															<c:param name="category"
																value="${productFilter.categoryId}" />
														</c:if>
														<c:if test="${productFilter.bannerId != null}">
															<c:param name="banner" value="${productFilter.bannerId}" />
														</c:if>
														<c:if test="${productFilter.brandId != null}">
															<c:param name="brand" value="${productFilter.brandId}" />
														</c:if>
														<c:if test="${productFilter.minPrice != null}">
															<c:param name="minPrice"
																value="${productFilter.minPrice}" />
														</c:if>
														<c:if test="${productFilter.maxPrice != null}">
															<c:param name="maxPrice"
																value="${productFilter.maxPrice}" />
														</c:if>
														<c:if test="${productFilter.keyword != null}">
															<c:param name="keyword" value="${productFilter.keyword}" />
														</c:if>
														<c:if test="${productFilter.sortBy != null}">
															<c:param name="sortBy" value="${productFilter.sortBy}" />
														</c:if>
														<c:param name="currentPage" value="${currentPage + 1}" />
													</c:url>
													<li class="page-item">
														<a class="page-link" href="${nextUrl}">»</a>
													</li>
												</c:if>
											</ul>
										</nav>
									</c:if>
								</c:otherwise>
							</c:choose>

						</div>
						<!-- end col-md-9 -->
					</div>
					<!-- end row -->
				</div>
				<!-- end container -->

				<script>
					// Xử lý form filter: chỉ áp dụng khi bấm nút
					document.getElementById('filterForm').addEventListener('submit', function (e) {
						const form = this;
						['minPrice', 'maxPrice'].forEach(name => {
							const el = form.querySelector(`[name="${name}"]`);
							if (el && !el.value) el.disabled = true;
						});
					});

					// Xử lý dropdown sort: giữ nguyên bộ lọc hiện tại, chỉ thay sortBy
					document.querySelectorAll('.sort-link').forEach(item => {
						item.addEventListener('click', function (e) {
							e.preventDefault();
							const sortVal = this.getAttribute('data-sort');
							const url = new URL(window.location.href);
							url.searchParams.set('sortBy', sortVal);
							url.searchParams.set('currentPage', '1');
							window.location.href = url.toString();
						});
					});

					// Không auto submit khi thay đổi radio; người dùng phải ấn "Áp dụng bộ lọc"
				</script>

			</body>
			
			<!-- Include Cart JS for quick add to cart functionality -->
			<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>

			</html>