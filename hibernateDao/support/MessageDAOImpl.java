package edu.unsw.comp9321.hibernateDao.support;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.unsw.comp9321.Constant;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateDao.HibernateUtil;
import edu.unsw.comp9321.hibernateDao.MessageDAO;

public class MessageDAOImpl implements MessageDAO {
	
	public List<MessageBean> getMessagesByUsername(String username) {
		List<MessageBean> list = new ArrayList<MessageBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from MessageBean"
					+ " where username = :username"
					+ " order by insertTime desc");
			query.setParameter("username", username);
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
	
	public List<MessageBean> getMessagesNotSent() {
		List<MessageBean> list = new ArrayList<MessageBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from MessageBean"
					+ " where status = :status"
					+ " order by insertTime");
			query.setParameter("status", Constant.NOTSENT);
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
	
	public void addMessage(MessageBean message) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(message);
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
	
	public void updateMessage(long id, int status) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;	
		
		try {
			tx = session.beginTransaction();
			String hql = "update MessageBean set status = :status"
					+ " where messageId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("status", status);
			query.setParameter("id", id);
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
}
