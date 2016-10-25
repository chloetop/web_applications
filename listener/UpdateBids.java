package edu.unsw.comp9321.listener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import edu.unsw.comp9321.Constant;
import edu.unsw.comp9321.EmailUtil;
import edu.unsw.comp9321.Util;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.ItemStatus;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class UpdateBids implements Runnable {
	private String host;
  private String port;
  private String username;
  private String password;
	
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	/** Creates a new instance of LoginCommand */
	public UpdateBids(String host, String port, String username, String password) {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
		
		// reads SMTP server setting from web.xml file
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	@Override
  public void run() {
		System.out.println("Generate sitemap ... " + new Date());
	  
		Timestamp currentTime = Util.formatTimestamp(new Timestamp(System.currentTimeMillis()), "yyyy-MM-dd HH:mm");
		List<ItemBean> soldItems = auctionDelegate.updateItemStatus(currentTime, ItemStatus.SOLD);
		List<ItemBean> pendingItems = auctionDelegate.updateItemStatus(currentTime, ItemStatus.PENDING);
		List<ItemBean> notSoldItems = auctionDelegate.updateItemStatus(currentTime, ItemStatus.NOTSOLD);
		
		for (ItemBean item: soldItems) {
			notifyBidder(item);
			notifySellerSold(item);
		}

		for (ItemBean item: pendingItems) {
			notifySellerPending(item);
		}
		
		for (ItemBean item: notSoldItems) {
			notifySellerNotSold(item);
		}
		
		for (MessageBean message : auctionDelegate.getMessagesNotSent()) {
			if (EmailUtil.sendEmail(host, port, username, password
					, message.getUser().getEmail()
					, message.getSubject()
					, message.getContent())) {
				auctionDelegate.updateMessage(message.getMessageId(), Constant.SUCCESS);
			} else {
				auctionDelegate.updateMessage(message.getMessageId(), Constant.FAIL);
			}
			
		}
		
  }

	void notifyBidder(ItemBean item) {
	  String itemTitle = item.getTitle();
	  UserBean bidder = item.getBestBidder();
	  
	  String subject = "You have won the bid of \"" + itemTitle + "\"!";
	  String content = "<p>Hello "+bidder.getUsername()+",</p><br>"
	  		+ "<p>Congratulations! You have won the bid of item: "+itemTitle+". Your item will be shipped soon.</p><br>"
	  		+ "<p>Best regards,<br>SquareRoot Team</p>";
	  
	  auctionDelegate.addMessage(new MessageBean(subject, content, bidder));
  }

	void notifySellerSold(ItemBean item) {
	  String itemTitle = item.getTitle();
	  UserBean seller = item.getSeller();
	  UserBean bidder = item.getBestBidder();

	  String subject = "Your item of \"" + itemTitle + "\" has been sold!";
	  String content = "<p>Hello "+seller.getUsername()+",</p><br>"
	  		+ "<p>Congratulations! Your order of item: "+itemTitle+" has been sold. Please ship it to the following address.</p><br>"
	  		+ "<p>Recipient: "+bidder.getFirstName()+" "+bidder.getLastName()+"<br>"
	  		+ "Street: "+bidder.getAddressStreet()+"<br>"
	  		+ "City: "+bidder.getAddressCity()+"<br>"
	  		+ "State: "+bidder.getAddressState()+"<br>"
	  		+ "Country: "+bidder.getAddressCountry()+"<br>"
	  		+ "Postcode: "+bidder.getAddressPostcode()+"</p><br>"
	  		+ "<p>Best regards,<br>SquareRoot Team</p>";
	  
	  auctionDelegate.addMessage(new MessageBean(subject, content, seller));
  }
	
	void notifySellerPending(ItemBean item) {
		String itemTitle = item.getTitle();
	  UserBean seller = item.getSeller();
	  
	  String subject = "The status of \"" + itemTitle + "\" has been changed to PENDING.";
	  String content = "<p>Hello "+seller.getUsername()+",</p><br>"
	  		+ "<p>The closing time of item: "+itemTitle+" is crossed and the winning bid is lower than the reserve price.<br>"
	  		+ "Please go to 'My Collections' to accept or reject this winning bid.</p><br>"
	  		+ "<p>Best regards,<br>SquareRoot Team</p>";
	  
	  auctionDelegate.addMessage(new MessageBean(subject, content, seller));
  }

	void notifySellerNotSold(ItemBean item) {
		String itemTitle = item.getTitle();
	  UserBean seller = item.getSeller();
	  
	  String subject = "Your item of \"" + itemTitle + "\" has not been sold.";
	  String content = "<p>Hello "+seller.getUsername()+",</p><br>"
	  		+ "<p>The closing time of item: "+itemTitle+" is crossed but no one bid this item.</p><br>"
	  		+ "<p>Best regards,<br>SquareRoot Team</p>";
	  
	  auctionDelegate.addMessage(new MessageBean(subject, content, seller));
  }

}
