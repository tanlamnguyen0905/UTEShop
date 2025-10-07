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
@Table(name = "Reviews")
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Reviews {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ReviewID;
	
	@Column(name = "Rating", nullable = false, columnDefinition = "int check (Rating >=1 and Rating <=5)")
	private int Rating;
	
	@Column(name = "content",columnDefinition =  "nvarchar(max)", nullable = false)
	private String content;
	
	@Column(name = "CreatAt", nullable = false)
	private Date CreatAt;
	
	@Column(name = "image",columnDefinition =  "nvarchar(max)")
	private String image;
	
	@ManyToOne
	@JoinColumn(name = "productID", nullable = false)
	private Products product;
	
	@ManyToOne
	@JoinColumn(name = "userID", nullable = false)
	private Users user;
	
	
}
