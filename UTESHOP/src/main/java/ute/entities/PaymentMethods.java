package ute.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PaymentMethods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethods {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int paymentMethodID;
	
	@Column(name = "methodName", nullable = false)
	private String methodName;
	
	@Column(name = "description", columnDefinition = "nvarchar(max)")
	private String description;
	

}
