package mybookstore.user.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import mybookstore.user.domain.User;


public class Loginfilter implements Filter {

  
    public Loginfilter() {
       
    }

	
	public void destroy() {
	
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		User user=(User)req.getSession().getAttribute("session_user");
		if(user != null){
			chain.doFilter(request, response);
		}else{
			req.setAttribute("msg", "游客没有访问权限，请登录后重试！");
			req.getRequestDispatcher("jsps/user/login.jsp").forward(req, response);
		}
	
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
