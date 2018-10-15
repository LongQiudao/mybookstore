package mybookstore.cart.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybookstore.book.domain.Book;
import mybookstore.book.service.BookService;
import mybookstore.cart.domain.Cart;
import mybookstore.cart.domain.CartItem;

import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet {
	/**
	 * 添加购物条目
	 * */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 得到登录时候发的车
		 * */
		Cart cart =(Cart) req.getSession().getAttribute("cart");
		String bid = req.getParameter("bid");
		Book book =new BookService().load(bid);
		int count = Integer.parseInt(req.getParameter("count"));
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		cart.add(cartItem);
	return "f:/jsps/cart/list.jsp";

	}
	/**
	*清空购物条目
	*/
	public String clear(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cart cart =(Cart) req.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";

	}
	/**
	 * 
	 * 删除购物条目
	 * */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cart cart =(Cart) req.getSession().getAttribute("cart");
		String bid = req.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";

	}
}
