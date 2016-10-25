package edu.unsw.comp9321.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.Util;
import edu.unsw.comp9321.business.UserLoginFailedException;
import edu.unsw.comp9321.hibernateBeans.Category;
import edu.unsw.comp9321.hibernateBeans.Currency;
import edu.unsw.comp9321.hibernateBeans.ItemBean;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.web.helper.AuctionDelegate;
import edu.unsw.comp9321.web.helper.DelegateFactory;

/**
 * This is the command that is used for searching products.
 */
public class SearchCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;

	/** Creates a new instance of SearchCommand */
	public SearchCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}

	public String execute(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		try {
			String keywords = request.getParameter("keywords");
			String matchMethod = request.getParameter("match");
			String categoryAsString = request.getParameter("category");
			Category category = null;
			if (categoryAsString != null && !categoryAsString.equalsIgnoreCase("All")) {
				category = Category.valueOf(categoryAsString.toUpperCase());
			}
		  String city = request.getParameter("city");
		  String state = request.getParameter("state");
		  String country = request.getParameter("country");
		  String postcode = request.getParameter("postalCode");
		  String currencyAsString = request.getParameter("currency");
		  
		  Currency currency = null;
			if (currencyAsString != null && !currencyAsString.equalsIgnoreCase("Select")) {
				currency = Currency.valueOf(currencyAsString.toUpperCase());
			}
		  BigDecimal priceMin = Util.parseToBigDecimal(request.getParameter("priceMin"));
		  BigDecimal priceMax = Util.parseToBigDecimal(request.getParameter("priceMax"));
		  
		  String timeOption = request.getParameter("timeOption");
		  String timeAsString = request.getParameter("timeInMinutes");
		  int timeInMinutes = 0;
			if (timeAsString != null && !timeAsString.equalsIgnoreCase("Select")) {
				timeInMinutes = Integer.parseInt(timeAsString);
			}
			
			List<ItemBean> items = auctionDelegate.searchItems(keywords, matchMethod,
			    category, city, state, country, postcode, currency, priceMin, priceMax,
			    timeOption, timeInMinutes);
	
			request.setAttribute("items", items);
	
			return "/result.jsp";

		} catch (Exception e) {
			e.printStackTrace();		
			return "error.jsp";		
		}
	}
}
