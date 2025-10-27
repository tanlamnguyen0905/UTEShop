package ute.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDTO {
    private Long deliveryID;
    private Long orderID;
    private Long shipperID;
    private String shipperName;
    private String shipperPhone;
    private LocalDateTime assignedDate;
    private LocalDateTime completedDate;
    private String deliveryStatus;
    private String notes;
    private String failureReason;
    
    // Order information (nested DTO)
    private OrderBasicDTO order;
}

