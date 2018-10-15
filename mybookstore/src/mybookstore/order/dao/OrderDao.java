package mybookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mybookstore.book.domain.Book;
import mybookstore.order.domain.Order;
import mybookstore.order.domain.OrderItem;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.sun.jmx.snmp.daemon.CommunicationException;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 通过oid查看订单状态
	 * */
	public int getStateByOid(String oid) {
		try {
			String sql = "select state from orders where oid =?";
			Number num = (Number) qr.query(sql, new ScalarHandler(), oid);
			return num.intValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 修改订单状态
	 * */
	public void updateState(String oid, int state) {
		try {
			String sql = "update orders set state =? where oid =?";
			qr.update(sql, state, oid);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 添加订单
	 * */
	public void addOrder(Order order) {
		try {
			String sql = "insert into orders values(?,?,?,?,?,?)";
			/**
			 * 处理util的Date转化成sql中的Timestamp
			 * */
			Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
			Object[] params = { order.getOid(), timestamp, order.getTotal(),
					order.getState(), order.getOwner().getUid(),
					order.getAddress() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 插入订单条目
	 * */
	public void addOrderItemList(List<OrderItem> orderItemList) {
		try {
			String sql = "insert into orderitem values (?,?,?,?,?)";
			/*
			 * 吧orderitemlist转化为一个二维数组
			 */
			Object[][] params = new Object[orderItemList.size()][];
			for (int i = 0; i < orderItemList.size(); i++) {
				OrderItem item = orderItemList.get(i);
				params[i] = new Object[] { item.getIid(), item.getCount(),
						item.getSubtotal(), item.getOrder().getOid(),
						item.getBook().getBid() };
			}
			qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按uid查询订单 我的订单功能
	 * 
	 * */
	public List<Order> findByUid(String uid) {
		try {
			/*
			 * 得到当前用户的所有订单
			 */
			String sql = "select * from orders where uid=?";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class), uid);
			/*
			 * 循环遍历所有订单，并未自己加载所有订单条目
			 */
			for (Order order : orderList) {
				loadOrderItems(order);// 未order对象加载他的所有订单条目
			}
			return orderList;
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}

	}

	/*
	 * 加载所有指定订单的所有订单条目
	 */
	private void loadOrderItems(Order order) throws SQLException {
		String sql = "select * from orderitem i,book b  where i.bid=b.bid and oid =?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}

	/*
	 * 把maplist中的每个对象转换成两个对象并建立关系
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	/**
	 * 把一个map转换成一个orderItem对象
	 * */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	/**
	 * 加载订单条目
	 * 
	 * */
	public Order load(String oid) {
		try {
			/*
			 * 得到当前用户的所有订单
			 */
			String sql = "select * from orders where oid=?";
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class),
					oid);
			/*
			 * 为order加载所有订单条目
			 */

			loadOrderItems(order);// 未order对象加载他的所有订单条目

			return order;
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}

	}

	/**
	 * 查询所有订单
	 * */
	public List<Order> findAll() {
		try {
			String sql = "select * from orders";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class));
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	/**
	 * 查询未付款订单
	 * */
	public List<Order> findUnpaid() {
		try {
			String sql = "select * from orders where state=1";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class));
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	/**
	 * 查询已付款订单
	 * */
	public List<Order> findPaid() {
		try {
			String sql = "select * from orders where state=2";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class));
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询未收货订单
	 * */
	public List<Order> findUnreceive() {
		try {
			String sql = "select * from orders where state=3";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class));
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询交易成功订单
	 * */
	public List<Order> findSuccess() {
		try {
			String sql = "select * from orders where state=4";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class));
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
