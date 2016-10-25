/**
 * 
 */
package edu.unsw.comp9321.hibernateDao.support;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.hibernateDao.HibernateUtil;
import edu.unsw.comp9321.hibernateDao.UserDAO;

/**
 * @author Millyto
 *
 */
public class UserDAOImpl implements UserDAO {

	public List<UserBean> findAllUsers() {		
		List<UserBean> list = new ArrayList<UserBean>();
		 Session session = HibernateUtil.getSessionFactory().openSession();
		 Transaction tx = null;	
		 try {
			 tx = session.getTransaction();
			 tx.begin();
			 list = session.createQuery("from UserBean").list();					
			 tx.commit();
		 } catch (Exception e) {
			 if (tx != null) {
				 tx.rollback();
			 }
			 e.printStackTrace();
		 } finally {
			 session.close();
		 }
		 return list;
	}
	
	public boolean register(UserBean user) {
		if(isUserExists(user) || isEmailExists(user))
			return false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(user);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}
	
	public String getEmail(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		String email = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :name");
			query.setParameter("name", username);
			UserBean u = (UserBean) query.uniqueResult();
			email = u.getEmail();
			tx.commit();
		} catch (Exception ex) {
			if(tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return email;
		
	}
	
	public boolean updateUser(UserBean newUser){
		if(!getEmail(newUser.getUsername()).equals(newUser.getEmail())) {
			if(isEmailExists(newUser)) {
				return false;
			}
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		
			String hql = "update UserBean"
					+ " set password = :newPassword, "
					+ " email = :newEmail, "
					+ " nickname = :newNickname, "
					+ " first_name = :newFirstName, "
					+ " last_name = :newLastName, "
					+ " date_of_birth = :newDateOfBirth, "
					+ " address_street = :newAddressStreet, "
					+ " address_city = :newAddressCity, "
					+ " address_state = :newAddressState, "
					+ " address_country = :newAddressCountry, "
					+ " address_postcode = :newAddressPostcode, "
					+ " credit_card = :newCreditCard "
					+ " where username = :name";
			
			Query query = session.createQuery(hql);
			query.setParameter("name", newUser.getUsername());
			query.setParameter("newPassword", newUser.getPassword());
			query.setParameter("newEmail", newUser.getEmail());
			query.setParameter("newNickname", newUser.getNickname());
			query.setParameter("newFirstName", newUser.getFirstName());
			query.setParameter("newLastName", newUser.getLastName());
			query.setParameter("newDateOfBirth", newUser.getDateOfBirth());
			query.setParameter("newAddressStreet", newUser.getAddressStreet());
			query.setParameter("newAddressCity", newUser.getAddressCity());
			query.setParameter("newAddressState", newUser.getAddressState());
			query.setParameter("newAddressCountry", newUser.getAddressCountry());
			query.setParameter("newAddressPostcode", newUser.getAddressPostcode());
			query.setParameter("newCreditCard", newUser.getCreditCard());
			query.executeUpdate();
			
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();			
		} finally {
			session.close();
		}
		return false;
	}
	
	public boolean updateUserStatus(String name, UserStatus newStatus) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;			
		try {
			tx = session.beginTransaction();
		
			String hql = "update UserBean"
					+ " set userStatus = :newStatus"
					+ " where username = :name";
			
			Query query = session.createQuery(hql);
			query.setParameter("newStatus", newStatus);
			query.setParameter("name", name);
			query.executeUpdate();
			
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();			
		} finally {
			session.close();
		}
		return false;
	}
	
	public boolean isUserExists(UserBean user) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		boolean result = false;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :name");
			query.setParameter("name", user.getUsername());
			UserBean u = (UserBean) query.uniqueResult();
			tx.commit();
			if(u != null)
				result = true;
		} catch (Exception ex) {
			if(tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}
	
	public boolean isEmailExists(UserBean user) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		boolean result = false;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where email= :useremail");
			query.setParameter("useremail", user.getEmail());
			UserBean u = (UserBean) query.uniqueResult();
			tx.commit();
			if(u != null)
				result = true;
		} catch (Exception ex) {
			if(tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}
	
	public boolean authenticate(String username, String password) {
		UserBean user = findUserByUsername(username);
		Boolean value = false;
		
		if(user != null && user.getUsername().equals(username) && user.getPassword().equals(password) && !user.getIsAdmin()) {
			if (user.getUserStatus().equals(UserStatus.LIVE)){
				value = true;
			}
		} else {
			value = false;
		}
		return value;
	}
	
	public boolean adminAuthenticate(String username, String password) {
		UserBean user = findUserByUsername(username);
		Boolean value = false;
		
		if(user != null && user.getUsername().equals(username) && user.getPassword().equals(password) && user.getIsAdmin()) {
			value = true;
		} else {
			value = false;
		}
		return value;
	}
	
	public UserBean findUserByUsername(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		UserBean user = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
		
	}
	
	public void addItemToWishlist(String username, ItemBean item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		UserBean user = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			
			Boolean found = false;
			Iterator<ItemBean> iterator = user.getWishlistItems().iterator();
			while (iterator.hasNext()) {
			    ItemBean element = iterator.next();
			    if (element.getItemId() == item.getItemId()) {
			        found = true;
			    }
			}
			if (found != true) {
				user.getWishlistItems().add(item);
			}
			
			session.saveOrUpdate(user);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public Set<ItemBean> getWishlist(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Set<ItemBean> list = new HashSet<ItemBean>();
		UserBean user = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			list = user.getWishlistItems();
			for (ItemBean i : list) {
				System.out.println(i.getTitle());
			}
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public void deleteItemFromWishlist(String username, ItemBean item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		UserBean user = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			
			Iterator<ItemBean> iterator = user.getWishlistItems().iterator();
			while (iterator.hasNext()) {
			    ItemBean element = iterator.next();
			    if (element.getItemId() == item.getItemId()) {
			        iterator.remove();
			    }
			}
			//user.getWishlistItems().remove(item);
			
			session.saveOrUpdate(user);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public boolean banUser(String username) {
		Boolean value = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		UserBean user = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			if (user != null) {
				if(!user.getIsAdmin()) {
					if (user.getUserStatus().equals(UserStatus.LIVE)){
						user.setUserStatus(UserStatus.BANNED);
						session.saveOrUpdate(user);
						value = true;
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return value;
	}
	
	public List<UserBean> findLiveUsers() {
		List<UserBean> list = new ArrayList<UserBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(UserBean.class);
		Transaction tx = null;

		try {
			tx = session.getTransaction();
			tx.begin();

			crit = session.createCriteria(UserBean.class);
			crit.add(Restrictions.eq("userStatus", UserStatus.LIVE));
			list = crit.list();

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public List<UserBean> findBanUsers() {
		List<UserBean> list = new ArrayList<UserBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(UserBean.class);
		Transaction tx = null;

		try {
			tx = session.getTransaction();
			tx.begin();

			crit = session.createCriteria(UserBean.class);
			crit.add(Restrictions.eq("userStatus", UserStatus.BANNED));
			list = crit.list();

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public boolean setLiveUser(String username) {
		Boolean value = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		UserBean user = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			if (user != null) {
				if(!user.getIsAdmin()) {
					if (user.getUserStatus().equals(UserStatus.BANNED)){
						user.setUserStatus(UserStatus.LIVE);
						session.saveOrUpdate(user);
						value = true;
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return value;
	}
	
	public boolean wishlistContain(String username, ItemBean item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		UserBean user = null;
		Boolean found = false;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			
			Iterator<ItemBean> iterator = user.getWishlistItems().iterator();
			while (iterator.hasNext()) {
			    ItemBean element = iterator.next();
			    if (element.getItemId() == item.getItemId()) {
			        found = true;
			    }
			}
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return found;
	}
}
