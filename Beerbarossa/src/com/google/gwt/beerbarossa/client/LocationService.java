package com.google.gwt.beerbarossa.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("locations")
public interface LocationService extends RemoteService {
	
	// adds all the locations from the csv file and stores it in the app-engine
	public void addAllLocations(List<Locations> listOfLocations);
	
	// Creates a parser object, runs the parser.  This then calls addAllLocations()
	public List<Locations> runLocationParser();
	
	public List<Locations> getItAll();

}
