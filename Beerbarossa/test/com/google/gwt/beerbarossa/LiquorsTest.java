package com.google.gwt.beerbarossa;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.beerbarossa.client.Liquors;


public class LiquorsTest {
	
	public Liquors abottle;
	public String[] stringArray = {"LIQUOR", "BEER", "", "", "GERMANY", "12345", "BECKS TALL CAN", "878787878", "0.5", "1", "5", "2.79", "NA"};

	
	@Test
	public void testName() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getName(), "LIQUOR");
	}
	@Test
	public void testClassName() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getClassname(), "BEER");
	}
	@Test
	public void testsubClassName() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getSubClassname(), "UNKNOWN");
	}
	@Test
	public void testMinorName() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getMinorClassName(), "UNKNOWN");
	}
	@Test
	public void testProductCountryName() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getProductCountryName(), "GERMANY");
	}
	@Test
	public void testSKUno() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getSkuNo(), "12345");
	}
	
	//"BECKS TALL CAN", "878787878", "0.5", "1", "5", "2.79", "NA", ""};
	@Test
	public void testProductLongName() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getProductLongName(), "BECKS TALL CAN");
	}
	@Test
	public void testUPC() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getUPC(), "878787878");
	}
	@Test
	public void testLitres() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getLitres(), 0.5, 0.1);
	}
	@Test
	public void testBottlesPerContainer() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getBottlesPerContainer(), 1);
	}
	@Test
	public void testAlcoholContent() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getAlcoholContent(), 5, 0.1);
	}
	@Test
	public void testPrice() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getPrice(), 2.79, 0.1);
	}
	@Test
	public void testSweetnessCode() {
		abottle = new Liquors(stringArray);
		assertEquals(abottle.getSweetnessCode(), "UNKNOWN");
	}
	

}
