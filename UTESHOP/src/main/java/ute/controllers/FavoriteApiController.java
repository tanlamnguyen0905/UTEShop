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
import ute.service.impl.FavoriteServiceImpl;

@WebServlet(urlPatterns = { "/api/favorite/*" })
public class FavoriteApiController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FavoriteServiceImpl FavoriteService = new FavoriteServiceImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
                case "/remove":
                    handleRemoveFromFavorite(req, resp, currentUser);
                    break;
                case "/clear":
                    handleClearFavorite(req, resp, currentUser);
                    break;
                default:
                    sendErrorResponse(resp, "Endpoint không tồn tại", 404);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Có lỗi xảy ra: " + e.getMessage(), 500);
        }
    }

    private void handleRemoveFromFavorite(HttpServletRequest req, HttpServletResponse resp, Users user)
            throws IOException {

        JsonObject requestBody = getRequestBody(req);

        if (requestBody == null || !requestBody.has("productID")) {
            sendErrorResponse(resp, "Thiếu thông tin", 400);
            return;
        }

        try {
            Long productID = requestBody.get("productID").getAsLong();

            FavoriteService.removeFavorite(user.getUserID(), productID);

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Đã xóa sản phẩm khỏi yêu thích");

            resp.getWriter().print(gson.toJson(response));

        } catch (RuntimeException e) {
            sendErrorResponse(resp, e.getMessage(), 400);
        }
    }

    private void handleClearFavorite(HttpServletRequest req, HttpServletResponse resp, Users user)
            throws IOException {

        try {
            FavoriteService.clearFavorite(user.getUserID());

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Đã xóa tất cả sản phẩm trong yêu thích");

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
