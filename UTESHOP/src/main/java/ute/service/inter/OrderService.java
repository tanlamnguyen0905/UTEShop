package ute.service.inter;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

import ute.entities.Orders;

public interface OrderService {

    /**
     * Tạo đơn hàng từ giỏ hàng
     */
    Orders createOrderFromCart(Long userId, Long addressId, Long paymentMethodId, String notes, Long userCouponId);
    
    /**
     * Cập nhật trạng thái đơn hàng
     */
    void updateOrderStatus(Long orderId, String newStatus);
    
    /**
     * Cập nhật trạng thái thanh toán
     */
    void updatePaymentStatus(Long orderId, String newStatus);
    
    /**
     * Hủy đơn hàng
     */
    void cancelOrder(Long orderId, String reason);
    
    /**
     * Tìm đơn hàng theo ID
     */
    Orders findById(Long orderId);
    
    /**
     * Tìm tất cả đơn hàng của user
     */
    List<Orders> findByUserId(Long userId);
    
    /**
     * Tìm đơn hàng theo trạng thái
     */
    List<Orders> findByStatus(String status);
    
    /**
     * Tìm tất cả đơn hàng
     */
    List<Orders> findAll();
    
    /**
     * Tìm đơn hàng của user với phân trang
     */
    List<Orders> findByUserIdPaginated(Long userId, int page, int pageSize);
    
    /**
     * Đếm số đơn hàng của user
     */
    long countByUserId(Long userId);
    
    /**
     * Tính tổng tiền đơn hàng
     */
    Double calculateOrderTotal(Orders order);

    // Total revenue calculation
    Double getTotalRevenue();
    Double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Order counts
    Long getTotalOrderCount();
    Long getOrderCountByStatus(String status);
    
    // Order status distribution
    List<Map<String, Object>> getOrderStatusDistribution();
    
    // Daily revenue breakdown
    List<Map<String, Object>> getDailyRevenue(LocalDate startDate, LocalDate endDate);
}