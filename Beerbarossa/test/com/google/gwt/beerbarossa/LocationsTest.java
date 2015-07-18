package com.google.gwt.beerbarossa;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.beerbarossa.client.Locations;

public class LocationsTest {
	
	private String[] stringArray = {"2", "Stuff store", "666 Satan Clause Street", "Vancouver", "V9K C1C", "604-123-1234", "504-123-4321"};
	
	@Test
	public void testStoreName() {
		Locations l = new Locations(stringArray);
		assertEquals(l.getStorenumber(), 2);
	}
	@Test
	public void testStreet() {
		Locations l = new Locations(stringArray);
		assertEquals(l.getAddress(), "666 Satan Clause Street");
	}
	@Test
	public void testCity() {
		Locations l = new Locations(stringArray);
		assertEquals(l.getCity(), "Vancouver");
	}
	@Test
	public void testPostalCode() {
		Locations l = new Locations(stringArray);
		assertEquals(l.getPostalCode(), "V9K C1C");
	}
	@Test
	public void testPhone() {
		Locations l = new Locations(stringArray);
		assertEquals(l.getPhone(), "604-123-1234");
	}
	@Test
	public void testFax() {
		Locations l = new Locations(stringArray);
		assertEquals(l.getFax(), "504-123-4321");
	}
}
