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
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryID;

    @Column(name = "categoryName", columnDefinition = "nvarchar(100)", nullable = false)
    private String categoryName;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "image", columnDefinition = "nvarchar(max)")
    private String image;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Thêm trường parent reference để fix mappedBy
    @ManyToOne
    @JoinColumn(name = "parentCategoryID")
    private Categories category;

    @OneToMany(mappedBy = "category")
    private List<Categories> subCategories;
}
