package ute.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.service.inter.ProductService;
import ute.service.inter.CategoriesService;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.CategoriesServiceImpl;
import ute.entities.Product;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminHomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProductService productService = new ProductServiceImpl();
    private CategoriesService categoriesService = new CategoriesServiceImpl();

    public AdminHomeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch statistics
            long totalProducts = productService.count();
            long totalCategories = categoriesService.count();
            
            // Get recent products
            List<Product> recentProducts = productService.findNewProduct(5);
            
            // Get best sellers
            List<Product> bestSellers = productService.findBestSeller(5);
            
            // Set attributes
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("totalCategories", totalCategories);
            request.setAttribute("totalOrders", 45); // TODO: Implement order service
            request.setAttribute("totalUsers", 23); // TODO: Implement user count
            request.setAttribute("totalRevenue", 125000000); // TODO: Implement revenue calculation
            request.setAttribute("recentProducts", recentProducts);
            request.setAttribute("bestSellers", bestSellers);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Set default values if services fail
            request.setAttribute("totalProducts", 0);
            request.setAttribute("totalCategories", 0);
            request.setAttribute("totalOrders", 0);
            request.setAttribute("totalUsers", 0);
            request.setAttribute("totalRevenue", 0);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}