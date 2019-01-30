package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import model.*;

/**
 * The Factory responsible for creating proper dao 
 * @author zhouy
 *
 */
public class DaoFactory {

	private static DaoFactory df;
	private OrderDao oDao = null;
	private UserDao uDao = null;
	static Log log = LogFactory.getLog(DaoFactory.class);
	String classname = this.getClass().getName();
	private DaoFactory(){
	}
	
	public static DaoFactory getInstance(){
		if (df == null)
			df = new DaoFactory();
		return df;
	}
	
	public OrderDao getOrderDao(){
		if (oDao == null){
			Properties properties = new Properties() ;
			try{
				properties.load(this.getClass().getResourceAsStream("/ecommerce.properties"));		
				String className = properties.getProperty("dao.OrderDaoName");
				if (className!=null){
					oDao = (OrderDao)Class.forName(className).newInstance();
					log.info("Using " + className + " to get OrderInfo...");
				}else{
					log.info("property not found");
					System.out.println("property not found");
					return null;
				}
			}catch (Exception e){ 
				e.printStackTrace();
				log.info(e.getMessage());
			}
		}
		return oDao;
	}	
	
	public UserDao getUserDao(){
		if (uDao == null){
			Properties properties = new Properties() ;
			try{
				properties.load(this.getClass().getResourceAsStream("/ecommerce.properties"));		
				String className = properties.getProperty("dao.UserDaoName");
				if (className!=null){
					uDao = (UserDao)Class.forName(className).newInstance();
					log.info("Using " + className + " to get UserInfo...");
				}else{
					log.info("property not found");
					System.out.println("property not found");
					return null;
				}
			}catch (Exception e){ 
				log.info(e.getMessage());
			}
		}
		return uDao;
		
		
	}
	
}

