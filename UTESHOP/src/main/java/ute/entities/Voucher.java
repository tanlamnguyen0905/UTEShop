package ute.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    Long id;

    @Column(name = "voucher_code", nullable = false, unique = true, length = 50)
    String code;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "discount_type", nullable = false, length = 20)
    String discountType; // e.g., "PERCENT" hoặc "AMOUNT"

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    BigDecimal discountValue;

    @Column(name = "min_order_amount", precision = 10, scale = 2)
    BigDecimal minOrderAmount;

    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    BigDecimal maxDiscountAmount;

    @Column(name = "start_date", nullable = false)
    LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    LocalDateTime endDate;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "used_count", nullable = false)
    Integer usedCount;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (usedCount == null) usedCount = 0;
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
