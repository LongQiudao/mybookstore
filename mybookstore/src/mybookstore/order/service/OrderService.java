package mybookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import mybookstore.order.dao.OrderDao;
import mybookstore.order.domain.Order;

public class OrderService {
	
	private OrderDao orderDao= new OrderDao();
	/**
	 * 支付方法
	 * */
	public void pay(String oid){
		/*
		 * 获取订单状态
		 * 如果状态为1 执行下面的方法  如果不为1 那么什么也不做
		 * */
		int state =orderDao.getStateByOid(oid);
		if(state==1){
			orderDao.updateState(oid, 2);
		}
	}
	/**
	 * 确认收货
	 * 
	 * */
	public void confirm(String oid)throws OrderException{
		int state = orderDao.getStateByOid(oid);
		if(state !=3) throw new OrderException("你这个坏蛋 ，没给钱还想收货");
		orderDao.updateState(oid, 4);
		
	}
	/**
	 * 添加订单需要处理事务
	 * */
	public void add(Order order){
		try {
			JdbcUtils.beginTransaction();
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				throw new RuntimeException();
			}
		}
	}
	/**
	 * 我的订单
	 * */
	public List<Order> myOders(String uid) {
		
		return orderDao.findByUid(uid);
	}
	
	/*
	*
	*加载订单条目
	*/
	public Order load(String oid) {
		
		return orderDao.load(oid);
	}
	/**
	 * 查询所有订单
	 * */
	public List<Order> findAll() {
		
		return orderDao.findAll();
	}
	/**
	 * 查询未付款订单
	 * */
	public List<Order> findUnpaid() {
		return orderDao.findUnpaid();
	}
	/**
	 * 查询已付款订单
	 * */
	public List<Order> findPaid() {
		return orderDao.findPaid();
	}
	/**
	 * 查询未收货订单
	 * */
	public List<Order> findUnreceive() {
		
		return orderDao.findUnreceive();
	}
	/**
	 * 查询交易成功订单
	 * */
	public List<Order> findSuccess() {
		return orderDao.findSuccess();
	}
}
