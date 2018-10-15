package mybookstore.book.web.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybookstore.book.domain.Book;
import mybookstore.book.service.BookService;
import mybookstore.category.domain.Category;
import mybookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	/**
	 * 删除图书
	 * */
	public  String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		bookService.delete(bid);
		return findAll(req,resp);
	}
	/**
	 * 添加图书
	 * */
	public  String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setAttribute("categoryList",categoryService.findAll());
		
		return "f:/adminjsps/admin/book/add.jsp";
	}
	/**
	 * 加载方法
	 * */
	public  String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 获取参数bid
		 * 调用service方法得到book对象
		 * 获取所有分类保存到requset域中
		 * 保存book到requset域中，转发到desc.jsp
		 * 
		 * */
		String bid =req.getParameter("bid");
		req.setAttribute("book", bookService.load(bid));
		req.setAttribute("categoryList",categoryService.findAll());
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	/**
	 * 查看所有图书
	 * */
	public  String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("bookList", bookService.findAll());
		return "f:/adminjsps/admin/book/list.jsp";
	}
	public  String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		book.setCategory(category);
		bookService.edit(book);
		return findAll(req, resp);
	}
}
