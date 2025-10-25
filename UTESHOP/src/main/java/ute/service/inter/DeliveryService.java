package ute.service.inter;

import java.util.List;
import ute.dto.DeliveryDTO;

public interface DeliveryService {
    
    /**
     * Gán đơn hàng cho shipper
     */
    DeliveryDTO assignOrderToShipper(Long orderId, Long shipperId, String notes);
    
    /**
     * Shipper xác nhận giao hàng thành công
     */
    void completeDelivery(Long deliveryId);
    
    /**
     * Shipper báo giao hàng thất bại
     */
    void failDelivery(Long deliveryId, String failureReason);
    
    /**
     * Cập nhật trạng thái delivery
     */
    void updateDeliveryStatus(Long deliveryId, String newStatus);
    
    /**
     * Cập nhật ghi chú
     */
    void updateNotes(Long deliveryId, String notes);
    
    /**
     * Tìm delivery theo ID
     */
    DeliveryDTO findById(Long deliveryId);
    
    /**
     * Tìm delivery theo OrderID
     */
    DeliveryDTO findByOrderId(Long orderId);
    
    /**
     * Tìm tất cả delivery của shipper
     */
    List<DeliveryDTO> findByShipperId(Long shipperId);
    
    /**
     * Tìm delivery theo shipper và trạng thái
     */
    List<DeliveryDTO> findByShipperIdAndStatus(Long shipperId, String status);
    
    /**
     * Tìm tất cả delivery
     */
    List<DeliveryDTO> findAll();
    
    /**
     * Đếm số delivery của shipper
     */
    long countByShipperId(Long shipperId);
    
    /**
     * Đếm số delivery theo trạng thái của shipper
     */
    long countByShipperIdAndStatus(Long shipperId, String status);
    
    /**
     * Tìm delivery của shipper với phân trang
     */
    List<DeliveryDTO> findByShipperIdPaginated(Long shipperId, int page, int pageSize);
}
