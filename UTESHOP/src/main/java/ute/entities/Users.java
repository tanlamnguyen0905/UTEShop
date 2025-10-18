package ute.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;

	@Column(name = "username", columnDefinition = "nvarchar(255)", nullable = false, unique = true)
	private String username;

	@Column(name = "password", columnDefinition = "nvarchar(255)", nullable = false)
	private String password;

	@Column(name = "fullName", columnDefinition = "nvarchar(255)", nullable = false)
	private String fullName;

	@Column(name = "Dob", nullable = false)
	private Date dob;

	@Column(name = "Avatar", columnDefinition = "nvarchar(max)")
	private String avatar;

	@Column(name = "email", columnDefinition = "nvarchar(255)", nullable = false, unique = true)
	private String email;

	@Column(name = "phone", columnDefinition = "nvarchar(20)", nullable = false, unique = true)
	private String phone;

	@Column(name = "Role", columnDefinition = "nvarchar(50)", nullable = false)
	private String role;

	@Column(name = "active", nullable = false)
	private boolean active;

	@Column(name = "CreatedAt", nullable = false)
	private Date createdAt;

	@OneToMany(mappedBy = "user")
	private List<Orders> orders;

	@OneToMany(mappedBy = "user")
	private List<Reviews> reviews;

	@OneToMany(mappedBy = "user")
	private List<Addresses> addresses;

	private String username;
	private String password;

	@Column(name = "fullname", columnDefinition = "NVARCHAR(100)")
	private String fullname;

	private String email;
	private String phone;
	private String avatar;
	private String role;
	private String status;

	private LocalDateTime createAt;
	private LocalDateTime lastLoginAt;

	// Relations
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Addresses> addresses;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Cart> carts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Orders> orders;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserCoupon> userCoupons;
}
