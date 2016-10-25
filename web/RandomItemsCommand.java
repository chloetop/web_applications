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

/**
 * This is the command that is used for searching products.
 */
public class RandomItemsCommand implements Command {
	/**
	 * The helper class to delegate all function calls to
	 */
	private static AuctionDelegate auctionDelegate;

	/** Creates a new instance of SearchCommand */
	public RandomItemsCommand() {
		auctionDelegate = DelegateFactory.getInstance().getAuctionDelegate();
	}

	public String execute(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		List<ItemBean> items = auctionDelegate.getRandomItem();
		request.setAttribute("selectedItems", items);
		
		if(session.getAttribute("user") != null) {
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			Set<ItemBean> list = auctionDelegate.getWishlist(user.getUsername());
	
			request.getSession().setAttribute("wishlist", list);
		}
		
//		if(session.getAttribute("user") == null) {
//			return "/welcome.jsp";
//		} else {
			return "/home.jsp";
//		}
	}

}
