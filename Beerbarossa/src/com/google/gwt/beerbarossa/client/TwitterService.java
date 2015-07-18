package com.google.gwt.beerbarossa.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TwitterService extends RemoteService {

	public TwitterInfo login();
	
	public void tweetNote(String symbol);
	
	public String logoutTwitter();	
	
}
