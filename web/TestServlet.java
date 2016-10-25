package edu.unsw.comp9321.web;
import edu.unsw.comp9321.hibernateBeans.UserBean;
import edu.unsw.comp9321.hibernateDao.*;
import edu.unsw.comp9321.hibernateDao.support.UserDAOImpl;
import edu.unsw.comp9321.business.support.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class TestServlet extends HttpServlet {
protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		 throws ServletException, IOException {	
	 String userId = request.getParameter("userId");	
	 String password = request.getParameter("password");
	 UserDAOImpl loginService = new UserDAOImpl();
	 boolean result = loginService.authenticate(userId, password);
	 UserBean user = loginService.findUserByUsername(userId);
	 if(result == true){
		 request.getSession().setAttribute("user", user);		
		 response.sendRedirect("home.jsp");
	 }
	 else{
		 response.sendRedirect("login.jsp");
	 }
}
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
		 throws ServletException, IOException {
	 processRequest(request, response);
}
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
		 throws ServletException, IOException {
	 processRequest(request, response);
}
@Override
public String getServletInfo() {
	 return "Short description";
}
}