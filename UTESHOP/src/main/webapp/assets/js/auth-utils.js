/**
 * Authentication Utilities
 * Quản lý JWT token và thông tin người dùng trong localStorage
 */

const AuthUtils = {
	// Token expiration: 1 giờ (phải khớp với backend)
	TOKEN_EXPIRATION: 3600000, // 1 giờ = 60 * 60 * 1000 ms

	/**
	 * Lưu thông tin đăng nhập (Token đã được lưu vào Cookie từ server)
	 */
	saveAuth(token, username, role) {
		// Token đã được lưu vào cookie từ server
		// Chỉ lưu username và role vào localStorage để hiển thị UI
		localStorage.setItem('username', username);
		localStorage.setItem('role', role);
		localStorage.setItem('loginTime', new Date().getTime());

		console.log('Thông tin xác thực đã được lưu');
		console.log('Username:', username);
		console.log('Role:', role);
		console.log('Token được lưu trong Cookie');
	},

	/**
	 * Lấy token từ Cookie
	 */
	getToken() {
		const cookies = document.cookie.split(';');
		for (let cookie of cookies) {
			const [name, value] = cookie.trim().split('=');
			if (name === 'authToken') {
				return value;
			}
		}
		return null;
	},

	/**
	 * Lấy username từ localStorage
	 */
	getUsername() {
		return localStorage.getItem('username');
	},

	/**
	 * Lấy role từ localStorage
	 */
	getRole() {
		return localStorage.getItem('role');
	},

	/**
	 * Kiểm tra xem user đã đăng nhập chưa
	 */
	isAuthenticated() {
		const token = this.getToken();
		const loginTime = localStorage.getItem('loginTime');

		if (!token || !loginTime) {
			return false;
		}

		// Kiểm tra token đã hết hạn chưa
		const currentTime = new Date().getTime();
		const elapsed = currentTime - parseInt(loginTime);

		if (elapsed > this.TOKEN_EXPIRATION) {
			console.warn('⚠️ Token đã hết hạn!');
			this.clearAuth();
			return false;
		}

		return true;
	},

	/**
	 * Kiểm tra role của user
	 */
	hasRole(role) {
		return this.getRole() === role;
	},

	/**
	 * Kiểm tra user có phải admin không
	 */
	isAdmin() {
		return this.hasRole('ADMIN');
	},

	/**
	 * Kiểm tra user có phải shipper không
	 */
	isShipper() {
		return this.hasRole('SHIPPER');
	},

	/**
	 * Xóa tất cả thông tin xác thực
	 */
	clearAuth() {
		// Xóa cookie
		document.cookie = 'authToken=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC';

		// Xóa localStorage
		localStorage.removeItem('username');
		localStorage.removeItem('role');
		localStorage.removeItem('loginTime');

		console.log('Đã xóa thông tin xác thực (Cookie + localStorage)');
	},

	/**
	 * Lấy thời gian còn lại của token (ms)
	 */
	getTokenTimeRemaining() {
		const loginTime = localStorage.getItem('loginTime');
		if (!loginTime) {
			return 0;
		}

		const currentTime = new Date().getTime();
		const elapsed = currentTime - parseInt(loginTime);
		const remaining = this.TOKEN_EXPIRATION - elapsed;

		return remaining > 0 ? remaining : 0;
	},

	/**
	 * Lấy thời gian còn lại của token (định dạng đọc được)
	 */
	getTokenTimeRemainingFormatted() {
		const remaining = this.getTokenTimeRemaining();

		if (remaining <= 0) {
			return 'Đã hết hạn';
		}

		const minutes = Math.floor(remaining / 60000);
		const seconds = Math.floor((remaining % 60000) / 1000);

		return `${minutes} phút ${seconds} giây`;
	},

	/**
	 * Tạo Authorization header cho API request
	 */
	getAuthHeader() {
		const token = this.getToken();
		return token ? { 'Authorization': `Bearer ${token}` } : {};
	},


	async fetchWithAuth(url, options = {}) {
		// Kiểm tra token trước khi request
		if (!this.isAuthenticated()) {
			throw new Error('Token đã hết hạn. Vui lòng đăng nhập lại.');
		}

		const headers = {
			...options.headers,
			...this.getAuthHeader()
		};

		try {
			const response = await fetch(url, {
				...options,
				headers
			});

			// Nếu server trả về 401 (Unauthorized), xóa token và yêu cầu đăng nhập lại
			if (response.status === 401) {
				console.warn('⚠️ Server trả về 401. Token không hợp lệ.');
				this.clearAuth();
				window.location.href = '/UTESHOP/auth/login';
				throw new Error('Phiên đăng nhập đã hết hạn.');
			}

			return response;
		} catch (error) {
			console.error('❌ Lỗi khi gọi API:', error);
			throw error;
		}
	},

	setupAutoLogout(callback) {
		const remaining = this.getTokenTimeRemaining();

		if (remaining > 0) {
			console.log('⏰ Token sẽ hết hạn sau: ${this.getTokenTimeRemainingFormatted()}');

			setTimeout(() => {
				console.warn('⚠️ Token đã hết hạn! Tự động đăng xuất...');
				this.clearAuth();

				if (callback && typeof callback === 'function') {
					callback();
				} else {
					alert('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
					window.location.href = '/UTESHOP/auth/login';
				}
			}, remaining);
		}
	},

	/**
	 * Hiển thị thông tin debug (chỉ dùng khi development)
	 */
	debug() {
		console.log('=== AUTH DEBUG INFO ===');
		console.log('Authenticated:', this.isAuthenticated());
		console.log('Token:', this.getToken()?.substring(0, 20) + '...');
		console.log('Username:', this.getUsername());
		console.log('Role:', this.getRole());
		console.log('Time Remaining:', this.getTokenTimeRemainingFormatted());
		console.log('======================');
	}
};


window.AuthUtils = AuthUtils;

// Auto-setup logout khi page load (nếu đã đăng nhập)
document.addEventListener('DOMContentLoaded', () => {
	if (AuthUtils.isAuthenticated()) {
		// Setup auto-logout
		AuthUtils.setupAutoLogout();

	}
});

