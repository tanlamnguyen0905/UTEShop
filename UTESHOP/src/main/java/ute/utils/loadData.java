package ute.utils;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.entities.*;
import ute.service.impl.*;

public class loadData {

    // ========================= CATEGORY =========================
    public static void loadCategories(HttpServletRequest request, HttpServletResponse response) {
        List<Categories> listCate = new CategoryServiceImpl().findAll();
        request.setAttribute("listCate", listCate);
    }

    // ========================= PRODUCT =========================
    public static void loadProducts(HttpServletRequest request, HttpServletResponse response) {
        List<Product> listProducts = new ProductServiceImpl().findAll();
        request.setAttribute("listProducts", listProducts);
    }

    // ========================= BEST SELLER =========================
    public static void loadProductBestSeller(HttpServletRequest request, HttpServletResponse response) {
        loadProductBestSeller(request, response, 6); // gọi hàm có limit mặc định 6
    }

    public static void loadProductBestSeller(HttpServletRequest request, HttpServletResponse response, int limit) {
        List<Product> listProducts = new ProductServiceImpl().findBestSeller(limit);
        request.setAttribute("listBestSell", listProducts);
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
        List<Product> listNewProducts = new ProductServiceImpl().findNewProduct(limit);
        request.setAttribute("listNewProducts", listNewProducts);
    }

    // ========================= TOP REVIEW =========================
    public static void loadTopReviewProduct(HttpServletRequest request, HttpServletResponse response) {
        loadTopReviewProduct(request, response, 6);
    }

    public static void loadTopReviewProduct(HttpServletRequest request, HttpServletResponse response, int limit) {
        List<Product> listTopReviewProducts = new ProductServiceImpl().findTopReview(limit);
        request.setAttribute("listTopReviewProducts", listTopReviewProducts);
    }

    // ========================= TOP FAVORITE =========================
    public static void loadTopFavoriteProduct(HttpServletRequest request, HttpServletResponse response) {
        loadTopFavoriteProduct(request, response, 6);
    }

    public static void loadTopFavoriteProduct(HttpServletRequest request, HttpServletResponse response, int limit) {
        List<Product> listTopFavoriteProducts = new ProductServiceImpl().findTopFavorite(limit);
        request.setAttribute("listTopFavoriteProducts", listTopFavoriteProducts);
    }
}
