package ute.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponID;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)")
    private String description;
    private Double discountPercent;
    private LocalDateTime userCouponStart;
    private LocalDateTime userCouponEnd;
    private Double maxDiscountAmount;

    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;
}