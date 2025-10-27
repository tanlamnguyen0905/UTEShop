package ute.service.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ute.configs.JPAConfig;
import ute.dao.impl.CartDaoImpl;
import ute.dao.impl.ProductDaoImpl;
import ute.dao.impl.UserDaoImpl;
import ute.entities.Cart;
import ute.entities.CartDetail;
import ute.entities.Product;
import ute.entities.Users;
import ute.service.inter.CartService;

public class CartServiceImpl implements CartService {

    private final CartDaoImpl cartDao;
    private final ProductDaoImpl productDao;
    private final UserDaoImpl userDao;

    public CartServiceImpl() {
        this.cartDao = new CartDaoImpl();
        this.productDao = new ProductDaoImpl();
        this.userDao = new UserDaoImpl();
    }

    @Override
    public Cart getOrCreateCart(Long userId) {
        Cart cart = cartDao.findCartByUserId(userId);

        if (cart == null) {
            // Tạo cart mới cho user
            Users user = userDao.findById(userId);
            if (user == null) {
                throw new RuntimeException("Không tìm thấy người dùng");
            }

            cart = Cart.builder()
                    .user(user)
                    .totalPrice(0.0)
                    .build();

            cartDao.createCart(cart);
            cart = cartDao.findCartByUserId(userId);
        }

        return cart;
    }

    @Override
    public List<CartDetail> getCartItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartDao.findCartDetailsByCartId(cart.getCartID());
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        // Lấy hoặc tạo giỏ hàng
        Cart cart = getOrCreateCart(userId);

        // Tìm sản phẩm
        Product product = productDao.findById(productId.intValue());
        if (product == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        // Kiểm tra tồn kho
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Sản phẩm tạm hết hàng!");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartDetail existingDetail = cartDao.findCartDetailByCartAndProduct(cart.getCartID(), productId);

        if (existingDetail != null) {
            // Cập nhật số lượng
            int newQuantity = existingDetail.getQuantity() + quantity;

            if (newQuantity > product.getStockQuantity()) {
                throw new RuntimeException("Sản phẩm tạm hết hàng! ");
            }

            existingDetail.setQuantity(newQuantity);
            cartDao.updateCartDetail(existingDetail);
        } else {

            CartDetail cartDetail = CartDetail.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getUnitPrice())
                    .build();

            cartDao.addCartDetail(cartDetail);
        }

        // Cập nhật tổng giá của cart
        updateCartTotal(cart.getCartID());
    }

    @Override
    public void updateCartItemQuantity(Long cartDetailId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Số lượng không thể là số âm");
        }

        CartDetail cartDetail = cartDao.findCartDetailById(cartDetailId);
        if (cartDetail == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }

        if (quantity == 0) {
            // Xóa item nếu quantity = 0
            removeFromCart(cartDetailId);
            return;
        }

        // Kiểm tra tồn kho
        Product product = cartDetail.getProduct();
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Sản phẩm tạm hết hàng! ");
        }

        cartDetail.setQuantity(quantity);
        cartDao.updateCartDetail(cartDetail);

        // Cập nhật tổng giá
        updateCartTotal(cartDetail.getCart().getCartID());
    }

    @Override
    public void removeFromCart(Long cartDetailId) {
        CartDetail cartDetail = cartDao.findCartDetailById(cartDetailId);
        if (cartDetail == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }

        Long cartId = cartDetail.getCart().getCartID();
        cartDao.deleteCartDetail(cartDetailId);

        // Cập nhật tổng giá
        updateCartTotal(cartId);
    }

    @Override
    public void clearCart(Long userId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            
            // Tìm cart trong cùng một EntityManager
            Cart cart = em.createQuery(
                "SELECT c FROM Cart c WHERE c.user.userID = :userId", Cart.class)
                .setParameter("userId", userId)
                .getSingleResult();
            
            if (cart != null) {
                // Xóa tất cả CartDetails trong cùng transaction
                em.createQuery("DELETE FROM CartDetail cd WHERE cd.cart.cartID = :cartId")
                    .setParameter("cartId", cart.getCartID())
                    .executeUpdate();
                
                // Cập nhật total price trong cùng transaction
                cart.setTotalPrice(0.0);
                // Không cần merge vì cart đã được quản lý bởi EntityManager hiện tại
            }
            
            trans.commit();
        } catch (jakarta.persistence.NoResultException e) {
            // Cart không tồn tại, rollback và bỏ qua
            if (trans.isActive()) {
                trans.rollback();
            }
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException("Lỗi khi xóa giỏ hàng: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public double calculateCartTotal(Long userId) {
        Cart cart = cartDao.findCartByUserId(userId);
        if (cart == null) {
            return 0.0;
        }

        List<CartDetail> cartDetails = cartDao.findCartDetailsByCartId(cart.getCartID());

        double total = cartDetails.stream()
                .mapToDouble(detail -> detail.getUnitPrice() * detail.getQuantity())
                .sum();

        return total;
    }

    @Override
    public int getCartItemCount(Long userId) {
        Cart cart = cartDao.findCartByUserId(userId);
        if (cart == null) {
            return 0;
        }
        return cartDao.getTotalItemsInCart(cart.getCartID());
    }

    /**
     * Helper method để cập nhật tổng giá của cart
     */
    private void updateCartTotal(Long cartId) {
        List<CartDetail> cartDetails = cartDao.findCartDetailsByCartId(cartId);

        double total = cartDetails.stream()
                .mapToDouble(detail -> detail.getUnitPrice() * detail.getQuantity())
                .sum();

        // Lấy Cart trực tiếp từ EntityManager thay vì qua CartDetail
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Cart cart = em.find(Cart.class, cartId);
            if (cart != null) {
                EntityTransaction trans = em.getTransaction();
                try {
                    trans.begin();
                    cart.setTotalPrice(total);
                    em.merge(cart);
                    trans.commit();
                } catch (Exception e) {
                    if (trans.isActive()) {
                        trans.rollback();
                    }
                    throw e;
                }
            }
        } finally {
            em.close();
        }
    }
}