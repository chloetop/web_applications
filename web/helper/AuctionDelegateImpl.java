package edu.unsw.comp9321.web.helper;

import edu.unsw.comp9321.business.AuctionService;
import edu.unsw.comp9321.business.support.AuctionServiceImpl;


/**
 * Implementation that instantiates an instance of auction service
 */
public class AuctionDelegateImpl extends AuctionDelegate {

	private static AuctionDelegateImpl instance = new AuctionDelegateImpl();
	
	private AuctionService service;
	
	private AuctionDelegateImpl() {
		service = new AuctionServiceImpl();
	}
	
	public static AuctionDelegate getInstance() {
		return instance;
	}

	protected AuctionService getService() {
		return service;
	}

}
