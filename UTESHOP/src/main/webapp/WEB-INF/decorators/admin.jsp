<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no" />

    <title><c:out value="${title}" default="Admin Panel"/></title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/templates/admin/assets/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/templates/admin/css/styles.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">


    <!-- Head content từ trang con -->
    <c:out value="${head}" escapeXml="false" />
</head>

<body class="sb-nav-fixed">
<div class="wrapper">

    <!-- Header -->
    <jsp:include page="/commons/admin/header.jsp" />

    <div id="layoutSidenav">
        <!-- Sidebar -->
        <jsp:include page="/commons/admin/sidebar.jsp" />

        <!-- Main content -->
        <div id="layoutSidenav_content">
            <main class="p-4">
                <!-- Nội dung trang con sẽ được SiteMesh 3 inject vào đây -->
                <c:out value="${body}" escapeXml="false" />
            </main>

            <!-- Footer -->
            <jsp:include page="/commons/admin/footer.jsp" />
        </div>
    </div>
    <div class="container">
        <div class="page-inner">
            <sitemesh:write property="body" />
        </div>
    </div>
</div>

<!-- Core JS -->
<script src="${pageContext.request.contextPath}/templates/admin/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/templates/admin/js/scripts.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script từ trang con -->
<c:out value="${script}" escapeXml="false" />
</body>
</html>
