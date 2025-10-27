package ute.dao.inter;

import java.util.List;
import ute.entities.Delivery;

public interface DeliveryDao {
    
    /**
     * Tạo delivery mới
     */
    void insert(Delivery delivery);
    
    /**
     * Cập nhật delivery
     */
    void update(Delivery delivery);
    
    /**
     * Xóa delivery
     */
    void delete(Long deliveryId);
    
    /**
     * Tìm delivery theo ID
     */
    Delivery findById(Long deliveryId);
    
    /**
     * Tìm tất cả delivery của shipper
     */
    List<Delivery> findByShipperId(Long shipperId);
    
    /**
     * Tìm delivery theo trạng thái của một shipper
     */
    List<Delivery> findByShipperIdAndStatus(Long shipperId, String status);
    
    /**
     * Tìm delivery theo orderId
     */
    Delivery findByOrderId(Long orderId);
    
    /**
     * Tìm tất cả delivery
     */
    List<Delivery> findAll();
    
    /**
     * Đếm tổng số delivery của shipper
     */
    long countByShipperId(Long shipperId);
    
    /**
     * Đếm số delivery theo trạng thái của shipper
     */
    long countByShipperIdAndStatus(Long shipperId, String status);
}
