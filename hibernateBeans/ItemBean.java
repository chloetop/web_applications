/**
 * 
 */
package edu.unsw.comp9321.hibernateBeans;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Millyto
 *
 */
public class ItemBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long itemId;
	private UserBean seller;
	private String title;
	private Category category;
	private String description;
	private String picture;
	private Currency currency;
	private BigDecimal reservePrice;
	private BigDecimal bidIncrements;
	private BigDecimal bestBid; // initially set by seller
	private Timestamp bestBidTime;
	private UserBean bestBidder; 
	private Timestamp endTime;
	private ItemStatus itemStatus; // 'LIVE','SOLD','NOTSOLD','PENDING','HALT'
	
	public ItemBean() {
	}
	
	public ItemBean(UserBean seller, String title, Category category,
			String description, String picture, Currency currency,
			BigDecimal reservePrice, BigDecimal bidIncrements,
			BigDecimal bestBid, Timestamp bestBidTime, UserBean bestBidder,
			Timestamp endTime, ItemStatus itemStatus) {
		this.seller = seller;
		this.title = title;
		this.category = category;
		this.description = description;
		this.picture = picture;
		this.currency = currency;
		this.reservePrice = reservePrice;
		this.bidIncrements = bidIncrements;
		this.bestBid = bestBid;
		this.bestBidTime = bestBidTime;
		this.bestBidder = bestBidder;
		this.endTime = endTime;
		this.itemStatus = itemStatus;
	}
	
	public ItemBean(long itemId, UserBean seller, String title, Category category,
			String description, String picture, Currency currency,
			BigDecimal reservePrice, BigDecimal bidIncrements,
			BigDecimal bestBid, Timestamp bestBidTime, UserBean bestBidder,
			Timestamp endTime, ItemStatus itemStatus) {
		this.itemId = itemId;
		this.seller = seller;
		this.title = title;
		this.category = category;
		this.description = description;
		this.picture = picture;
		this.currency = currency;
		this.reservePrice = reservePrice;
		this.bidIncrements = bidIncrements;
		this.bestBid = bestBid;
		this.bestBidTime = bestBidTime;
		this.bestBidder = bestBidder;
		this.endTime = endTime;
		this.itemStatus = itemStatus;
//		this.wishlistUsers = wishlistUsers;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public UserBean getSeller() {
		return seller;
	}

	public void setSeller(UserBean seller) {
		this.seller = seller;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(BigDecimal reservePrice) {
		this.reservePrice = reservePrice;
	}

	public BigDecimal getBidIncrements() {
		return bidIncrements;
	}

	public void setBidIncrements(BigDecimal bidIncrements) {
		this.bidIncrements = bidIncrements;
	}

	public BigDecimal getBestBid() {
		return bestBid;
	}

	public void setBestBid(BigDecimal bestBid) {
		this.bestBid = bestBid;
	}

	public Timestamp getBestBidTime() {
		return bestBidTime;
	}

	public void setBestBidTime(Timestamp bestBidTime) {
		this.bestBidTime = bestBidTime;
	}

	public UserBean getBestBidder() {
		return bestBidder;
	}

	public void setBestBidder(UserBean bestBidder) {
		this.bestBidder = bestBidder;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public ItemStatus getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(ItemStatus itemStatus) {
		this.itemStatus = itemStatus;
	}
}