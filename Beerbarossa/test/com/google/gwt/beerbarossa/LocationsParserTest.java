package com.google.gwt.beerbarossa;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.beerbarossa.client.Locations;
import com.google.gwt.beerbarossa.server.LocationsParser;

public class LocationsParserTest {
	
	@Test
	public void testNumberOfEntries() {
		//can't be completed yet until the .csv is fixed.
	}
	
	@Test
	public void testParsedEntries() {
		try {
			LocationsParser l = new LocationsParser();
			List<Locations> listOfLocations = new ArrayList<Locations>();
			listOfLocations = l.parseCsvUrl();
			
			for (Locations loc : listOfLocations) {
				
				if (loc.getAddress().equals("")) {
					fail("Address space must be present");
				}
				if (loc.getCity().equals("")) {
					fail("City space must be present");
				}
				if (loc.getFax().equals("")) {
					fail("Fax space must be present");
				}
				if (loc.getPhone().equals("")) {
					fail("phone space must be present");
				}
				if (loc.getName().equals("")) {
					fail("name space must be present");
				}
				if (loc.getPostalCode().equals("")) {
					fail("postal code must be present");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Bad file name, foo");
		}
	}

}
