# Đề Xuất Cải Tiến Orders Entity

## 📝 Version hiện tại

Orders.java có **15 thuộc tính** - đủ cho chức năng cơ bản nhưng còn thiếu một số tính năng nâng cao.

## 🔧 Các sửa đổi đề xuất

### 1. Sửa lỗi và tối ưu (QUAN TRỌNG)

```java
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
    
    // ✅ FIX 1: Thêm @Builder.Default
    @Column(name = "TotalDiscount", nullable = false)
    @Builder.Default
    private Double totalDiscount = 0.0;

    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(name = "ShippingFee", nullable = false)
    @Builder.Default
    private Double shippingFee = 25000.0;

    @Column(name = "recipientName", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String recipientName;
    
    @Column(name = "orderStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String orderStatus = "PENDING";
    
    @Column(name = "paymentStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String paymentStatus = "UNPAID";
    
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
    
    // ✅ FIX 2: Thêm @PrePersist
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.orderStatus == null) {
            this.orderStatus = "PENDING";
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = "UNPAID";
        }
    }
}
```

### 2. Version đầy đủ với tracking (Khuyến nghị)

```java
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

    @Column(nullable = false)
    private Double amount;
    
    private LocalDateTime orderDate;
    
    @Column(name = "TotalDiscount", nullable = false)
    @Builder.Default
    private Double totalDiscount = 0.0;

    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(name = "ShippingFee", nullable = false)
    @Builder.Default
    private Double shippingFee = 25000.0;

    @Column(name = "recipientName", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String recipientName;
    
    @Column(name = "orderStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String orderStatus = "PENDING";
    
    @Column(name = "paymentStatus", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Builder.Default
    private String paymentStatus = "UNPAID";
    
    @Column(name = "notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;
    
    // ========== TRACKING & DELIVERY ==========
    
    @Column(name = "trackingNumber", columnDefinition = "VARCHAR(100)")
    private String trackingNumber;
    
    @Column(name = "estimatedDeliveryDate")
    private LocalDateTime estimatedDeliveryDate;
    
    @Column(name = "deliveryDate")
    private LocalDateTime deliveryDate;
    
    // ========== AUDIT FIELDS ==========
    
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    
    @Column(name = "completedAt")
    private LocalDateTime completedAt;
    
    // ========== CANCEL HANDLING ==========
    
    @Column(name = "cancelReason", columnDefinition = "NVARCHAR(500)")
    private String cancelReason;
    
    @Column(name = "cancelledBy", columnDefinition = "NVARCHAR(100)")
    private String cancelledBy;
    
    // ========== OPTIONAL BUT USEFUL ==========
    
    @Column(name = "contactEmail", columnDefinition = "VARCHAR(255)")
    private String contactEmail;
    
    // ========== RELATIONSHIPS ==========
    
    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "addressID")
    private Addresses address;

    @ManyToOne
    @JoinColumn(name = "paymentID")
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    
    @ManyToOne
    @JoinColumn(name = "userCouponID")
    private UserCoupon userCoupon;
    
    @ManyToOne
    @JoinColumn(name = "shipperID")
    private Users shipper;  // User with ROLE_SHIPPER
    
    // ========== LIFECYCLE CALLBACKS ==========
    
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.orderStatus == null) {
            this.orderStatus = "PENDING";
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = "UNPAID";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Tính tổng tiền đơn hàng (amount + shipping - discount)
     */
    public Double calculateFinalAmount() {
        double base = amount != null ? amount : 0.0;
        double ship = shippingFee != null ? shippingFee : 0.0;
        double discount = totalDiscount != null ? totalDiscount : 0.0;
        return base + ship - discount;
    }
    
    /**
     * Kiểm tra đơn hàng có thể hủy không
     */
    public boolean canBeCancelled() {
        return "PENDING".equals(orderStatus) || 
               "CONFIRMED".equals(orderStatus);
    }
    
    /**
     * Kiểm tra đơn hàng đã hoàn thành chưa
     */
    public boolean isCompleted() {
        return "DELIVERED".equals(orderStatus) || 
               "COMPLETED".equals(orderStatus);
    }
}
```

## 📊 So sánh versions

| Feature | Current | Fixed | Full |
|---------|---------|-------|------|
| **Cơ bản** | ✅ | ✅ | ✅ |
| **Auto orderDate** | ❌ | ✅ | ✅ |
| **Default values** | Partial | ✅ | ✅ |
| **Tracking** | ❌ | ❌ | ✅ |
| **Shipper** | ❌ | ❌ | ✅ |
| **Audit trail** | ❌ | ❌ | ✅ |
| **Cancel reason** | ❌ | ❌ | ✅ |
| **Helper methods** | ❌ | ❌ | ✅ |

## 🎯 Khuyến nghị

### Nếu dự án nhỏ/học tập:
→ Dùng **Fixed version** (sửa lỗi + @PrePersist)

### Nếu dự án thực tế/production:
→ Dùng **Full version** (đầy đủ tracking, audit)

### Tối thiểu phải làm:
1. ✅ Thêm `@PrePersist` cho orderDate
2. ✅ Thêm `@Builder.Default` cho totalDiscount
3. ✅ Thêm default cho orderStatus và paymentStatus
4. ✅ Thêm `orphanRemoval = true` cho orderDetails

## 🔄 Migration SQL (nếu thêm fields mới)

```sql
-- Thêm tracking fields
ALTER TABLE Orders
ADD trackingNumber VARCHAR(100),
ADD estimatedDeliveryDate DATETIME2,
ADD deliveryDate DATETIME2;

-- Thêm audit fields
ALTER TABLE Orders
ADD updatedAt DATETIME2,
ADD completedAt DATETIME2;

-- Thêm cancel handling
ALTER TABLE Orders
ADD cancelReason NVARCHAR(500),
ADD cancelledBy NVARCHAR(100);

-- Thêm email
ALTER TABLE Orders
ADD contactEmail VARCHAR(255);

-- Thêm shipper
ALTER TABLE Orders
ADD shipperID BIGINT,
ADD FOREIGN KEY (shipperID) REFERENCES Users(userID);

-- Set default cho orders cũ
UPDATE Orders 
SET totalDiscount = 0.0 
WHERE totalDiscount IS NULL;

UPDATE Orders 
SET orderStatus = 'COMPLETED' 
WHERE orderStatus IS NULL AND deliveryDate IS NOT NULL;

UPDATE Orders 
SET orderStatus = 'PENDING' 
WHERE orderStatus IS NULL;

UPDATE Orders 
SET paymentStatus = 'UNPAID' 
WHERE paymentStatus IS NULL;
```

## 🚀 Cách áp dụng

### Bước 1: Backup database
```sql
BACKUP DATABASE YourDB TO DISK = 'path/to/backup.bak'
```

### Bước 2: Sửa entity
Chọn 1 trong 2 versions trên

### Bước 3: Update database
- Nếu dùng JPA auto-update: `hibernate.hbm2ddl.auto=update`
- Hoặc chạy SQL migration thủ công

### Bước 4: Test
```java
@Test
public void testOrderCreation() {
    Orders order = Orders.builder()
            .amount(100000.0)
            .recipientName("Nguyễn Văn A")
            .phoneNumber("0123456789")
            .build();
    
    ordersRepository.save(order);
    
    assertNotNull(order.getOrderDate());  // Auto set by @PrePersist
    assertEquals("PENDING", order.getOrderStatus());
    assertEquals(0.0, order.getTotalDiscount());
    assertEquals(25000.0, order.getShippingFee());
}
```

## ⚡ Performance tips

1. **Lazy loading cho shipper** (nếu không cần mọi lúc):
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "shipperID")
private Users shipper;
```

2. **Index cho tracking**:
```sql
CREATE INDEX idx_tracking ON Orders(trackingNumber);
CREATE INDEX idx_order_status ON Orders(orderStatus);
CREATE INDEX idx_order_date ON Orders(orderDate DESC);
```

3. **Query optimization**:
```java
// Fetch orders với relationships cần thiết
@Query("SELECT o FROM Orders o " +
       "LEFT JOIN FETCH o.user " +
       "LEFT JOIN FETCH o.address " +
       "WHERE o.orderID = :id")
Orders findByIdWithDetails(@Param("id") Long id);
```

## 📋 Checklist implementation

- [ ] Backup database
- [ ] Sửa entity Orders.java
- [ ] Thêm @PrePersist và @PreUpdate
- [ ] Thêm @Builder.Default cho các field bắt buộc
- [ ] (Optional) Thêm tracking fields
- [ ] (Optional) Thêm helper methods
- [ ] Run migration SQL
- [ ] Update OrderService để handle new fields
- [ ] Update OrderController
- [ ] Update JSP views
- [ ] Test create order
- [ ] Test update order status
- [ ] Test cancel order

## 🎓 Kết luận

**Hiện tại:** Entity đủ dùng cho chức năng cơ bản ✅

**Cần sửa ngay:** 
- @PrePersist cho orderDate
- @Builder.Default cho totalDiscount
- Default status values

**Nên thêm (production):**
- Tracking fields
- Shipper relationship  
- Audit trail
- Cancel handling

**Ưu tiên:** Fix bugs trước, thêm features sau!

