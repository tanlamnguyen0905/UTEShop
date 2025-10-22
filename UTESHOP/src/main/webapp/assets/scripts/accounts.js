document.addEventListener("DOMContentLoaded", () => {
    const apiUrl = `${contextPath}/api/admin/accounts`;
    const messageDiv = document.getElementById("message");
    const table = document.getElementById("accountsTable");
    const tbody = table.querySelector("tbody");
    const userModal = new bootstrap.Modal(document.getElementById("userModal"));
    const deleteModal = new bootstrap.Modal(document.getElementById("deleteModal"));
    const form = document.getElementById("userForm");
    const btnAddUser = document.getElementById("btnAddUser");

    let editingUser = null;

    // Load accounts
    fetchAccounts();

    async function fetchAccounts() {
        messageDiv.textContent = "Đang tải thông tin...";
        table.classList.add("d-none");

        try {
            const res = await fetch(`${apiUrl}/list`);
            if (!res.ok) throw new Error();
            const data = await res.json();
            renderTable(data);
        } catch {
            messageDiv.textContent = "Đã có lỗi :(";
        }
    }

    function renderTable(data) {
        tbody.innerHTML = "";
        if (!data || data.length === 0) {
            messageDiv.textContent = "Không có dữ liệu";
            messageDiv.classList.remove("d-none");
            table.classList.add("d-none");
            return;
        }

        data.forEach(u => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${u.username}</td>
                <td>${u.fullname || ""}</td>
                <td>${u.email || ""}</td>
                <td>${u.phone || ""}</td>
                <td>${u.role || ""}</td>
                <td>${u.status || ""}</td>
                <td class="text-center">
                    <button class="btn btn-sm btn-warning me-2 btn-edit">Sửa</button>
                    <button class="btn btn-sm btn-danger btn-delete">Xóa</button>
                </td>`;
            tr.querySelector(".btn-edit").onclick = () => openEdit(u);
            tr.querySelector(".btn-delete").onclick = () => openDelete(u);
            tbody.appendChild(tr);
        });

        table.classList.remove("d-none");
        messageDiv.classList.add("d-none");
    }

    // --- Modal handling ---
    btnAddUser.onclick = () => {
        editingUser = null;
        form.reset();
        document.getElementById("avatarPreview").src = "";
        document.getElementById("userModalLabel").textContent = "Thêm tài khoản";
        userModal.show();
    };

    function openEdit(user) {
        editingUser = user;
        document.getElementById("userModalLabel").textContent = "Chỉnh sửa tài khoản";
        document.getElementById("userId").value = user.userID;
        document.getElementById("username").value = user.username;
        document.getElementById("fullname").value = user.fullname || "";
        document.getElementById("email").value = user.email || "";
        document.getElementById("phone").value = user.phone || "";
        document.getElementById("role").value = user.role || "USER";
        document.getElementById("status").value = user.status || "ACTIVE";
        document.getElementById("password").value = "";
        document.getElementById("password2").value = "";
        document.getElementById("avatarPreview").src = user.avatar ? `${contextPath}/assets/images/${user.avatar}` : "";
        userModal.show();
    }

    form.onsubmit = async e => {
        e.preventDefault();

        const payload = {
            username: document.getElementById("username").value,
            fullname: document.getElementById("fullname").value,
            email: document.getElementById("email").value,
            phone: document.getElementById("phone").value,
            role: document.getElementById("role").value,
            status: document.getElementById("status").value,
            password: document.getElementById("password").value
        };

        const password2 = document.getElementById("password2").value;
        if (payload.password !== password2) {
            alert("Mật khẩu không khớp!");
            return;
        }

        const method = editingUser ? "PUT" : "POST";
        const url = editingUser ? `${apiUrl}/update/${editingUser.userID}` : `${apiUrl}/create`;

        try {
            const res = await fetch(url, {
                method,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });
            if (!res.ok) throw new Error();
            userModal.hide();
            fetchAccounts();
        } catch {
            alert("Không thể lưu tài khoản!");
        }
    };

    function openDelete(user) {
        document.getElementById("deleteUsername").textContent = user.username;
        document.getElementById("confirmDeleteBtn").onclick = async () => {
            try {
                const res = await fetch(`${apiUrl}/delete/${user.userID}`, { method: "DELETE" });
                if (!res.ok) throw new Error();
                deleteModal.hide();
                fetchAccounts();
            } catch {
                alert("Xóa thất bại!");
            }
        };
        deleteModal.show();
    }
});
