package edu.unsw.comp9321.web;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class MyCollections implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	
	public MyCollections() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			
			if (user != null) {
				List<ItemBean> list = auctionDelegate.getMyCollections(user.getUsername());
				
				request.setAttribute("myCollections", list);

				return "/mycollections.jsp";
			}
			
			return "login.jsp";
		} catch (Exception e) {
			e.printStackTrace();		
			return "error.jsp";		
		}
	}
	
}