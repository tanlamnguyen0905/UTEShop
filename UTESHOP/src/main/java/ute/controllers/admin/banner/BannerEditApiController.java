package ute.controllers.admin.banner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.dto.ProductDTO;
import ute.entities.Banner;
import ute.entities.Product;
import ute.service.impl.BannerServiceImpl;
import ute.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {
        "/api/admin/banner/addProduct",
        "/api/admin/banner/removeProduct",
        "/api/admin/product/search"
})
public class BannerEditApiController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();
    private final BannerServiceImpl bannerService = new BannerServiceImpl();
    private final ProductServiceImpl productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        resp.setContentType("application/json;charset=UTF-8");

        // --- SEARCH PRODUCT ---
        if (uri.contains("/api/admin/product/search")) {
            String keyword = req.getParameter("kw");

            // Validate keyword
            if (keyword == null || keyword.trim().isEmpty()) {
                resp.getWriter().print("[]");
                return;
            }

            try {
                List<Product> temp = productService.findByName(keyword.trim());

                // Null check
                if (temp == null) {
                    resp.getWriter().print("[]");
                    return;
                }

                List<ProductDTO> result = productService.MapToProductDTO(temp);

                // Filter null results
                if (result != null) {
                    result = result.stream()
                            .filter(p -> p != null)
                            .collect(Collectors.toList());
                }

                resp.getWriter().print(gson.toJson(result != null ? result : List.of()));

            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(500);
                resp.getWriter().print("{\"error\":\"Lỗi khi tìm kiếm sản phẩm\"}");
            }
        } else {
            resp.setStatus(404);
            resp.getWriter().print("{\"error\":\"Endpoint không tồn tại\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        resp.setContentType("application/json;charset=UTF-8");

        JsonObject body = getRequestBody(req);
        if (body == null) {
            sendError(resp, "Dữ liệu JSON không hợp lệ", 400);
            return;
        }

        try {
            if (uri.contains("/api/admin/banner/addProduct")) {
                handleAddProduct(body, resp);
            } else if (uri.contains("/api/admin/banner/removeProduct")) {
                handleRemoveProduct(body, resp);
            } else {
                sendError(resp, "Endpoint không tồn tại", 404);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            sendError(resp, "Dữ liệu không hợp lệ: " + e.getMessage(), 400);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, "Lỗi hệ thống: " + e.getMessage(), 500);
        }
    }

    /**
     * Xử lý thêm sản phẩm vào banner
     */
    private void handleAddProduct(JsonObject body, HttpServletResponse resp) throws IOException {
        // Validate input
        if (!body.has("bannerId") || !body.has("productId")) {
            sendError(resp, "Thiếu bannerId hoặc productId", 400);
            return;
        }

        final Long bannerId, productId;

        try {
            bannerId = body.get("bannerId").getAsLong();
            productId = body.get("productId").getAsLong();
        } catch (Exception e) {
            sendError(resp, "bannerId và productId phải là số nguyên", 400);
            return;
        }

        // Validate banner exists
        Banner banner = bannerService.findById(bannerId);
        if (banner == null) {
            sendError(resp, "Banner không tồn tại", 404);
            return;
        }

        // Validate product exists
        Product product = productService.findById(productId);
        if (product == null) {
            sendError(resp, "Sản phẩm không tồn tại", 404);
            return;
        }

        // Check if product already in banner
        if (banner.getProducts() != null &&
                banner.getProducts().stream().anyMatch(p -> p.getProductID().equals(productId))) {
            sendError(resp, "Sản phẩm đã có trong banner", 400);
            return;
        }

        // Add product to banner
        try {
            bannerService.addProductToBanner(bannerId, productId);
            sendSuccess(resp, "Thêm sản phẩm vào banner thành công");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, "Không thể thêm sản phẩm: " + e.getMessage(), 500);
        }
    }

    /**
     * Xử lý xóa sản phẩm khỏi banner
     */
    private void handleRemoveProduct(JsonObject body, HttpServletResponse resp) throws IOException {
        // Validate input
        if (!body.has("bannerId") || !body.has("productId")) {
            sendError(resp, "Thiếu bannerId hoặc productId", 400);
            return;
        }

        final Long bannerId, productId;

        try {
            bannerId = body.get("bannerId").getAsLong();
            productId = body.get("productId").getAsLong();
        } catch (Exception e) {
            sendError(resp, "bannerId và productId phải là số nguyên", 400);
            return;
        }

        // Validate banner exists
        Banner banner = bannerService.findById(bannerId);
        if (banner == null) {
            sendError(resp, "Banner không tồn tại", 404);
            return;
        }

        // Check if product in banner
        if (banner.getProducts() == null ||
                banner.getProducts().stream().noneMatch(p -> p.getProductID().equals(productId))) {
            sendError(resp, "Sản phẩm không có trong banner", 400);
            return;
        }

        // Remove product from banner
        try {
            bannerService.removeProductFromBanner(bannerId, productId);
            sendSuccess(resp, "Xóa sản phẩm khỏi banner thành công");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, "Không thể xóa sản phẩm: " + e.getMessage(), 500);
        }
    }

    /**
     * Đọc JSON body từ request
     */
    private JsonObject getRequestBody(HttpServletRequest req) throws IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            if (sb.length() == 0) {
                return null;
            }

            return gson.fromJson(sb.toString(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Trả về lỗi JSON
     */
    private void sendError(HttpServletResponse resp, String message, int code) throws IOException {
        resp.setStatus(code);
        JsonObject json = new JsonObject();
        json.addProperty("success", false);
        json.addProperty("error", message);
        resp.getWriter().print(gson.toJson(json));
    }

    /**
     * Trả về thành công JSON
     */
    private void sendSuccess(HttpServletResponse resp, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", true);
        json.addProperty("message", message);
        resp.getWriter().print(gson.toJson(json));
    }
}