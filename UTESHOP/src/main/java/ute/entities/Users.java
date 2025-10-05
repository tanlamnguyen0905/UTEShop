package ute.entities;

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
	
	
	
	@OneToMany(mappedBy = "user")
	private List<Orders> orders;
	
	@OneToMany(mappedBy = "user")
	private List<ProductComments> productComments;
	
	@OneToMany(mappedBy = "user")
	private List<Addresses> addresses;

}
