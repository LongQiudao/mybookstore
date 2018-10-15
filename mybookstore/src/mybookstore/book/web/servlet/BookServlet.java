package mybookstore.book.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybookstore.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	/**
	 * 加载图书
	 * */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		req.setAttribute("book", bookService.load(bid));
		return "f:/jsps/book/desc.jsp";
	}
	/**
	 * 查询所有图书
	 * 
	 * */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		req.setAttribute("bookList", bookService.findAll());
		return "f:/jsps/book/list.jsp";
	}
	/**
	 * 按分类查询
	 * 
	 * */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		req.setAttribute("bookList", bookService.findByCategory(cid));
		return "f:/jsps/book/list.jsp";
	}
}
