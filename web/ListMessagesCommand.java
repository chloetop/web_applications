package edu.unsw.comp9321.web;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class ListMessagesCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	
	public ListMessagesCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			if (user != null) {
				List<MessageBean> messages = auctionDelegate.getMessagesByUsername(user.getUsername());

				request.setAttribute("messages", messages);

				return "/messages.jsp";
			}

			return "login.jsp";
		} catch (Exception e) {	
			return "error.jsp";		
		}
	}
	
}