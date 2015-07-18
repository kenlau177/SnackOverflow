package com.google.gwt.beerbarossa.server;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.PersistenceManager;

import com.google.gwt.beerbarossa.client.LiquorService;
import com.google.gwt.beerbarossa.client.Liquors;
import com.google.gwt.beerbarossa.client.Pmf;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LiquorServiceImpl extends RemoteServiceServlet implements
		LiquorService {

	@Override
	public void addLiquor(String liquorName) {
		// TODO Auto-generated method stub
	}

	// this method will be private because we don't want anyone to accidentally
	// call this.
	// removes all the liquor from the google app-engine database
	private void rmAllLiquors() {
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		try {
			Query aNewQuery = pm.newQuery(Liquors.class);
			System.out.println("Removing all liquors");
			aNewQuery.deletePersistentAll();
		} catch (ClassCastException e) {
		}
			finally {
			pm.close();
		}
	}
	@Override
	public void addAllLiquors(List<Liquors> listOfLiquors) {
		// To save multiple objects in JDO, call the makePersistentAll(...)
		// method with a Collection of objects. This method will use a single
		// low-level batch save operation that is more efficient than a series its cached type number does not match the type number in the database. This exception always indicates that you have referenced a persistent object with stale cache inf
		// of individual makePersistent(...) invocations.
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		System.out.println("Making Liquors persistent");
		pm.makePersistentAll(listOfLiquors);
	}

	@Override
	public List<Liquors> runLiquorParser() {
		ProductParser parser = new ProductParser();
		List<Liquors> listOfLiquors = new ArrayList<Liquors>();
		try {
			listOfLiquors = parser.parseCsvUrl();
			System.out.println("Running LIQUORS parser...");
			// removes all the 'previous' objects stored in the app engine
			this.rmAllLiquors();
			// adds all the objects to the app engine
			this.addAllLiquors(listOfLiquors);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return listOfLiquors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Liquors> getItAll() {
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(Liquors.class);
			List<Liquors> temporaryListOfLiquors = (List<Liquors>) q.execute();
			List<Liquors> toBeReturned = new ArrayList<Liquors>();
			toBeReturned.addAll(temporaryListOfLiquors);
			return toBeReturned;
		} finally {
			pm.close();
		}
	}

	@Override
	public Liquors getLiquorsByClassName(String classname) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Liquors> getLiquorsBySubClassName(String subclassname) {
		// TODO Auto-generated method stub
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		Query q = pm.newQuery(Liquors.class);
		
		// this pretty much just takes the parameter for this function and adds on the word "Param"
		// the "subClassName" on the left is a PROPERTY of Liquors class
		q.setFilter("subClassName == subclassnameParam");
		q.declareParameters("String subclassnameParam");
		try {
			List<Liquors> temporaryListOfLiquors = (List<Liquors>) q.execute(subclassname);
			List<Liquors> toBeReturned = new ArrayList<Liquors>();
			toBeReturned.addAll(temporaryListOfLiquors);
			return toBeReturned;

		} finally {
			pm.close();
		}
	}

	@Override
	public Liquors getLiquorsByMinorClassName(String minorclassname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Liquors getLiquorsByAlcoholPercent(long percentage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLiquors(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public Liquors getLiquorsByAlcoholPercentRange(double lower, double upper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Liquors> getLiquorsByPriceRange(double lower, double upper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Liquors getLiquorsByLitresRange(double lower, double upper) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Liquors> getLiquorsByProductLongName(String productlongname){
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		Query q = pm.newQuery(Liquors.class);
		q.setFilter("productLongName == productlongnameParam");
		q.declareParameters("String productlongnameParam");
		try{
			List<Liquors> temporaryListOfLiquors = (List<Liquors>) q.execute(productlongname);
			List<Liquors> toBeReturned = new ArrayList<Liquors>();
			toBeReturned.addAll(temporaryListOfLiquors);	
			return toBeReturned;	
		} finally {
			pm.close();
		}	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Liquors> getLiquorsBySkuNo(String skuno){
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		Query q = pm.newQuery(Liquors.class);
		q.setFilter("skuNo == skunoParam");
		q.declareParameters("String skunoParam");
		try{
			List<Liquors> temporaryListOfLiquors = (List<Liquors>) q.execute(skuno);
			List<Liquors> toBeReturned = new ArrayList<Liquors>();
			toBeReturned.addAll(temporaryListOfLiquors);			
			return toBeReturned;	
		} finally {
			pm.close();
		}	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Liquors> getLiquorsByDropDown(String priceForQuery, String nameForQuery,
													String volumeForQuery, String contentForQuery){
		System.out.println("Here");
		
		PersistenceManager pm = Pmf.get().getPersistenceManager();
		Query q = pm.newQuery(Liquors.class);
		
		String[] priceSplit = parseDropDownText(priceForQuery);
		
		//String[] priceSplit = priceForQuery.split("-");
		if(nameForQuery.equals("all")){
			q.setFilter("price >= priceMinParam && " +
				"price <= priceMaxParam");
		} else {
			q.setFilter("price >= priceMinParam && " +
				"price <= priceMaxParam && className == classNameParam");
		}
		double pricemin = Double.parseDouble(priceSplit[0]);
		double pricemax = Double.parseDouble(priceSplit[1]);
		
		q.declareParameters("double priceMinParam, double priceMaxParam, "
				+ "String classNameParam");
		
		//add to this query
		try{
//			if(nameForQuery.equals("all")){
//				List<Liquors> temporaryListOfLiquors = 
//					(List<Liquors>) q.execute(pricemin, pricemax);
//			}
			List<Liquors> temporaryListOfLiquors =
					(List<Liquors>) q.execute(pricemin, pricemax, nameForQuery);
			List<Liquors> toBeReturned = new ArrayList<Liquors>();
			toBeReturned.addAll(temporaryListOfLiquors);
			toBeReturned = filterByLitres(toBeReturned, volumeForQuery);
			toBeReturned = filterByContent(toBeReturned, contentForQuery);
			return toBeReturned;
		
		} finally {
			pm.close();
		}
	
	}

	public List<Liquors> filterByLitres(List<Liquors> listOfLiquors, String volumeForQuery){
		String[] litresSplit = parseDropDownText(volumeForQuery); 
		double litresmin = Double.parseDouble(litresSplit[0]);
		double litresmax = Double.parseDouble(litresSplit[1]);
		
		List<Liquors> toBeReturned = new ArrayList<Liquors>();
		for (Liquors l: listOfLiquors){
			double litres = l.getLitres();
			if(litres>=litresmin && litres<=litresmax){
				toBeReturned.add(l);
			}
		}
		return toBeReturned;
	}
	
	public List<Liquors> filterByContent(List<Liquors> listOfLiquors, String contentForQuery){
		String[] contentSplit = parseDropDownText(contentForQuery); 
		double contentmin = Double.parseDouble(contentSplit[0]);
		double contentmax = Double.parseDouble(contentSplit[1]);
		
		List<Liquors> toBeReturned = new ArrayList<Liquors>();
		for (Liquors l: listOfLiquors){
			double content = l.getAlcoholContent();
			if(content>=contentmin && content<=contentmax){
				toBeReturned.add(l);
			}
		}
		return toBeReturned;
	}
	
	private String[] parseDropDownText(String text){
		String[] contentSplit = new String[2];
		String[] toBeReturned = new String[2];
				
		if(text.equals("all")){
			toBeReturned[0] = "0";
			toBeReturned[1] = "1000";			
		} else if(text.contains("+")){
			toBeReturned[0] = text.replaceAll("\\+","");
			toBeReturned[1] = "1000";
		} else {
			contentSplit = text.split("-");
			toBeReturned[0] = contentSplit[0];
			toBeReturned[1] = contentSplit[1];
		}
		return toBeReturned;
	}
	
}
