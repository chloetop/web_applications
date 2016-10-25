package edu.unsw.comp9321.web.helper;


import java.util.Date;
import java.util.Set;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import edu.unsw.comp9321.business.AuctionService;
import edu.unsw.comp9321.business.GetUserFailedException;
import edu.unsw.comp9321.business.ItemsFetchingFailedException;
import edu.unsw.comp9321.business.UserLoginFailedException;
import edu.unsw.comp9321.business.UserProfileUpdateFailedException;
import edu.unsw.comp9321.business.UserRegistrationFailedException;
import edu.unsw.comp9321.business.UserStatusUpdationFailedException;
import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.hibernateBeans.ItemBean;

/**
 * Abstract business delegate. This class implements all the methods of getAuctionService()
 */
public abstract class AuctionDelegate {

    /**
     * Find the auction service.
     * @return The business interface AuctionService
     */
	protected abstract AuctionService getService();

	private AuctionService auctionService;
	
	/**
	 * Finds a user with the given details
	 * @param username The username to login with
	 * @param password The password to login with
	 * @return A user if one exists with the given details
	 * @throws UserLoginFailedException When no such user is found
	 */
	public UserBean login(String username, String password) throws UserLoginFailedException {
		return getService().login(username, password);
	}
	
	public boolean register(UserBean user) throws UserRegistrationFailedException{
		return getService().register(user);				
	}
	
	public boolean updateUserStatus(String name,UserStatus newStatus) throws UserStatusUpdationFailedException{
		return getService().updateUserStatus(name, newStatus);
	}
	
	public boolean updateUser(UserBean newUser) throws UserProfileUpdateFailedException{
		return getService().updateUser(newUser);
	}
	
	public List<ItemBean> getItems() throws ItemsFetchingFailedException{
		return getService().getItems();
	}
		
	/**
	 * Adds a new auction item to the database
	 */
	public void addItem(ItemBean newItem) {
		getService().addItem(newItem);
	}
	
	/**
	 * Returns a the new bid and updates the database if it is valid.
	 * Returns the latest database bid if it has changed
	 * Returns -1 if the auction is not live
	 */
	public BigDecimal newBid(long ID, UserBean user, BigDecimal bid, BigDecimal previousBid, Timestamp bidTime) {
		return getService().newBid(ID, user, bid, previousBid, bidTime);
	}
		
	public List<ItemBean> searchItems(String keywords, String matchMethod, Category category
			, String city, String state, String country, String postcode, Currency currency
			, BigDecimal priceMin, BigDecimal priceMax, String timeOption, int timeInMinutes) {
		
		return getService().searchItems(keywords, matchMethod, category, city, state, country, postcode, currency, priceMin, priceMax, timeOption, timeInMinutes);
	}
	
	public List<ItemBean> updateItemStatus(Timestamp currentTime, ItemStatus newStatus)
	{
		return getService().updateItemStatus(currentTime, newStatus);
	}

	public void updatePendingItemStatus(long id, ItemStatus newStatus) {
		getService().updatePendingItemStatus(id, newStatus);
	}
	
	/**
	 * Get twelve random items from database
	 */
	public List<ItemBean> getRandomItem() {
		return getService().getRandomItems();
	}
	
	public ItemBean findItem(long id) {
		return getService().findItem(id);
	}
	
	public void addToWishlist(String username, ItemBean item) {
		getService().addToWishlist(username, item);
	}
	
	public UserBean findUserByUsername(String username) {
		return getService().findUserByUsername(username);
	}
	
	public Set<ItemBean> getWishlist(String username) {
		return getService().getWishlist(username);
	}
	
	public void deleteItemFromWishlist(String username, ItemBean item) {
		getService().deleteItemFromWishlist(username, item);
	}
	
	public void addMessage(MessageBean message) {
		getService().addMessage(message);
	}
	
	public List<MessageBean> getMessagesByUsername(String username) {
		return getService().getMessagesByUsername(username);
	}
	
	public List<ItemBean> getMyCollections(String username) {
		return getService().getMyCollections(username);
	}
	
	public UserBean adminLogin(String username, String password) throws UserLoginFailedException {
		return getService().adminLogin(username, password);
	}
	
	public Boolean banUser(String username) {
		return getService().banUser(username);
	}
	
	public List<UserBean> findLiveUsers() {
		return getService().findLiveUsers();
	}
	
	public List<ItemBean> findLiveItems() {
		return getService().findLiveItems();
	}
	
	public Boolean haltAuction(long id) {
		return getService().haltAuction(id);
	}
	
	public List<ItemBean> findNotLiveItems() {
		return getService().findNotLiveItems();
	}
	
	public boolean removeItem(long id) {
		return getService().removeItem(id);
	}
	
	public List<ItemBean> findHaltItems() {
		return getService().findHaltItems();
	}
	
	public boolean backLiveAuction(long id) {
		return getService().backLiveAuction(id);
	}
	
	public List<UserBean> findBanUsers() {
		return getService().findBanUsers();
	}
	
	public boolean setLiveUser(String username) {
		return getService().setLiveUser(username);
	}
	
	public List<MessageBean> getMessagesNotSent() {
		return getService().getMessagesNotSent();
	}
	
	public void updateMessage(long id, int status) {
		getService().updateMessage(id, status);
	}
}
