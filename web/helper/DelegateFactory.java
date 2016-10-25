package edu.unsw.comp9321.web.helper;

import java.util.HashMap;

public class DelegateFactory {

	private HashMap delegates = new HashMap();

	private static DelegateFactory instance = new DelegateFactory();

	public static DelegateFactory getInstance() {
		return instance;
	}
		
	private DelegateFactory() {
		delegates.put("Auction", AuctionDelegateImpl.getInstance());		
	}

	public AuctionDelegate getAuctionDelegate() {
		return (AuctionDelegate) delegates.get("Auction");
	}
}
