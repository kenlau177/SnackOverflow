package com.google.gwt.beerbarossa.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.beerbarossa.client.Locations;

public class LocationsParser {

	@SuppressWarnings("null")
	public List<Locations> parseCsvUrl() throws FileNotFoundException {

		BufferedReader br = null;
		String line = "";
		String separator = ",";
		List<Locations> listOfLocations = new ArrayList<Locations>();

		try {
			URL link = new URL("http://www.ugrad.cs.ubc.ca/~s2o7/BC_Liquor_Store_Locations.csv");
			br = new BufferedReader(new InputStreamReader(link.openStream()));
			line = br.readLine();
			while ((line = br.readLine()) != null ) {
				//System.out.println(line);
				listOfLocations.add(new Locations(line.split(separator)));
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Location CSV url not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br == null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return listOfLocations;
	}
}
