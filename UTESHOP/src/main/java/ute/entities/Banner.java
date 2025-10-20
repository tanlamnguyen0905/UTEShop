package ute.entities;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "products")
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerID;

    @Column(columnDefinition = "nvarchar(255)")
    private String bannerName;

    private String bannerImage;

    @ManyToMany(mappedBy = "banners")
    private Set<Product> products = new HashSet<>();
}
