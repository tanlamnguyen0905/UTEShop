package ute.controllers.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.Users;
import ute.service.impl.CartServiceImpl;

@WebServlet(urlPatterns = {"/api/cart/*"})
public class CartApiController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final CartServiceImpl cartService = new CartServiceImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        PrintWriter out = resp.getWriter();
        
        try {
            Users currentUser = getCurrentUser(req);
            if (currentUser == null) {
                sendErrorResponse(resp, "Vui lòng đăng nhập", 401);
                return;
            }
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // Lấy thông tin giỏ hàng
                int itemCount = cartService.getCartItemCount(currentUser.getUserID());
                double totalPrice = cartService.calculateCartTotal(currentUser.getUserID());
                
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                response.addProperty("itemCount", itemCount);
                response.addProperty("totalPrice", totalPrice);
                
                out.print(gson.toJson(response));
            } else if (pathInfo.equals("/count")) {
                // Lấy số lượng item trong giỏ hàng
                int itemCount = cartService.getCartItemCount(currentUser.getUserID());
                
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                response.addProperty("count", itemCount);
                
                out.print(gson.toJson(response));
            } else {
                sendErrorResponse(resp, "Endpoint không tồn tại", 404);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Có lỗi xảy ra: " + e.getMessage(), 500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            Users currentUser = getCurrentUser(req);
            if (currentUser == null) {
                sendErrorResponse(resp, "Vui lòng đăng nhập", 401);
                return;
            }
            
            if (pathInfo == null || pathInfo.equals("/")) {
                sendErrorResponse(resp, "Method not allowed", 405);
                return;
            }
            
            switch (pathInfo) {
                case "/add":
                    handleAddToCart(req, resp, currentUser);
                    break;
                case "/update":
                    handleUpdateQuantity(req, resp, currentUser);
                    break;
                case "/remove":
                    handleRemoveFromCart(req, resp, currentUser);
                    break;
                case "/clear":
                    handleClearCart(req, resp, currentUser);
                    break;
                default:
                    sendErrorResponse(resp, "Endpoint không tồn tại", 404);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Có lỗi xảy ra: " + e.getMessage(), 500);
        }
    }
    
    private void handleAddToCart(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws IOException {
        
        JsonObject requestBody = getRequestBody(req);
        
        if (requestBody == null || !requestBody.has("productId") || !requestBody.has("quantity")) {
            sendErrorResponse(resp, "Thiếu thông tin sản phẩm hoặc số lượng", 400);
            return;
        }
        
        try {
            Long productId = requestBody.get("productId").getAsLong();
            int quantity = requestBody.get("quantity").getAsInt();
            
            cartService.addToCart(user.getUserID(), productId, quantity);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Đã thêm sản phẩm vào giỏ hàng");
            response.addProperty("itemCount", cartService.getCartItemCount(user.getUserID()));
            
            resp.getWriter().print(gson.toJson(response));
            
        } catch (RuntimeException e) {
            sendErrorResponse(resp, e.getMessage(), 400);
        }
    }
    
    private void handleUpdateQuantity(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws IOException {
        
        JsonObject requestBody = getRequestBody(req);
        
        if (requestBody == null || !requestBody.has("cartDetailId") || !requestBody.has("quantity")) {
            sendErrorResponse(resp, "Thiếu thông tin", 400);
            return;
        }
        
        try {
            Long cartDetailId = requestBody.get("cartDetailId").getAsLong();
            int quantity = requestBody.get("quantity").getAsInt();
            
            cartService.updateCartItemQuantity(cartDetailId, quantity);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Đã cập nhật số lượng");
            response.addProperty("itemCount", cartService.getCartItemCount(user.getUserID()));
            
            resp.getWriter().print(gson.toJson(response));
            
        } catch (RuntimeException e) {
            sendErrorResponse(resp, e.getMessage(), 400);
        }
    }
    
    private void handleRemoveFromCart(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws IOException {
        
        JsonObject requestBody = getRequestBody(req);
        
        if (requestBody == null || !requestBody.has("cartDetailId")) {
            sendErrorResponse(resp, "Thiếu thông tin", 400);
            return;
        }
        
        try {
            Long cartDetailId = requestBody.get("cartDetailId").getAsLong();
            
            cartService.removeFromCart(cartDetailId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Đã xóa sản phẩm khỏi giỏ hàng");
            response.addProperty("itemCount", cartService.getCartItemCount(user.getUserID()));
            
            resp.getWriter().print(gson.toJson(response));
            
        } catch (RuntimeException e) {
            sendErrorResponse(resp, e.getMessage(), 400);
        }
    }
    
    private void handleClearCart(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws IOException {
        
        try {
            cartService.clearCart(user.getUserID());
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Đã xóa tất cả sản phẩm trong giỏ hàng");
            response.addProperty("itemCount", 0);
            
            resp.getWriter().print(gson.toJson(response));
            
        } catch (RuntimeException e) {
            sendErrorResponse(resp, e.getMessage(), 400);
        }
    }
    
    private Users getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return (Users) session.getAttribute("currentUser");
        }
        return null;
    }
    
    private JsonObject getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        if (sb.length() == 0) {
            return null;
        }
        
        return gson.fromJson(sb.toString(), JsonObject.class);
    }
    
    private void sendErrorResponse(HttpServletResponse resp, String message, int statusCode) 
            throws IOException {
        resp.setStatus(statusCode);
        
        JsonObject error = new JsonObject();
        error.addProperty("success", false);
        error.addProperty("error", message);
        
        resp.getWriter().print(gson.toJson(error));
    }
}