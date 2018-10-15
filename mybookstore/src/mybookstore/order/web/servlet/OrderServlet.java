package mybookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.generic.NEW;

import mybookstore.cart.domain.Cart;
import mybookstore.cart.domain.CartItem;
import mybookstore.order.domain.Order;
import mybookstore.order.domain.OrderItem;
import mybookstore.order.service.OrderException;
import mybookstore.order.service.OrderService;
import mybookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();

	public String pay(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);

		/*
		 * 准备13个参数
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = req.getParameter("oid");
		String p3_Amt = "0.01";// req.getParamater("total")测试状态米有钱财，就一分吧
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = req.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";

		/*
		 * 计算hmac
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		/*
		 * 连接支付网关
		 */
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);
		/*
		 * 重定向到易宝
		 */
		resp.sendRedirect(url.toString());
		return null;

	}

	/**
	 * 必须判断调用本方法的是不是易宝
	 * 
	 * */
	public String back(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 修改订单的状态
		 */
		/*
		 * 获取11+1个参数
		 */
		String p1_MerId = req.getParameter("p1_MerId");
		String r0_Cmd = req.getParameter("r0_Cmd");
		String r1_Code = req.getParameter("r1_Code");
		String r2_TrxId = req.getParameter("r2_TrxId");
		String r3_Amt = req.getParameter("r3_Amt");
		String r4_Cur = req.getParameter("r4_Cur");
		String r5_Pid = req.getParameter("r5_Pid");
		String r6_Order = req.getParameter("r6_Order");
		String r7_Uid = req.getParameter("r7_Uid");
		String r8_MP = req.getParameter("r8_MP");
		String r9_BType = req.getParameter("r9_BType");
		String hmac = req.getParameter("hmac");
		/*
		 * 校验访问者是否为易宝
		 */
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);
		String keyValue = props.getProperty("keyValue");
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if(!bool){
			/*
			 * 校验失败
			 * */
			req.setAttribute("msg", "你需要一个b number");
			return "f:/jsps/msg.jsp";
		}
		/*
		 * 校验成功
		 * 获取订单状态，确定是否要修改订单状态以及添加积分等业务操作
		 * */
		orderService.pay(r6_Order);//有可能对数据库进行操作有可能不操作
		/*
		 * 判断当前回调方式
		 * 如果为点对点需要回馈以sucess开头的字符窜
		 * */
		if(r9_BType.equals("2")){
			resp.getWriter().print("sucessclearlove43967");
		}
		/*
		 * 保存成功信息转发到msg.jsp
		 * */
		req.setAttribute("msg", "恭喜支付成功,等待卖家发货");
		return "f:/jsps/msg.jsp";

	}

	/**
	 * 确认收货
	 * */
	public String confirm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		try {
			orderService.confirm(oid);
			req.setAttribute("msg", "恭喜交易成功");
		} catch (OrderException e) {
			req.setAttribute("msg", e.getMessage());

		}
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 加载订单条目
	 * */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("order", orderService.load(req.getParameter("oid")));
		return "f:/jsps/order/desc.jsp";
	}

	/**
	 * 我的订单功能
	 * 
	 * */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.myOders(user.getUid());
		req.setAttribute("orderList", orderList);

		return "f:/jsps/order/list.jsp";

	}

	/**
	 * 添加订单 把session中的车拿过来生成order对象
	 * */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cart cart = (Cart) req.getSession().getAttribute("cart");
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(1);
		User user = (User) req.getSession().getAttribute("session_user");
		order.setOwner(user);
		order.setTotal(cart.getTotal());
		/*
		 * 创建订单条目
		 */
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem oi = new OrderItem();
			oi.setIid(CommonUtils.uuid());
			oi.setCount(cartItem.getCount());
			oi.setBook(cartItem.getBook());
			oi.setSubtotal(cartItem.getSubtotal());
			oi.setOrder(order);
			orderItemList.add(oi);
		}
		order.setOrderItemList(orderItemList);
		cart.clear();
		orderService.add(order);
		req.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
}
