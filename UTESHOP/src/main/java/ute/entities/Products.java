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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
@Data

public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productID;
	
	@Column(name = "productName", columnDefinition = "nvarchar(max)", nullable = false)
	private String productName;
	
	@Column(name = "image", columnDefinition = "nvarchar(max)", nullable = false)
	private String image;
	@Column(name = "price", nullable = false)
	private double price;
	
	@Column(name = "stockQuantity", nullable = false)
	private int stockQuantity;
	
	@Column(name = "description", columnDefinition = "nvarchar(max)")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "categoryID", nullable = false)
	private Categories category;
	
	@OneToMany(mappedBy = "product")
	private List<ProductComments> comments;
	
	@OneToMany(mappedBy = "product")
	private List<OrderDetails> orderDetails;
	
	
	

}
