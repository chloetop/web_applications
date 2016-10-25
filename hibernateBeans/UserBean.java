/**
 * 
 */
package edu.unsw.comp9321.hibernateBeans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Millyto
 *
 */
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String email;
	private String nickname;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String addressStreet;
	private String addressCity;
	private String addressState;
	private String addressCountry;
	private String addressPostcode;
	private String creditCard;
	private UserStatus userStatus; // 'LIVE','BANNED','PENDACK'
	private Boolean isAdmin;
	private Set<ItemBean> wishlistItems = new HashSet<ItemBean>(0);
	
	public UserBean() {
	}
	
	public UserBean(String username, String password, String email,
			String nickname, String firstName, String lastName,
			Date dateOfBirth, String addressStreet, String addressCity,
			String addressState, String addressCountry, String addressPostcode,
			String creditCard, UserStatus userStatus, Boolean isAdmin) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.nickname = nickname;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.addressStreet = addressStreet;
		this.addressCity = addressCity;
		this.addressState = addressState;
		this.addressCountry = addressCountry;
		this.addressPostcode = addressPostcode;
		this.creditCard = creditCard;
		this.userStatus = userStatus;
		this.isAdmin = isAdmin;
	}

	public UserBean(String username, String password, String email,
			String nickname, String firstName, String lastName,
			Date dateOfBirth, String addressStreet, String addressCity,
			String addressState, String addressCountry, String addressPostcode,
			String creditCard, UserStatus userStatus, Boolean isAdmin,
			Set<ItemBean> wishlistItems) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.nickname = nickname;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.addressStreet = addressStreet;
		this.addressCity = addressCity;
		this.addressState = addressState;
		this.addressCountry = addressCountry;
		this.addressPostcode = addressPostcode;
		this.creditCard = creditCard;
		this.userStatus = userStatus;
		this.isAdmin = isAdmin;
		this.wishlistItems = wishlistItems;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getAddressPostcode() {
		return addressPostcode;
	}

	public void setAddressPostcode(String addressPostcode) {
		this.addressPostcode = addressPostcode;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Set<ItemBean> getWishlistItems() {
		return wishlistItems;
	}

	public void setWishlistItems(Set<ItemBean> wishlistItems) {
		this.wishlistItems = wishlistItems;
	}
}
