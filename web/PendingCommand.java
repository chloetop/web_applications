package edu.unsw.comp9321.web;


import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.EmailUtil;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class PendingCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	
	public PendingCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			long id = Long.parseLong(request.getParameter("itemID"));
			String action = request.getParameter("action");
			
			System.out.println("Values are: " + id + " " + action);
			

			if (action.equals("REJECT")) {
				auctionDelegate.updatePendingItemStatus(id, ItemStatus.NOTSOLD);

				
			} else {
				auctionDelegate.updatePendingItemStatus(id, ItemStatus.SOLD);
				ItemBean item = auctionDelegate.findItem(id);
				String itemTitle = item.getTitle();
				UserBean bidder = item.getBestBidder();
				String subject = "You have won the bid of \"" + itemTitle + "\"!";
				String content = "<p>Hello "+bidder.getUsername()+",</p><br>"
						+ "<p>Congratulations! You have won the bid of item: "+itemTitle+". Your item will be shipped soon.</p><br>"
					  	+ "<p>Best regards,<br>SquareRoot Team</p>";			  
				auctionDelegate.addMessage(new MessageBean(subject, content, bidder));
				
				UserBean seller = item.getSeller();
				subject = "Your item of \"" + itemTitle + "\" has been sold!";
				String content1 = "<p>Hello "+seller.getUsername()+",</p><br>"
				  		+ "<p>Congratulations! Your order of item: "+itemTitle+" has been sold. Please ship it to the following address.</p><br>"
				  		+ "<p>Recipient: "+bidder.getFirstName()+" "+bidder.getLastName()+"<br>"
				  		+ "Street: "+bidder.getAddressStreet()+"<br>"
				  		+ "City: "+bidder.getAddressCity()+"<br>"
				  		+ "State: "+bidder.getAddressState()+"<br>"
				  		+ "Country: "+bidder.getAddressCountry()+"<br>"
				  		+ "Postcode: "+bidder.getAddressPostcode()+"</p><br>"
				  		+ "<p>Best regards,<br>SquareRoot Team</p>";
				
				auctionDelegate.addMessage(new MessageBean(subject, content1, seller));

			}
						
			return "/forward3.jsp";

		} catch (Exception e) {
			e.printStackTrace();		
			return "error.jsp";		
		}
	}
	
}
