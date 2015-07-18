package com.google.gwt.beerbarossa.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("note")
public interface NoteService extends RemoteService {
	public String[] getNotes() throws NotLoggedInException;
	public void addNote(String content) throws NotLoggedInException;
	public void removeNote(long id) throws NotLoggedInException;
}
