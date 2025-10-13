package ute.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

	@Column(name = "full_name", columnDefinition = "nvarchar(255)", nullable = false)
	private String fullName;

	@Temporal(TemporalType.DATE)
	@Column(name = "dob", nullable = false)
	private Date dob;

	@Column(name = "avatar", columnDefinition = "nvarchar(max)")
	private String avatar;

	@Column(name = "email", columnDefinition = "nvarchar(255)", nullable = false, unique = true)
	private String email;

	@Column(name = "sex", columnDefinition = "nvarchar(10)", nullable = false)
	private String sex;

	@Column(name = "phone", columnDefinition = "nvarchar(20)", nullable = false, unique = true)
	private String phone;

	@Column(name = "role", columnDefinition = "nvarchar(50)", nullable = false)
	private String role;

	@Column(name = "active", nullable = false)
	private boolean active = true;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt = new Date();

	@OneToMany(mappedBy = "user")
	private List<Orders> orders = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Reviews> reviews = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Addresses> addresses = new ArrayList<>();
}
