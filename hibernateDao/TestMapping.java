package edu.unsw.comp9321.hibernateDao;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.hibernate.Session;

import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;

public class TestMapping {
	public static void main(String[] args) {
		 
        System.out.println("Hibernate many to many (XML Mapping)");
		Session session = HibernateUtil.getSessionFactory().openSession();
	 
		session.beginTransaction();
		UserBean user = new UserBean();
		user.setUsername("testing username");
		user.setPassword("testing password");
		user.setEmail("testing email");
		user.setNickname("testing nickname");
		user.setFirstName("testing first name");
		user.setLastName("testing last name");
		// YYYY-MM-DD
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "1990-12-31";
		Date date;
		try {
			date = sdf.parse(dateInString);
			user.setDateOfBirth(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		user.setAddressStreet("testing street");
		user.setAddressCity("testing city");
		user.setAddressState("testing state");
		user.setAddressCountry("testing country");
		int postcode = 1234;
		user.setAddressPostcode("1234");
		user.setCreditCard("1234567812345678");
		user.setUserStatus(UserStatus.LIVE);
		Boolean isadmin = false;
		user.setIsAdmin(isadmin);
		
		ItemBean item3 = new ItemBean();
		item3.setSeller(user);
		item3.setTitle("testing title");
		item3.setCategory(Category.ARTS);
		item3.setDescription("test description");
		item3.setPicture("testing picture");
		item3.setCurrency(Currency.AUD);
		item3.setReservePrice(new BigDecimal(120.50));
		item3.setBidIncrements(new BigDecimal(5.00));
		item3.setBestBid(new BigDecimal(55.00));
		Date date1 = new Date();
		item3.setBestBidTime(new Timestamp(date1.getTime()));
		item3.setEndTime(new Timestamp(date1.getTime()));
		item3.setItemStatus(ItemStatus.LIVE);
		
		ItemBean item1 = new ItemBean(user, "testing title", Category.BOOKS, "testing descrip"
				, "testing pic", Currency.AUD, new BigDecimal(120.25), new BigDecimal(5.00), 
				new BigDecimal(50.00), new Timestamp(date1.getTime()),
				user, new Timestamp(date1.getTime()), ItemStatus.LIVE);
		
		ItemBean item2 = new ItemBean(user, "testing title2", Category.ARTS, "testing descrip2"
				, "testing pic2", Currency.AUD, new BigDecimal(1222.25), new BigDecimal(52), 
				new BigDecimal(123.00), new Timestamp(date1.getTime()),
				null, new Timestamp(date1.getTime()), ItemStatus.LIVE);
		Set<ItemBean> itemWishlist = new HashSet<ItemBean>();
		itemWishlist.add(item1);
		itemWishlist.add(item2);
		
		user.setWishlistItems(itemWishlist);
		session.save(user);
		session.save(item3);
		System.out.println("Commit");
		session.getTransaction().commit();
	
		System.out.println("Done");
	}
}