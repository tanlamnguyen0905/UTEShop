package ute.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.Addresses;
import ute.entities.Users;
import ute.service.impl.AddressServiceImpl;
import ute.service.inter.AddressService;

@WebServlet(urlPatterns = { "/user/address/add", "/user/address/edit", "/user/address/delete" })
public class AddressController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        // Kiểm tra đăng nhập
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        // Xử lý các action
        if ("/user/address/add".equals(path)) {
            addAddress(req, resp, currentUser);
        } else if ("/user/address/edit".equals(path)) {
            updateAddress(req, resp, currentUser);
        } else if ("/user/address/delete".equals(path)) {
            deleteAddress(req, resp, currentUser);
        }
    }

    // Thêm địa chỉ mới
    private void addAddress(HttpServletRequest req, HttpServletResponse resp, Users currentUser) 
            throws IOException, ServletException {
        
        try {
            // Set UTF-8 encoding
            req.setCharacterEncoding("UTF-8");
            
            String province = req.getParameter("province");
            String district = req.getParameter("district");
            String ward = req.getParameter("ward");
            String addressDetail = req.getParameter("addressDetail");

            System.out.println("=== ADD ADDRESS DEBUG ===");
            System.out.println("Province: " + province);
            System.out.println("District: " + district);
            System.out.println("Ward: " + ward);
            System.out.println("AddressDetail: " + addressDetail);

            // Validation
            if (province == null || province.trim().isEmpty() ||
                district == null || district.trim().isEmpty() ||
                ward == null || ward.trim().isEmpty() ||
                addressDetail == null || addressDetail.trim().isEmpty()) {
                
                System.out.println("ERROR: Missing required fields");
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                    java.net.URLEncoder.encode("Vui lòng điền đầy đủ thông tin!", "UTF-8") + "#address");
                return;
            }

            // Tạo address
            Addresses address = Addresses.builder()
                    .province(province.trim())
                    .district(district.trim())
                    .ward(ward.trim())
                    .addressDetail(addressDetail.trim())
                    .user(currentUser)
                    .build();

            // Lưu vào DB
            addressService.addAddress(address);
            System.out.println("Address saved successfully!");
            
            // Redirect về profile với thông báo thành công
            resp.sendRedirect(req.getContextPath() + "/user/profile?success=" + 
                java.net.URLEncoder.encode("Thêm địa chỉ thành công!", "UTF-8") + "#address");
            
        } catch (Exception e) {
            System.out.println("ERROR saving address: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                java.net.URLEncoder.encode("Lỗi khi thêm địa chỉ!", "UTF-8") + "#address");
        }
    }

    // Cập nhật địa chỉ
    private void updateAddress(HttpServletRequest req, HttpServletResponse resp, Users currentUser) 
            throws IOException, ServletException {
        
        try {
            req.setCharacterEncoding("UTF-8");
            
            String addressIdStr = req.getParameter("addressId");
            if (addressIdStr == null || addressIdStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                    java.net.URLEncoder.encode("ID địa chỉ không hợp lệ!", "UTF-8") + "#address");
                return;
            }

            Long addressId = Long.parseLong(addressIdStr);
            Addresses address = addressService.getAddressById(addressId);

            if (address == null || !address.getUser().getUserID().equals(currentUser.getUserID())) {
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                    java.net.URLEncoder.encode("Bạn không có quyền sửa địa chỉ này!", "UTF-8") + "#address");
                return;
            }

            String province = req.getParameter("province");
            String district = req.getParameter("district");
            String ward = req.getParameter("ward");
            String addressDetail = req.getParameter("addressDetail");
            
            // Validation
            if (province == null || province.trim().isEmpty() ||
                district == null || district.trim().isEmpty() ||
                ward == null || ward.trim().isEmpty() ||
                addressDetail == null || addressDetail.trim().isEmpty()) {
                
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                    java.net.URLEncoder.encode("Vui lòng điền đầy đủ thông tin!", "UTF-8") + "#address");
                return;
            }

            address.setProvince(province.trim());
            address.setDistrict(district.trim());
            address.setWard(ward.trim());
            address.setAddressDetail(addressDetail.trim());

            addressService.updateAddress(address);
            
            // Redirect về profile với thông báo thành công
            resp.sendRedirect(req.getContextPath() + "/user/profile?success=" + 
                java.net.URLEncoder.encode("Cập nhật địa chỉ thành công!", "UTF-8") + "#address");
                
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                java.net.URLEncoder.encode("Lỗi khi cập nhật địa chỉ!", "UTF-8") + "#address");
        }
    }

    // Xóa địa chỉ
    private void deleteAddress(HttpServletRequest req, HttpServletResponse resp, Users currentUser) 
            throws IOException, ServletException {
        
        try {
            String addressIdStr = req.getParameter("id");
            if (addressIdStr == null || addressIdStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                    java.net.URLEncoder.encode("ID địa chỉ không hợp lệ!", "UTF-8") + "#address");
                return;
            }

            Long addressId = Long.parseLong(addressIdStr);
            Addresses address = addressService.getAddressById(addressId);

            if (address == null || !address.getUser().getUserID().equals(currentUser.getUserID())) {
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                    java.net.URLEncoder.encode("Bạn không có quyền xóa địa chỉ này!", "UTF-8") + "#address");
                return;
            }

            addressService.deleteAddress(addressId);
            
            // Redirect về profile với thông báo thành công
            resp.sendRedirect(req.getContextPath() + "/user/profile?success=" + 
                java.net.URLEncoder.encode("Xóa địa chỉ thành công!", "UTF-8") + "#address");
                
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/user/profile?error=" + 
                java.net.URLEncoder.encode("Lỗi khi xóa địa chỉ!", "UTF-8") + "#address");
        }
    }
}

