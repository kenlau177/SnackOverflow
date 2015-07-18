package com.google.gwt.beerbarossa.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Locations implements Serializable {
	@Persistent
	private int storenumber;
	@Persistent
	private String name;
	@Persistent
	private String address;
	@Persistent
	private String city;
	@Persistent
	private String postalcode;
	@Persistent
	private String phone;
	@Persistent
	private String fax;
	@Persistent
	private double lat;
	@Persistent
	private double lon;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	// Constructor
	public Locations() {
	}

	public Locations(String[] stringArray) {
		storenumber = Integer.parseInt(stringArray[0]);
		name = stringArray[1];
		address = stringArray[2];
		city = stringArray[3];
		postalcode = stringArray[4];
		phone = stringArray[5];
		fax = stringArray[6];
		lat = Double.parseDouble(stringArray[7]);
		lon = Double.parseDouble(stringArray[8]);
	}

	public void setStoreNumber(int storenumber) {
		this.storenumber = storenumber;
	}

	public void setStoreName(String storename) {
		this.name = storename;
	}

	public void setStoreAddress(String storeaddress) {
		this.address = storeaddress;
	}

	public void setStoreCity(String storecity) {
		this.city = storecity;
	}
	
	public void setPostalCode(String postalcode) {
		this.postalcode = postalcode;
	}

	public void setStorePhone(String phonenumber) {
		this.phone = phonenumber;
	}

	public void setStoreFax(String faxnumber) {
		this.fax = faxnumber;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}

	// Getters:
	public int getStorenumber() {
		return this.storenumber;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getCity() {
		return this.city;
	}
	
	public String getPostalCode() {
		return this.postalcode;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getFax() {
		return this.fax;
	}
	
	public double getLon() {
		return this.lon;
	}
	
	public double getLat() {
		return this.lat;
	}
	
	public Long getID() {
		return this.id;
	}

}
