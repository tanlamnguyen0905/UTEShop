<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Danh mục sản phẩm</title>
            </head>

            <body class="bg-light py-5">
                <div class="container">

                    <!-- ====================== SLIDER BANNER ====================== -->
                    <div id="mainCarousel" class="carousel slide mb-5" data-bs-ride="carousel">
                        <div class="carousel-inner">
                            <c:forEach var="banner" items="${listBanner}" varStatus="loop">
                                <c:url value="/image?fname=${banner.bannerImage}" var="imgUrl" />
                                <div class="carousel-item ${loop.first ? 'active' : ''}">
                                    <a href="${pageContext.request.contextPath}/filter?banner=${banner.bannerID}">
                                        <img src="${imgUrl}" class="d-block w-100 rounded-4" alt="${banner.bannerName}"
                                            style="height: 400px; object-fit: cover;">
                                    </a>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Điều hướng -->
                        <button class="carousel-control-prev" type="button" data-bs-target="#mainCarousel"
                            data-bs-slide="prev">
                            <span class="carousel-control-prev-icon"></span>
                            <span class="visually-hidden">Trước</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#mainCarousel"
                            data-bs-slide="next">
                            <span class="carousel-control-next-icon"></span>
                            <span class="visually-hidden">Sau</span>
                        </button>
                    </div>

                    <!-- ====================== DANH MỤC ====================== -->
                    <section class="py-5 text-center">
                        <div class="text-start mb-4">
                            <button class="btn btn-dark rounded-pill px-4 fw-bold text-white">Phân loại</button>
                        </div>

                        <div class="row g-4">
                            <c:forEach var="cate" items="${listCate}" end="5">
                                <div class="col-6 col-md-4 col-lg-2">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <c:url value="/image?fname=${cate.image}" var="imgUrl" />
                                        <a href="${pageContext.request.contextPath}/filter?category=${cate.categoryID}"
                                            class="text-decoration-none">
                                            <img src="${imgUrl}" class="card-img-top rounded-4"
                                                alt="${cate.categoryName}" style="height: 250px; object-fit: cover;">
                                            <div class="card-body p-2">
                                                <h6 class="text-dark text-uppercase">${cate.categoryName}</h6>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>

                    <!-- ====================== SẢN PHẨM BÁN CHẠY ====================== -->
                    <section class="py-5">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h3 class="mb-0">SẢN PHẨM BÁN CHẠY</h3>
                            <a href="${pageContext.request.contextPath}/filter?sortBy=0"
                                class="fw-semibold text-dark text-decoration-underline">Xem thêm</a>
                        </div>

                        <div class="row g-4">
                            <c:forEach var="p" items="${listBestSell}" end="5">
                                <div class="col-6 col-md-4 col-lg-2">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <c:choose>
                                            <c:when test="${not empty p.image}">
                                                <c:url value="/image?fname=${p.image}" var="imgUrl" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:url value="/image?fname=logo.png" var="imgUrl" />
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}"
                                            class="text-decoration-none">
                                            <img src="${imgUrl}" class="card-img-top rounded-4" alt="${p.productName}"
                                                style="height: 250px; object-fit: cover;">
                                            <div class="card-body p-2">
                                                <h6 class="text-dark mb-2">${p.productName}</h6>

                                                <span class="fw-bold text-dark">
                                                    <fmt:formatNumber value="${p.unitPrice}" type="number"
                                                        maxFractionDigits="0" />đ
                                                </span>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>

                    <!-- ====================== SẢN PHẨM MỚI ====================== -->
                    <section class="py-5">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h3 class="mb-0">SẢN PHẨM MỚI</h3>
                            <a href="${pageContext.request.contextPath}/filter?sortBy=1"
                                class="fw-semibold text-dark text-decoration-underline">Xem thêm</a>
                        </div>

                        <div class="row g-4">
                            <c:forEach var="p" items="${listNewProducts}" end="5">
                                <div class="col-6 col-md-4 col-lg-2">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <c:choose>
                                            <c:when test="${not empty p.image}">
                                                <c:url value="/image?fname=${p.image}" var="imgUrl" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:url value="/image?fname=logo.png" var="imgUrl" />
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}"
                                            class="text-decoration-none">
                                            <img src="${imgUrl}" class="card-img-top rounded-4" alt="${p.productName}"
                                                style="height: 250px; object-fit: cover;">
                                            <div class="card-body p-2">
                                                <h6 class="text-dark mb-2">${p.productName}</h6>

                                                <span class="fw-bold text-dark">
                                                    <fmt:formatNumber value="${p.unitPrice}" type="number"
                                                        maxFractionDigits="0" />đ
                                                </span>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>


                    <!-- ====================== SẢN PHẨM TOP Review ====================== -->
                    <section class="py-5">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h3 class="mb-0">SẢN PHẨM NHIỀU ĐÁNH GIÁ</h3>
                            <a href="${pageContext.request.contextPath}/filter?sortBy=2"
                                class="fw-semibold text-dark text-decoration-underline">Xem thêm</a>
                        </div>

                        <div class="row g-4">
                            <c:forEach var="p" items="${listTopReviewProducts}" end="5">
                                <div class="col-6 col-md-4 col-lg-2">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <c:choose>
                                            <c:when test="${not empty p.image}">
                                                <c:url value="/image?fname=${p.image}" var="imgUrl" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:url value="/image?fname=logo.png" var="imgUrl" />
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}"
                                            class="text-decoration-none">
                                            <img src="${imgUrl}" class="card-img-top rounded-4" alt="${p.productName}"
                                                style="height: 250px; object-fit: cover;">
                                            <div class="card-body p-2">
                                                <h6 class="text-dark mb-2">${p.productName}</h6>

                                                <span class="fw-bold text-dark">
                                                    <fmt:formatNumber value="${p.unitPrice}" type="number"
                                                        maxFractionDigits="0" />đ
                                                </span>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>


                    <!-- ====================== SẢN PHẨM TOP Favorite ====================== -->
                    <section class="py-5">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h3 class="mb-0">SẢN PHẨM NHIỀU YÊU THÍCH</h3>
                            <a href="${pageContext.request.contextPath}/filter?sortBy=3"
                                class="fw-semibold text-dark text-decoration-underline">Xem thêm</a>
                        </div>

                        <div class="row g-4">
                            <c:forEach var="p" items="${listTopFavoriteProducts}" end="5">
                                <div class="col-6 col-md-4 col-lg-2">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <c:choose>
                                            <c:when test="${not empty p.image}">
                                                <c:url value="/image?fname=${p.image}" var="imgUrl" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:url value="/image?fname=logo.png" var="imgUrl" />
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/detailProduct?productID=${p.productID}"
                                            class="text-decoration-none">
                                            <img src="${imgUrl}" class="card-img-top rounded-4" alt="${p.productName}"
                                                style="height: 250px; object-fit: cover;">
                                            <div class="card-body p-2">
                                                <h6 class="text-dark mb-2">${p.productName}</h6>

                                                <span class="fw-bold text-dark">
                                                    <fmt:formatNumber value="${p.unitPrice}" type="number"
                                                        maxFractionDigits="0" />đ
                                                </span>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>

                    <!-- ====================== BANNER + SẢN PHẨM THEO BANNER ====================== -->
                    <%-- <c:forEach var="banner" items="${listBanner}" end="5">
                        <c:url value="/image?fname=${banner.bannerImage}" var="bannerImgUrl" />
                        <section class="py-5">
                            <a href="${pageContext.request.contextPath}/filter?banner=${banner.bannerID}">
                                <img src="${bannerImgUrl}" class="d-block w-100 rounded-4 mb-3"
                                    alt="${banner.bannerName}" style="height: 300px; object-fit: cover;">
                            </a>

                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h3 class="mb-0">${banner.bannerName}</h3>
                                <a href="#" class="fw-semibold text-dark text-decoration-underline">Xem thêm</a>
                            </div>

                            <div class="row g-3">
                                <c:forEach var="p" items="${banner.products}">
                                    <div class="col-6 col-md-4 col-lg-2">
                                        <div class="card border-0 shadow-sm rounded-4 h-100">
                                            <c:choose>
                                                <c:when test="${not empty p.images}">
                                                    <c:url value="/image?fname=${p.images[0].dirImage}" var="imgUrl" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:url value="/image?fname=logo.png" var="imgUrl" />
                                                </c:otherwise>
                                            </c:choose>

                                            <a href="#" class="text-decoration-none">
                                                <img src="${imgUrl}" class="card-img-top rounded-4"
                                                    alt="${p.productName}" style="height: 250px; object-fit: cover;">
                                                <div class="card-body p-2">
                                                    <h6 class="text-dark mb-2">${p.productName}</h6>
                                                    <span class="fw-bold text-dark">
                                                        <fmt:formatNumber value="${p.unitPrice}" type="number"
                                                            maxFractionDigits="0" />đ
                                                    </span>
                                                </div>
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </section>
                        </c:forEach>
                        --%>
                </div>
            </body>

            </html>