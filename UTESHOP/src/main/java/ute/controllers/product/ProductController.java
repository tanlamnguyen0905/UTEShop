package ute.controllers.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import ute.entities.Product;
import ute.entities.Brand;
import ute.entities.Categories;

@WebServlet({"/product/detail"})
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // (Giả lập dữ liệu sản phẩm — sau này thay bằng ProductService.findById(id))
        Product product = new Product();
        product.setProductID(1L);
        product.setProductName("Quần thể thao nam 7'' Ultra Short");
        product.setImage("/assets/images/product_main.jpg");
        product.setUnitPrice(350000.0);
        product.setStockQuantity(25);
        product.setDescribe("Chiếc quần thể thao được thiết kế để mang lại sự thoải mái, co giãn 4 chiều, khô nhanh và phù hợp cho cả chạy bộ lẫn tập gym.");
        product.setStockQuantity(512);

        Brand brand = new Brand();
        brand.setBrandName("Coolmate");
        product.setBrand(brand);

        Categories category = new Categories();
        category.setCategoryName("Thời trang thể thao");
        product.setCategory(category);

        // Gửi dữ liệu sang JSP
        req.setAttribute("product", product);

        req.getRequestDispatcher("/WEB-INF/views/product/productDetail.jsp").forward(req, resp);
    }
}
