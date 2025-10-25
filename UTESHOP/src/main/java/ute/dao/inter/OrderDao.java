package ute.dao.inter;

import java.util.List;
import ute.entities.Orders;

public interface OrderDao {
    
    /**
     * Tạo đơn hàng mới
     */
    void insert(Orders order);
    
    /**
     * Cập nhật đơn hàng
     */
    void update(Orders order);
    
    /**
     * Xóa đơn hàng
     */
    void delete(Long orderId);
    
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
     * Đếm tổng số đơn hàng
     */
    long count();
    
    /**
     * Tìm đơn hàng với phân trang
     */
    List<Orders> findPage(int page, int pageSize);
    
    /**
     * Tìm đơn hàng của user với phân trang
     */
    List<Orders> findByUserIdPaginated(Long userId, int offset, int limit);
    
    /**
     * Đếm số đơn hàng của user
     */
    long countByUserId(Long userId);
    
    /**
     * Tìm đơn hàng chưa có trong bảng Delivery (pending orders - chưa có shipper nhận)
     * Điều kiện: orderStatus != 'Đang chờ' AND orderID không có trong Delivery
     */
    List<Orders> findConfirmedOrders();
    
    /**
     * Đếm số đơn hàng chưa có shipper
     */
    long countConfirmedOrders();
}

