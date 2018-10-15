package mybookstore.order.domain;

import java.util.Date;
import java.util.List;

import mybookstore.user.domain.User;

public class Order {
	private String oid;
	private Date ordertime;
	private double total;
	private int state;//4钟状态 1：未付款 2：已付款单未发货 3：已发货单未确认收货 4：交易完成
	private String address;//收货地址
	private User owner;//下单人
	private List<OrderItem> orderItemList;
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String toString() {
		return "OrderItem [oid=" + oid + ", ordertime=" + ordertime
				+ ", total=" + total + ", state=" + state + ", address="
				+ address + ", owner=" + owner + "]";
	}
}
