package ute.entities;

import java.util.Date;
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
@Table(name = "Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderID;
	
	@Column(name = "orderDate", nullable = false)
	private Date orderDate;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Column(name = "amount", nullable = false)
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "paymentMethonId", nullable = false)
	private PaymentMethods paymentMethod;
	
	
	@ManyToOne
	@JoinColumn(name = "userID", nullable = false)
	private Users user;
	
	
	@ManyToOne
	@JoinColumn(name = "addressID", nullable = false)
	private Addresses address;
	
	@OneToMany(mappedBy = "order")
	private List<OrderDetails> orderDetails;

}
