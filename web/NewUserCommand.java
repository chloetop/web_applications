package edu.unsw.comp9321.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.business.UserRegistrationFailedException;
import edu.unsw.comp9321.hibernateBeans.MessageBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;
import edu.unsw.comp9321.*;

public class NewUserCommand implements Command{
	
	private static AuctionDelegate auctionDelegate;
	
	public NewUserCommand(){
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;			
			try{
				date = format.parse(request.getParameter("dateOfBirth"));
			}catch(Exception exception){
				date = null;		
			}
			HttpSession session = request.getSession();
			session.removeAttribute("registerFailed");
			
			String emailAddress = request.getParameter("email");
			String name = request.getParameter("username");

			UserBean user = new UserBean(name,request.getParameter("password"), 
					emailAddress, request.getParameter("nickname"), request.getParameter("firstName"), 
					request.getParameter("lastName"), date , request.getParameter("addressStreet"), request.getParameter("addressCity"),
					request.getParameter("addressState"), request.getParameter("addressCountry"), request.getParameter("addressPostcode"), 
					request.getParameter("creditCard"), UserStatus.PENDACK, false);
			
			boolean register = auctionDelegate.register(user); 
			
			if (!register)
				return "register.jsp";

			saveMessage(user);
				
			return "registerSuccess.jsp?emailAdd="+request.getParameter("email")+"";
			
		}catch(UserRegistrationFailedException exception){
			System.out.println("registration failed");

			return "register.jsp?failed=true";
		}
	}
	
	public void saveMessage(UserBean user){
		String subject = "Account Confirmation from Square Root";
		String url = "http://localhost:8080/Assign2/dispatcher?operation=confirmUser&username="+user.getUsername();
		
		String htmlString = "<p>Dear ,"+user.getUsername()+"</p><br><p>Please click on the following "
				+ "link to activate your account:</p><br>"+"<p><a href=\""+url+"\">"+url+"</a><p><br>"
						+ "<p>Best regards,<br>SquareRoot Team</p>";
				
		auctionDelegate.addMessage(new MessageBean(subject, htmlString, user));
	}

}
