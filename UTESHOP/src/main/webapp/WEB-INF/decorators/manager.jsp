<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no" />

    <title><sitemesh:write property="title" default="Manager Panel" /></title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/templates/manager/css/styles.css" rel="stylesheet" />

    <style>
        /* SIDEBAR CRITICAL STYLES - Inline để đảm bảo load */
        .sb-nav-fixed #layoutSidenav #layoutSidenav_nav {
            position: fixed;
            top: 0;
            left: 0;
            width: 250px;
            height: 100vh;
            z-index: 1038;
        }

        .sb-nav-fixed #layoutSidenav #layoutSidenav_content {
            padding-left: 250px;
            padding-top: 56px;
        }

        .sb-sidenav {
            display: flex;
            flex-direction: column;
            height: 100%;
        }

        .sb-sidenav .sb-sidenav-menu {
            flex-grow: 1;
            overflow-y: auto;
        }

        .sb-sidenav .sb-sidenav-menu .nav {
            flex-direction: column !important;
        }

        .sb-sidenav .sb-sidenav-menu .nav .nav-link {
            padding: 0.75rem 1rem;
            color: rgba(255,255,255,0.7);
        }

        .sb-sidenav-primary {
            background-color: #0d6efd;
        }

        .sb-topnav {
            height: 56px;
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1039;
        }
    </style>

    <!-- Head content từ trang con -->
    <sitemesh:write property="head" />
</head>

<body class="sb-nav-fixed">
<!-- Header -->
<jsp:include page="/commons/manager/header.jsp" />

<div id="layoutSidenav">
    <!-- Sidebar -->
    <jsp:include page="/commons/manager/sidebar.jsp" />

    <!-- Main content -->
    <div id="layoutSidenav_content">
        <main class="p-4">
            <!-- Nội dung trang con sẽ được SiteMesh 3 inject vào đây -->
            <sitemesh:write property="body" />
        </main>

        <!-- Footer -->
        <jsp:include page="/commons/manager/footer.jsp" />
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom Scripts -->
<script src="${pageContext.request.contextPath}/templates/manager/js/scripts.js"></script>

<!-- Sidebar Toggle Script -->
<script>
    window.addEventListener('DOMContentLoaded', event => {
        const sidebarToggle = document.body.querySelector('#sidebarToggle');
        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', event => {
                event.preventDefault();
                document.body.classList.toggle('sb-sidenav-toggled');
                // Lưu trạng thái vào localStorage
                if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
                    localStorage.removeItem('sb|sidebar-toggle');
                } else {
                    localStorage.setItem('sb|sidebar-toggle', 'true');
                }
            });
        }

        // Khôi phục trạng thái sidebar từ localStorage
        if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
            document.body.classList.toggle('sb-sidenav-toggled');
        }
    });
</script>

<!-- Script từ trang con -->
<c:out value="${script}" escapeXml="false" />
</body>
</html>