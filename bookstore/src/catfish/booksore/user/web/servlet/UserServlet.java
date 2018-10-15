package catfish.booksore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.Request;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

import catfish.booksore.user.domain.User;
import catfish.booksore.user.service.UserException;
import catfish.booksore.user.service.UserService;

public class UserServlet extends HttpServlet {
	private UserService userservice = new UserService();

	/*
	 * 激活功能
	 */
	public String active(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("你激活啦");
		return null;
	}

	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User form = CommonUtils.toBean(req.getParameterMap(), User.class);
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		Map<String, String> errors = new HashMap<String, String>();
		String username = form.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名长度必须在3-10之间");
		}
		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码长度必须在3-10之间");
		}
		String email = form.getEmial();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "email不能为空");
		} else if (email.matches("\\w+@\\w\\.\\w")) {
			errors.put("email", "email格式错误");
		}
		// 判断是否存在错误信息
		if (errors.size() > 0) {
			/*
			 * 1。保存错误信息 2.保存表单数据 3.转发到regist.jsp
			 */
			req.setAttribute("errors", errors);
			req.setAttribute("form", form);
			return "f:/front/register.jsp";
		}
		try {
			userservice.regist(form);

		} catch (UserException e) {
			/*
			 * 1 保存异常信息 2 保存form 3 转发到register.jsp
			 */
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("form", form);
			return "f:/front/user/register.jsp";
		}
		/*
		 * 发邮件 准备配置文件
		 */
		// 获取配置文件内容
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");
		String usname = props.getProperty("usname");
		String pwd = props.getProperty("pwd");
		String from = props.getProperty("from");
		String to = form.getEmial();
		String subject = props.getProperty("subject");
		String content = props.getProperty("content");
		content = MessageFormat.format(content, form.getCode());
		Session session = MailUtils.createSession(host, usname, pwd);
		Mail mail = new Mail(from, to, subject, content);
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			
		}
		/*
		 * 
		 * 执行到这里说明userservice执行成功 没有抛出任何异常 1 保存成功信息 2 转发到msg.jsp
		 */
		req.setAttribute("msg", "恭喜大佬b套圣药海博伦超时空毕业,请马上打开邮箱接收");
		return "f:/front/user/msg.jsp";
	}
}
