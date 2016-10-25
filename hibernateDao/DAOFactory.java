package edu.unsw.comp9321.hibernateDao;

import java.util.HashMap;
import java.util.Map;

import edu.unsw.comp9321.hibernateDao.support.ItemDAOImpl;
import edu.unsw.comp9321.hibernateDao.support.MessageDAOImpl;
import edu.unsw.comp9321.hibernateDao.support.UserDAOImpl;

public class DAOFactory {
	
	private static final String USER_DAO = "userDAO";
	private static final String ITEM_DAO = "itemDAO";
	private static final String MESSAGE_DAO = "messageDAO";
	
	private Map daos;
	
	private static DAOFactory instance = new DAOFactory();
	
	/** Creates a new instance of DAOFactory */
	private DAOFactory() {
		daos = new HashMap();
		daos.put(USER_DAO, new UserDAOImpl());
		daos.put(ITEM_DAO, new ItemDAOImpl());
		daos.put(MESSAGE_DAO, new MessageDAOImpl());
	}
	
	/**
	 * Finds the user dao
	 * @return
	 */
	public UserDAO getUserDAO() {
		return (UserDAO) daos.get(USER_DAO);
	}

	/**
	 * Retrieves the items dao
	 * @return
	 */
	public ItemDAO getItemDAO() {
		return (ItemDAO) daos.get(ITEM_DAO);
	}
	
	/**
	 * Retrieves the message dao
	 * @return
	 */
	public MessageDAO getMessageDAO() {
		return (MessageDAO) daos.get(MESSAGE_DAO);
	}
	
	public static DAOFactory getInstance() {
		return instance;
	}
}