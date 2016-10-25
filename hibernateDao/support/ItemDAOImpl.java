/**
 * 
 */
package edu.unsw.comp9321.hibernateDao.support;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;

import edu.unsw.comp9321.Util;
import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.hibernateDao.HibernateUtil;
import edu.unsw.comp9321.hibernateDao.ItemDAO;

/**
 * @author Millyto
 *
 */
public class ItemDAOImpl implements ItemDAO {
	
	public List<ItemBean> findAllItems() {
		List<ItemBean> list = new ArrayList<ItemBean>();
		 Session session = HibernateUtil.getSessionFactory().openSession();
		 Transaction tx = null;	
		 try {
			 tx = session.getTransaction();
			 tx.begin();
			 list = session.createQuery("from ItemBean").list();		
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
	
	public void addItem(ItemBean item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(item);
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
	
	public BigDecimal bestBid(long ID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		ItemBean item = null;
		BigDecimal bestBid = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ItemBean where itemId= :id");
			query.setParameter("id", ID);
			item = (ItemBean) query.uniqueResult();
			bestBid = item.getBestBid(); 
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return bestBid;		
	}
	
	public boolean liveAuction(long ID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		ItemBean item = null;
		ItemStatus status = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ItemBean where itemId= :id");
			query.setParameter("id", ID);			
			item = (ItemBean) query.uniqueResult();
			status = item.getItemStatus(); 
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (status == ItemStatus.LIVE)
			return true;
		return false;		
	}
	
	public int updateBid(long ID, BigDecimal newBid, BigDecimal currentBid, Timestamp bidTime, String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		int result = 0;
		try {
			tx = session.beginTransaction();
			String hql = "update ItemBean set "
					+ "best_bidder= :username, "
					+ "best_bid= :bid, "
					+ "best_bid_time= :bidtime "
					+ "where itemId= :id "
					+ "and best_bid = :currentbid "
					+ "and status = 'LIVE'";
			Query query = session.createQuery(hql);
			query.setParameter("id", ID);			
			query.setParameter("bidtime", bidTime);			
			query.setParameter("bid", newBid);			
			query.setParameter("username", username);			
			query.setParameter("currentbid", currentBid);			
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}
	
	public List<ItemBean> findItemsByCriteria(String keywordsInString, String matchMethod, Category category
			, String city, String state, String country, String postcode, Currency currency
			, BigDecimal priceMin, BigDecimal priceMax, String timeOption, int timeInMinutes) {
		
		List<ItemBean> list = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;	
		
		Criteria criteria = session.createCriteria(ItemBean.class);
		criteria.add(Restrictions.eq("itemStatus", ItemStatus.LIVE));
		
		if (keywordsInString != null && !keywordsInString.trim().isEmpty()) {
			String[] keywords = keywordsInString.split("\\s+");
			
			if (matchMethod == null || matchMethod.equalsIgnoreCase("Any")) {
				Disjunction or = Restrictions.disjunction();
				
				for (String keyword : keywords) {
					addKeywordCriteria(keyword, or);
				}
				
				criteria.add(or);
			}
			else {
				for (String keyword : keywords) {
					Disjunction or = Restrictions.disjunction();
					addKeywordCriteria(keyword, or);
					criteria.add(or);
				}
			}	
		}
		
		if (category != null) {
			criteria.add(Restrictions.eq("category", category));
		}
		
		if (!Util.isNullOrEmpty(city) || !Util.isNullOrEmpty(state) 
				|| !Util.isNullOrEmpty(country) || !Util.isNullOrEmpty(postcode)) {
			
			DetachedCriteria userSubquery = getUserSubquery(city, state, country, postcode);	
			criteria.add(Subqueries.propertyIn("seller", userSubquery));
		}

		if (currency != null) {
			criteria.add(Restrictions.eq("currency", currency));
		}
		
		if (priceMin != null) {
			criteria.add(Restrictions.ge("bestBid", priceMin));
		}
		
		if (priceMax != null) {
			criteria.add(Restrictions.le("bestBid", priceMax));
		}
		
		if (timeInMinutes > 0 && timeOption != null) {
			Timestamp time = Util.addMinutes(new Timestamp(System.currentTimeMillis()), timeInMinutes);
			
			if (timeOption.equalsIgnoreCase("Within")) {
				criteria.add(Restrictions.le("endTime", time));
			}
			else {
				criteria.add(Restrictions.gt("endTime", time));
			}
		}
		
		try {
			tx = session.getTransaction();
			tx.begin();
			list = criteria.list();					
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return list;
	}

	void addKeywordCriteria(String keyword, Disjunction or) {
	  or.add(Restrictions.ilike("title", keyword, MatchMode.ANYWHERE));
	  or.add(Restrictions.ilike("description", keyword, MatchMode.ANYWHERE));
	  
	  Category categoryKeywords = Category.getEnumBySubstring(keyword);
	  if (categoryKeywords != null) {
	  	or.add(Restrictions.eq("category", categoryKeywords));
	  }
	}
	
	DetachedCriteria getUserSubquery(String city, String state, String country,
      String postcode) {
	  DetachedCriteria userSubquery = DetachedCriteria.forClass(UserBean.class);
	  userSubquery.setProjection(Property.forName("username"));
	  
	  if (!Util.isNullOrEmpty(city)) {
	  	userSubquery.add(Restrictions.eq("addressCity", city).ignoreCase());
	  }
	  
	  if (!Util.isNullOrEmpty(state)) {
	  	userSubquery.add(Restrictions.eq("addressState", state).ignoreCase());
	  }
	  
	  if (!Util.isNullOrEmpty(country)) {
	  	userSubquery.add(Restrictions.eq("addressCountry", country).ignoreCase());
	  }
	  
	  if (!Util.isNullOrEmpty(postcode)) {
	  	userSubquery.add(Restrictions.eq("addressPostcode", postcode).ignoreCase());
	  }
	  return userSubquery;
	}
	
	public List<ItemBean> updateItemStatus(Timestamp currentTime, ItemStatus newStatus) {
		List<ItemBean> list = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;	
		String op = newStatus.equals(ItemStatus.SOLD) ? ">=" : "<";
		String isOrIsNot = newStatus.equals(ItemStatus.NOTSOLD) ? "is" : "is not";
		
		try {
			tx = session.beginTransaction();
		
			String hql = "from ItemBean where itemStatus = :status"
							+ " and endTime < :currentTime"
							+ " and bestBid " + op + " reservePrice"
							+ " and bestBidder " + isOrIsNot + " null";
			Query query = session.createQuery(hql);
			query.setParameter("currentTime", currentTime);
			query.setParameter("status", ItemStatus.LIVE);
			list = query.list();
			
			hql = "update ItemBean set itemStatus = :newStatus"
					+ " where itemStatus = :status"
					+ " and endTime < :currentTime"
					+ " and bestBid " + op + " reservePrice"
					+ " and bestBidder " + isOrIsNot + " null";;
			query = session.createQuery(hql);
			query.setParameter("newStatus", newStatus);
			query.setParameter("currentTime", currentTime);
			query.setParameter("status", ItemStatus.LIVE);
			query.executeUpdate();
			
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
	
	public void updatePendingItemStatus(long ID, ItemStatus newStatus) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;	
		
		try {
			tx = session.beginTransaction();
			String hql = "update ItemBean set itemStatus = :newStatus"
					+ " where item_id = :id";
			Query query = session.createQuery(hql);
			query = session.createQuery(hql);
			query.setParameter("newStatus", newStatus);
			query.setParameter("id", ID);
			query.executeUpdate();
			
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
		}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public List<ItemBean> getTwelveRandomItems() {
		List<ItemBean> list = new ArrayList<ItemBean>();
		List<Integer> selectedIndex = new ArrayList<Integer>();
		List<ItemBean> returnList = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(ItemBean.class);
		Transaction tx = null;
		//System.out.println("OK");
		try {
			tx = session.getTransaction();
			tx.begin();
			crit.setProjection(Projections.rowCount());
			int count = ((Number) crit.uniqueResult()).intValue();
			if (count < 12) {
				crit = session.createCriteria(ItemBean.class);
				crit.add(Restrictions.eq("itemStatus", ItemStatus.LIVE));
				returnList = crit.list();
			} else {
				crit = session.createCriteria(ItemBean.class);
				crit.add(Restrictions.eq("itemStatus", ItemStatus.LIVE));
				list = crit.list();
				for(int i = 0; i < 12; i++) {
					int index = new Random().nextInt(list.size());
					while(selectedIndex.contains(index)) {
						index = new Random().nextInt(list.size());
					}
					selectedIndex.add(index);
					returnList.add(list.get(index));
				}
			}				
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return returnList;
	}
	
	public ItemBean findItem(long id) {
		ItemBean item = new ItemBean();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ItemBean where itemId= :id");
			query.setParameter("id", id);
			item = (ItemBean) query.uniqueResult();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return item;			
	}
	
	public List<ItemBean> getMyCollections(String username) {
		List<ItemBean> list = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(ItemBean.class);
		Transaction tx = null;
		UserBean user = null;
		
		try {
			tx = session.getTransaction();
			tx.begin();
			
			Query query = session.createQuery("from UserBean where username= :username");
			query.setParameter("username", username);
			user = (UserBean) query.uniqueResult();
			
			crit = session.createCriteria(ItemBean.class);
			crit.add(Restrictions.eq("seller", user));
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
	
	public List<ItemBean> findLiveItems() {
		List<ItemBean> list = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(ItemBean.class);
		Transaction tx = null;
		//System.out.println("OK");
		try {
			tx = session.getTransaction();
			tx.begin();

			crit = session.createCriteria(ItemBean.class);
			crit.add(Restrictions.eq("itemStatus", ItemStatus.LIVE));
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
	
	public boolean haltAuction(long id) {
		Boolean value = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		ItemBean item = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ItemBean where itemId= :id");
			query.setParameter("id", id);
			item = (ItemBean) query.uniqueResult();
			if (item != null) {
				if(item.getItemStatus().equals(ItemStatus.LIVE)) {
					item.setItemStatus(ItemStatus.HALT);
					session.saveOrUpdate(item);
					value = true;
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
	
	public List<ItemBean> findNotLiveItems() {
		List<ItemBean> list = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.getTransaction();
			tx.begin();

			Query query = session.createQuery("from ItemBean where itemStatus= :notsold or itemStatus= :halt or itemStatus= :pending or itemStatus= :sold");	
			query.setParameter("notsold", ItemStatus.NOTSOLD);
			query.setParameter("halt", ItemStatus.HALT);
			query.setParameter("pending", ItemStatus.PENDING);
			query.setParameter("sold", ItemStatus.SOLD);
			list = query.list();
			
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
	
	public boolean removeItem(long id) {
		Boolean value = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		ItemBean item = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ItemBean where itemId= :id");
			query.setParameter("id", id);
			item = (ItemBean) query.uniqueResult();
			if (item != null) {
				if(!item.getItemStatus().equals(ItemStatus.LIVE)) {
					Query query2 = session.createQuery("delete WishListBean where itemId= :id");
					query2.setParameter("id", id);
					query2.executeUpdate();
					
					Query query3 = session.createQuery("delete ItemBean where itemId= :id");
					query3.setParameter("id", id);
					query3.executeUpdate();
					
					value = true;
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
	
	public List<ItemBean> findHaltItems() {
		List<ItemBean> list = new ArrayList<ItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(ItemBean.class);
		Transaction tx = null;

		try {
			tx = session.getTransaction();
			tx.begin();

			crit = session.createCriteria(ItemBean.class);
			crit.add(Restrictions.eq("itemStatus", ItemStatus.HALT));
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
	
	public boolean backLiveAuction(long id) {
		Boolean value = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		ItemBean item = null;
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ItemBean where itemId= :id");
			query.setParameter("id", id);
			item = (ItemBean) query.uniqueResult();
			if (item != null) {
				if(item.getItemStatus().equals(ItemStatus.HALT)) {
					item.setItemStatus(ItemStatus.LIVE);
					session.saveOrUpdate(item);
					value = true;
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

}
