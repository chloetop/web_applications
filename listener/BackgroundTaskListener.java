package edu.unsw.comp9321.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BackgroundTaskListener implements ServletContextListener {

  private ScheduledExecutorService executor = null;
	
	@Override
  public void contextInitialized(ServletContextEvent sce) {
		executor = Executors.newSingleThreadScheduledExecutor();
		ServletContext context = sce.getServletContext();
		String intervalAsString = context.getInitParameter("updateBidInterval");
		int interval = Integer.parseInt(intervalAsString);
		 
    String host = context.getInitParameter("host");
    String port = context.getInitParameter("port");
    String username = context.getInitParameter("username");
    String password = context.getInitParameter("password");
		
		executor.scheduleAtFixedRate(new UpdateBids(host, port, username, password)
																	, 0, interval, TimeUnit.SECONDS);
  }

	@Override
  public void contextDestroyed(ServletContextEvent arg0) {
		executor.shutdownNow();
  }

}
