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
@Table(name = "Discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discounts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int discountID;
	
	@Column(name = "discountName", columnDefinition = "nvarchar(255)", nullable = false)
	private String discountName;
	
	@Column(name = "description", columnDefinition = "nvarchar(max)")
	private String description;
	
	@Column(name = "discountPercent", nullable = false)
	private double discountPercent;
	
	@ManyToOne
	@JoinColumn(name = "orderDetailID", nullable = false)
	private OrderDetails orderDetails;
	
	
}
