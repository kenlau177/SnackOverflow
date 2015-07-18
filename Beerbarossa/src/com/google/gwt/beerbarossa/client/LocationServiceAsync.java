package com.google.gwt.beerbarossa.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LocationServiceAsync {
	public void addAllLocations(List<Locations> listOfLocations, AsyncCallback<Void> async);
	public void runLocationParser(AsyncCallback<List<Locations>> async);
	public void getItAll(AsyncCallback<List<Locations>> async);
	
}
