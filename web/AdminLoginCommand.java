package edu.unsw.comp9321.web;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.business.UserLoginFailedException;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

public class AdminLoginCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	

	public AdminLoginCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			HttpSession session = request.getSession();
			
			UserBean user = auctionDelegate.adminLogin(request.getParameter("username"), request.getParameter("password"));
			
			if (user == null) 
				return "adminLogin.jsp";
		
			session.setAttribute("user", user);
			

			return "randomItems";
		} catch (UserLoginFailedException e) {
			System.out.println("login failed");
			HttpSession session = request.getSession();
			session.removeAttribute("user");

			return "adminLogin.jsp?failed=true";
		}
	}
	
}