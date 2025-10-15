package ute.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(columnDefinition = "nvarchar(255)" )
    private String productName;
    @Column(columnDefinition = "nvarchar(255)" )
    private String describe;
    private Double unitPrice;
    private Integer stockQuantity;
    private Long soldCount;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "brandID")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDiscount> productDiscounts;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;
    
}

