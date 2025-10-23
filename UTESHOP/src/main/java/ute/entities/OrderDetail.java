package ute.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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

    @Transient
    private Double totalPrice;

    public Double getTotalPrice() {
        return quantity * unitPrice;
    }

    private int status;

    @ManyToOne
    @JoinColumn(name = "orderID")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

}
