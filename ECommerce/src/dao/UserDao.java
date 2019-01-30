package dao;

import java.util.Map;

import model.User;


public interface UserDao {
	public User getUserByUserName(String userName);
}
