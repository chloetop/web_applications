/**
 * 
 */
package edu.unsw.comp9321.hibernateBeans;

import java.io.Serializable;
/**
 * @author Millyto
 *
 */
public class WishListBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private long itemId;
	
	public WishListBean() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	

}
