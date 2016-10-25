package edu.unsw.comp9321.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.DecimalFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.EmailUtil;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class BidCommand implements Command {
	
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	/** Creates a new instance of LoginCommand */

	public BidCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Object object = session.getAttribute("user");
		UserBean user = (UserBean) object;
		
		try {			
			long ID = Long.parseLong(request.getParameter("ID"));
			ItemBean item = auctionDelegate.findItem(ID);

			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.setParseBigDecimal(true);
			BigDecimal bid = (BigDecimal) decimalFormat.parse(request.getParameter("bid"));			
			BigDecimal previousBid = (BigDecimal) decimalFormat.parse(request.getParameter("previousBid"));		
			BigDecimal increment = item.getBidIncrements();
			Timestamp bidTime = new Timestamp(new Date(System.currentTimeMillis()).getTime());
			UserBean currentBidder = item.getBestBidder();
			
			// Check if new bid is a multiple of increment more than previous bid
			int bidInt = (bid.multiply(new BigDecimal(100))).intValue();
			int prevInt = (previousBid.multiply(new BigDecimal(100))).intValue();
			int incInt = (increment.multiply(new BigDecimal(100))).intValue();
			System.out.println("new: " + bidInt + " prev: " + prevInt + " inc: " + incInt);
			
			if ((bidInt - prevInt - incInt) < 0) {
				return String.format("/invalidbid.jsp?title=%s&bid=%s&ccy=%s"
						, item.getTitle(), bid, item.getCurrency());
			}
						
			BigDecimal refreshedBid = auctionDelegate.newBid(ID, user, bid, previousBid, bidTime);
			
			String dollar = null;
			if(item.getCurrency() == Currency.GBP) {
				dollar = "£";
			} else if (item.getCurrency() == Currency.EUR) {
				dollar = "€";
			} else {
				dollar = "$";
			}

			if (refreshedBid == bid) {
			    String itemTitle = item.getTitle();
				String recipient = user.getEmail();
				String subject = "New bid placed for \"" + itemTitle + "\"!";
				String content = "<p>Hello "+user.getUsername()
						+ ",</p><br><p>Your bid of "+item.getCurrency()+" "+dollar+bid+" for item: "+itemTitle+" has been accepted.</p><br>"
						+ "<p>Best regards,<br>SquareRoot Team</p>";
				auctionDelegate.addMessage(new MessageBean(subject, content, user));
				
				if (currentBidder != null) {
					recipient = currentBidder.getEmail();
					if (recipient != null) {
						subject = "An increased bid has been placed for \"" + itemTitle + "\"!";
						content = "<p>Hello "+currentBidder.getUsername()+",</p><br>"
								+ "<p>A new bid of "+item.getCurrency()+" "+dollar+bid+" for item: "+itemTitle+" has been accepted.<br>"
								+ "Please visit our website if you wish to increase your bid.</p><br>"
								+ "<p>Best regards,<br>SquareRoot Team</p>";
						auctionDelegate.addMessage(new MessageBean(subject, content, currentBidder));
					}
				}
				return "bidaccepted.jsp";			
			}
			else if (refreshedBid.intValue() == -1) {
				return "auctionnotlive.jsp";
			} else {
				return String.format("/bidfailed.jsp?title=%s&yourBid=%s&ccy=%s&refreshedBid=%s"
						, item.getTitle(), bid, item.getCurrency(), refreshedBid);	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error.jsp";		
		}
	}	
	
	
}
