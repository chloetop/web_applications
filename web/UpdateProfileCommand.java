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

import edu.unsw.comp9321.business.UserProfileUpdateFailedException;
import edu.unsw.comp9321.business.UserRegistrationFailedException;
import edu.unsw.comp9321.business.UserStatusUpdationFailedException;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;
import edu.unsw.comp9321.*;

public class UpdateProfileCommand implements Command{
	
	private static AuctionDelegate auctionDelegate;
	
	public UpdateProfileCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		try {
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;			
			try{
				date = format.parse(request.getParameter("dateOfBirth"));
			}catch(Exception exception){
				date = null;		
			}
			
			HttpSession session = request.getSession();
			UserBean currentUser = (UserBean)session.getAttribute("user");
			
			UserBean newUser = new UserBean(currentUser.getUsername(), request.getParameter("password"), request.getParameter("email"), 
					request.getParameter("nickname"), request.getParameter("firstName"), request.getParameter("lastName"), date, 
					request.getParameter("addressStreet"), request.getParameter("addressCity"), request.getParameter("addressState"), 
					request.getParameter("addressCountry"), request.getParameter("addressPostcode"), request.getParameter("creditCard"), 
					currentUser.getUserStatus(), currentUser.getIsAdmin());
			
			boolean update = auctionDelegate.updateUser(newUser);
			
			if(update){
				session.setAttribute("user", newUser);
				return "profile.jsp?state=updated";
			}
			
			return 	"profile.jsp?state=failed";
			
		} catch (UserProfileUpdateFailedException e) {
			System.out.println("user status updation failed");
			return "profile.jsp?state=failed";
		}
	}


}
