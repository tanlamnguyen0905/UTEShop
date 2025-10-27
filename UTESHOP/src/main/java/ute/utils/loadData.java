package ute.utils;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.entities.*;
import ute.dto.ProductDTO;
import ute.service.impl.*;

public class loadData {

    // ========================= CATEGORY =========================
    public static void loadCategories(HttpServletRequest request, HttpServletResponse response) {
        List<Categories> listCate = new CategoriesServiceImpl().findAll();
        request.setAttribute("listCate", listCate);
    }

    // ========================= PRODUCT =========================
    public static void loadProducts(HttpServletRequest request, HttpServletResponse response) {
        ProductServiceImpl service = new ProductServiceImpl();
        List<Product> listProducts = service.findAll();
        List<ProductDTO> listProductDTOs = service.MapToProductDTO(listProducts);
        request.setAttribute("listProducts", listProductDTOs);
    }

    // ========================= BEST SELLER =========================
    public static void loadProductBestSeller(HttpServletRequest request, HttpServletResponse response) {
        loadProductBestSeller(request, response, 6); // gọi hàm có limit mặc định 6
    }

    public static void loadProductBestSeller(HttpServletRequest request, HttpServletResponse response, int limit) {
        ProductServiceImpl service = new ProductServiceImpl();
        List<Product> listProducts = service.findBestSeller(limit);
        List<ProductDTO> listDTO = service.MapToProductDTO(listProducts);
        request.setAttribute("listBestSell", listDTO);
    }

    // ========================= BANNER =========================
    public static void loadBanner(HttpServletRequest request, HttpServletResponse response) {
        List<Banner> listBanner = new BannerServiceImpl().findAll();
        request.setAttribute("listBanner", listBanner);
    }

    // ========================= NEW PRODUCTS =========================
    public static void loadNewProduct(HttpServletRequest request, HttpServletResponse response) {
        loadNewProduct(request, response, 6);
    }

    public static void loadNewProduct(HttpServletRequest request, HttpServletResponse response, int limit) {
        ProductServiceImpl service = new ProductServiceImpl();
        List<Product> listNewProducts = service.findNewProduct(limit);
        List<ProductDTO> listDTO = service.MapToProductDTO(listNewProducts);
        request.setAttribute("listNewProducts", listDTO);
    }

    // ========================= TOP REVIEW =========================
    public static void loadTopReviewProduct(HttpServletRequest request, HttpServletResponse response) {
        loadTopReviewProduct(request, response, 6);
    }

    public static void loadTopReviewProduct(HttpServletRequest request, HttpServletResponse response, int limit) {
        ProductServiceImpl service = new ProductServiceImpl();
        List<Product> listTopReviewProducts = service.findTopReview(limit);
        List<ProductDTO> listDTO = service.MapToProductDTO(listTopReviewProducts);
        request.setAttribute("listTopReviewProducts", listDTO);
    }

    // ========================= TOP FAVORITE =========================
    public static void loadTopFavoriteProduct(HttpServletRequest request, HttpServletResponse response) {
        loadTopFavoriteProduct(request, response, 6);
    }

    public static void loadTopFavoriteProduct(HttpServletRequest request, HttpServletResponse response, int limit) {
        ProductServiceImpl service = new ProductServiceImpl();
        List<Product> listTopFavoriteProducts = service.findTopFavorite(limit);
        List<ProductDTO> listDTO = service.MapToProductDTO(listTopFavoriteProducts);
        request.setAttribute("listTopFavoriteProducts", listDTO);
    }
}
