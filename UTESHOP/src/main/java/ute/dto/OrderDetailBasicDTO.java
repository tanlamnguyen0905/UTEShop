package ute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailBasicDTO {
    private Long orderDetailID;
    private Integer quantity;
    private Double unitPrice;
    
    // Thông tin sản phẩm
    private Long productID;
    private String productName;
    private String categoryName;
    private String imageUrl;
    
    public Double getTotalPrice() {
        if (quantity == null || unitPrice == null) return 0.0;
        return quantity * unitPrice;
    }
}

