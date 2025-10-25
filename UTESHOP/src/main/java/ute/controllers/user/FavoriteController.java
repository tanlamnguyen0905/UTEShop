package ute.controllers.user;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dto.ProductDTO;
import ute.entities.Users;
import ute.service.impl.FavoriteServiceImpl;

@WebServlet(urlPatterns = { "/user/favorites" })
public class FavoriteController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Kiểm tra user đã đăng nhập chưa
        HttpSession session = req.getSession(false);
        Users currentUser = null;

        if (session != null) {
            currentUser = (Users) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // Chuyển về trang home và hiển thị modal đăng nhập
            resp.sendRedirect(req.getContextPath() + "/?showLogin=true");
            return;
        }

        try {
            // Lấy danh sách sản phẩm yêu thích
            List<ProductDTO> favoriteItems = favoriteService.findByUserId(currentUser.getUserID());
            int itemCount = favoriteItems.size();

            // DEBUG: Log để kiểm tra
            System.out.println("=== DEBUG FAVORITE ===");
            System.out.println("User ID: " + currentUser.getUserID());
            System.out.println("Favorite Items Count: " + itemCount);
            if (favoriteItems != null && !favoriteItems.isEmpty()) {
                System.out.println("First Item: " + favoriteItems.get(0).getProductName());
            }
            System.out.println("=====================");

            // Set attributes để sử dụng trong JSP (CHÚ Ý: viết thường favoriteItems)
            req.setAttribute("favoriteItems", favoriteItems);
            req.setAttribute("itemCount", itemCount);

            // Forward đến trang yêu thích
            req.getRequestDispatcher("/WEB-INF/views/web/favorite.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra khi tải danh sách yêu thích: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/web/favorite.jsp").forward(req, resp);
        }
    }
}