package com.google.gwt.beerbarossa.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LiquorServiceAsync {
	public void addLiquor(String liquorName, AsyncCallback<Void> async);
	public void addAllLiquors(List<Liquors> listOfLiquors, AsyncCallback<Void> async);
	public void getLiquorsByClassName(String classname, AsyncCallback<Liquors> async);
	public void getLiquorsBySubClassName(String subclassname, AsyncCallback<List<Liquors>> async);
	public void getLiquorsByMinorClassName(String minorclassname, AsyncCallback<Liquors> async);
	public void getLiquorsByAlcoholPercent(long percentage, AsyncCallback<Liquors> async);
	public void getLiquorsByAlcoholPercentRange(double lower, double upper, AsyncCallback<Liquors> async);
	public void getLiquorsByPriceRange(double lower, double upper, AsyncCallback<List<Liquors>> async);
	public void getLiquorsByLitresRange(double lower, double upper, AsyncCallback<Liquors> async);
	public void removeLiquors(String name, AsyncCallback<Void> async);
	public void runLiquorParser(AsyncCallback<List<Liquors>> async);
	public void getItAll(AsyncCallback<List<Liquors>> async);
	
	public void getLiquorsByProductLongName(String productlongname,
			AsyncCallback<List<Liquors>> async);
	public void getLiquorsBySkuNo(String skuNo,
			AsyncCallback<List<Liquors>> async);

	public void getLiquorsByDropDown(String priceForQuery, String nameForQuery,
			String volumeForQuery, String contentForQuery,  
			AsyncCallback<List<Liquors>> async);
	
}
