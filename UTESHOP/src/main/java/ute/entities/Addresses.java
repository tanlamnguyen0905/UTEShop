package ute.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Addresses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int addressID;

	@Column(name = "address", columnDefinition = "nvarchar(max)", nullable = false)
	private String address;

	@ManyToOne
	@JoinColumn(name = "userID", nullable = false)
	private Users user;

	@OneToMany(mappedBy = "address")
	private List<Orders> orders;
}
