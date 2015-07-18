package com.google.gwt.beerbarossa.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("liquors")
public interface LiquorService extends RemoteService {
	
	//get product by Class Name:
	public Liquors getLiquorsByClassName(String classname);
	
	//get product by Sub Class Name:
	public List<Liquors> getLiquorsBySubClassName(String subclassname);
	
	//get product by Minor Class Name:
	public Liquors getLiquorsByMinorClassName(String minorclassname);
	
	//get product by AlcoholPercent
	public Liquors getLiquorsByAlcoholPercent(long percentage);
	
	//get product by Alcohol Percent Range
	public Liquors getLiquorsByAlcoholPercentRange(double lower, double upper);
	
	//get product by Price Range
	public List<Liquors> getLiquorsByPriceRange(double lower, double upper);
	
	//get product by litres per container ranger
	public Liquors getLiquorsByLitresRange(double lower, double upper);
	
	
	//gets all the liquors currently stored in the app-engine and returns it
	//as a list of Liquor objects.
	public List<Liquors> getItAll();

	// adds all the liquors from the csv file and stores it in the app-engine
	public void addAllLiquors(List<Liquors> listOfLiquors);
	
	// Creates a parser object, runs the parser.  This calls addAllLiquors.
	public List<Liquors> runLiquorParser();

	// These methods I'm not sure we need...but I'll leave it here in case:
	
	//adds product by Name:
	public void addLiquor(String liquorName);
	
	public void removeLiquors(String name);
	
	public List<Liquors> getLiquorsByProductLongName(String productlongname);
	
	public List<Liquors> getLiquorsBySkuNo(String skuNo);
	
	public List<Liquors> getLiquorsByDropDown(String priceForQuery, String nameForQuery,
		String volumeForQuery, String contentForQuery);	
	
	
}
