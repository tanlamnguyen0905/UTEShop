package ute.controllers.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.configs.GsonConfig;
import ute.service.impl.DashboardServiceImpl;
import ute.service.inter.DashboardService;

@WebServlet({ "/api/admin/dashboard/*" })
public class DashboardApiController extends HttpServlet {
	private final Gson gson = GsonConfig.getGson();
	private DashboardService dashboardService;

	@Override
	public void init() throws ServletException {
		dashboardService = new DashboardServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		String pathInfo = req.getPathInfo();

		if (pathInfo == null) {
			sendError(resp, "Invalid endpoint");
			return;
		}

		switch (pathInfo) {
			case "/stats":
				handleStats(resp);
				break;
			case "/revenue-daily":
				handleRevenueDaily(req, resp);
				break;
			case "/order-status":
				handleOrderStatus(resp);
				break;
			case "/top-products":
				handleTopProducts(req, resp);
				break;
			case "/top-customers":
				handleTopCustomers(req, resp);
				break;
			case "/category-sales":
				handleCategorySales(resp);
				break;
			default:
				sendError(resp, "Invalid endpoint");
				break;
		}
	}

	// ---------------------- Endpoint Handlers ------------------------

	private void handleStats(HttpServletResponse resp) throws IOException {
		Map<String, Object> stats = dashboardService.getOverallStats();
		resp.getWriter().write(gson.toJson(stats));
	}

	private void handleRevenueDaily(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Get date range from request params, default to last 14 days
		String startDateStr = req.getParameter("startDate");
		String endDateStr = req.getParameter("endDate");
		
		LocalDate endDate = (endDateStr != null) 
			? LocalDate.parse(endDateStr) 
			: LocalDate.now();
		
		LocalDate startDate = (startDateStr != null) 
			? LocalDate.parse(startDateStr) 
			: endDate.minusDays(13); // Last 14 days
		
		List<Map<String, Object>> dailyRevenue = dashboardService.getRevenueDailyByDateRange(startDate, endDate);
		resp.getWriter().write(gson.toJson(dailyRevenue));
	}

	private void handleOrderStatus(HttpServletResponse resp) throws IOException {
		List<Map<String, Object>> statuses = dashboardService.getOrderStatusDistribution();
		resp.getWriter().write(gson.toJson(statuses));
	}

	private void handleTopProducts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Get limit from request param, default to 10
		String limitStr = req.getParameter("limit");
		int limit = (limitStr != null) ? Integer.parseInt(limitStr) : 10;
		
		List<Map<String, Object>> topProducts = dashboardService.getTopProducts(limit);
		resp.getWriter().write(gson.toJson(topProducts));
	}

	private void handleTopCustomers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Get limit from request param, default to 10
		String limitStr = req.getParameter("limit");
		int limit = (limitStr != null) ? Integer.parseInt(limitStr) : 10;
		
		List<Map<String, Object>> topCustomers = dashboardService.getTopCustomers(limit);
		resp.getWriter().write(gson.toJson(topCustomers));
	}

	private void handleCategorySales(HttpServletResponse resp) throws IOException {
		List<Map<String, Object>> categorySales = dashboardService.getCategorySales();
		resp.getWriter().write(gson.toJson(categorySales));
	}

	private void sendError(HttpServletResponse resp, String message) throws IOException {
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		resp.getWriter().write("{\"error\":\"" + message + "\"}");
	}
}
