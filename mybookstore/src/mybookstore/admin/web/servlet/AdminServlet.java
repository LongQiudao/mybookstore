package mybookstore.admin.web.servlet;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybookstore.admin.domain.Admin;
import mybookstore.admin.service.AdminException;
import mybookstore.admin.service.AdminService;
import mybookstore.cart.domain.Cart;
import mybookstore.user.domain.User;
import mybookstore.user.service.UserException;



import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {
	private AdminService adminService = new AdminService();
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, AdminException {
		Admin form = CommonUtils.toBean(req.getParameterMap(),Admin.class);
		
		Map<String, String> errors = new HashMap<String, String>();
		String adminname = form.getAdminname();
		if (adminname == null || adminname.trim().isEmpty()) {
			errors.put("adminname", "管理员用户名不能为空");
		} else if (adminname.length() < 3 || adminname.length() > 10) {
			errors.put("adminname", "管理员用户名长度必须在3到10之间");
		}
		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码长度必须在3到10之间");
		}
		if (errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("form", form);
			return "f:/adminjsps/login.jsp";
		}
		try {
			Admin admin = adminService.login(form);
			req.getSession().setAttribute("session_admin",admin);
		
			
			return "r:/adminjsps/admin/index.jsp";
		} catch (AdminException e) {
			req.setAttribute("msg",e.getMessage());
			req.setAttribute("form", form);
			return "f:/adminjsps/login.jsp";
		}
	}
	}
