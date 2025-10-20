package ute.entities;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Table(name = "CartProduct")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartProductID;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "totalPrice", nullable = false)
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "cartID", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;
}