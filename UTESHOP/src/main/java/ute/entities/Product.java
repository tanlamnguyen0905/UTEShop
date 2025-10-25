package ute.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "banners")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(name = "productName", columnDefinition = "NVARCHAR(255)")
    private String productName;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String describe;
    private Double unitPrice;
    private Integer stockQuantity;
    @Builder.Default
    private Long soldCount = 0L;
    @Builder.Default
    private Long reviewCount = 0L;
    private LocalDateTime importDate;

    @PrePersist
    protected void onCreate() {
        this.importDate = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryID")
    private Categories category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brandID")
    private Brand brand;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")

    private List<Favorite> favorites;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @ManyToMany
    @JoinTable(name = "product_banner", // tên bảng trung gian
            joinColumns = @JoinColumn(name = "productID"), // khóa ngoại trỏ về Product
            inverseJoinColumns = @JoinColumn(name = "bannerID") // khóa ngoại trỏ về Banner
    )
    @Builder.Default
    private Set<Banner> banners = new HashSet<>();

    @Transient
    public float rating;

    public float getRating() {
        if (reviews == null || reviews.isEmpty())
            return 0f;
        return (float) reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Transient
    public int favoriteCount;

    public int getFavoriteCount() {
        return favorites.size();
    }

}