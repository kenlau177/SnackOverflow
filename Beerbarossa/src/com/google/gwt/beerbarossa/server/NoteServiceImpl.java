package com.google.gwt.beerbarossa.server;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.beerbarossa.client.NotLoggedInException;
import com.google.gwt.beerbarossa.client.NoteService;
import com.google.gwt.beerbarossa.client.Pmf;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class NoteServiceImpl extends RemoteServiceServlet implements NoteService {
	
	  private static final Logger LOG = Logger.getLogger(NoteServiceImpl.class.getName());
	  private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@SuppressWarnings("unchecked")
	public String[] getNotes() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> content = new ArrayList<String>();
		try {
			Query q = pm.newQuery(Note.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
		    q.setOrdering("createDate");
			List<Note> notes = (List<Note>) q.execute(getUser());
			for (Note note : notes) {
				content.add(note.getContent());
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String theDate = formatter.format(note.getDate());
				content.add(theDate);
				String theId = Long.toString(note.getId());
				content.add(theId);
			}
		} finally {
			pm.close();
		}
		return (String[]) content.toArray(new String[0]);
	}
	
	public void addNote(String content) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		try {
			pm.makePersistent(new Note(getUser(), content));
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void removeNote(long id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(Note.class, "user == u");
		    q.declareParameters("com.google.appengine.api.users.User u");
		    List<Note> notes = (List<Note>) q.execute(getUser());
		    for (Note note : notes) {
		    	if (id == note.getId()) {
		    		deleteCount++;
		    		pm.deletePersistent(note);
		    	}
		    }
		    if (deleteCount != 1) {
		    	LOG.log(Level.WARNING, "removeStock deleted "+deleteCount+" Notes");
		    }
		} finally {
			pm.close();
		}
	}
	
	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}
	
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
	  private PersistenceManager getPersistenceManager() {
		    return PMF.getPersistenceManager();
		  }

}
