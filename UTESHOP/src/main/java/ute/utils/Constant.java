package ute.utils;

public class Constant {
	
	// User Roles
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";
	public static final String ROLE_SHIPPER = "SHIPPER";
	public static final String ROLE_MANAGER = "MANAGER";

	
	// User Status
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_INACTIVE = "INACTIVE";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_DELETED = "DELETED";
	
	// Order Status
	public static final String ORDER_PENDING = "Đang chờ";
	public static final String ORDER_PROCESSING = "Đang xử lý";
	public static final String ORDER_SHIPPED = "Đang giao";
	public static final String ORDER_DELIVERED = "Đã giao";
	public static final String ORDER_CANCELED = "Đã hủy";
	
	// payment status
	public static final String PAYMENT_PAID = "Đã thanh toán";
	public static final String PAYMENT_UNPAID = "Chưa thanh toán";

	// Uploading
	public static final String UPLOAD_DIR_BANNER = "assets/uploads/banner";
    public static final String UPLOAD_DIR_BRAND = "assets/uploads/brand";
    public static final String UPLOAD_DIR_CATEGORY = "assets/uploads/category";
    public static final String UPLOAD_DIR_PRODUCT = "assets/uploads/product";
    public static final String UPLOAD_DIR_REVIEW = "assets/uploads/review";
    public static final String UPLOAD_DIR_AVATAR = "assets/uploads/avatar";
	public static final String UPLOAD_DIR_CHAT = "assets/uploads/chat";

    public static final String[] ALL_UPLOAD_DIRS = {
        UPLOAD_DIR_BANNER,
        UPLOAD_DIR_BRAND,
        UPLOAD_DIR_CATEGORY,
        UPLOAD_DIR_PRODUCT,
        UPLOAD_DIR_REVIEW,
        UPLOAD_DIR_AVATAR,
		UPLOAD_DIR_CHAT
    };

	public static final String DEFAULT_AVATAR = "default-avatar.png";
}
