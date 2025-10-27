<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Demo Trang Đánh Giá - UTEShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .demo-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .demo-content {
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            padding: 2rem;
        }
    </style>
</head>
<body>
    <!-- Demo Header -->
    <div class="demo-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1><i class="fas fa-star"></i> Demo Trang Đánh Giá UTEShop</h1>
                    <p class="mb-0">Thiết kế hiện đại với UI/UX tối ưu</p>
                </div>
                <div class="col-md-4 text-end">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-light">
                        <i class="fas fa-home"></i> Về trang chủ
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="demo-content">
            <!-- Product Info Demo -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <img src="${pageContext.request.contextPath}/assets/images/logo.png" 
                         class="img-fluid rounded" alt="Product Demo">
                </div>
                <div class="col-md-6">
                    <h2>iPhone 15 Pro Max</h2>
                    <div class="rating-summary mb-3">
                        <div class="d-flex align-items-center gap-3">
                            <div class="rating-stars">
                                <i class="fas fa-star text-warning"></i>
                                <i class="fas fa-star text-warning"></i>
                                <i class="fas fa-star text-warning"></i>
                                <i class="fas fa-star text-warning"></i>
                                <i class="fas fa-star text-secondary"></i>
                                <span class="ms-2"><strong>4.2</strong>/5</span>
                            </div>
                            <div class="vr"></div>
                            <div class="text-muted">
                                <i class="fas fa-comment-dots"></i>
                                156 đánh giá
                            </div>
                        </div>
                    </div>
                    <div class="price-tag mb-3">
                        <span class="fw-bold text-danger fs-4">29.990.000₫</span>
                    </div>
                    <p class="text-muted">iPhone 15 Pro Max với chip A17 Pro mạnh mẽ, camera 48MP và thiết kế titan cao cấp.</p>
                </div>
            </div>

            <!-- Include Reviews Component -->
            <jsp:include page="reviews.jsp" />
        </div>
    </div>

    <!-- Toast container -->
    <div id="toastContainer" class="position-fixed top-0 end-0 p-3" style="z-index: 11000;"></div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
