package edu.unsw.comp9321.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.business.ItemsFetchingFailedException;
import edu.unsw.comp9321.business.UserRegistrationFailedException;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.UserStatus;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;


public class ShowCommand implements Command{
	
	private static AuctionDelegate auctionDelegate;
	
	public ShowCommand(){
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		try{
			
			List<ItemBean> items = auctionDelegate.getItems();
			
			request.setAttribute("items", items);
			
			return "/home.jsp";
			
		}catch(ItemsFetchingFailedException exception){
			System.out.println("Unable to fetch items");
			return "test.jsp";
		}
	}

}
