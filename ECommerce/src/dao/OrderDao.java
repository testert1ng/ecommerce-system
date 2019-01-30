package dao;

import java.util.List;

import model.Order;
import model.Product;

public interface OrderDao {
	public List<Order> getAllOrders();
	public void addOrder(Order o);
	public Order getOrderById(int orId);
	public void updateOrder(Order o);
	public void deleteOrder(int orId);	
	public List<Order> getOrdersByCustomer(String customer);
}
