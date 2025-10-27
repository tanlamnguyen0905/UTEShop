document.addEventListener("DOMContentLoaded", () => {
	const apiUrl = `${contextPath}/api/admin/accounts`;
	const searchInput = document.getElementById("searchInput");
	const roleSelect = document.getElementById("roleSelect");
	const statusSelect = document.getElementById("statusSelect");
	const messageDiv = document.getElementById("message");
	const table = document.getElementById("accountsTable");
	const tbody = table.querySelector("tbody");
	const userModal = new bootstrap.Modal(document.getElementById("acc_mdl_userModal"));
	const deleteModal = new bootstrap.Modal(document.getElementById("acc_mdl_deleteModal"));
	const form = document.getElementById("acc_mdl_userForm");
	const btnAddUser = document.getElementById("btnAddUser");

	let editingUser = null;

	// Load accounts
	fetchAccounts();

	[searchInput, roleSelect, statusSelect].forEach(el => {
		el.addEventListener("input", debounce(fetchAccounts, 400));
		// el.addEventListener("change", fetchAccounts);
	});

	function debounce(fn, delay) {
		let timeout;
		return function(...args) {
			clearTimeout(timeout);
			timeout = setTimeout(() => fn.apply(this, args), delay);
		};
	}

	async function fetchAccounts() {
		const search = searchInput.value.trim();
		const role = roleSelect.value;
		const status = statusSelect.value;

		messageDiv.classList.remove("d-none");
		messageDiv.textContent = "Đang tải thông tin...";
		table.classList.add("d-none");

		try {
			const params = new URLSearchParams({ search, role, status });
			const res = await fetch(`${apiUrl}/list?${params.toString()}`);
			const data = await res.json();
			if (!res.ok)
				throw new Error(data.error ?? res.statusText);
			renderTable(data);
		} catch (err) {
			console.error(err);
			messageDiv.textContent = `Đã có lỗi ${err}`;
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
		document.getElementById("acc_mdl_avatarPreview").src = "";
		document.getElementById("acc_mdl_userModalLabel").textContent = "Thêm tài khoản";
		userModal.show();
	};

	function openEdit(user) {
		editingUser = user;
		document.getElementById("acc_mdl_userModalLabel").textContent = "Chỉnh sửa tài khoản";
		document.getElementById("acc_mdl_userId").value = user.userID;
		document.getElementById("acc_mdl_username").value = user.username;
		document.getElementById("acc_mdl_fullname").value = user.fullname || "";
		document.getElementById("acc_mdl_email").value = user.email || "";
		document.getElementById("acc_mdl_phone").value = user.phone || "";
		document.getElementById("acc_mdl_role").value = user.role || "USER";
		document.getElementById("acc_mdl_status").value = user.status || "ACTIVE";
		document.getElementById("acc_mdl_password").value = "";
		document.getElementById("acc_mdl_password2").value = "";
		document.getElementById("acc_mdl_avatarPreview").src = user.avatar ? `${contextPath}/assets/images/${user.avatar}` : "";
		userModal.show();
	}

	form.onsubmit = async e => {
		e.preventDefault();

		const payload = {
			username: document.getElementById("acc_mdl_username").value,
			fullname: document.getElementById("acc_mdl_fullname").value,
			email: document.getElementById("acc_mdl_email").value,
			phone: document.getElementById("acc_mdl_phone").value,
			role: document.getElementById("acc_mdl_role").value,
			status: document.getElementById("acc_mdl_status").value,
			password: document.getElementById("acc_mdl_password").value
		};

		const password2 = document.getElementById("acc_mdl_password2").value;
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
