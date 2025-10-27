package ute.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import ute.entities.Users;

@Getter
public class UserDTO {
	private Long userID;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String avatar; 
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public UserDTO(Users user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.avatar = user.getAvatar();
        this.createdAt = user.getCreateAt();
        this.lastLoginAt = user.getLastLoginAt();
    }
}
