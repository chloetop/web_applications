package edu.unsw.comp9321.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.business.UserLoginFailedException;
import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateDao.DAOFactory;
import edu.unsw.comp9321.hibernateDao.UserDAO;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class NewItemCommand implements Command {

	
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	/** Creates a new instance of LoginCommand */
	public NewItemCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Object object = session.getAttribute("user");
		UserBean user = (UserBean) object;
		
		try {
			
			ItemBean item = new ItemBean();
			String title = request.getParameter("title");
			Category category = Category.valueOf(request.getParameter("category").toUpperCase());
			String description = request.getParameter("description");
			String picture = request.getParameter("picture");
			Currency currency = Currency.valueOf(request.getParameter("currency"));
			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.setParseBigDecimal(true);
			BigDecimal reservePrice = (BigDecimal) decimalFormat.parse(request.getParameter("reservePrice"));
			BigDecimal bidIncrements = (BigDecimal) decimalFormat.parse(request.getParameter("bidIncrements"));
			BigDecimal bestBid = (BigDecimal) decimalFormat.parse(request.getParameter("startingBid"));
			int endMinutes = Integer.parseInt(request.getParameter("endMinutes"));
			Timestamp endTime = new Timestamp((new Date(System.currentTimeMillis() + endMinutes * 60 * 1000)).getTime());
			
			// Validation
			boolean validation = validate(request);
			if (!validation)
				return "notadded.jsp";
			
			
			item.setSeller(user);
			item.setTitle(title);
			item.setCategory(category);
			item.setDescription(description);
			item.setPicture(picture);
			item.setCurrency(currency);
			item.setReservePrice(reservePrice);
			item.setBidIncrements(bidIncrements);
			item.setBestBid(bestBid);
			item.setBestBidTime(null);
			item.setBestBidder(null);
			item.setEndTime(endTime);
			item.setItemStatus(ItemStatus.LIVE);
			
			auctionDelegate.addItem(item);
			
			request.setAttribute("title", item.getTitle());
			return "added.jsp";
			
		} catch (Exception e) {
			e.printStackTrace();		
			return "error.jsp";		
		}
	}
	
	public boolean validate(HttpServletRequest request){
		
		boolean titleCheck = false;
		boolean reservePriceCheck = false;
		boolean bidIncrementsCheck = false;
		boolean startingBidCheck = false;
		boolean endMinutesCheck = false;
		
		String title = request.getParameter("title");
		System.out.println(title);
		if(title != null && !title.isEmpty()) {
			titleCheck = true;
			System.out.println("title check passed");
		}
		
		String reservePrice = request.getParameter("reservePrice");
		System.out.println(reservePrice);
		if(reservePrice != null && isFloat(reservePrice) && Float.parseFloat(reservePrice) >= 0 && Float.parseFloat(reservePrice) < 1000000 ) {
			reservePriceCheck = true;
			System.out.println("reservePrice check passed");
		}
		
		String bidIncrements = request.getParameter("bidIncrements");
		System.out.println(bidIncrements);
		if(bidIncrements != null && isFloat(bidIncrements) && Float.parseFloat(bidIncrements) > 0.009 && Float.parseFloat(bidIncrements) < 1000000 ) { 
			bidIncrementsCheck = true;
			System.out.println("bidIncrements check passed");
		}

		String startingBid = request.getParameter("startingBid");
		System.out.println(startingBid);
		if(startingBid != null && isFloat(startingBid) && Float.parseFloat(startingBid) >= 0 && Float.parseFloat(startingBid) < 1000000 ) {
			startingBidCheck = true;
			System.out.println("startingBid check passed");
		}
		
		String endMinutes = request.getParameter("endMinutes");
		System.out.println(endMinutes);
		if(endMinutes != null && isFloat(endMinutes) && Float.parseFloat(endMinutes) >= 3 && Float.parseFloat(endMinutes) <= 60) {
			endMinutesCheck = true;
			System.out.println("startingBid check passed");
		}
			
		if(titleCheck && reservePriceCheck && bidIncrementsCheck && startingBidCheck && endMinutesCheck)
			return true;
		else
			return false;		
	}
	
	public boolean isFloat(String input) {
		try {
			Float.parseFloat(input);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
}
