package ute.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import ute.entities.Product;
import ute.entities.Users;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.CartServiceImpl;
import ute.service.inter.ProductService;
import ute.service.inter.CartService;

@WebServlet(urlPatterns = { "/cart/add" })
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductService productService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl();
        this.cartService = new CartServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String productIdStr = req.getParameter("productId");
            String quantityStr = req.getParameter("quantity");
            if (productIdStr == null) {
                out.print("{\"success\":false, \"message\":\"Missing productId\"}");
                return;
            }

            Long productId = Long.parseLong(productIdStr);
            int quantity = 1;
            if (quantityStr != null) {
                quantity = Integer.parseInt(quantityStr);
            }

            Product product = productService.findById(productId);
            if (product == null) {
                out.print("{\"success\":false, \"message\":\"Product not found\"}");
                return;
            }

            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("user");
            if (user == null)
                user = (Users) session.getAttribute("currentUser");
            if (user == null) {
                out.print("{\"success\":false, \"message\":\"Not authenticated\"}");
                return;
            }

            cartService.addToCart(user.getUserID(), productId, quantity);
            // return simple cart count (sum of quantities)
            int totalCount = 0;
            var cart = cartService.getOrCreateCartForUser(user.getUserID());
            if (cart != null && cart.getCartDetails() != null) {
                for (var d : cart.getCartDetails())
                    totalCount += d.getQuantity();
            }
            out.print("{\"success\":true, \"message\":\"Added to cart\", \"cartCount\":" + totalCount + "}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }
}
