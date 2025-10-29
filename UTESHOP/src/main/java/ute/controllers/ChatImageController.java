package ute.controllers;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ute.utils.Constant;
import ute.utils.FileStorage;

import com.google.gson.JsonObject;

@WebServlet(urlPatterns = {"/chat/upload-image"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 15     // 15 MB
)
public class ChatImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Kiểm tra đăng nhập
            if (request.getSession(false) == null || request.getSession(false).getAttribute("currentUser") == null) {
                sendJsonResponse(response, false, "Vui lòng đăng nhập", null);
                return;
            }
            
            Part filePart = request.getPart("image");
            
            if (filePart == null || filePart.getSize() == 0) {
                sendJsonResponse(response, false, "Vui lòng chọn ảnh để tải lên", null);
                return;
            }
            
            // Validate file type
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                sendJsonResponse(response, false, "Chỉ chấp nhận file hình ảnh!", null);
                return;
            }
            
            // Validate file size (max 10MB)
            if (filePart.getSize() > 10 * 1024 * 1024) {
                sendJsonResponse(response, false, "Kích thước ảnh tối đa 10MB!", null);
                return;
            }
            
            FileStorage chatStorage = new FileStorage(request.getServletContext(), Constant.UPLOAD_DIR_CHAT);
            String filename = chatStorage.save(filePart);
            
            if (filename != null) {
                String imageUrl = filename;
                sendJsonResponse(response, true, "Upload ảnh thành công!", imageUrl);
            } else {
                sendJsonResponse(response, false, "Lỗi khi lưu file!", null);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Lỗi: " + e.getMessage(), null);
        }
    }
    
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, String imageUrl) 
            throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        if (imageUrl != null) {
            json.addProperty("imageUrl", imageUrl);
        }
        response.getWriter().write(json.toString());
    }
}

