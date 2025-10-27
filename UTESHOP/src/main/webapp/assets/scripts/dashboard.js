(function () {
    const apiBase = `${contextPath}/api/admin/dashboard`;

    // DOM references
    const kpiRow = document.getElementById('kpiRow');
    const topProductsTbody = document.querySelector('#topProductsTable tbody');
    const topCustomersTbody = document.querySelector('#topCustomersTable tbody');
    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
    const statusCtx = document.getElementById('statusChart').getContext('2d');
    const categoryCtx = document.getElementById('categoryChart').getContext('2d');

    // Chart instances
    let revenueChart = null;
    let statusChart = null;
    let categoryChart = null;

    // Helpers
    function formatCurrency(v) {
        return Number(v || 0).toLocaleString('vi-VN') + ' ₫';
    }

    function showEmptyTable(tbody, colspan, msg) {
        tbody.innerHTML = `<tr><td colspan="${colspan}" class="text-center text-muted">${msg}</td></tr>`;
    }

    // Fetch all required endpoints in parallel
    async function loadDashboard() {
        try {
            const [
                statsRes, revRes, statusRes, topProdRes, topCustRes, catRes
            ] = await Promise.all([
                fetch(apiBase + '/stats'),
                fetch(apiBase + '/revenue-daily'),
                fetch(apiBase + '/order-status'),
                fetch(apiBase + '/top-products'),
                fetch(apiBase + '/top-customers'),
                fetch(apiBase + '/category-sales')
            ]);

            if (!statsRes.ok) throw new Error('stats');
            if (!revRes.ok) throw new Error('revenue-daily');
            if (!statusRes.ok) throw new Error('order-status');
            if (!topProdRes.ok) throw new Error('top-products');
            if (!topCustRes.ok) throw new Error('top-customers');
            if (!catRes.ok) throw new Error('category-sales');

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
            kpiRow.innerHTML = '<div class="alert alert-danger">Không thể tải dữ liệu dashboard.</div>';
        }
    }

    // KPI rendering
    function renderKPIs(stats) {
        // expected fields in stats: totalRevenue, totalOrders, totalCustomers, totalProducts, totalReviews, avgStars
        kpiRow.innerHTML = '';
        const cards = [
            {label: 'Doanh thu', value: formatCurrency(stats.totalRevenue), sub: 'Đã giao & thanh toán', cls:'gradient-green'},
            {label: 'Đơn hàng', value: (stats.totalOrders || 0), sub: 'Tổng phát sinh', cls:'gradient-blue'},
            {label: 'Khách hàng', value: (stats.totalCustomers || 0), sub: 'Đã đăng ký', cls:'gradient-orange'},
            {label: 'Sản phẩm', value: (stats.totalProducts || 0), sub: 'Đang hoạt động', cls:'gradient-purple'},
            {label: 'Đánh giá', value: ((stats.totalReviews || 0) + ' / ' + (stats.avgStars ? stats.avgStars.toFixed(1)+' ★' : '0.0 ★')), sub: '' , cls:'gradient-teal'},
            {label: 'Tỷ lệ đánh giá', value: (stats.avgStars != null ? (Number(stats.avgStars).toFixed(1)) : '0'), sub: 'Điểm trung bình', cls:'gradient-gray'}
        ];
        cards.forEach(c => {
            const col = document.createElement('div');
            col.className = 'col-6 col-md-4 col-lg-2';
            col.innerHTML = `
                <div class="kpi-card ${c.cls} h-100">
                    <div class="kpi-label">${c.label}</div>
                    <div class="kpi-value">${c.value}</div>
                    <div class="kpi-sub">${c.sub}</div>
                </div>
            `;
            kpiRow.appendChild(col);
        });
    }

    // Revenue line chart
    function renderRevenueChart(data) {
        const labels = data.map(d => d.date);
        const vals = data.map(d => Number(d.total || 0));
        if (revenueChart) revenueChart.destroy();
        revenueChart = new Chart(revenueCtx, {
            type: 'line',
            data: {
                labels,
                datasets: [{ label: 'Doanh thu (₫)', data: vals, tension: .35, fill: true, borderColor: '#198754', backgroundColor: 'rgba(25,135,84,.15)', pointRadius: 3 }]
            },
            options: { plugins: { legend: { display: false } }, scales: { y: { ticks: { callback: v => v.toLocaleString('vi-VN') } } } }
        });
    }

    // Status donut
    function renderStatusChart(data) {
        const labels = data.map(d => d.status || 'N/A');
        const vals = data.map(d => Number(d.count || 0));
        if (statusChart) statusChart.destroy();
        statusChart = new Chart(statusCtx, {
            type: 'doughnut',
            data: { labels, datasets: [{ data: vals, backgroundColor: ['#0d6efd', '#ffc107', '#dc3545', '#198754', '#6f42c1', '#20c997'] }] },
            options: { plugins: { legend: { position: 'bottom' } }, cutout: '55%' }
        });
    }

    // Top products
    function renderTopProducts(data) {
        topProductsTbody.innerHTML = '';
        if (!data || data.length === 0) {
            showEmptyTable(topProductsTbody, 4, 'Chưa có dữ liệu.');
            return;
        }
        data.forEach((p, i) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${i+1}</td><td>${p.name}</td><td class="text-end">${p.qty}</td><td class="text-end">${formatCurrency(p.amount)}</td>`;
            topProductsTbody.appendChild(tr);
        });
    }

    // Top customers
    function renderTopCustomers(data) {
        topCustomersTbody.innerHTML = '';
        if (!data || data.length === 0) {
            showEmptyTable(topCustomersTbody, 4, 'Chưa có dữ liệu.');
            return;
        }
        data.forEach((c, i) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${i+1}</td><td>${c.name}</td><td class="text-end">${c.orders}</td><td class="text-end">${formatCurrency(c.spend)}</td>`;
            topCustomersTbody.appendChild(tr);
        });
    }

    // Category bar
    function renderCategoryChart(data) {
        const labels = data.map(d => d.name || 'Không rõ');
        const vals = data.map(d => Number(d.amount || 0));
        if (categoryChart) categoryChart.destroy();
        categoryChart = new Chart(categoryCtx, {
            type: 'bar',
            data: { labels, datasets: [{ label: 'Doanh thu (₫)', data: vals, backgroundColor: '#6610f2' }] },
            options: { plugins: { legend: { display: false } }, scales: { y: { ticks: { callback: v => v.toLocaleString('vi-VN') } } } }
        });
    }

    // initialize
    document.addEventListener('DOMContentLoaded', loadDashboard);
})();
