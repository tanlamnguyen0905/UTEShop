package ute.controllers.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dto.AddressDTO;
import ute.entities.Addresses;
import ute.entities.Users;
import ute.service.impl.AddressServiceImpl;
import ute.service.inter.AddressService;

@WebServlet(urlPatterns = {"/api/user/addresses", "/api/user/addresses/*"})
public class AddressApiController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final AddressService addressService = new AddressServiceImpl();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }
        
        try {
            String pathInfo = req.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/user/addresses - Lấy tất cả địa chỉ của user
                getAllAddresses(currentUser, resp, out);
            } else {
                // GET /api/user/addresses/{id} - Lấy 1 địa chỉ theo ID
                String[] paths = pathInfo.split("/");
                if (paths.length == 2) {
                    Long addressId = Long.parseLong(paths[1]);
                    getAddressById(addressId, currentUser, resp, out);
                } else {
                    sendError(resp, out, 400, "Invalid path");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }
        
        try {
            // POST /api/user/addresses - Tạo địa chỉ mới
            AddressDTO dto = gson.fromJson(req.getReader(), AddressDTO.class);
            
            // Validation
            if (dto == null || dto.getName() == null || dto.getPhone() == null ||
                dto.getProvince() == null || dto.getDistrict() == null || 
                dto.getWard() == null || dto.getAddressDetail() == null) {
                sendError(resp, out, 400, "Missing required fields");
                return;
            }
            
            // Convert DTO sang Entity
            Addresses address = dto.toEntity(currentUser);
            
            // Lưu vào DB
            addressService.addAddress(address);
            
            // Trả về DTO của địa chỉ vừa tạo
            AddressDTO result = AddressDTO.fromEntity(address);
            sendSuccess(resp, out, result, "Address created successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }
        
        try {
            // PUT /api/user/addresses/{id} - Update địa chỉ
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                sendError(resp, out, 400, "Address ID required");
                return;
            }
            
            String[] paths = pathInfo.split("/");
            if (paths.length != 2) {
                sendError(resp, out, 400, "Invalid path");
                return;
            }
            
            Long addressId = Long.parseLong(paths[1]);
            
            // Kiểm tra quyền sở hữu
            Addresses existingAddress = addressService.getAddressById(addressId);
            if (existingAddress == null || 
                !existingAddress.getUser().getUserID().equals(currentUser.getUserID())) {
                sendError(resp, out, 403, "Forbidden - You don't own this address");
                return;
            }
            
            // Parse DTO từ request
            AddressDTO dto = gson.fromJson(req.getReader(), AddressDTO.class);
            dto.setAddressID(addressId); // Ensure correct ID
            
            // Validation
            if (dto.getName() == null || dto.getPhone() == null ||
                dto.getProvince() == null || dto.getDistrict() == null || 
                dto.getWard() == null || dto.getAddressDetail() == null) {
                sendError(resp, out, 400, "Missing required fields");
                return;
            }
            
            // Update entity từ DTO
            dto.updateEntity(existingAddress);
            
            // Update vào DB
            addressService.updateAddress(existingAddress);
            
            // Trả về DTO đã update
            AddressDTO result = AddressDTO.fromEntity(existingAddress);
            sendSuccess(resp, out, result, "Address updated successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }
        
        try {
            // DELETE /api/user/addresses/{id}
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                sendError(resp, out, 400, "Address ID required");
                return;
            }
            
            String[] paths = pathInfo.split("/");
            if (paths.length != 2) {
                sendError(resp, out, 400, "Invalid path");
                return;
            }
            
            Long addressId = Long.parseLong(paths[1]);
            
            // Kiểm tra quyền sở hữu
            Addresses address = addressService.getAddressById(addressId);
            if (address == null || 
                !address.getUser().getUserID().equals(currentUser.getUserID())) {
                sendError(resp, out, 403, "Forbidden - You don't own this address");
                return;
            }
            
            // Không cho xóa địa chỉ mặc định
            if (address.getIsDefault()) {
                sendError(resp, out, 400, "Cannot delete default address");
                return;
            }
            
            // Xóa
            addressService.deleteAddress(addressId);
            
            sendSuccess(resp, out, null, "Address deleted successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    // Helper methods
    
    private void getAllAddresses(Users currentUser, HttpServletResponse resp, PrintWriter out) {
        List<Addresses> addresses = addressService.getAddressesByUserId(currentUser.getUserID());
        
        // Convert sang DTO list
        List<AddressDTO> dtoList = addresses.stream()
                .map(AddressDTO::fromEntity)
                .collect(Collectors.toList());
        
        sendSuccess(resp, out, dtoList, "Success");
    }
    
    private void getAddressById(Long addressId, Users currentUser, 
                                HttpServletResponse resp, PrintWriter out) {
        Addresses address = addressService.getAddressById(addressId);
        
        if (address == null) {
            sendError(resp, out, 404, "Address not found");
            return;
        }
        
        // Kiểm tra quyền sở hữu
        if (!address.getUser().getUserID().equals(currentUser.getUserID())) {
            sendError(resp, out, 403, "Forbidden - You don't own this address");
            return;
        }
        
        // Convert sang DTO
        AddressDTO dto = AddressDTO.fromEntity(address);
        sendSuccess(resp, out, dto, "Success");
    }
    
    private void sendSuccess(HttpServletResponse resp, PrintWriter out, 
                            Object data, String message) {
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        out.print(gson.toJson(response));
    }
    
    private void sendError(HttpServletResponse resp, PrintWriter out, 
                          int statusCode, String message) {
        resp.setStatus(statusCode);
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        out.print(gson.toJson(response));
    }
}

