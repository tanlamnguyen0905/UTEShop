package ute.entities;

import java.math.BigDecimal;
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
@Table(name = "OrderDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailID;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "totalPrice", nullable = false)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "orderDetails")
    private List<UserCoupon> discounts;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Products product;

}