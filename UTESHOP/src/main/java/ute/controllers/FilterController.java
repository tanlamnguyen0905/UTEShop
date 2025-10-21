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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {

	// ProductServiceImpl productService = new ProductServiceImpl();
	// ProductFilter productFilter = new ProductFilter();

	// // Xử lý các tham số filter (safely)
	// try {
	// String cat = request.getParameter("category");
	// if (cat != null && !cat.isEmpty())
	// productFilter.setCategoryId(Long.parseLong(cat));
	// } catch (NumberFormatException e) {
	// // ignore invalid param
	// }
	// try {
	// String ban = request.getParameter("banner");
	// if (ban != null && !ban.isEmpty())
	// productFilter.setBannerId(Long.parseLong(ban));
	// } catch (NumberFormatException e) {
	// }
	// try {
	// String br = request.getParameter("brand");
	// if (br != null && !br.isEmpty())
	// productFilter.setBrandId(Long.parseLong(br));
	// } catch (NumberFormatException e) {
	// }
	// try {
	// String minP = request.getParameter("minPrice");
	// if (minP != null && !minP.isEmpty())
	// productFilter.setMinPrice(Double.parseDouble(minP));
	// } catch (NumberFormatException e) {
	// }
	// try {
	// String maxP = request.getParameter("maxPrice");
	// if (maxP != null && !maxP.isEmpty())
	// productFilter.setMaxPrice(Double.parseDouble(maxP));
	// } catch (NumberFormatException e) {
	// }
	// String kw = request.getParameter("keyword");
	// if (kw != null && !kw.isEmpty())
	// productFilter.setKeyword(kw.trim());

	// // sortBy: prefer explicit sortBy param (0..5). If not present, map known
	// // pageName values
	// String sortBy = request.getParameter("sortBy");
	// if (sortBy != null && !sortBy.isEmpty()) {
	// productFilter.setSortBy(sortBy);
	// } else {
	// String pageName = request.getParameter("pageName");
	// if (pageName != null && !pageName.isEmpty()) {
	// // map legacy page names to sort codes
	// switch (pageName.toLowerCase()) {
	// case "banchay":
	// case "bestseller":
	// case "best":
	// productFilter.setSortBy("0");
	// break;
	// case "new":
	// case "moi":
	// productFilter.setSortBy("1");
	// break;
	// case "topreview":
	// case "review":
	// productFilter.setSortBy("2");
	// break;
	// case "favorite":
	// case "yeuthich":
	// productFilter.setSortBy("3");
	// break;
	// case "priceasc":
	// case "giaasc":
	// productFilter.setSortBy("4");
	// break;
	// case "pricedesc":
	// case "giadesc":
	// productFilter.setSortBy("5");
	// break;
	// default:
	// // if pageName is already a numeric code, accept it
	// if (pageName.matches("[0-5]")) {
	// productFilter.setSortBy(pageName);
	// } else {
	// productFilter.setSortBy("1"); // default newest
	// }
	// }
	// } else {
	// productFilter.setSortBy("1"); // default newest
	// }
	// }

	// // Xử lý phân trang
	// String currentPageParam = request.getParameter("currentPage");
	// if (currentPageParam == null || currentPageParam.isEmpty())
	// currentPageParam = "1";
	// int currentPage = 1;
	// try {
	// currentPage = Integer.parseInt(currentPageParam);
	// if (currentPage < 1)
	// currentPage = 1;
	// } catch (NumberFormatException e) {
	// currentPage = 1;
	// }
	// int pageSize = 20;
	// // reflect paging into filter (optional)
	// productFilter.setCurrentPage(currentPage);
	// productFilter.setPageSize(pageSize);

	// // Lấy danh sách sản phẩm và tổng số
	// List<Product> listPro = productService.findProductsByFilter(productFilter,
	// currentPage, pageSize);
	// List<ProductDTO> listProDTO = productService.MapToProductDTO(listPro);
	// int total = productService.countProductsByFilter(productFilter);

	// // Tính toán phân trang
	// int totalPages = (int) Math.ceil((double) total / pageSize);

	// // Set attributes cho JSP
	// request.setAttribute("listPro", listProDTO);
	// request.setAttribute("total", total);
	// request.setAttribute("currentPage", currentPage);
	// request.setAttribute("totalPages", totalPages);
	// request.setAttribute("productFilter", productFilter);

	// // Load thêm dữ liệu cho filter sidebar
	// loadData.loadCategories(request, response);
	// loadData.loadBanner(request, response);

	// request.getRequestDispatcher("/WEB-INF/views/web/filter.jsp").forward(request,
	// response);
	// }
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
		// TODO Auto-generated method stub
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
