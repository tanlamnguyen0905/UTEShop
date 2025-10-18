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
@Data
@Getter
@Setter
@Table(name = "UserCoupon")
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userCouponID;

    @Column(name = "couponCode", columnDefinition = "nvarchar(max)", nullable = false)
    private String couponCode;

    @Column(name = "used", nullable = false)
    private boolean used;

    @Column(name = "couponCreateAt", nullable = false)
    private Date couponCreateAt;

    @Column(name = "couponEnd", nullable = false)
    private Date couponEnd;

    @Column(name = "discountPercent", nullable = false)
    private BigDecimal discountPercent;

    @Column(name = "maxDiscountAmount", nullable = false)
    private BigDecimal maxDiscountAmount;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "orderDetailID")
    private OrderDetails orderDetails;
}
