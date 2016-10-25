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

/**
 * This is the command that is used for logging in users.
 * If logon is successful, the command places wishlist entries
 * in the request attributes.
 */
public class LoginCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;	
	/** Creates a new instance of LoginCommand */
	public LoginCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		try {
			HttpSession session = request.getSession();
			session.removeAttribute("loginFailed");
			
			UserBean user = auctionDelegate.login(
				request.getParameter("username"), request.getParameter("password"));
			
			if (user == null) 
				return "login.jsp";
		
			session.setAttribute("user", user);

			return "randomItems";
		} catch (UserLoginFailedException e) {
			System.out.println("login failed");
			HttpSession session = request.getSession();
			session.removeAttribute("user");

			return "login.jsp?failed=true";
		}
	}
	
}
