/**
 * 
 */
package edu.unsw.comp9321.hibernateDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;

import edu.unsw.comp9321.business.UserProfileUpdateFailedException;
import edu.unsw.comp9321.business.UserStatusUpdationFailedException;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;

/**
 * @author Millyto
 *
 */
public interface UserDAO {
	
	// Retrieve all the 'User' records from DB
	public List<UserBean> findAllUsers();
	
	// Register a new user into DB
	public boolean register(UserBean user);
	
	// Updates the status of user.
	public boolean updateUserStatus(String email,UserStatus newStatus);
	
	// Updates user with new information.
	public boolean updateUser(UserBean newUser) throws UserProfileUpdateFailedException;
	
	// Check if the username exists in DB or not
	// This method is used in register(UserBean user)
	public boolean isUserExists(UserBean user);
	
	// Check if the email exists in DB or not
	// This method is used in register(UserBean user)
	public boolean isEmailExists(UserBean user);
	
	// Check if the username and password provided matches the record in DB
	public boolean authenticate(String username, String password);
	
	// Retrieve a user detail from DB
	// Call the authenticate method before calling this method,
	// if authenticate return true, then call this method
	public UserBean findUserByUsername(String username);
	
	public void addItemToWishlist(String username, ItemBean item);
	
	public Set<ItemBean> getWishlist(String username);
	
	public void deleteItemFromWishlist(String username, ItemBean item);
	
	public boolean adminAuthenticate(String username, String password);
	
	public boolean banUser(String username);
	
	public List<UserBean> findLiveUsers() ;
	
	public List<UserBean> findBanUsers();
	
	public boolean setLiveUser(String username);
	
	public boolean wishlistContain(String username, ItemBean item);
}
