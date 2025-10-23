package ute.service.inter;

import ute.entities.Cart;
import ute.entities.CartDetail;

public interface CartService {
    Cart getOrCreateCartForUser(Long userId);

    CartDetail addToCart(Long userId, Long productId, int quantity);
}
