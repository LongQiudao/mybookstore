package mybookstore.order.web.servlet.admin;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybookstore.order.domain.Order;
import mybookstore.order.service.OrderService;

import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	OrderService orderService = new OrderService();
	/**
	 * 查询所有订单
	 * */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("orderList", orderService.findAll());
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/**
	 * 查询未付款订单
	 * */
	public String findUnpaid(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("orderList", orderService.findUnpaid());
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/**
	 * 查询已付款订单
	 * */
	public String findPaid(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("orderList", orderService.findPaid());
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/**
	 * 查询未收货订单
	 * */
	public String findUnreceive(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("orderList", orderService.findUnreceive());
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/**
	 * 查询交易成功订单
	 * */
	public String findSuccess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("orderList", orderService.findSuccess());
		return "f:/adminjsps/admin/order/list.jsp";
	}
}
