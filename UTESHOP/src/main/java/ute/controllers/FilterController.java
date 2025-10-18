package ute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.BannerServiceImpl;
import ute.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class FilterController
 */
@WebServlet(urlPatterns = { "/filter" })
public class FilterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FilterController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String category = request.getParameter("category");
		if (category != null) {
			List<Product> listCate = new ProductServiceImpl().findByCategoryId(Integer.parseInt(category));
			System.out.println(listCate.size());
			request.setAttribute("listPro", listCate);
			request.getRequestDispatcher("/WEB-INF/views/web/filter.jsp").forward(request, response);
		} else {
			response.sendRedirect("home");
		}
		String banner = request.getParameter("banner");
		if (banner != null) {
			List<Product> listBanner = new BannerServiceImpl().findByBannerId(Integer.parseInt(banner));
			System.out.println(listBanner.size());
			request.setAttribute("listPro", listBanner);
			request.getRequestDispatcher("/WEB-INF/views/web/filter.jsp").forward(request, response);
		} else {
			response.sendRedirect("home");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
