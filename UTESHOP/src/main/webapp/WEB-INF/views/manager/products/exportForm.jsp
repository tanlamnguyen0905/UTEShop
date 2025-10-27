<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Xuất hàng - Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .form-card { border-radius: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
        .btn { border-radius: 8px; }
        .warning-text { color: #ffc107; }
    </style>
</head>
<body>
<div class="container-fluid px-4">
    <h1 class="mt-4 mb-4 fw-bold text-primary">
        <i class="fas fa-minus-circle me-2"></i>Xuất hàng sản phẩm
    </h1>

    <!-- Error/Message -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card form-card">
        <div class="card-body">
            <form method="POST" action="${pageContext.request.contextPath}/api/manager/products/export">
                <input type="hidden" name="id" value="${product.productID}">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Sản phẩm</label>
                        <input type="text" class="form-control" value="${product.productName}" readonly>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Tồn kho hiện tại</label>
                        <input type="number" class="form-control" value="${product.stockQuantity}" readonly>
                        <small class="warning-text">Chỉ xuất <= tồn kho</small>
                    </div>
                    <div class="col-md-12">
                        <label class="form-label fw-bold">Số lượng xuất <span class="text-danger">*</span></label>
                        <input type="number" name="quantity" class="form-control" min="1" max="${product.stockQuantity}" required placeholder="Nhập số lượng...">
                        <small class="text-muted">Số lượng > 0 và <= tồn kho</small>
                    </div>
                </div>
                <div class="row mt-4">
                    <div class="col-12">
                        <button type="submit" class="btn btn-warning">
                            <i class="fas fa-save me-2"></i>Lưu xuất hàng
                        </button>
                        <a href="${pageContext.request.contextPath}/api/manager/products/searchpaginated" class="btn btn-secondary ms-2">
                            <i class="fas fa-arrow-left me-2"></i>Quay lại
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Client-side validation for max quantity
    document.querySelector('input[name="quantity"]').addEventListener('input', function() {
        const max = ${product.stockQuantity};
        if (this.value > max) {
            this.value = max;
            alert('Số lượng không được vượt quá tồn kho!');
        }
    });
</script>
</body>
</html>