package ute.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ProductComments")
@Data
@NoArgsConstructor

@AllArgsConstructor
public class ProductComments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentID;
	
	@Column(name = "starsNumber", nullable = false, columnDefinition = "int check (starsNumber >=1 and starsNumber <=5)")
	private int starsNumber;
	
	@Column(name = "content",columnDefinition =  "nvarchar(max)", nullable = false)
	private String content;
	
	@Column(name = "commentDate", nullable = false)
	private Date commentDate;
	
	@Column(name = "image",columnDefinition =  "nvarchar(max)")
	private String image;
	
	
	
	@ManyToOne
	@JoinColumn(name = "productID", nullable = false)
	private Products product;
	
	@ManyToOne
	@JoinColumn(name = "userID", nullable = false)
	private Users user;
	
	
}
