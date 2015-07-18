package com.google.gwt.beerbarossa;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.beerbarossa.client.Liquors;
import com.google.gwt.beerbarossa.server.ProductParser;

public class LiquorsParserTest {

	@Test
	public void testNumberOfEntries() {
		try {
			ProductParser p = new ProductParser();
			List<Liquors> listOfLiquors = new ArrayList<Liquors>();
			listOfLiquors = p.parseCsvUrl();

			if (listOfLiquors.size() == 149) {
				assertTrue(true);
			} else {
				fail("either too many, or too little German liquors");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Bad file name, fool");
		}
	}

	@Test
	public void testParsedEntries() {
		try {
			ProductParser p = new ProductParser();
			List<Liquors> listOfLiquors = new ArrayList<Liquors>();
			listOfLiquors = p.parseCsvUrl();
			
			for (Liquors bottle : listOfLiquors) {
				
				if (bottle.getAlcoholContent() <= 0) {
					fail("Alcohol content cannot be smaller than 0");
				}
				if (bottle.getBottlesPerContainer() <= 0) {
					fail("Cannot be fewer than 0 bottles");
				}
				if (!bottle.getClassname().equals("SPIRITS") && !bottle.getClassname().equals("WINE") 
						&& !bottle.getClassname().equals("BEER") && !bottle.getClassname().equals("DE-ALCOHOLIZED BEER") ) {
					fail("Class Name cannot be anything else but these");
				}
				if (bottle.getLitres() <= 0) {
					fail("Litres cannot be smaller than 0");
				}
				if (!bottle.getMinorClassName().equals("APPLE") && !bottle.getMinorClassName().equals("HERBAL") &&
						!bottle.getMinorClassName().equals("SPARKLING WINE ROSE") && !bottle.getMinorClassName().equals("SPARKLING WINE WHITE")
						&& !bottle.getMinorClassName().equals("TABLE WINE RED") && !bottle.getMinorClassName().equals("TABLE WINE WHITE")
						&& !bottle.getMinorClassName().equals("TABLE WINE ROSE") && !bottle.getMinorClassName().equals("UNKNOWN") ) {
					fail("Minor Class Name cannot be anything else but these");
				}
				if (!bottle.getName().equals("LIQUOR") && !bottle.getName().equals("NON LIQUOR") ) {
					fail("the Name must be Liquor");
				}
				if (bottle.getPrice() <= 0) {
					fail("no such thing as free liquor");
				}
				if (!bottle.getProductCountryName().equals("GERMANY") ) {
					fail("liquor has to be from Germany");
				}
				if (bottle.getAlcoholContent() <= 0) {
					fail("liquor has to have some form of alcohol content");
				}
				if (bottle.getProductLongName().equals("") ) {
					fail("liquor has to have a long name");
				}
				if (!bottle.getSubClassname().equals("LIQUEURS") && !bottle.getSubClassname().equals("SPARKLING WINE") 
						&& !bottle.getSubClassname().equals("TABLE WINE") && !bottle.getSubClassname().equals("UNKNOWN") 
						&& !bottle.getSubClassname().equals("BRANDY") && !bottle.getSubClassname().equals("SPIRIT - GIFT PACKS") ) {
					fail("liquor subclass name is wrong");
						}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Bad file name, fool");
		}
	}
}
