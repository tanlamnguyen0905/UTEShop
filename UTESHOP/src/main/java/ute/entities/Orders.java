package ute.entities;

import java.time.LocalDateTime;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    private Double amount;
    private LocalDateTime orderDate;
    
    @Column(name = "TotalDiscount", nullable = false)
    Double totalDiscount;

    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(name = "ShippingFee", nullable = false)
    @Builder.Default
    Double shippingFee = 25000.0;

    @Column(name = "recipientName", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String recipientName;
    
    @Column(name = "orderStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String orderStatus = "Đang chờ";
    
    @Column(name = "paymentStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String paymentStatus = "Chưa thanh toán";
    
    @Column(name = "notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;
    
    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "addressID")
    private Addresses address;

    @ManyToOne
    @JoinColumn(name = "paymentID")
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
    
    @ManyToOne
    @JoinColumn(name = "userCouponID")
    private UserCoupon userCoupon;

    @PrePersist
    protected void onPersist() {
    this.orderDate = LocalDateTime.now();
}
}
