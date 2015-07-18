package com.google.gwt.beerbarossa.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {

	void login(AsyncCallback<TwitterInfo> callback);

	void tweetNote(String symbol, AsyncCallback<Void> callback);

	void logoutTwitter(AsyncCallback<String> callback);	
	
}
