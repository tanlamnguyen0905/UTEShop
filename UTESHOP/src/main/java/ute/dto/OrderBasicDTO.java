package ute.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBasicDTO {
    private Long orderID;
    private LocalDateTime orderDate;
    private String orderStatus;
    private String paymentStatus;
    private Double amount;
    private Double shippingFee;
    private Double totalDiscount;
    private String notes;
    
    // Thông tin người nhận
    private String recipientName;
    private String phoneNumber;
    
    // Thông tin địa chỉ
    private AddressBasicDTO address;
    
    // Thông tin thanh toán
    private String paymentMethodName;
    
    // Chi tiết đơn hàng
    private List<OrderDetailBasicDTO> orderDetails;
    
    // Tính tổng tiền
    public Double getTotalAmount() {
        if (amount == null) return 0.0;
        double shipping = shippingFee != null ? shippingFee : 0.0;
        double discount = totalDiscount != null ? totalDiscount : 0.0;
        return amount + shipping - discount;
    }
}

