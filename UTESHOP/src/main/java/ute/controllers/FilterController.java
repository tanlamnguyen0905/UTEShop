package ute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.service.impl.ProductServiceImpl;
import ute.utils.ProductFilter;
import ute.utils.loadData;
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

		ProductServiceImpl productService = new ProductServiceImpl();
		ProductFilter productFilter = new ProductFilter();

		// Xử lý các tham số filter
		if (request.getParameter("category") != null && !request.getParameter("category").isEmpty()) {
			productFilter.setCategoryId(Long.parseLong(request.getParameter("category")));
		}
		if (request.getParameter("banner") != null && !request.getParameter("banner").isEmpty()) {
			productFilter.setBannerId(Long.parseLong(request.getParameter("banner")));
		}
		if (request.getParameter("brand") != null && !request.getParameter("brand").isEmpty()) {
			productFilter.setBrandId(Long.parseLong(request.getParameter("brand")));
		}
		if (request.getParameter("minPrice") != null && !request.getParameter("minPrice").isEmpty()) {
			productFilter.setMinPrice(Double.parseDouble(request.getParameter("minPrice")));
		}
		if (request.getParameter("maxPrice") != null && !request.getParameter("maxPrice").isEmpty()) {
			productFilter.setMaxPrice(Double.parseDouble(request.getParameter("maxPrice")));
		}
		if (request.getParameter("keyword") != null && !request.getParameter("keyword").isEmpty()) {
			productFilter.setKeyword(request.getParameter("keyword"));
		}
		if (request.getParameter("sortBy") != null && !request.getParameter("sortBy").isEmpty()) {
			productFilter.setSortBy(request.getParameter("sortBy"));
		} else if (request.getParameter("pageName") != null && !request.getParameter("pageName").isEmpty()) {
			// Hỗ trợ tham số cũ từ home page
			productFilter.setSortBy(request.getParameter("pageName"));
		}

		// Xử lý phân trang
		String currentPageParam = request.getParameter("currentPage");
		if (currentPageParam == null || currentPageParam.isEmpty())
			currentPageParam = "1";

		int currentPage = Integer.parseInt(currentPageParam);
		int pageSize = 20;

		// Lấy danh sách sản phẩm và tổng số
		List<Product> listPro = productService.findProductsByFilter(productFilter, currentPage, pageSize);
		int total = productService.countProductsByFilter(productFilter);

		// Tính toán phân trang
		int totalPages = (int) Math.ceil((double) total / pageSize);

		// Set attributes cho JSP
		request.setAttribute("listPro", listPro);
		request.setAttribute("total", total);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("productFilter", productFilter);

		// Load thêm dữ liệu cho filter sidebar
		loadData.loadCategories(request, response);
		loadData.loadBanner(request, response);

		request.getRequestDispatcher("/WEB-INF/views/web/filter.jsp").forward(request, response);
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
