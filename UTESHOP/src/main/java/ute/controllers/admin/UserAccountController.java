package ute.controllers.admin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/admin/accounts" })
public class UserAccountController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> roles = Arrays.asList("ADMIN", "EMPLOYEE", "USER");
        List<String> statuses = Arrays.asList("ACTIVE", "INACTIVE", "LOCKED");

        req.setAttribute("roles", roles);
        req.setAttribute("statuses", statuses);

        req.getRequestDispatcher("/WEB-INF/views/admin/accounts.jsp").forward(req, resp);
	}
}
