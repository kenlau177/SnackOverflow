package com.google.gwt.beerbarossa.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotLoggedInException extends Exception implements Serializable {

  public NotLoggedInException() {
    super();
  }

  public NotLoggedInException(String message) {
    super(message);
  }

}