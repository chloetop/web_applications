package edu.unsw.comp9321.business.support;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.sql.Timestamp;

import edu.unsw.comp9321.business.AuctionService;
import edu.unsw.comp9321.business.GetUserFailedException;
import edu.unsw.comp9321.business.UserLoginFailedException;
import edu.unsw.comp9321.business.UserProfileUpdateFailedException;
import edu.unsw.comp9321.business.UserRegistrationFailedException;
import edu.unsw.comp9321.business.ItemsFetchingFailedException;
import edu.unsw.comp9321.business.UserStatusUpdationFailedException;
import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateDao.DAOFactory;
import edu.unsw.comp9321.hibernateDao.DataAccessException;
import edu.unsw.comp9321.hibernateDao.ItemDAO;
import edu.unsw.comp9321.hibernateDao.MessageDAO;
import edu.unsw.comp9321.hibernateDao.UserDAO;


public class AuctionServiceImpl implements AuctionService {

	private ItemDAO itemDao;
	private UserDAO userDao;
	private MessageDAO messageDao;

	// Constructor
	public AuctionServiceImpl() {
		super();
		itemDao = DAOFactory.getInstance().getItemDAO();
		userDao = DAOFactory.getInstance().getUserDAO();
		messageDao = DAOFactory.getInstance().getMessageDAO();
	}

	public UserBean login(String username, String password)
		throws UserLoginFailedException {

		UserBean user = null;
		
		if (userDao.authenticate(username, password)) {
			user = userDao.findUserByUsername(username);
		} else {
			throw new UserLoginFailedException("Login failed for " + username);
		}
		return user;
	}
	
	public boolean register(UserBean user)
		throws UserRegistrationFailedException{
		boolean result = userDao.register(user);
		
		if(!result)
			throw new UserRegistrationFailedException("User registration failed " + user.getUsername());
				
		return result;
	}
	
	public boolean updateUserStatus(String name,UserStatus newStatus) throws UserStatusUpdationFailedException{
		return userDao.updateUserStatus(name, newStatus);
	}
	
	public boolean updateUser(UserBean newUser) throws UserProfileUpdateFailedException{
		return userDao.updateUser(newUser);
	}
	
	public List<ItemBean> getItems() throws ItemsFetchingFailedException{
		
		List<ItemBean> items = itemDao.findAllItems();
		if(items == null)
			throw new ItemsFetchingFailedException("Unable to fetch Items : " + items);
		return items;
	}

	public void addItem(ItemBean newItem) {			
		itemDao.addItem(newItem);
	}

	public BigDecimal newBid(long ID, UserBean user, BigDecimal bid, BigDecimal previousBid, Timestamp bidTime) {
		
		if (itemDao.updateBid(ID, bid, previousBid, bidTime, user.getUsername()) == 0) {
			if (!itemDao.liveAuction(ID))
				return new BigDecimal(-1);
			return itemDao.bestBid(ID);
		}
		return bid;
	}
	
	public List<ItemBean> searchItems(String keywords, String matchMethod, Category category
			, String city, String state, String country, String postcode, Currency currency
			, BigDecimal priceMin, BigDecimal priceMax, String timeOption, int timeInMinutes) {
		
		return itemDao.findItemsByCriteria(keywords, matchMethod, category, city, state, country, postcode
				, currency, priceMin, priceMax, timeOption, timeInMinutes);
	}
	
	public List<ItemBean> updateItemStatus(Timestamp currentTime, ItemStatus newStatus) {
		return itemDao.updateItemStatus(currentTime, newStatus);
	}
	

	public void updatePendingItemStatus(long ID, ItemStatus newStatus) {
		itemDao.updatePendingItemStatus(ID, newStatus);		
	}

	
	public List<ItemBean> getRandomItems() {
		return itemDao.getTwelveRandomItems();
	}
	
	public ItemBean findItem(long id) {
		return itemDao.findItem(id);
	}
	
	public void addToWishlist(String username, ItemBean item) {
		userDao.addItemToWishlist(username, item);
	}
	
	public UserBean findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}
	
	public Set<ItemBean> getWishlist(String username) {
		return userDao.getWishlist(username);
	}
	
	public void deleteItemFromWishlist(String username, ItemBean item) {
		userDao.deleteItemFromWishlist(username, item);
	}

	public void addMessage(MessageBean message) {
		messageDao.addMessage(message);
	}
	
	public List<MessageBean> getMessagesByUsername(String username) {
		return messageDao.getMessagesByUsername(username);
	}
	
	public List<ItemBean> getMyCollections(String username) {
		return itemDao.getMyCollections(username);
	}
	
	public UserBean adminLogin(String username, String password) throws UserLoginFailedException {
		UserBean user = null;
			
		if (userDao.adminAuthenticate(username, password)) {
			user = userDao.findUserByUsername(username);
		} else {
			throw new UserLoginFailedException("Login failed for " + username);
		}
		return user;
	}
	
	public Boolean banUser(String username) {
		return userDao.banUser(username);

	}
	
	public List<UserBean> findLiveUsers() {
		return userDao.findLiveUsers();
	}
	
	public List<ItemBean> findLiveItems() {
		return itemDao.findLiveItems();
	}
	
	public Boolean haltAuction(long id) {
		return itemDao.haltAuction(id);
	}
	
	public List<ItemBean> findNotLiveItems() {
		return itemDao.findNotLiveItems();
	}
	
	public boolean removeItem(long id) {
		return itemDao.removeItem(id);
	}
	
	public List<ItemBean> findHaltItems() {
		return itemDao.findHaltItems();
	}
	
	public boolean backLiveAuction(long id) {
		return itemDao.backLiveAuction(id);
	}
	
	public List<UserBean> findBanUsers() {
		return userDao.findBanUsers();
	}
	
	public boolean setLiveUser(String username) {
		return userDao.setLiveUser(username);
	}
	
	public List<MessageBean> getMessagesNotSent() {
		return messageDao.getMessagesNotSent();
	}
	
	public void updateMessage(long id, int status) {
		messageDao.updateMessage(id, status);
	}
}
