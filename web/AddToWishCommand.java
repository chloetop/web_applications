package edu.unsw.comp9321.web;


import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class AddToWishCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	

	public AddToWishCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			ItemBean item = auctionDelegate.findItem(Long.parseLong(request.getParameter("addWishID")));
			String username = request.getParameter("addWishByUser");
			auctionDelegate.addToWishlist(username, item);
			
			Set<ItemBean> list = auctionDelegate.getWishlist(username);
			request.getSession().setAttribute("wishlist", list);

			return "home.jsp";
		} catch (Exception e) {
			e.printStackTrace();		
			return "error.jsp";		
		}
	}
	
}
