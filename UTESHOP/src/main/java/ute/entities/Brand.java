package ute.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandID;

    @Column( columnDefinition = "nvarchar(255)" )
    private String brandName;
    @Column( columnDefinition = "nvarchar(255)" )
    
    private String description;
    @Column( columnDefinition = "nvarchar(255)" )
    private String brandLogo;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Product> products;
}
