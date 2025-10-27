package ute.service.inter;

import java.util.List;

import ute.entities.Cart;
import ute.entities.CartDetail;

public interface CartService {

    Cart getOrCreateCart(Long userId);

    List<CartDetail> getCartItems(Long userId);

    void addToCart(Long userId, Long productId, int quantity);

    void updateCartItemQuantity(Long cartDetailId, int quantity);

    void removeFromCart(Long cartDetailId);

    void clearCart(Long userId);

    double calculateCartTotal(Long userId);

    int getCartItemCount(Long userId);

}
