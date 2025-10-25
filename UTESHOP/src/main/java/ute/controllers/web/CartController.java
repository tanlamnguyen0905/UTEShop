package ute.controllers.web;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.CartDetail;
import ute.entities.Users;
import ute.service.impl.CartServiceImpl;

@WebServlet(urlPatterns = { "/cart" })
public class CartController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CartServiceImpl cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
            // Lấy danh sách sản phẩm trong giỏ hàng
            List<CartDetail> cartItems = cartService.getCartItems(currentUser.getUserID());

            // Tính tổng giá và số lượng sản phẩm
            double totalPrice = cartService.calculateCartTotal(currentUser.getUserID());
            int itemCount = cartService.getCartItemCount(currentUser.getUserID());

            // Set attributes để sử dụng trong JSP
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("totalPrice", totalPrice);
            req.setAttribute("itemCount", itemCount);

            // Forward đến trang giỏ hàng
            req.getRequestDispatcher("/WEB-INF/views/web/cart.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra khi tải giỏ hàng: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/web/cart.jsp").forward(req, resp);
        }
    }
}
