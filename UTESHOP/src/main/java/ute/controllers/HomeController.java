package ute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.dao.impl.BannerDaoImpl;
import ute.dao.impl.CategoryDaoImpl;
import ute.dao.impl.ProductDaoImpl;
import ute.entities.Banner;
import ute.entities.Categories;
import ute.entities.Product;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/home" })
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		loadCategories(request, response);
		loadProducts(request, response);
		loadProductBestSeller(request, response);
		loadBanner(request, response);
		request.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void loadCategories(HttpServletRequest request, HttpServletResponse response) {
		List<Categories> listCate = new CategoryDaoImpl().findAll();
		request.setAttribute("listCate", listCate);

	}

	public void loadProducts(HttpServletRequest request, HttpServletResponse response) {
		List<Product> listProducts = new ProductDaoImpl().findAll();
		request.setAttribute("listProducts", listProducts);

	}

	public void loadProductBestSeller(HttpServletRequest request, HttpServletResponse response) {
		List<Product> listProducts = new ProductDaoImpl().findBestSeller(50);
		request.setAttribute("listBestSell", listProducts);

	}

	public void loadBanner(HttpServletRequest request, HttpServletResponse response) {
		List<Banner> listBanner = new BannerDaoImpl().findAll();
		request.setAttribute("listBanner", listBanner);
	}

}
