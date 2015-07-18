package com.google.gwt.beerbarossa.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NoteServiceAsync {
	public void getNotes(AsyncCallback<String[]> async);
	public void addNote(String symbol, AsyncCallback<Void> async);
	public void removeNote(long id, AsyncCallback<Void> async);

}
