package com.google.gwt.beerbarossa.server;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.google.gwt.beerbarossa.client.TwitterInfo;
import com.google.gwt.beerbarossa.client.TwitterService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterServiceImpl extends RemoteServiceServlet implements
		TwitterService {

	private static final long serialVersionUID = -8841899724874097232L;

	private static final String CONSUMER_KEY = "MdSbedrXojaMaglAZ7Bl81R4K";
	private static final String CONSUMER_SECRET = "HFoXUfeApooxk13bJjzsydTCQDsTlMEBdI9QYoEXngzYaApwXo";
	
	@Override
	public TwitterInfo login() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET); 
		
		HttpSession session = super.getThreadLocalRequest().getSession();
		HttpServletRequest request = this.getThreadLocalRequest();
		String oauthVerifier = getOauthVerifier(request);
		
		User user = getUserLogin(twitter, session, oauthVerifier);
			
		if (user != null) {
			TwitterInfo result = new TwitterInfo();
			result.setUser( user.getName() );
			return result;
		} else {
			TwitterInfo result = requestToken(twitter, session);
			return result;
		}
	}

	private String getBaseUrl(HttpServletRequest request){
		String referer = request.getHeader("referer");
		//String[] refererParts = referer.split("&");
		String[] refererParts = referer.split("oauth_token");
		String out = refererParts[0];
		out = out.substring(0, out.length()-1);
		return out;
	}	
	
	private String getOauthVerifier(HttpServletRequest request){
		String referer = request.getHeader("referer");
		//String[] refererParts = referer.split("&");
		String[] refererParts = referer.split("oauth_verifier=");
		int len = refererParts.length;
		String out = "";
		if(len > 1){
			out = refererParts[1];
		}
		return out;
	}	

	//make sure to enable read, write, direct permission and 
	//check the allow applications on settings
 	protected User getUserLogin(Twitter twitter, HttpSession session, 
 			String oauthVerifier) {
	 // try to get credentials
	User user = null;
	 	 
	if(session.getAttribute("requestToken") != null && oauthVerifier.length() > 0){
	 
	 try {
		 RequestToken requestToken = (RequestToken) session.getAttribute("requestToken");
		 AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
		 session.setAttribute("accessToken", accessToken);
		 twitter.setOAuthAccessToken(accessToken);
		 user = twitter.verifyCredentials();
		} catch (TwitterException e) {
			if (e.getStatusCode() == 401) {
				Logger.getLogger( TwitterInfo.class.getName() ).info( e.getMessage() );   
			} else {
				Logger.getLogger( TwitterInfo.class.getName() ).severe( e.getMessage() );
			}
		 }
	 }
	 return user;
	}	

	private TwitterInfo requestToken( Twitter twitter, HttpSession session ) {   
		RequestToken requestToken;
		try {
			session.setAttribute("token", null);
			session.setAttribute("tokenSecret", null);
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			Logger.getLogger( TwitterInfo.class.getName() ).severe( e.getMessage() );
			return null;
		}
 
		session.setAttribute("requestToken", requestToken);
				
		TwitterInfo result = new TwitterInfo();
		result.setLoginURL( requestToken.getAuthorizationURL() );
		
		return result;
	} 	
 	
	@Override
	public void tweetNote(String symbol) {
		HttpSession session = super.getThreadLocalRequest().getSession();
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET); 
		AccessToken accessToken = (AccessToken) session.getAttribute("accessToken");
		twitter.setOAuthAccessToken(accessToken);
		try {
			Status status = twitter.updateStatus(symbol);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String logoutTwitter() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = super.getThreadLocalRequest().getSession();
		session.removeAttribute("requestToken");
		session.removeAttribute("accessToken");
		
		String baseUrl = getBaseUrl(request);
		return(baseUrl);
	}

}
