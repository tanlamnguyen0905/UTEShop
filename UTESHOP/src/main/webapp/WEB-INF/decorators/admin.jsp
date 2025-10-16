<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${page.title}</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/templates/admin/assets/css/bootstrap.min.css" rel="stylesheet" />

    <!-- SB Admin CSS -->
    <link href="${pageContext.request.contextPath}/templates/admin/css/styles.css" rel="stylesheet" />

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

    ${page.head}
</head>

<body class="sb-nav-fixed">

<!-- Header -->
<jsp:include page="/commons/admin/header.jsp" />

<div id="layoutSidenav">

    <!-- Sidebar -->
    <jsp:include page="/commons/admin/sidebar.jsp" />

    <div id="layoutSidenav_content">
        <main class="p-4">
            ${page.body}
        </main>

        <!-- Footer -->
        <jsp:include page="/commons/admin/footer.jsp" />
    </div>
</div>

<!-- Core JS -->
<script src="${pageContext.request.contextPath}/templates/admin/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/templates/admin/js/scripts.js"></script>

${page.script}
</body>
</html>
