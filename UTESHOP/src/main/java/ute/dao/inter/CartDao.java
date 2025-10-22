package ute.dao.inter;

import ute.entities.Cart;
import ute.entities.CartDetail;

public interface CartDao {
    Cart findByUserId(Long userId);

    Cart createCart(Cart cart);

    CartDetail addOrUpdateCartDetail(CartDetail detail);
}
