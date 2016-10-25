package edu.unsw.comp9321.web;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Controller Servlet that accepts all client requests and performs the
 * lookup required to process the request. This class uses the command
 * design pattern to find the required <i>Command</i> class that will
 * process the request.
 * 
 */
@WebServlet(name = "dispatcher", urlPatterns = {"/dispatcher"})
public class ControllerServlet extends HttpServlet {
	
	private Map commands;
	
	/** Initialises the servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		commands = new HashMap();
		commands.put("login", new LoginCommand());
		commands.put("newitem", new NewItemCommand());
		commands.put("register", new NewUserCommand());
		commands.put("bid", new BidCommand());
		commands.put("search", new SearchCommand());	
		commands.put("home", new ShowCommand());
		commands.put("randomItems", new RandomItemsCommand());
		commands.put("addwish", new AddToWishCommand());
		commands.put("listwish", new ListWishCommand());
		commands.put("itemInfo", new ShowItemCommand());
		commands.put("deletewish", new DeleteFromWishCommand());
		commands.put("confirmUser", new ConfirmUserCommand());
		commands.put("updateProfile", new UpdateProfileCommand());
		commands.put("mycollections", new MyCollections());
		commands.put("adminlogin", new AdminLoginCommand());
		commands.put("liveitems", new FindLiveItemsCommand());
		commands.put("liveusers", new FindLiveUsersCommand());
		commands.put("notliveitems", new FindNotLiveItemsCommand());
		commands.put("halt", new HaltAuctionCommand());
		commands.put("ban", new BanUserCommand());
		commands.put("removeitem", new RemoveItemCommand());
		commands.put("listmessages", new ListMessagesCommand());
		commands.put("haltitems", new FindHaltItemsCommand());
		commands.put("live", new BackLiveAuctionCommand());
		commands.put("banusers", new FindBanUsersCommand());
		commands.put("freeuser", new SetLiveUserCommand());
		commands.put("pending", new PendingCommand());
	}

	
	/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		Command cmd = resolveCommand(request);
		String next = cmd.execute(request, response);
		
		if (next.indexOf('.') < 0) {
			cmd = (Command) commands.get(next);
			next = cmd.execute(request, response);
		}
		
		if (next.startsWith("/")) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(next);
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(next);
		}
	}
	
	private Command resolveCommand(HttpServletRequest request) {
		Command cmd = (Command) commands.get(request.getParameter("operation"));
		if (cmd == null) {
			cmd = (Command) commands.get("PAGE_NOT_FOUND");
		}
		return cmd;
	}
	
	/** Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/** Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/** Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "This servlet implements a command pattern for an auction application";
	}
	
}
