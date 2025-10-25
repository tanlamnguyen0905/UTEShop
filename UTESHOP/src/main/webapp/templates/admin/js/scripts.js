/*!
    * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2023 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
// 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

    // Sidebar Collapse - Simple Toggle
    document.querySelectorAll('[data-bs-toggle="collapse"]').forEach(function (toggle) {
        toggle.addEventListener('click', function (e) {
            e.preventDefault();

            const targetId = this.getAttribute('data-bs-target');
            const target = document.querySelector(targetId);

            if (target) {
                // Toggle collapse
                target.classList.toggle('show');
                this.classList.toggle('collapsed');

                // Toggle arrow icon
                const arrow = this.querySelector('.sb-sidenav-collapse-arrow i');
                if (arrow) {
                    if (target.classList.contains('show')) {
                        arrow.classList.remove('fa-angle-down');
                        arrow.classList.add('fa-angle-up');
                    } else {
                        arrow.classList.remove('fa-angle-up');
                        arrow.classList.add('fa-angle-down');
                    }
                }
            }
        });
    });

});
