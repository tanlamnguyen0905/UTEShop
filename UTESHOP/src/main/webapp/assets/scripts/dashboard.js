(function () {
    const apiBase = `${contextPath}/api/admin/dashboard`;

    // DOM references
    const kpiRow = document.getElementById('kpiRow');
    const topProductsTbody = document.querySelector('#topProductsTable tbody');
    const topCustomersTbody = document.querySelector('#topCustomersTable tbody');
    const revenueCtx = document.getElementById('revenueChart');
    const statusCtx = document.getElementById('statusChart');
    const categoryCtx = document.getElementById('categoryChart');

    // Chart instances
    let revenueChart = null;
    let statusChart = null;
    let categoryChart = null;

    // Helpers
    function formatCurrency(v) {
        return Number(v || 0).toLocaleString('vi-VN') + ' ₫';
    }

    function showEmptyTable(tbody, colspan, msg) {
        tbody.innerHTML = `<tr><td colspan="${colspan}" class="text-center text-muted py-4">${msg}</td></tr>`;
    }

    function showLoading(element, message = 'Đang tải...') {
        element.innerHTML = `
            <div class="text-center py-4">
                <div class="spinner-border spinner-border-sm text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <div class="mt-2 text-muted">${message}</div>
            </div>
        `;
    }

    // Fetch all required endpoints in parallel
    async function loadDashboard() {
        try {
            // Show loading states
            kpiRow.innerHTML = '<div class="col-12"><div class="alert alert-info"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</div></div>';
            showLoading(topProductsTbody, 'Đang tải sản phẩm...');
            showLoading(topCustomersTbody, 'Đang tải khách hàng...');

            const [
                statsRes, revRes, statusRes, topProdRes, topCustRes, catRes
            ] = await Promise.all([
                fetch(apiBase + '/stats'),
                fetch(apiBase + '/revenue-daily'),
                fetch(apiBase + '/order-status'),
                fetch(apiBase + '/top-products?limit=5'),
                fetch(apiBase + '/top-customers?limit=5'),
                fetch(apiBase + '/category-sales')
            ]);

            if (!statsRes.ok) throw new Error('Không thể tải thống kê');
            if (!revRes.ok) throw new Error('Không thể tải doanh thu');
            if (!statusRes.ok) throw new Error('Không thể tải trạng thái đơn hàng');
            if (!topProdRes.ok) throw new Error('Không thể tải top sản phẩm');
            if (!topCustRes.ok) throw new Error('Không thể tải top khách hàng');
            if (!catRes.ok) throw new Error('Không thể tải doanh thu danh mục');

            const stats = await statsRes.json();
            const revenueDaily = await revRes.json();
            const orderStatus = await statusRes.json();
            const topProducts = await topProdRes.json();
            const topCustomers = await topCustRes.json();
            const categorySales = await catRes.json();

            renderKPIs(stats);
            renderRevenueChart(revenueDaily);
            renderStatusChart(orderStatus);
            renderTopProducts(topProducts);
            renderTopCustomers(topCustomers);
            renderCategoryChart(categorySales);

        } catch (err) {
            console.error('Failed to load dashboard', err);
            kpiRow.innerHTML = `
                <div class="col-12">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-triangle"></i> 
                        Không thể tải dữ liệu dashboard: ${err.message}
                    </div>
                </div>
            `;
        }
    }

    // KPI rendering with Dashboard B style
    function renderKPIs(stats) {
        kpiRow.innerHTML = '';
        
        const cards = [
            {
                label: 'Tổng sản phẩm',
                value: stats.totalProducts || 0,
                sub: 'Đang hoạt động',
                icon: 'fa-box',
                color: 'primary',
                gradient: 'gradient-blue'
            },
            {
                label: 'Đơn hàng',
                value: stats.totalOrders || 0,
                sub: 'Tổng phát sinh',
                icon: 'fa-shopping-cart',
                color: 'success',
                gradient: 'gradient-green'
            },
            {
                label: 'Khách hàng',
                value: stats.totalCustomers || 0,
                sub: 'Đã đăng ký',
                icon: 'fa-users',
                color: 'info',
                gradient: 'gradient-teal'
            },
            {
                label: 'Doanh thu',
                value: formatCurrency(stats.totalRevenue),
                sub: 'Tổng thu nhập',
                icon: 'fa-dollar-sign',
                color: 'warning',
                gradient: 'gradient-orange'
            },
            {
                label: 'Đánh giá',
                value: (stats.totalReviews || 0) + ' đánh giá',
                sub: (stats.avgStars ? stats.avgStars.toFixed(1) + ' ★' : '0.0 ★'),
                icon: 'fa-star',
                color: 'purple',
                gradient: 'gradient-purple'
            },
            {
                label: 'Điểm trung bình',
                value: (stats.avgStars != null ? Number(stats.avgStars).toFixed(1) : '0.0'),
                sub: 'Từ khách hàng',
                icon: 'fa-chart-line',
                color: 'secondary',
                gradient: 'gradient-gray'
            }
        ];

        cards.forEach(card => {
            const col = document.createElement('div');
            col.className = 'col-xl-2 col-md-4 col-sm-6 mb-3';
            col.innerHTML = `
                <div class="dashboard-card ${card.gradient}">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div class="flex-grow-1">
                                <div class="stat-label text-white-50">${card.label}</div>
                                <div class="stat-value text-white">${card.value}</div>
                                <small class="text-white-50">${card.sub}</small>
                            </div>
                            <div class="card-icon-float">
                                <i class="fas ${card.icon}"></i>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            kpiRow.appendChild(col);
        });
    }

    // Revenue line chart
    function renderRevenueChart(data) {
        if (!data || data.length === 0) {
            revenueCtx.parentElement.innerHTML = '<p class="text-center text-muted py-4">Không có dữ liệu doanh thu</p>';
            return;
        }

        const labels = data.map(d => d.date);
        const vals = data.map(d => Number(d.total || 0));
        
        if (revenueChart) revenueChart.destroy();
        
        revenueChart = new Chart(revenueCtx.getContext('2d'), {
            type: 'line',
            data: {
                labels,
                datasets: [{
                    label: 'Doanh thu (₫)',
                    data: vals,
                    tension: 0.4,
                    fill: true,
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.1)',
                    pointRadius: 4,
                    pointBackgroundColor: 'rgb(75, 192, 192)',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Doanh thu: ' + formatCurrency(context.parsed.y);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: v => formatCurrency(v)
                        }
                    }
                }
            }
        });
    }

    // Status doughnut chart
    function renderStatusChart(data) {
        if (!data || data.length === 0) {
            statusCtx.parentElement.innerHTML = '<p class="text-center text-muted py-4">Không có dữ liệu trạng thái</p>';
            return;
        }

        const stats = Object.entries(data);
        const labels = stats.map(d => d[0] || 'N/A');
        const vals = stats.map(d => Number(d[1] || 0));
        
        if (statusChart) statusChart.destroy();
        
        statusChart = new Chart(statusCtx.getContext('2d'), {
            type: 'doughnut',
            data: {
                labels,
                datasets: [{
                    data: vals,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.8)',
                        'rgba(54, 162, 235, 0.8)',
                        'rgba(255, 206, 86, 0.8)',
                        'rgba(75, 192, 192, 0.8)',
                        'rgba(153, 102, 255, 0.8)',
                        'rgba(255, 159, 64, 0.8)'
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                },
                cutout: '60%'
            }
        });
    }

    // Top products table
    function renderTopProducts(data) {
        topProductsTbody.innerHTML = '';
        
        if (!data || data.length === 0) {
            showEmptyTable(topProductsTbody, 4, '<i class="fas fa-info-circle"></i> Chưa có dữ liệu sản phẩm');
            return;
        }
        
        data.forEach((p, i) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>
                    <span class="badge ${i < 3 ? 'bg-warning text-dark' : 'bg-secondary'}">
                        ${i < 3 ? '<i class="fas fa-fire"></i>' : ''} #${i + 1}
                    </span>
                </td>
                <td><strong>${p.name}</strong></td>
                <td class="text-end">${p.qty}</td>
                <td class="text-end"><strong>${formatCurrency(p.amount)}</strong></td>
            `;
            topProductsTbody.appendChild(tr);
        });
    }

    // Top customers table
    function renderTopCustomers(data) {
        topCustomersTbody.innerHTML = '';
        
        if (!data || data.length === 0) {
            showEmptyTable(topCustomersTbody, 4, '<i class="fas fa-info-circle"></i> Chưa có dữ liệu khách hàng');
            return;
        }
        
        data.forEach((c, i) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>
                    <span class="badge ${i < 3 ? 'bg-success' : 'bg-secondary'}">
                        ${i < 3 ? '<i class="fas fa-crown"></i>' : ''} #${i + 1}
                    </span>
                </td>
                <td><strong>${c.name}</strong></td>
                <td class="text-end">${c.orders} đơn</td>
                <td class="text-end"><strong>${formatCurrency(c.spend)}</strong></td>
            `;
            topCustomersTbody.appendChild(tr);
        });
    }

    // Category bar chart
    function renderCategoryChart(data) {
        if (!data || data.length === 0) {
            categoryCtx.parentElement.innerHTML = '<p class="text-center text-muted py-4">Không có dữ liệu danh mục</p>';
            return;
        }

        const labels = data.map(d => d.name || 'Không rõ');
        const vals = data.map(d => Number(d.amount || 0));
        
        if (categoryChart) categoryChart.destroy();
        
        categoryChart = new Chart(categoryCtx.getContext('2d'), {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'Doanh thu (₫)',
                    data: vals,
                    backgroundColor: 'rgba(102, 16, 242, 0.8)',
                    borderColor: 'rgba(102, 16, 242, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Doanh thu: ' + formatCurrency(context.parsed.y);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: v => formatCurrency(v)
                        }
                    }
                }
            }
        });
    }

    // Initialize on DOM ready
    document.addEventListener('DOMContentLoaded', loadDashboard);
})();