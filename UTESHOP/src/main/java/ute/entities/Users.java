package ute.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Column(name ="Dob", nullable = false)
	private Date dob;
	
	@Column(name = "Avatar", columnDefinition = "nvarchar(max)")
	private String avatar;
	
	@Column(name = "email", columnDefinition = "nvarchar(255)", nullable = false, unique = true)
	private String email;
	
	@Column(name = "phone", columnDefinition = "nvarchar(20)", nullable = false, unique = true)
	private String phone;
	
	@Column(name = "isAdmin", nullable = false)
	private boolean isAdmin;
	
	@Column(name = "isManager", nullable = false)
	private boolean isManager;
	
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

}
