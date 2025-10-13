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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    @Column(name = "productName", columnDefinition = "nvarchar(max)", nullable = false)
    private String productName;

    @Column(name = "image", columnDefinition = "nvarchar(max)", nullable = false)
    private String image;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stockQuantity", nullable = false)
    private int stockQuantity;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "discount")
    private int discount;

    @Column(name = "soldCount", nullable = false)
    private int soldCount;

    @Column(name = "reviewCount", nullable = false)
    private int reviewCount;

    @Column(name = "Rating", nullable = false, columnDefinition = "int check (Rating >=1 and Rating <=5)")
    private float rating;

    @Column(name = "Available", nullable = false)
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "brandID", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "categoryID", nullable = false)
    private Categories category;

    @OneToMany(mappedBy = "product")
    private List<Reviews> reviews;

    @OneToMany(mappedBy = "product")
    private List<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "productDiscountID")
    private ProductDiscounts product;
}