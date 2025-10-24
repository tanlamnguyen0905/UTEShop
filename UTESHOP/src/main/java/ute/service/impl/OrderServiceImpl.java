package ute.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ute.configs.JPAConfig;
import ute.dao.impl.OrderDaoImpl;
import ute.dao.impl.CartDaoImpl;
import ute.dao.impl.ProductDaoImpl;
import ute.entities.Addresses;
import ute.entities.Cart;
import ute.entities.CartDetail;
import ute.entities.OrderDetail;
import ute.entities.Orders;
import ute.entities.PaymentMethod;
import ute.entities.Product;
import ute.entities.UserCoupon;
import ute.entities.Users;
import ute.service.inter.OrderService;

public class OrderServiceImpl implements OrderService {
    
    private final OrderDaoImpl orderDao;
    private final CartDaoImpl cartDao;
    private final ProductDaoImpl productDao;
    
    public OrderServiceImpl() {
        this.orderDao = new OrderDaoImpl();
        this.cartDao = new CartDaoImpl();
        this.productDao = new ProductDaoImpl();
    }

    @Override
    public Orders createOrderFromCart(Long userId, Long addressId, Long paymentMethodId, 
                                      String notes, Long userCouponId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            // 1. Lấy giỏ hàng của user
            Cart cart = cartDao.findCartByUserId(userId);
            if (cart == null || cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
                throw new RuntimeException("Giỏ hàng trống!");
            }
            
            // 2. Lấy thông tin cần thiết
            Users user = em.find(Users.class, userId);
            Addresses address = em.find(Addresses.class, addressId);
            PaymentMethod paymentMethod = em.find(PaymentMethod.class, paymentMethodId);
            
            if (user == null) throw new RuntimeException("User không tồn tại!");
            if (address == null) throw new RuntimeException("Địa chỉ không tồn tại!");
            if (paymentMethod == null) throw new RuntimeException("Phương thức thanh toán không hợp lệ!");
            
            // 3. Tính tổng tiền
            double totalAmount = 0.0;
            for (CartDetail item : cart.getCartDetails()) {
                totalAmount += item.getQuantity() * item.getProduct().getUnitPrice();
            }
            
            // 4. Xử lý coupon (nếu có)
            double totalDiscount = 0.0;
            UserCoupon userCoupon = null;
            if (userCouponId != null) {
                userCoupon = em.find(UserCoupon.class, userCouponId);
                if (userCoupon != null && isValidCoupon(userCoupon)) {
                    double discountAmount = totalAmount * (userCoupon.getDiscountPercent() / 100);
                    if (userCoupon.getMaxDiscountAmount() != null && 
                        discountAmount > userCoupon.getMaxDiscountAmount()) {
                        discountAmount = userCoupon.getMaxDiscountAmount();
                    }
                    totalDiscount = discountAmount;
                }
            }
            
            // 5. Tạo đơn hàng
            Orders order = Orders.builder()
                    .user(user)
                    .address(address)
                    .paymentMethod(paymentMethod)
                    .amount(totalAmount)
                    .totalDiscount(totalDiscount)
                    .shippingFee(25000.0)  // Default shipping fee
                    .orderDate(LocalDateTime.now())
                    .orderStatus("PENDING")
                    .paymentStatus("UNPAID")
                    .phoneNumber(address.getPhone())
                    .recipientName(address.getName())
                    .notes(notes)
                    .userCoupon(userCoupon)
                    .build();
            
            // 6. Tạo order details từ cart items
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartDetail cartItem : cart.getCartDetails()) {
                Product product = cartItem.getProduct();
                
                // Kiểm tra tồn kho
                if (product.getStockQuantity() < cartItem.getQuantity()) {
                    throw new RuntimeException("Sản phẩm " + product.getProductName() + 
                                             " không đủ số lượng trong kho!");
                }
                
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(cartItem.getQuantity())
                        .unitPrice(product.getUnitPrice())
                        .build();
                
                orderDetails.add(orderDetail);
                
                // Cập nhật tồn kho và số lượng đã bán
                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                product.setSoldCount((product.getSoldCount() != null ? product.getSoldCount() : 0L) + cartItem.getQuantity());
                em.merge(product);
            }
            
            order.setOrderDetails(orderDetails);
            
            // 7. Lưu đơn hàng
            em.persist(order);
            
            // 8. Xóa giỏ hàng sau khi đặt hàng thành công
            cartDao.clearCart(cart.getCartID());
            
            trans.commit();
            
            return order;
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo đơn hàng: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    private boolean isValidCoupon(UserCoupon coupon) {
        LocalDateTime now = LocalDateTime.now();
        return coupon.getUserCouponStart() != null && 
               coupon.getUserCouponEnd() != null &&
               now.isAfter(coupon.getUserCouponStart()) && 
               now.isBefore(coupon.getUserCouponEnd());
    }

    @Override
    public void updateOrderStatus(Long orderId, String newStatus) {
        Orders order = orderDao.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        order.setOrderStatus(newStatus);
        orderDao.update(order);
    }

    @Override
    public void updatePaymentStatus(Long orderId, String newStatus) {
        Orders order = orderDao.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        order.setPaymentStatus(newStatus);
        orderDao.update(order);
    }

    @Override
    public void cancelOrder(Long orderId, String reason) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            
            Orders order = em.find(Orders.class, orderId);
            if (order == null) {
                throw new RuntimeException("Đơn hàng không tồn tại!");
            }
            
            // Chỉ cho phép hủy đơn hàng ở trạng thái PENDING
            if (!"PENDING".equals(order.getOrderStatus())) {
                throw new RuntimeException("Không thể hủy đơn hàng ở trạng thái " + order.getOrderStatus());
            }
            
            // Hoàn lại tồn kho
            for (OrderDetail detail : order.getOrderDetails()) {
                Product product = detail.getProduct();
                product.setStockQuantity(product.getStockQuantity() + detail.getQuantity());
                product.setSoldCount(product.getSoldCount() - detail.getQuantity());
                em.merge(product);
            }
            
            // Cập nhật trạng thái đơn hàng
            order.setOrderStatus("CANCELLED");
            order.setNotes((order.getNotes() != null ? order.getNotes() + "\n" : "") + 
                          "Lý do hủy: " + reason);
            em.merge(order);
            
            trans.commit();
            
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi hủy đơn hàng: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Orders findById(Long orderId) {
        return orderDao.findById(orderId);
    }

    @Override
    public List<Orders> findByUserId(Long userId) {
        return orderDao.findByUserId(userId);
    }

    @Override
    public List<Orders> findByStatus(String status) {
        return orderDao.findByStatus(status);
    }

    @Override
    public List<Orders> findAll() {
        return orderDao.findAll();
    }

    @Override
    public List<Orders> findByUserIdPaginated(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return orderDao.findByUserIdPaginated(userId, offset, pageSize);
    }

    @Override
    public long countByUserId(Long userId) {
        return orderDao.countByUserId(userId);
    }

    @Override
    public Double calculateOrderTotal(Orders order) {
        if (order == null) return 0.0;
        
        double amount = order.getAmount() != null ? order.getAmount() : 0.0;
        double shipping = order.getShippingFee() != null ? order.getShippingFee() : 0.0;
        double discount = order.getTotalDiscount() != null ? order.getTotalDiscount() : 0.0;
        
        return amount + shipping - discount;
    }
}

