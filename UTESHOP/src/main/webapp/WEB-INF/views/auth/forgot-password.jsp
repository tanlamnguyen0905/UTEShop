<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" id="forgotPasswordModal" tabindex="-1" aria-labelledby="forgotPasswordModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content p-0 rounded-4 shadow-lg">
            <div class="modal-body p-0">
                <style>
#forgotPasswordModal .modal-content {
    background: linear-gradient(135deg, #8EC5FC 0%, #E0C3FC 100%);
    font-family: "Segoe UI", sans-serif;
    border-radius: 16px;
}
.forgot-card {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    padding: 35px 40px;
    max-width: 450px;
    width: 100%;
    margin: auto;
}
.forgot-card h3 { color: #6c5ce7; font-weight: 600; }
.btn-forgot { background-color: #6c5ce7; color: #fff; font-weight: 600; border-radius: 10px; transition: 0.3s; }
.btn-forgot:hover { background-color: #5941d8; }
#forgotErrorBox { display: none; background: #ffe0e0; border-left: 5px solid #d63031; color: #c0392b; padding: 10px; border-radius: 8px; margin-bottom: 15px; text-align: center; font-weight: 500; }
#forgotSuccessBox { display: none; background: #e9f7ef; border-left: 5px solid #27ae60; color: #1e8449; padding: 10px; border-radius: 8px; margin-bottom: 15px; text-align: center; font-weight: 500; }
                </style>

                <div class="forgot-card">
                    <div id="forgotErrorBox"></div>
                    <div id="forgotSuccessBox"></div>

                    <div class="text-start">
                        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Logo" style="height:60px;">
                    </div>
                    <h3 class="text-center mt-2">
                        <i class="fa-solid fa-key me-2"></i> Quên mật khẩu
                    </h3>

                    <form id="forgotForm" action="${pageContext.request.contextPath}/auth/forgot-password" method="post" class="mt-4">
                        <div class="mb-3">
                            <label for="email" class="form-label">Nhập email đăng ký</label>
                            <input type="email" id="email" name="email" class="form-control" placeholder="example@gmail.com" required>
                        </div>
                        <button type="submit" class="btn btn-forgot w-100">
                            <i class="fa-solid fa-paper-plane me-1"></i> Gửi yêu cầu
                        </button>

                        <div class="text-center mt-3">
                            <a href="#" class="text-muted" data-bs-toggle="modal" data-bs-target="#loginModal" data-bs-dismiss="modal">Quay lại đăng nhập</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("forgotForm");
    if (!form) return;

    const errorBox = document.getElementById("forgotErrorBox");
    const successBox = document.getElementById("forgotSuccessBox");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Ẩn thông báo cũ
        errorBox.style.display = "none";
        successBox.style.display = "none";
        errorBox.textContent = "";
        successBox.textContent = "";

        try {
            const formData = new FormData(form);
            const res = await fetch(form.action, {
                method: "POST",
                body: formData,
                redirect: "follow"
            });

            // ✅ Nếu server trả JSON
            const contentType = res.headers.get("content-type") || "";
            if (contentType.includes("application/json")) {
                const data = await res.json(); // 🧠 parse JSON ở đây!
                if (data.success) {
                    successBox.textContent = data.message || "Đã gửi OTP đến email!";
                    successBox.style.display = "block";

                    // 🔄 Sau 1 giây, ẩn forgot → mở reset
                    setTimeout(() => {
                        const forgotModal = bootstrap.Modal.getInstance(document.getElementById("forgotPasswordModal"));
                        if (forgotModal) forgotModal.hide();

                        const resetModal = new bootstrap.Modal(document.getElementById("resetPasswordModal"));
                        resetModal.show();
                    }, 1000);
                } else {
                    errorBox.textContent = data.error || data.message || "Không thể gửi OTP. Vui lòng thử lại.";
                    errorBox.style.display = "block";
                }
                return; // ⛔ Dừng ở đây, không chạy tiếp fallback
            }

            // ✅ Nếu server redirect (thường khi servlet dùng resp.sendRedirect)
            if (res.redirected) {
                window.location = res.url;
                return;
            }

            // ✅ Fallback nếu server không trả JSON hoặc redirect
            if (res.ok) {
                successBox.textContent = "Yêu cầu đã được xử lý. Vui lòng kiểm tra email.";
                successBox.style.display = "block";

                setTimeout(() => {
                    const forgotModal = bootstrap.Modal.getInstance(document.getElementById("forgotPasswordModal"));
                    if (forgotModal) forgotModal.hide();

                    const resetModal = new bootstrap.Modal(document.getElementById("resetPasswordModal"));
                    resetModal.show();
                }, 1000);
            } else {
                errorBox.textContent = "Không thể xử lý yêu cầu. Mã lỗi: " + res.status;
                errorBox.style.display = "block";
            }

        } catch (err) {
            console.error("❌ Lỗi khi gửi yêu cầu:", err);
            errorBox.textContent = "Có lỗi xảy ra. Vui lòng thử lại.";
            errorBox.style.display = "block";
        }
    });
});

</script>
