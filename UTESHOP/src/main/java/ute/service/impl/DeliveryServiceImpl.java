package ute.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ute.configs.JPAConfig;
import ute.dao.impl.DeliveryDaoImpl;
import ute.dao.impl.OrderDaoImpl;
import ute.entities.Delivery;
import ute.entities.Orders;
import ute.entities.Users;
import ute.service.inter.DeliveryService;

public class DeliveryServiceImpl implements DeliveryService {
    
    private final DeliveryDaoImpl deliveryDao;
    private final OrderDaoImpl orderDao;
    
    public DeliveryServiceImpl() {
        this.deliveryDao = new DeliveryDaoImpl();
        this.orderDao = new OrderDaoImpl();
    }

    @Override
    public Delivery acceptOrder(Long orderId, Long shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            // Lấy thông tin order và shipper
            Orders order = em.find(Orders.class, orderId);
            Users shipper = em.find(Users.class, shipperId);
            
            if (order == null) {
                throw new RuntimeException("Đơn hàng không tồn tại!");
            }
            
            if (shipper == null || shipper.getRole() == null || 
                !shipper.getRole().equalsIgnoreCase("shipper")) {
                throw new RuntimeException("Shipper không hợp lệ!");
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (!"Đã xác nhận".equals(order.getOrderStatus())) {
                throw new RuntimeException("Đơn hàng chưa được xác nhận!");
            }
            
            // Kiểm tra xem đơn hàng đã có shipper chưa
            Delivery existingDelivery = deliveryDao.findByOrderId(orderId);
            if (existingDelivery != null) {
                throw new RuntimeException("Đơn hàng này đã được shipper khác nhận!");
            }
            
            // Tạo delivery mới
            Delivery delivery = Delivery.builder()
                    .order(order)
                    .shipper(shipper)
                    .assignedDate(LocalDateTime.now())
                    .deliveryStatus("Đang giao hàng")
                    .build();
            
            em.persist(delivery);
            
            // Cập nhật trạng thái đơn hàng
            order.setOrderStatus("Đang giao hàng");
            em.merge(order);
            
            trans.commit();
            
            return delivery;
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi nhận đơn hàng: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateDeliveryStatus(Long deliveryId, String newStatus, String notes) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery == null) {
                throw new RuntimeException("Delivery không tồn tại!");
            }
            
            delivery.setDeliveryStatus(newStatus);
            if (notes != null && !notes.isEmpty()) {
                delivery.setNotes(notes);
            }
            
            // Cập nhật trạng thái đơn hàng tương ứng
            Orders order = delivery.getOrder();
            if ("Đã giao hàng".equals(newStatus)) {
                delivery.setCompletedDate(LocalDateTime.now());
                order.setOrderStatus("Đã giao hàng");
                order.setPaymentStatus("Đã thanh toán"); // COD đã thanh toán
            } else if ("Giao hàng thất bại".equals(newStatus)) {
                order.setOrderStatus("Giao hàng thất bại");
            } else {
                order.setOrderStatus(newStatus);
            }
            
            em.merge(delivery);
            em.merge(order);
            
            trans.commit();
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi cập nhật trạng thái: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void markAsCompleted(Long deliveryId) {
        updateDeliveryStatus(deliveryId, "Đã giao hàng", null);
    }

    @Override
    public void markAsFailed(Long deliveryId, String failureReason) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery == null) {
                throw new RuntimeException("Delivery không tồn tại!");
            }
            
            delivery.setDeliveryStatus("Giao hàng thất bại");
            delivery.setFailureReason(failureReason);
            
            // Cập nhật trạng thái đơn hàng
            Orders order = delivery.getOrder();
            order.setOrderStatus("Giao hàng thất bại");
            
            em.merge(delivery);
            em.merge(order);
            
            trans.commit();
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi đánh dấu thất bại: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Delivery findById(Long deliveryId) {
        return deliveryDao.findById(deliveryId);
    }

    @Override
    public List<Delivery> findByShipperId(Long shipperId) {
        return deliveryDao.findByShipperId(shipperId);
    }

    @Override
    public List<Delivery> findByShipperIdAndStatus(Long shipperId, String status) {
        return deliveryDao.findByShipperIdAndStatus(shipperId, status);
    }

    @Override
    public long countByShipperIdAndStatus(Long shipperId, String status) {
        return deliveryDao.countByShipperIdAndStatus(shipperId, status);
    }

    @Override
    public Map<String, Long> getShipperStats(Long shipperId) {
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("total", deliveryDao.countByShipperId(shipperId));
        stats.put("delivering", countByShipperIdAndStatus(shipperId, "Đang giao hàng"));
        stats.put("completed", countByShipperIdAndStatus(shipperId, "Đã giao hàng"));
        stats.put("failed", countByShipperIdAndStatus(shipperId, "Giao hàng thất bại"));
        
        return stats;
    }
}

