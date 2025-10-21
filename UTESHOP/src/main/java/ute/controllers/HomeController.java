package ute.controllers;
import ute.utils.loadData;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = { "/home" })
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		loadData.loadCategories(request, response);
//		loadProducts(request, response);
		loadData.loadProductBestSeller(request, response);
		loadData.loadBanner(request, response);
		loadData.loadNewProduct(request, response);
		loadData.loadTopReviewProduct(request, response);
		loadData.loadTopFavoriteProduct(request, response);
		request.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}



}
