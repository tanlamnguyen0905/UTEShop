package ute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.service.impl.ProductServiceImpl;
import ute.dto.ProductDTO;
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
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductServiceImpl productService = new ProductServiceImpl();
		ProductFilter filter = buildFilterFromRequest(request);

		// Pagination
		int currentPage = parseIntParam(request, "currentPage", 1);
		int pageSize = 20;

		// Get products
		List<Product> products = productService.findProductsByFilter(filter, currentPage, pageSize);
		List<ProductDTO> productDTOs = productService.MapToProductDTO(products);
		int total = productService.countProductsByFilter(filter);
		int totalPages = (int) Math.ceil((double) total / pageSize);

		// Set attributes
		request.setAttribute("listPro", productDTOs);
		request.setAttribute("total", total);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("productFilter", filter);

		// Load data for sidebar
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
		doGet(request, response);
	}

	// Utility methods
	private ProductFilter buildFilterFromRequest(HttpServletRequest request) {
		ProductFilter filter = new ProductFilter();
		filter.setCategoryId(parseLongParam(request, "category", null));
		filter.setBannerId(parseLongParam(request, "banner", null));
		filter.setBrandId(parseLongParam(request, "brand", null));
		filter.setMinPrice(parseDoubleParam(request, "minPrice", null));
		filter.setMaxPrice(parseDoubleParam(request, "maxPrice", null));

		String keyword = request.getParameter("keyword");
		if (keyword != null && !keyword.trim().isEmpty()) {
			filter.setKeyword(keyword.trim());
		}

		String sortBy = request.getParameter("sortBy");
		filter.setSortBy(sortBy != null && sortBy.matches("[0-5]") ? sortBy : "1");

		return filter;
	}

	private Long parseLongParam(HttpServletRequest request, String name, Long defaultValue) {
		String value = request.getParameter(name);
		if (value == null || value.isEmpty())
			return defaultValue;
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	private int parseIntParam(HttpServletRequest request, String name, int defaultValue) {
		String value = request.getParameter(name);
		if (value == null || value.isEmpty())
			return defaultValue;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	private Double parseDoubleParam(HttpServletRequest request, String name, Double defaultValue) {
		String value = request.getParameter(name);
		if (value == null || value.isEmpty())
			return defaultValue;
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
