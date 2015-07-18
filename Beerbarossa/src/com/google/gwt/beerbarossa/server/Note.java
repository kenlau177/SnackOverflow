package com.google.gwt.beerbarossa.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Note {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private User user;
	
	@Persistent
	private String content;
	
	@Persistent
	private Date createDate;
	
	//IT'S MIGHTY MORPHIN' CONSTRUCTOR TIME
	public Note() {
		this.createDate = new Date();
	}
	
	public Note(User user, String content) {
		this();
		this.user = user;
		this.content = content;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public Date getDate() {
		return this.createDate;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

}
