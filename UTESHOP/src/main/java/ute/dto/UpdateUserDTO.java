package ute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho việc cập nhật thông tin User từ Admin
 * Không chứa password - nếu cần đổi password thì dùng chức năng riêng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private Long userID;
    private String fullname;
    private String email;
    private String phone;
    private String avatar;
    private String role; // ADMIN, SHIPPER, USER
    private String status; // ACTIVE, INACTIVE, LOCKED
    
    /**
     * Validate dữ liệu đầu vào
     */
    public boolean isValid() {
        return userID != null && userID > 0
            && fullname != null && !fullname.trim().isEmpty()
            && email != null && !email.trim().isEmpty()
            && role != null && !role.trim().isEmpty()
            && status != null && !status.trim().isEmpty();
    }
    
    /**
     * Lấy message lỗi validation
     */
    public String getValidationError() {
        if (userID == null || userID <= 0) {
            return "ID người dùng không hợp lệ";
        }
        if (fullname == null || fullname.trim().isEmpty()) {
            return "Họ tên không được để trống";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email không được để trống";
        }
        if (role == null || role.trim().isEmpty()) {
            return "Vai trò không được để trống";
        }
        if (status == null || status.trim().isEmpty()) {
            return "Trạng thái không được để trống";
        }
        return null;
    }
}

