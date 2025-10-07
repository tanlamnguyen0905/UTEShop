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
@Table(name = "productDiscounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscounts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productDiscountID;
	
	@Column(name ="productDiscountCode", columnDefinition = "nvarchar(50)", nullable = false, unique = true)
	private String productDiscountCode;
	
	@Column(name = "description", columnDefinition = "nvarchar(max)")
	private String description;
	
	@Column(name = "discountPercent", nullable = false)
	private double discountPercent;
	
	@Column(name = "discountStart", nullable = false)
	private Date discountStart;
	
	@Column(name = "discountEnd", nullable = false)
	private Date discountEnd;

	@Column(name = "active", nullable = false)
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = "orderDetailID", nullable = false)
	private OrderDetails orderDetails;
	
	@OneToMany(mappedBy = "product")
	private List<Products> products;
	
	
}
