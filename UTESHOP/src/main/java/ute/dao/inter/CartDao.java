package ute.dao.inter;

import java.util.List;

import ute.entities.Cart;
import ute.entities.CartDetail;

public interface CartDao {
    
    // Cart operations
    Cart findCartByUserId(Long userId);
    
    void createCart(Cart cart);
    
    void updateCart(Cart cart);
    
    void deleteCart(Long cartId);
    
    // CartDetail operations
    CartDetail findCartDetailById(Long cartDetailId);
    
    List<CartDetail> findCartDetailsByCartId(Long cartId);
    
    CartDetail findCartDetailByCartAndProduct(Long cartId, Long productId);
    
    void addCartDetail(CartDetail cartDetail);
    
    void updateCartDetail(CartDetail cartDetail);
    
    void deleteCartDetail(Long cartDetailId);
    
    void clearCart(Long cartId);
    
    int getTotalItemsInCart(Long cartId);
}

