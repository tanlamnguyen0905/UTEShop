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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter

@Table(name = "Brand")
public class Brand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int brandID;
	
	@Column(name = "brandName", columnDefinition = "nvarchar(max)", nullable = false)
	private String brandName;
	
	@Column(name = "brandLogo", columnDefinition = "nvarchar(max)")
	private String brandLogo;
	
	@Column(name = "description", columnDefinition = "nvarchar(max)")
	private String description;
	
	@OneToMany(mappedBy = "brand")
	private List<Products> products;
}
