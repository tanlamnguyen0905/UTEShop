document.addEventListener("DOMContentLoaded", () => {
    const apiUrl = `${contextPath}/api/admin/user`;
    const searchInput = document.getElementById("searchInput");
    const roleSelect = document.getElementById("roleSelect");
    const statusSelect = document.getElementById("statusSelect");
    const messageDiv = document.getElementById("message");
    const tableContainer = document.getElementById("usersTableContainer");
    const tbody = document.querySelector("#usersTable tbody");
    const addUserModal = new bootstrap.Modal(document.getElementById("addUserModal"));
    const deleteModal = new bootstrap.Modal(document.getElementById("deleteModal"));
    const btnAddUser = document.getElementById("btnAddUser");
    const addUserForm = document.getElementById("addUserForm");

    // Load users on init
    fetchUsers();

    // Search and filter listeners
    [searchInput, roleSelect, statusSelect].forEach(el => {
        el.addEventListener("input", debounce(fetchUsers, 400));
    });

    function debounce(fn, delay) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => fn.apply(this, args), delay);
        };
    }

    async function fetchUsers() {
        const search = searchInput.value.trim();
        const role = roleSelect.value;
        const status = statusSelect.value;

        messageDiv.classList.remove("d-none");
        messageDiv.textContent = "Đang tải thông tin...";
        tableContainer.classList.add("d-none");

        try {
            const params = new URLSearchParams({ search, role, status });
            const res = await fetch(`${apiUrl}/list?${params.toString()}`);
            const data = await res.json();
            if (!res.ok) throw new Error(data.error ?? res.statusText);
            renderTable(data);
        } catch (err) {
            console.error(err);
            messageDiv.textContent = `Đã có lỗi: ${err.message}`;
        }
    }

    function renderTable(data) {
        tbody.innerHTML = "";
        if (!data || data.length === 0) {
            messageDiv.textContent = "Không có dữ liệu";
            messageDiv.classList.remove("d-none");
            tableContainer.classList.add("d-none");
            return;
        }

        data.forEach(u => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td><strong>${u.username}</strong></td>
                <td>${u.fullname || ""}</td>
                <td>${u.email || ""}</td>
                <td>${u.phone || ""}</td>
                <td>${getRoleBadge(u.role)}</td>
                <td>${getStatusBadge(u.status)}</td>
                <td>${formatDate(u.createAt)}</td>
                <td class="text-center">
                    <a href="${contextPath}/api/admin/user/edit?id=${u.userID}" 
                       class="btn btn-sm btn-warning me-1" title="Sửa">
                        <i class="fas fa-edit"></i>
                    </a>
                    <button class="btn btn-sm btn-danger btn-delete" 
                            data-user-id="${u.userID}"
                            data-username="${u.username}"
                            title="Xóa">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>`;
            
            tr.querySelector(".btn-delete").onclick = function() {
                openDelete(this.getAttribute('data-user-id'), this.getAttribute('data-username'));
            };
            
            tbody.appendChild(tr);
        });

        tableContainer.classList.remove("d-none");
        messageDiv.classList.add("d-none");
    }

    function getRoleBadge(role) {
        const badges = {
            'ADMIN': '<span class="badge bg-danger"><i class="fas fa-user-shield me-1"></i>Admin</span>',
            'MANAGER': '<span class="badge bg-purple"><i class="fas fa-user-tie me-1"></i>Manager</span>',
            'SHIPPER': '<span class="badge bg-success"><i class="fas fa-shipping-fast me-1"></i>Shipper</span>',
            'USER': '<span class="badge bg-info"><i class="fas fa-user me-1"></i>User</span>'
        };
        return badges[role] || `<span class="badge bg-secondary">${role}</span>`;
    }

    function getStatusBadge(status) {
        if (status === 'ACTIVE' || status === 'active') {
            return '<span class="badge bg-success">Hoạt động</span>';
        } else if (status === 'LOCKED') {
            return '<span class="badge bg-danger">Khóa</span>';
        } else if (status === 'PENDING') {
            return '<span class="badge bg-warning text-dark">Chờ duyệt</span>';
        }
        return `<span class="badge bg-secondary">${status}</span>`;
    }

    function formatDate(dateStr) {
        if (!dateStr) return '-';
        return dateStr.toString().replace('T', ' ').substring(0, 16);
    }

    // Add user button
    btnAddUser.onclick = () => {
        addUserForm.reset();
        addUserModal.show();
    };

    // Validate password match before submit
    addUserForm.onsubmit = function(e) {
        const password = document.getElementById('addPassword').value;
        const password2 = document.getElementById('addPassword2').value;
        
        if (password !== password2) {
            e.preventDefault();
            alert('Mật khẩu không khớp!');
            return false;
        }
        return true;
    };

    // Delete user
    function openDelete(userId, username) {
        document.getElementById("deleteUsername").textContent = username;
        document.getElementById("deleteForm").action = `${contextPath}/api/admin/user/delete?id=${userId}`;
        deleteModal.show();
    }
});