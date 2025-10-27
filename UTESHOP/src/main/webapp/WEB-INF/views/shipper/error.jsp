<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lỗi - Shipper</title>
</head>
<body>
    <div class="container-fluid">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card border-danger">
                    <div class="card-body text-center py-5">
                        <i class="fas fa-exclamation-triangle text-danger fa-5x mb-4"></i>
                        <h2 class="text-danger mb-3">Có lỗi xảy ra!</h2>
                        <p class="text-muted mb-4">${error}</p>
                        <a href="${pageContext.request.contextPath}/shipper/dashboard" class="btn btn-primary">
                            <i class="fas fa-home me-2"></i>Về trang chủ
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

