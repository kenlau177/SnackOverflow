package com.google.gwt.beerbarossa.server;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.PersistenceManager;

import com.google.gwt.beerbarossa.client.LocationService;
import com.google.gwt.beerbarossa.client.Locations;
import com.google.gwt.beerbarossa.client.Pmf;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LocationServiceImpl extends RemoteServiceServlet implements LocationService {
	
	private void rmAllLocations() {
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		try {
			Query aNewQuery = pm.newQuery(Locations.class);
			System.out.println("Removing all locations");
			aNewQuery.deletePersistentAll();
		} finally {
			pm.close();
		}
	}
	@Override
	public void addAllLocations(List<Locations> listOfLocations) {
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		System.out.println("Making Locations persistent");
		try {
			pm.makePersistentAll(listOfLocations);
		} finally {
			pm.close();
		}
	}

	@Override
	public List<Locations> runLocationParser() {
		LocationsParser parser = new LocationsParser();
		List<Locations> listOfLocations = new ArrayList<Locations>();
		try {
			listOfLocations = parser.parseCsvUrl();
			System.out.println("Running LOCATIONS parser...");
			this.rmAllLocations();
			this.addAllLocations(listOfLocations);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return listOfLocations;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Locations> getItAll() {
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(Locations.class);
			List<Locations> temporaryListOfLocations = (List<Locations>) q.execute();
			List<Locations> toBeReturned = new ArrayList<Locations>();
			toBeReturned.addAll(temporaryListOfLocations);
			return toBeReturned;
		} finally {
			pm.close();
		}
	}

}
