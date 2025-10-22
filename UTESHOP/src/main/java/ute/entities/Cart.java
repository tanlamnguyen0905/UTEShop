package ute.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartID;

    @Transient
    @Builder.Default
    private Double totalPrice = 0D;

    public Double getTotalPrice() {
        double total = 0D;
        if (cartDetails != null) {
            for (CartDetail cartDetail : cartDetails) {
                total += cartDetail.getTotalPrice();
            }
        }
        return total;
    }

    @OneToOne
    @JoinColumn(name = "userID")
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartDetail> cartDetails;
}
