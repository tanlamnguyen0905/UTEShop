package ute.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailID;

    private Integer quantity;
    private Double unitPrice;
    
    @Column(name = "status", columnDefinition = "NVARCHAR(50)")
    @Builder.Default
    private String status = "ACTIVE";

    @Transient
    private Double totalPrice;

    public Double getTotalPrice() {
        return quantity * unitPrice;
    }

    @ManyToOne
    @JoinColumn(name = "orderID")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

}
