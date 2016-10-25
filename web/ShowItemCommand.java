package edu.unsw.comp9321.web;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class ShowItemCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	
	public ShowItemCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			String itemID = request.getParameter("itemID");
			ItemBean item = auctionDelegate.findItem(Long.parseLong(itemID));
			
			request.setAttribute("itemInfo", item);
			
			return "/itemInfo.jsp";

		} catch (Exception e) {
			e.printStackTrace();		
			return "error.jsp";		
		}
	}
	
}
