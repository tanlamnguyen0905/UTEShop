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
    
    <!-- Custom CSS for Admin -->
    <style>
        body {
            min-height: 100vh;
        }
        
        .sb-nav-fixed {
            padding-top: 56px;
        }
        
        #layoutSidenav {
            display: flex;
        }
        
        #layoutSidenav_nav {
            flex-basis: 225px;
            flex-shrink: 0;
            transition: transform 0.15s ease-in-out;
        }
        
        #layoutSidenav_content {
            position: relative;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            min-width: 0;
            flex-grow: 1;
            min-height: calc(100vh - 56px);
        }
        
        .sb-topnav {
            padding-left: 0;
            height: 56px;
            z-index: 1039;
        }
        
        .sb-topnav .navbar-brand {
            width: 225px;
            margin: 0;
            padding-left: 1rem;
            padding-right: 1rem;
        }
        
        .sb-sidenav {
            display: flex;
            flex-direction: column;
            height: 100%;
            flex-wrap: nowrap;
        }
        
        .sb-sidenav-dark {
            background-color: #212529;
            color: rgba(255, 255, 255, 0.5);
        }
        
        .sb-sidenav-dark .sb-sidenav-menu {
            background-color: #212529;
        }
        
        .sb-sidenav-dark .nav-link {
            color: rgba(255, 255, 255, 0.5);
            padding: 0.75rem 1rem;
        }
        
        .sb-sidenav-dark .nav-link:hover {
            color: #fff;
        }
        
        .sb-sidenav-dark .nav-link.active {
            color: #fff;
            background-color: rgba(255, 255, 255, 0.075);
        }
        
        .sb-sidenav-dark .sb-sidenav-menu-heading {
            color: rgba(255, 255, 255, 0.25);
            padding: 1.75rem 1rem 0.75rem;
            text-transform: uppercase;
            font-size: 0.75rem;
            font-weight: 800;
        }
        
        .sb-sidenav-dark .sb-sidenav-footer {
            background-color: #343a40;
            padding: 0.75rem;
        }
        
        .sb-nav-link-icon {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 1rem;
            font-size: 0.9rem;
            margin-right: 0.5rem;
        }
    </style>
    
    <decorator:head />
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
</body>
</html>
