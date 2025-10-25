package ute.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ute.configs.JPAConfig;
import ute.dao.impl.DeliveryDaoImpl;
import ute.dao.impl.OrderDaoImpl;
import ute.dto.DeliveryDTO;
import ute.entities.Delivery;
import ute.entities.Orders;
import ute.entities.Users;
import ute.mapper.DeliveryMapper;
import ute.service.inter.DeliveryService;

public class DeliveryServiceImpl implements DeliveryService {
    
    private final DeliveryDaoImpl deliveryDao;
    private final OrderDaoImpl orderDao;
    
    public DeliveryServiceImpl() {
        this.deliveryDao = new DeliveryDaoImpl();
        this.orderDao = new OrderDaoImpl();
    }

    @Override
    public DeliveryDTO assignOrderToShipper(Long orderId, Long shipperId, String notes) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            // Kiểm tra xem đơn hàng đã được gán cho shipper chưa
            Delivery existingDelivery = deliveryDao.findByOrderId(orderId);
            if (existingDelivery != null) {
                throw new RuntimeException("Đơn hàng này đã được gán cho shipper!");
            }
            
            // Lấy thông tin đơn hàng và shipper
            Orders order = em.find(Orders.class, orderId);
            Users shipper = em.find(Users.class, shipperId);
            
            if (order == null) {
                throw new RuntimeException("Đơn hàng không tồn tại!");
            }
            
            if (shipper == null || !"SHIPPER".equalsIgnoreCase(shipper.getRole())) {
                throw new RuntimeException("Shipper không hợp lệ!");
            }
            
            // Chỉ cho phép gán đơn hàng đã được xác nhận
            if (!"Đã xác nhận".equals(order.getOrderStatus())) {
                throw new RuntimeException("Chỉ có thể gán đơn hàng đã được xác nhận cho shipper!");
            }
            
            // Tạo delivery
            Delivery delivery = Delivery.builder()
                    .order(order)
                    .shipper(shipper)
                    .assignedDate(LocalDateTime.now())
                    .deliveryStatus("Đang giao hàng")
                    .notes(notes)
                    .build();
            
            em.persist(delivery);
            
            // Cập nhật trạng thái đơn hàng
            order.setOrderStatus("Đang giao hàng");
            em.merge(order);
            
            trans.commit();
            
            // Trả về DTO
            return DeliveryMapper.toDTO(deliveryDao.findById(delivery.getDeliveryID()));
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi gán đơn hàng cho shipper: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void completeDelivery(Long deliveryId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery == null) {
                throw new RuntimeException("Delivery không tồn tại!");
            }
            
            if (!"Đang giao hàng".equals(delivery.getDeliveryStatus())) {
                throw new RuntimeException("Không thể hoàn thành giao hàng ở trạng thái " + delivery.getDeliveryStatus());
            }
            
            delivery.setCompletedDate(LocalDateTime.now());
            delivery.setDeliveryStatus("Đã giao hàng");
            em.merge(delivery);
            
            // Cập nhật trạng thái đơn hàng
            Orders order = delivery.getOrder();
            order.setOrderStatus("Đã giao hàng");
            order.setPaymentStatus("Đã thanh toán");
            em.merge(order);
            
            trans.commit();
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi hoàn thành giao hàng: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void failDelivery(Long deliveryId, String failureReason) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery == null) {
                throw new RuntimeException("Delivery không tồn tại!");
            }
            
            if (!"Đang giao hàng".equals(delivery.getDeliveryStatus())) {
                throw new RuntimeException("Không thể báo thất bại ở trạng thái " + delivery.getDeliveryStatus());
            }
            
            delivery.setDeliveryStatus("Giao hàng thất bại");
            delivery.setFailureReason(failureReason);
            em.merge(delivery);
            
            // Cập nhật trạng thái đơn hàng
            Orders order = delivery.getOrder();
            order.setOrderStatus("Giao hàng thất bại");
            order.setNotes((order.getNotes() != null ? order.getNotes() + "\n" : "") + 
                          "Lý do thất bại: " + failureReason);
            em.merge(order);
            
            trans.commit();
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi báo giao hàng thất bại: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateDeliveryStatus(Long deliveryId, String newStatus) {
        Delivery delivery = deliveryDao.findById(deliveryId);
        if (delivery == null) {
            throw new RuntimeException("Delivery không tồn tại!");
        }
        delivery.setDeliveryStatus(newStatus);
        deliveryDao.update(delivery);
    }

    @Override
    public void updateNotes(Long deliveryId, String notes) {
        Delivery delivery = deliveryDao.findById(deliveryId);
        if (delivery == null) {
            throw new RuntimeException("Delivery không tồn tại!");
        }
        delivery.setNotes(notes);
        deliveryDao.update(delivery);
    }

    @Override
    public DeliveryDTO findById(Long deliveryId) {
        Delivery delivery = deliveryDao.findById(deliveryId);
        return DeliveryMapper.toDTO(delivery);
    }

    @Override
    public DeliveryDTO findByOrderId(Long orderId) {
        Delivery delivery = deliveryDao.findByOrderId(orderId);
        return DeliveryMapper.toDTO(delivery);
    }

    @Override
    public List<DeliveryDTO> findByShipperId(Long shipperId) {
        List<Delivery> deliveries = deliveryDao.findByShipperId(shipperId);
        return DeliveryMapper.toDTOList(deliveries);
    }

    @Override
    public List<DeliveryDTO> findByShipperIdAndStatus(Long shipperId, String status) {
        List<Delivery> deliveries = deliveryDao.findByShipperIdAndStatus(shipperId, status);
        return DeliveryMapper.toDTOList(deliveries);
    }

    @Override
    public List<DeliveryDTO> findAll() {
        List<Delivery> deliveries = deliveryDao.findAll();
        return DeliveryMapper.toDTOList(deliveries);
    }

    @Override
    public long countByShipperId(Long shipperId) {
        return deliveryDao.countByShipperId(shipperId);
    }

    @Override
    public long countByShipperIdAndStatus(Long shipperId, String status) {
        return deliveryDao.countByShipperIdAndStatus(shipperId, status);
    }

    @Override
    public List<DeliveryDTO> findByShipperIdPaginated(Long shipperId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Delivery> deliveries = deliveryDao.findByShipperIdPaginated(shipperId, offset, pageSize);
        return DeliveryMapper.toDTOList(deliveries);
    }
}
