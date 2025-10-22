package ute.service.impl;

import ute.dao.impl.CartDaoImpl;
import ute.dao.inter.CartDao;
import ute.entities.Cart;
import ute.entities.CartDetail;
import ute.entities.Product;
import ute.entities.Users;
import ute.service.inter.CartService;

public class CartServiceImpl implements CartService {
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
        return cart;
    }

    @Override
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
