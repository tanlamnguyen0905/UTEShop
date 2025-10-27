package ute.controllers.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.configs.GsonConfig;
import ute.dao.impl.UserDaoImpl;
import ute.dto.UserDTO;
import ute.entities.Users;
import ute.service.impl.UserServiceImpl;
import ute.service.inter.UserService;

@WebServlet({ "/api/admin/accounts/*" })
public class UserAccountApiController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private Gson gson = GsonConfig.getGson();

	@Override
	public void init() throws ServletException {
		userService = new UserServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo(); // e.g. /list
		resp.setContentType("application/json;charset=UTF-8");

		if (pathInfo == null || !pathInfo.equals("/list")) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
			return;
		}

		String search = req.getParameter("search");
		String role = req.getParameter("role");
		String status = req.getParameter("status");

		List<Users> users = userService.search(search, role, status);
		List<UserDTO> userDTOs = users.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
		resp.getWriter().write(gson.toJson(userDTOs));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		resp.setContentType("application/json;charset=UTF-8");

		if (pathInfo == null || !pathInfo.equals("/create")) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
			return;
		}

		BufferedReader reader = req.getReader();
		Users user = gson.fromJson(reader, Users.class);
		boolean success = userService.create(user);

		resp.getWriter().write("{\"success\":" + success + "}");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo(); // e.g. /update/5
		resp.setContentType("application/json;charset=UTF-8");

		if (pathInfo == null || !pathInfo.startsWith("/update/")) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
			return;
		}

		try {
			Long id = Long.parseLong(pathInfo.substring("/update/".length()));
			BufferedReader reader = req.getReader();
			Users user = gson.fromJson(reader, Users.class);
			user.setUserID(id);

			boolean success = userService.update(user);
			resp.getWriter().write("{\"success\":" + success + "}");
		} catch (NumberFormatException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("{\"error\":\"Invalid user ID\"}");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		resp.setContentType("application/json;charset=UTF-8");

		if (pathInfo == null || !pathInfo.startsWith("/delete/")) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
			return;
		}

		try {
			Long id = Long.parseLong(pathInfo.substring("/delete/".length()));
			boolean success = userService.delete(id);
			resp.getWriter().write("{\"success\":" + success + "}");
		} catch (NumberFormatException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("{\"error\":\"Invalid user ID\"}");
		}
	}
}
