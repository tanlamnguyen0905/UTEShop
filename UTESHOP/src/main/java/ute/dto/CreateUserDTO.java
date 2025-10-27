package ute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho việc tạo User mới từ Admin
 * Chứa password để admin có thể tạo user với mật khẩu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    private String username;
    private String password; // Sẽ được hash trước khi lưu
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
        return username != null && !username.trim().isEmpty()
            && password != null && password.length() >= 6
            && email != null && !email.trim().isEmpty()
            && fullname != null && !fullname.trim().isEmpty()
            && role != null && !role.trim().isEmpty();
    }
    
    /**
     * Lấy message lỗi validation
     */
    public String getValidationError() {
        if (username == null || username.trim().isEmpty()) {
            return "Username không được để trống";
        }
        if (password == null || password.length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email không được để trống";
        }
        if (fullname == null || fullname.trim().isEmpty()) {
            return "Họ tên không được để trống";
        }
        if (role == null || role.trim().isEmpty()) {
            return "Vai trò không được để trống";
        }
        return null;
    }
}

