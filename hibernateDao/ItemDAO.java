/**
 * 
 */
package edu.unsw.comp9321.hibernateDao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;

/**
 * @author Millyto
 *
 */
public interface ItemDAO {
	public List<ItemBean> findAllItems();
	public void addItem(ItemBean item);
	public BigDecimal bestBid(long ID);
	public boolean liveAuction(long ID);	
	public int updateBid(long ID, BigDecimal newBid, BigDecimal currentBid, Timestamp bidTime, String username);	
	public List<ItemBean> findItemsByCriteria(String keywords, String matchMethod, Category category
			, String city, String state, String country, String postcode, Currency currency
			, BigDecimal priceMin, BigDecimal priceMax, String timeOption, int timeInMinutes);
	public List<ItemBean> updateItemStatus(Timestamp currentTime, ItemStatus newStatus);
	public void updatePendingItemStatus(long ID, ItemStatus newStatus);
	public List<ItemBean> getTwelveRandomItems();
	public ItemBean findItem(long id);
	public List<ItemBean> getMyCollections(String username);
	public List<ItemBean> findLiveItems();
	public boolean haltAuction(long id);
	public List<ItemBean> findNotLiveItems();
	public boolean removeItem(long id);
	public List<ItemBean> findHaltItems();
	public boolean backLiveAuction(long id);
}
