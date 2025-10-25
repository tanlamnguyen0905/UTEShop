package ute.controllers.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dao.impl.UserDaoImpl;
import ute.dto.DeliveryDTO;
import ute.dto.ShipperDTO;
import ute.entities.Orders;
import ute.entities.Users;
import ute.mapper.ShipperMapper;
import ute.service.impl.DeliveryServiceImpl;
import ute.service.impl.OrderServiceImpl;

@WebServlet(urlPatterns = {
    "/api/admin/deliveries",
    "/api/admin/delivery/assign"
})
public class DeliveryManagementController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final DeliveryServiceImpl deliveryService;
    private final OrderServiceImpl orderService;
    private final UserDaoImpl userDao;
    
    public DeliveryManagementController() {
        this.deliveryService = new DeliveryServiceImpl();
        this.orderService = new OrderServiceImpl();
        this.userDao = new UserDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Filter đã kiểm tra authentication và role, chỉ cần lấy user
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        try {
            if (uri.contains("/api/admin/deliveries")) {
                showDeliveriesManagement(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Filter đã kiểm tra authentication và role, chỉ cần lấy user
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        try {
            if (uri.contains("/api/admin/delivery/assign")) {
                assignOrderToShipper(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/admin/deliveries");
        }
    }
    
    private void showDeliveriesManagement(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Lấy danh sách đơn hàng đã được xác nhận (chưa gán shipper)
        List<Orders> confirmedOrders = orderService.findByStatus("Đã xác nhận");
        
        // Lấy danh sách tất cả deliveries (sử dụng DTO)
        List<DeliveryDTO> allDeliveries = deliveryService.findAll();
        
        // Lấy danh sách shipper và chuyển sang DTO
        List<Users> shipperEntities = userDao.findByRole("SHIPPER");
        List<ShipperDTO> shippers = ShipperMapper.toDTOList(shipperEntities);
        
        req.setAttribute("confirmedOrders", confirmedOrders);
        req.setAttribute("allDeliveries", allDeliveries);
        req.setAttribute("shippers", shippers);
        
        req.getRequestDispatcher("/WEB-INF/views/admin/deliveries.jsp").forward(req, resp);
    }
    
    private void assignOrderToShipper(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        Long orderId = Long.parseLong(req.getParameter("orderId"));
        Long shipperId = Long.parseLong(req.getParameter("shipperId"));
        String notes = req.getParameter("notes");
        
        deliveryService.assignOrderToShipper(orderId, shipperId, notes);
        
        HttpSession session = req.getSession();
        session.setAttribute("success", "Đã gán đơn hàng cho shipper thành công!");
        
        resp.sendRedirect(req.getContextPath() + "/admin/deliveries");
    }
}
