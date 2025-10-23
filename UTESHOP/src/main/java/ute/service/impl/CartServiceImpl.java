package ute.service.impl;

<<<<<<< HEAD
import ute.dao.impl.CartDaoImpl;
import ute.dao.inter.CartDao;
=======
import java.util.List;

import ute.dao.impl.CartDaoImpl;
import ute.dao.impl.ProductDaoImpl;
import ute.dao.impl.UserDaoImpl;
>>>>>>> d2ca387 (thêm trang giỏ hàng, chỉnh sửa giao diện)
import ute.entities.Cart;
import ute.entities.CartDetail;
import ute.entities.Product;
import ute.entities.Users;
import ute.service.inter.CartService;

public class CartServiceImpl implements CartService {
<<<<<<< HEAD
    private CartDao cartDao;
    private ProductServiceImpl productService = new ProductServiceImpl();

    public CartServiceImpl() {
        this.cartDao = new CartDaoImpl();
    }

    @Override
    public Cart getOrCreateCartForUser(Long userId) {
        Cart cart = cartDao.findByUserId(userId);
        if (cart == null) {
            cart = Cart.builder().user(Users.builder().userID(userId).build()).build();
            cart = cartDao.createCart(cart);
        }
=======

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
                throw new RuntimeException("User not found with id: " + userId);
            }
            
            cart = Cart.builder()
                    .user(user)
                    .totalPrice(0.0)
                    .build();
            
            cartDao.createCart(cart);
            cart = cartDao.findCartByUserId(userId);
        }
        
>>>>>>> d2ca387 (thêm trang giỏ hàng, chỉnh sửa giao diện)
        return cart;
    }

    @Override
<<<<<<< HEAD
    public CartDetail addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getOrCreateCartForUser(userId);
        Product p = productService.findById(productId);
        if (p == null)
            throw new IllegalArgumentException("Product not found");
        CartDetail detail = CartDetail.builder()
                .cart(cart)
                .product(p)
                .quantity(quantity)
                .unitPrice(p.getUnitPrice())
                .build();
        return cartDao.addOrUpdateCartDetail(detail);
    }
}
=======
    public List<CartDetail> getCartItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartDao.findCartDetailsByCartId(cart.getCartID());
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // Lấy hoặc tạo giỏ hàng
        Cart cart = getOrCreateCart(userId);
        
        // Tìm sản phẩm
        Product product = productDao.findById(productId.intValue());
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        
        // Kiểm tra tồn kho
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock. Available: " + product.getStockQuantity());
        }
        
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartDetail existingDetail = cartDao.findCartDetailByCartAndProduct(cart.getCartID(), productId);
        
        if (existingDetail != null) {
            // Cập nhật số lượng
            int newQuantity = existingDetail.getQuantity() + quantity;
            
            if (newQuantity > product.getStockQuantity()) {
                throw new RuntimeException("Not enough stock. Available: " + product.getStockQuantity());
            }
            
            existingDetail.setQuantity(newQuantity);
            cartDao.updateCartDetail(existingDetail);
        } else {
            // Thêm mới cart detail
            Double unitPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getUnitPrice();
            
            CartDetail cartDetail = CartDetail.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .build();
            
            cartDao.addCartDetail(cartDetail);
        }
        
        // Cập nhật tổng giá của cart
        updateCartTotal(cart.getCartID());
    }

    @Override
    public void updateCartItemQuantity(Long cartDetailId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        CartDetail cartDetail = cartDao.findCartDetailById(cartDetailId);
        if (cartDetail == null) {
            throw new RuntimeException("Cart item not found with id: " + cartDetailId);
        }
        
        if (quantity == 0) {
            // Xóa item nếu quantity = 0
            removeFromCart(cartDetailId);
            return;
        }
        
        // Kiểm tra tồn kho
        Product product = cartDetail.getProduct();
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock. Available: " + product.getStockQuantity());
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
            throw new RuntimeException("Cart item not found with id: " + cartDetailId);
        }
        
        Long cartId = cartDetail.getCart().getCartID();
        cartDao.deleteCartDetail(cartDetailId);
        
        // Cập nhật tổng giá
        updateCartTotal(cartId);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartDao.findCartByUserId(userId);
        if (cart != null) {
            cartDao.clearCart(cart.getCartID());
            cart.setTotalPrice(0.0);
            cartDao.updateCart(cart);
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
        
        Cart cart = cartDao.findCartDetailById(cartDetails.isEmpty() ? null : cartDetails.get(0).getCartDetailID())
                .getCart();
        if (cart != null) {
            cart.setTotalPrice(total);
            cartDao.updateCart(cart);
        }
    }
}

>>>>>>> d2ca387 (thêm trang giỏ hàng, chỉnh sửa giao diện)
