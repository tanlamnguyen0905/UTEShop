package ute.controllers.web;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.Addresses;
import ute.entities.Cart;
import ute.entities.Orders;
import ute.entities.PaymentMethod;
import ute.entities.Users;
import ute.service.impl.AddressServiceImpl;
import ute.service.impl.CartServiceImpl;
import ute.service.impl.OrderServiceImpl;
import ute.dao.impl.PaymentMethodDaoImpl;

@WebServlet(urlPatterns = {"/checkout", "/checkout/process", "/checkout/success"})
public class CheckoutController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final CartServiceImpl cartService;
    private final AddressServiceImpl addressService;
    private final OrderServiceImpl orderService;
    private final PaymentMethodDaoImpl paymentMethodDao;
    
    public CheckoutController() {
        this.cartService = new CartServiceImpl();
        this.addressService = new AddressServiceImpl();
        this.orderService = new OrderServiceImpl();
        this.paymentMethodDao = new PaymentMethodDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Kiểm tra đăng nhập
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        
        if (uri.contains("/checkout/success")) {
            // Hiển thị trang thành công
            Long orderId = (Long) session.getAttribute("lastOrderId");
            if (orderId != null) {
                Orders order = orderService.findById(orderId);
                req.setAttribute("order", order);
                
                // Format date for JSP display
                if (order != null && order.getOrderDate() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    String formattedDate = order.getOrderDate().format(formatter);
                    req.setAttribute("formattedOrderDate", formattedDate);
                }
                
                session.removeAttribute("lastOrderId");
            }
            req.getRequestDispatcher("/WEB-INF/views/web/order-success.jsp").forward(req, resp);
            
        } else {
            // Trang checkout
            Long userId = currentUser.getUserID();
            
            // Lấy giỏ hàng
            Cart cart = cartService.getOrCreateCart(userId);
            if (cart == null || cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
            
            // Lấy danh sách địa chỉ của user
            List<Addresses> addresses = addressService.getAddressesByUserId(userId);
            
            // Lấy địa chỉ mặc định
            Addresses defaultAddress = addresses.stream()
                    .filter(a -> a.getIsDefault() != null && a.getIsDefault())
                    .findFirst()
                    .orElse(addresses.isEmpty() ? null : addresses.get(0));
            
            // Lấy danh sách phương thức thanh toán
            List<PaymentMethod> paymentMethods = paymentMethodDao.findAll();
            
            // Set attributes
            req.setAttribute("cart", cart);
            req.setAttribute("addresses", addresses);
            req.setAttribute("defaultAddress", defaultAddress);
            req.setAttribute("paymentMethods", paymentMethods);
            
            req.getRequestDispatcher("/WEB-INF/views/web/checkout.jsp").forward(req, resp);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Kiểm tra đăng nhập
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        
        if (uri.contains("/checkout/process")) {
            try {
                // Lấy dữ liệu từ form
                Long addressId = Long.parseLong(req.getParameter("addressId"));
                Long paymentMethodId = Long.parseLong(req.getParameter("paymentMethodId"));
                String notes = req.getParameter("notes");
                
                // Lấy couponId nếu có
                Long userCouponId = null;
                String couponIdParam = req.getParameter("userCouponId");
                if (couponIdParam != null && !couponIdParam.trim().isEmpty()) {
                    try {
                        userCouponId = Long.parseLong(couponIdParam);
                    } catch (NumberFormatException e) {
                        // Ignore invalid coupon ID
                    }
                }
                
                // Tạo đơn hàng
                Orders order = orderService.createOrderFromCart(
                    currentUser.getUserID(), 
                    addressId, 
                    paymentMethodId, 
                    notes,
                    userCouponId
                );
                
                // Lưu orderId vào session để hiển thị trang success
                session.setAttribute("lastOrderId", order.getOrderID());
                
                // Redirect đến trang thành công
                resp.sendRedirect(req.getContextPath() + "/checkout/success");
                
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Đặt hàng thất bại: " + e.getMessage());
                
                // Reload checkout page with error
                doGet(req, resp);
            }
        }
    }
}

