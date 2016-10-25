package edu.unsw.comp9321.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.business.UserRegistrationFailedException;
import edu.unsw.comp9321.business.UserStatusUpdationFailedException;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;
import edu.unsw.comp9321.*;

public class ConfirmUserCommand implements Command{
	
private static AuctionDelegate auctionDelegate;
	
	public ConfirmUserCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		try {
			String name = request.getParameter("username");
		
			boolean result = auctionDelegate.updateUserStatus(name, UserStatus.LIVE);
			if (result) 
				return "login.jsp?name="+name+"";
			
			return "error.jsp";
		} catch (UserStatusUpdationFailedException e) {
			System.out.println("user status updation failed");
			return "error.jsp";
		}
	}
}
