package ute.service.inter;

import java.util.List;
import ute.entities.Delivery;

public interface DeliveryService {
    
    /**
     * Shipper nhận đơn hàng (tạo delivery mới)
     */
    Delivery acceptOrder(Long orderId, Long shipperId);
    
    /**
     * Cập nhật trạng thái giao hàng
     */
    void updateDeliveryStatus(Long deliveryId, String newStatus, String notes);
    
    /**
     * Đánh dấu đã giao hàng thành công
     */
    void markAsCompleted(Long deliveryId);
    
    /**
     * Đánh dấu giao hàng thất bại
     */
    void markAsFailed(Long deliveryId, String failureReason);
    
    /**
     * Tìm delivery theo ID
     */
    Delivery findById(Long deliveryId);
    
    /**
     * Tìm tất cả delivery của shipper
     */
    List<Delivery> findByShipperId(Long shipperId);
    
    /**
     * Tìm delivery theo trạng thái của shipper
     */
    List<Delivery> findByShipperIdAndStatus(Long shipperId, String status);
    
    /**
     * Đếm số delivery theo trạng thái
     */
    long countByShipperIdAndStatus(Long shipperId, String status);
    
    /**
     * Lấy thống kê delivery của shipper
     */
    java.util.Map<String, Long> getShipperStats(Long shipperId);
}

