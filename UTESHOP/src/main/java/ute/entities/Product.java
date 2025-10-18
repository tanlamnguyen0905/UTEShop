package ute.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(name = "productName", columnDefinition = "NVARCHAR(255)")
    private String productName;
    @Column(columnDefinition = "nvarchar(255)")
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

    @ManyToMany
    @JoinTable(name = "product_banner", // tên bảng trung gian
            joinColumns = @JoinColumn(name = "productID"), // khóa ngoại trỏ về Product
            inverseJoinColumns = @JoinColumn(name = "bannerID") // khóa ngoại trỏ về Banner
    )
    private Set<Banner> banners = new HashSet<>();

    @Transient
    private Double discountPrice;

    public Double getDiscountPrice() {
        if (productDiscounts == null || productDiscounts.isEmpty()) {
            return unitPrice;
        }

        LocalDateTime today = LocalDateTime.now();
        double maxDiscount = 0;

        // Lọc ra các giảm giá đang còn hiệu lực
        for (ProductDiscount pd : productDiscounts) {
            if (pd.getDiscountStart() != null && pd.getDiscountEnd() != null) {
                if (!today.isBefore(pd.getDiscountStart()) && !today.isAfter(pd.getDiscountEnd())) {
                    maxDiscount = Math.max(maxDiscount, pd.getDiscountPercent());
                }
            }
        }

        if (maxDiscount > 0) {
            return unitPrice * (1 - maxDiscount / 100.0);
        } else {
            return unitPrice;
        }
    }

    @Transient
    public float rating;

    // public float getRating(){
    // Long sum;
    // for (Order)
    // }

}
