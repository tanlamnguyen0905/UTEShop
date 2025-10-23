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
import ute.entities.Favorite;
import ute.entities.Users;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.FavoriteServiceImpl;
import ute.service.inter.ProductService;
import ute.service.inter.FavoriteService;

@WebServlet(urlPatterns = { "/wishlist/add" })
public class WishlistController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductService productService;
    private FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl();
        this.favoriteService = new FavoriteServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {

            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                user = (Users) session.getAttribute("currentUser");
            }
            if (user == null) {
                out.print("{\"success\":false, \"message\":\"Not authenticated\"}");
                return;
            }

            String productIdStr = req.getParameter("productId");
            if (productIdStr == null) {
                out.print("{\"success\":false, \"message\":\"Missing productId\"}");
                return;
            }

            Long productId = Long.parseLong(productIdStr);
            Product product = productService.findById(productId);
            if (product == null) {
                out.print("{\"success\":false, \"message\":\"Product not found\"}");
                return;
            }

            Long userId = user.getUserID();
            boolean already = favoriteService.isFavorite(userId, productId);
            if (already) {
                favoriteService.removeFavorite(userId, productId);
                out.print("{\"success\":true, \"action\":\"removed\", \"message\":\"Removed from wishlist\"}");
            } else {
                Favorite fav = new Favorite();
                fav.setUser(user);
                fav.setProduct(product);
                favoriteService.addFavorite(fav);
                out.print("{\"success\":true, \"action\":\"added\", \"message\":\"Added to wishlist\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }
}
