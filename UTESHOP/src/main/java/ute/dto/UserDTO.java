package ute.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.entities.Users;

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

