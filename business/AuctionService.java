package edu.unsw.comp9321.business;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.sql.Timestamp;

import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;

public interface AuctionService {

	/**
	 * Finds a user with the given details
	 * @param username The username to login with
	 * @param password The password to login with
	 * @return A user if one exists with the given details
	 * @throws UserLoginFailedException When no such user is found
	 */
	public UserBean login(String username, String password) throws UserLoginFailedException;
	
	public boolean register(UserBean user)
		throws UserRegistrationFailedException;
	
	public boolean updateUserStatus(String name,UserStatus newStatus) throws UserStatusUpdationFailedException;
	
	public boolean updateUser(UserBean newUser) throws UserProfileUpdateFailedException;
	
	/**
	 * @return list of items.
	 * @throws ItemsFetchingFailedException
	 */
	public List<ItemBean> getItems() throws ItemsFetchingFailedException;
	
	/**
	 * Adds a new auction item to the database
	 */
	void addItem(ItemBean newItem);
	
	/**
	 * Returns the new bid if it is accepted or the database bid if it has increased, or -1 if auction is not live
	 */
	public BigDecimal newBid(long ID, UserBean user, BigDecimal bid, BigDecimal previousBid, Timestamp bidTime);
	
	public List<ItemBean> searchItems(String keywords, String matchMethod, Category category
			, String city, String state, String country, String postcode, Currency currency
			, BigDecimal priceMin, BigDecimal priceMax, String timeOption, int timeInMinutes);
	
	/**
	 * get a list of item and update them to new status
	 * @param currentTime
	 * @param newStatus
	 * @return
	 */
	public List<ItemBean> updateItemStatus(Timestamp currentTime, ItemStatus newStatus);
	
	public void updatePendingItemStatus(long ID, ItemStatus newStatus);
	
	/**
	 * Get twelve random items from database
	 */
	public List<ItemBean> getRandomItems();
	
	public void addToWishlist(String username, ItemBean item);
	
	public ItemBean findItem(long id);
	
	public UserBean findUserByUsername(String username);
	
	public Set<ItemBean> getWishlist(String username);
	
	public void deleteItemFromWishlist(String username, ItemBean item);
	
	public void addMessage(MessageBean message);
	
	public List<MessageBean> getMessagesByUsername(String username);
	
	public List<ItemBean> getMyCollections(String username);
	
	public UserBean adminLogin(String username, String password) throws UserLoginFailedException;
	
	public Boolean banUser(String username);
	
	public List<UserBean> findLiveUsers();
	
	public List<ItemBean> findLiveItems();
	
	public Boolean haltAuction(long id);
	
	public List<ItemBean> findNotLiveItems();
	
	public boolean removeItem(long id);
	
	public List<ItemBean> findHaltItems();
	
	public boolean backLiveAuction(long id);
	
	public List<UserBean> findBanUsers();
	
	public boolean setLiveUser(String username);
	
	public List<MessageBean> getMessagesNotSent();
	
	public void updateMessage(long id, int status);
}
