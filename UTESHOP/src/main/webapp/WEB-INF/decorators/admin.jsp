<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.sitemesh.com/decorator" prefix="decorator" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    
    <title><decorator:title default="Admin Panel - UTESHOP" /></title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">


    <!-- Head content từ trang con -->
    <c:out value="${head}" escapeXml="false" />
</head>

<body class="sb-nav-fixed">
    <!-- Header -->
    <jsp:include page="/commons/admin/header.jsp" />
    
    <div id="layoutSidenav">
        <!-- Sidebar -->
        <jsp:include page="/commons/admin/sidebar.jsp" />
        
        <!-- Main content -->
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <!-- Page content -->
                    <decorator:body />
                </div>
            </main>
            
            <!-- Footer -->
            <jsp:include page="/commons/admin/footer.jsp" />
        </div>
    </div>

<<<<<<< HEAD
<!-- Core JS -->
<script src="${pageContext.request.contextPath}/templates/admin/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/templates/admin/js/scripts.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script từ trang con -->
<c:out value="${script}" escapeXml="false" />
=======
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Sidebar Toggle Script -->
    <script>
        window.addEventListener('DOMContentLoaded', event => {
            const sidebarToggle = document.body.querySelector('#sidebarToggle');
            if (sidebarToggle) {
                sidebarToggle.addEventListener('click', event => {
                    event.preventDefault();
                    document.body.classList.toggle('sb-sidenav-toggled');
                });
            }
        });
    </script>
>>>>>>> origin/trung
</body>
</html>
