<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thanh toán đơn hàng</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
body {
	background: #fff;
	font-family: Arial, sans-serif;
}

.checkout-container {
	max-width: 1200px;
	margin: 30px auto;
}

.checkout-title {
	font-size: 1.5rem;
	margin-bottom: 20px;
	font-weight: 600;
}

.payment-method {
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 12px;
	margin-bottom: 10px;
	cursor: pointer;
}

.payment-method input {
	margin-right: 10px;
}

.order-summary {
	background: #f9f9f9;
	border-radius: 8px;
	padding: 20px;
}

.order-summary h5 {
	font-weight: 600;
	margin-bottom: 15px;
}

.btn-order {
	width: 100%;
	background: #333;
	color: #fff;
	font-size: 18px;
	padding: 12px;
	border-radius: 6px;
}

.btn-order:hover {
	background: #000;
}
</style>
</head>
<body>
	<div class="checkout-container container">
		<div class="row">
			<!-- Form thông tin giao hàng -->
			<div class="col-md-7">
				<div class="checkout-title">Thông tin vận chuyển</div>
				<form>
					<div class="mb-3 row">
						<div class="col">
							<label class="form-label">Họ tên</label> <input type="text"
								class="form-control" placeholder="Nhập họ tên của bạn">
						</div>
						<div class="col">
							<label class="form-label">Số điện thoại</label> <input
								type="text" class="form-control"
								placeholder="Nhập số điện thoại">
						</div>
					</div>
					<div class="mb-3">
						<label class="form-label">Email</label> <input type="email"
							class="form-control" placeholder="Nhập email của bạn">
					</div>
					<div class="mb-3">
						<label class="form-label">Địa chỉ</label> <input type="text"
							class="form-control" placeholder="Nhập địa chỉ">
					</div>
					<div class="mb-3 row">
						<div class="col">
							<label class="form-label">Tỉnh/Thành phố</label> <select
								class="form-select">
								<option>Chọn...</option>
								<option>Hà Nội</option>
								<option>TP.HCM</option>
							</select>
						</div>
						<div class="col">
							<label class="form-label">Ghi chú</label> <input type="text"
								class="form-control" placeholder="Nhập ghi chú">
						</div>
					</div>
					<div class="form-check mb-2">
						<input class="form-check-input" type="checkbox" id="otherReceiver">
						<label class="form-check-label" for="otherReceiver"> Gọi
							người khác nhận hàng (nếu có) </label>
					</div>
					<div class="form-check mb-4">
						<input class="form-check-input" type="checkbox" id="vat">
						<label class="form-check-label" for="vat"> Xuất hoá đơn
							VAT </label>
					</div>

					<style>
.payment-option {
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 12px 16px;
	margin-bottom: 10px;
	cursor: pointer;
	display: flex;
	align-items: center;
}

.payment-option input {
	margin-right: 10px;
}

/* Khi chọn thì highlight */
.payment-option input:checked+span {
	font-weight: bold;
	color: #0d6efd;
}
</style>

					<h4>Hình thức thanh toán</h4>

					<label class="payment-option"> <input type="radio"
						name="payment" value="cod" checked> <span>Thanh
							toán khi nhận hàng</span>
					</label> <label class="payment-option"> <input type="radio"
						name="payment" value="zalopay"> <span>Thanh toán
							qua ZaloPay</span>
					</label> <label class="payment-option"> <input type="radio"
						name="payment" value="applepay"> <span>Apple Pay</span>
					</label> <label class="payment-option"> <input type="radio"
						name="payment" value="momo"> <span>Ví Momo</span>
					</label> <label class="payment-option"> <input type="radio"
						name="payment" value="vnpay"> <span>Ví điện tử
							VNPay</span>
					</label>
				</form>
			</div>

			<!-- Tóm tắt đơn hàng -->
			<div class="col-md-5">
				<div class="order-summary">
					<h5>Chi tiết thanh toán</h5>
					<div class="d-flex justify-content-between mb-2">
						<span>Tạm tính</span> <span>0đ</span>
					</div>
					<div class="d-flex justify-content-between mb-2">
						<span>Voucher giảm giá</span> <span>-0đ</span>
					</div>
					<hr>
					<div class="d-flex justify-content-between mb-3">
						<strong>Thành tiền</strong> <strong>0đ</strong>
					</div>
					<button class="btn-order">ĐẶT HÀNG</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
