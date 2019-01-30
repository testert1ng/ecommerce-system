package controller;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import model.Cart;
import model.CartItem;
import model.Order;
import model.StatusData;
import model.User;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import dao.DaoFactory;
import dao.OrderDao;

public class OrderAction extends ActionSupport implements SessionAware {
	private StatusData statusData;
	private String address;
	private double totalPrice;
	private int orderId;
	private Collection<CartItem> items;
	private Order order;
	private List<Order> orders;
	
	private Map session;
	

	public void setSession(Map value) {
		// TODO Auto-generated method stub
		this.session = value;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Collection<CartItem> getItems() {
		return items;
	}

	public void setItems(Collection<CartItem> items) {
		this.items = items;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	

	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	
	public StatusData getStatusData() {
		return statusData;
	}

	public void setStatusData(StatusData statusData) {
		this.statusData = statusData;
	}

	public Map getSession() {
		return session;
	}

	public String checkOut() {
		Cart cart = (Cart) session.get(Constants.CART);
		setItems(cart.getItems());
		totalPrice = 0.0;
		for (CartItem ci : cart.getItems()) {
			totalPrice += ci.getQuantity() * ci.getProduct().getPrice();
		}
		return SUCCESS;
	}

	public String submitOrder() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		Cart cart = (Cart) session.get(Constants.CART);
		setItems(cart.getItems());
		totalPrice = 0.0;
		for (CartItem ci : cart.getItems()) {
			totalPrice += ci.getQuantity() * ci.getProduct().getPrice();
		}

		Order order = new Order();
		order.setAddress(address);//
		User usr = (User) session.get("regular");
		order.setCustomer(usr.getUserName());
		order.setTotalPrice(totalPrice);
		order.setCart(cart);

		ClientResponse response = service.path("rest").path("shipping")
				.accept(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, order);
		order = response.getEntity(Order.class);
		
		if(order.getAddress().equals("invalid")){
			return "invalid";
		}
		DaoFactory.getInstance().getOrderDao().addOrder(order);
		this.order = order;
		session.remove(Constants.CART);
		return SUCCESS;
	}
	
	public String discardOrder(){
		session.remove(Constants.CART);
		return SUCCESS;
	}
	
	public String myOrders(){
		OrderDao oDao = DaoFactory.getInstance().getOrderDao();
		User user = (User)session.get("regular");
		String customer = user.getUserName();
		this.setOrders(oDao.getOrdersByCustomer(customer));
		return SUCCESS;
	}
	
	public String itemInfo(){
		OrderDao oDao = DaoFactory.getInstance().getOrderDao();
		Order o = oDao.getOrderById(this.orderId);
		this.order = o;
		return SUCCESS;
	}
	
	public String showAllOrders(){
		OrderDao oDao = DaoFactory.getInstance().getOrderDao();
		this.orders = oDao.getAllOrders();
		return SUCCESS;
	}
	
	public String updateStatus(){
		OrderDao oDao = DaoFactory.getInstance().getOrderDao();
		Order o = oDao.getOrderById(orderId);
		String status = o.getStatus();
		if(status.equals("processing")){
			o.setStatus("shipped");
			oDao.updateOrder(o);
			statusData = new StatusData(orderId,"shipped");
		}else if(status.equals("shipped")){
			o.setStatus("processing");
			oDao.updateOrder(o);
			statusData = new StatusData(orderId,"processing");
		}
		
		
		return SUCCESS;
	}
	
	public String orderDeatil(){
		OrderDao oDao = DaoFactory.getInstance().getOrderDao();
		order = oDao.getOrderById(orderId);
		return SUCCESS;
	}
	
	private static URI getBaseURI() {
		return UriBuilder
				.fromUri("http://127.0.0.1:8090/WebAssignment2Shiping").build();
	}
	
	

	
	
	/*private static void printOrderInfo(Order order){
		System.out.println("-----------Order detail----------");
		System.out.println("Custom: " + order.getCustomer());
		System.out.println("----Item info--------");
		for (CartItem ci : order.getItems()) {
			System.out.println("ItemNo: "+ci.getProduct().getProductId());
			System.out.println("ItemTitle: "+ci.getProduct().getTitle());
			System.out.println("Item quantity: "+ci.getQuantity());
			System.out.println("Item description: "+ci.getProduct().getDescription());
			System.out.println("Item price: "+ci.getProduct().getPrice());
			System.out.println("Photo url: "+ci.getProduct().getImgUrl());
		}
		System.out.println("----Item info end----");
		System.out.println("Delivery Address: " + order.getAddress());
		System.out.println("Total Price: " + order.getTotalPrice());
		System.out.println("Shipping Cost: " + order.getShippingCost());
		System.out.println("Final Cost: " + order.getFinalCost());
		System.out.println("-----------Detail end----------");
	}*/

	
	
	
	

}
