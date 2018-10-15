package mybookstore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybookstore.cart.domain.Cart;
import mybookstore.user.domain.User;
import mybookstore.user.service.UserException;
import mybookstore.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userservice = new UserService();
	/**
	 * 退出功能
	 * 
	 * **/
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
			req.getSession().invalidate();
			return "r:/index.jsp";
	}
	
	/**
	 * 登录功能
	 * **/
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User form = CommonUtils.toBean(req.getParameterMap(), User.class);
		
		Map<String, String> errors = new HashMap<String, String>();
		String username = form.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名长度必须在3到10之间");
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
			return "f:/jsps/user/login.jsp";
		}
		try {
			User user = userservice.login(form);
			req.getSession().setAttribute("session_user",user);
			/*
			 *给用户发一辆车，鸡向session中保存一个cart对象 
			 * */
			req.getSession().setAttribute("cart",new Cart());
			return "r:/index.jsp";
		} catch (UserException e) {
			req.setAttribute("msg",e.getMessage());
			req.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
	}
	/**
	 * 注册功能
	 * **/
	// 激活功能
	public String active(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String code = req.getParameter("code");
		try {
			userservice.active(code);
			req.setAttribute("msg", "恭喜激活成功,请马上登录");
		} catch (UserException e) {
			
			req.setAttribute("msg", e.getMessage());
		}
		return "f://jsps/msg.jsp";
	}
	

	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 封装表单数据
		User form = CommonUtils.toBean(req.getParameterMap(), User.class);
		// 补全
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		// 输入校验
		Map<String, String> errors = new HashMap<String, String>();
		// 获取form中的username password email进行校验
		String username = form.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名长度必须在3到10之间");
		}
		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码长度必须在3到10之间");
		}
		String email = form.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "email不能为空");
		} else if (!email.matches("\\w+@\\w+\\.\\w+")) {
			errors.put("email", "email格式错误");
		}
		// 判断是否纯在错误信息
		if (errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		// 调用service的regist（）方法
		try {
			userservice.regist(form);
			// 执行成功 保存成功信息转发到msg.jsp

		} catch (UserException e) {
			// 失败保存错误信息 并转发到regist.jsp
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("from", form);
			return "f:/jsps/user/regist.jsp";
		}
		// 发邮件
		// 获取配置文件内容
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");
		String uname= props.getProperty("uname");
		String pwd=props.getProperty("pwd");
		String from = props.getProperty("from");
		String to = form.getEmail();
		String subject =props.getProperty("subject");
		String content = props.getProperty("content");
		content = MessageFormat.format(content, form.getCode());//替换{0}
		Session session = MailUtils.createSession(host, uname, pwd);
		Mail mail = new Mail(from,to,subject,content);
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			
		}
		req.setAttribute("msg", "恭喜注册成功,请马上到邮箱激活");
		return "f:jsps/msg.jsp";
	}
}
