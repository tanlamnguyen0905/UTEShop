package ute.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.entities.Users;

/**
 * DTO cho User - không expose password và các thông tin nhạy cảm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userID;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private String avatar;
    private String role;
    private String status;
    private LocalDateTime createAt;
    
    // Không có password field - bảo mật!
    
    /**
     * Convert từ Entity sang DTO (không có password - bảo mật!)
     */
    public static UserDTO fromEntity(Users entity) {
        if (entity == null) {
            return null;
        }
        
        return UserDTO.builder()
                .userID(entity.getUserID())
                .username(entity.getUsername())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .avatar(entity.getAvatar())
                .role(entity.getRole())
                .status(entity.getStatus())
                .createAt(entity.getCreateAt())
                .build();
    }
}

