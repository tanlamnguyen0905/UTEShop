package ute.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryID;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private Users shipper;

    @Column(name = "assignedDate")
    private LocalDateTime assignedDate;

    @Column(name = "completedDate")
    private LocalDateTime completedDate;

    @Column(name = "deliveryStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String deliveryStatus = "Đang giao hàng";

    @Column(name = "notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;

    @Column(name = "failureReason", columnDefinition = "NVARCHAR(MAX)")
    private String failureReason;

    @PrePersist
    protected void onPersist() {
        this.assignedDate = LocalDateTime.now();
    }
}

