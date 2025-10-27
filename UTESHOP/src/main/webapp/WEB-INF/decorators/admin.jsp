<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no" />
    
    <title><sitemesh:write property="title" default="Admin Panel - UTESHOP" /></title>
    
    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
    
    <!-- Admin Template CSS -->
    <link href="${pageContext.request.contextPath}/templates/admin/css/styles.css" rel="stylesheet" />
    
    <!-- Fix for header overlay issue -->
    <style>
        /* Ensure header is ALWAYS on top - highest priority */
        .sb-topnav {
            z-index: 9999 !important;
            position: fixed !important;
            top: 0 !important;
            left: 0 !important;
            right: 0 !important;
        }
        
        /* Ensure content doesn't overlay header */
        .sb-nav-fixed #layoutSidenav #layoutSidenav_content {
            margin-top: 56px !important;
            padding-top: 0 !important;
        }
        
        /* Override admin-content padding from individual pages */
        .admin-content {
            padding-top: 20px !important;
        }
        
        /* Fix sidebar z-index */
        .sb-nav-fixed #layoutSidenav #layoutSidenav_nav {
            z-index: 1040 !important;
            top: 56px !important;
        }
        
        /* Ensure dropdowns work */
        .dropdown-menu {
            z-index: 10000 !important;
        }
        
        /* Ensure all cards and content stay below header */
        .card, .table, .container-fluid, main {
            position: relative !important;
            z-index: 1 !important;
        }
    </style>
    
    <!-- Custom page styles -->
    <sitemesh:write property="head" />
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
                <!-- Page content from child pages -->
                <sitemesh:write property="body" />
            </main>
            
            <!-- Footer -->
            <jsp:include page="/commons/admin/footer.jsp" />
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Admin Template Scripts -->
    <script src="${pageContext.request.contextPath}/templates/admin/js/scripts.js"></script>
    
    <!-- Sidebar Toggle Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Sidebar toggle
            const sidebarToggle = document.getElementById('sidebarToggle');
            if (sidebarToggle) {
                sidebarToggle.addEventListener('click', function(event) {
                    event.preventDefault();
                    document.body.classList.toggle('sb-sidenav-toggled');
                    
                    // Save state to localStorage
                    localStorage.setItem('sb|sidebar-toggle', 
                        document.body.classList.contains('sb-sidenav-toggled'));
                });
            }
            
            // Restore sidebar state from localStorage
            if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
                document.body.classList.add('sb-sidenav-toggled');
            }
            
            // Auto-close dropdowns when clicking outside
            document.addEventListener('click', function(event) {
                const dropdowns = document.querySelectorAll('.dropdown-menu.show');
                dropdowns.forEach(function(dropdown) {
                    if (!dropdown.parentElement.contains(event.target)) {
                        const dropdownInstance = bootstrap.Dropdown.getInstance(
                            dropdown.previousElementSibling
                        );
                        if (dropdownInstance) {
                            dropdownInstance.hide();
                        }
                    }
                });
            });
        });
    </script>
    
    <!-- Custom page scripts -->
    <sitemesh:write property="script" />
</body>
</html>
