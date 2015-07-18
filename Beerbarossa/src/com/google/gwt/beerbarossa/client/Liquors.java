package com.google.gwt.beerbarossa.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Liquors implements Serializable {
	
	@Persistent
	private String name;
	@Persistent
	private String className;
	@Persistent
	private String subClassName;
	@Persistent
	private String minorClassName;
	@Persistent
	private String productCountryName;
	@Persistent
	private String skuNo;
	@Persistent
	private String productLongName;
	@Persistent
	private String UPC;
	@Persistent
	private double litres;
	@Persistent
	private int bottlesPerContainer;
	@Persistent
	private double alcoholContent;
	@Persistent
	private double price;
	@Persistent
	private String sweetnessCode;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    
    
    //Constructor
	public Liquors(){
	}
	
	public Liquors(String[] stringArray) {
		name = stringArray[0];
		className = stringArray[1];
		subClassName = stringArray[2];
		minorClassName = stringArray[3];
		productCountryName = stringArray[4];
		skuNo = stringArray[5];
		productLongName = stringArray[6];
		UPC = stringArray[7];
		litres = Double.parseDouble(stringArray[8]);
		bottlesPerContainer = Integer.parseInt(stringArray[9]);
		alcoholContent = Double.parseDouble(stringArray[10]);
		price = Double.parseDouble(stringArray[11]);
		//price = (double)Double.parseDouble(stringArray[11]);
		//price = stringArray[11];
		sweetnessCode = stringArray[12];
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setSubClassName(String subClassName) {
		this.subClassName = subClassName;
	}
	public void setMinorClassName(String minorClassName) {
		this.minorClassName = minorClassName;
	}
	public void setProductCountryName(String productCountryName) {
		this.productCountryName = productCountryName;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	public void setProductLongName(String productLongName) {
		this.productLongName = productLongName;
	}
	public void setUPC(String UPC) {
		this.UPC = UPC;
	}
	public void setLitres(double litres) {
		this.litres = litres;
	}
	public void setBottlesPerContainer(int bottlesPerContainer) {
		this.bottlesPerContainer = bottlesPerContainer;
	}
	public void setAlcoholContent(double alcoholContent) {
		this.alcoholContent = alcoholContent;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setSweetnessCode(String sweetNessCode) {
		this.sweetnessCode = sweetNessCode;
	}
	
	//Getters
	
	public String getName() { 
		if (this.name.equals("")) {
			return "UNKNOWN";
		} else return this.name; 
	}
	public String getClassname() {
		if (this.className.equals("")) {
			return "UNKNOWN";
		} else return this.className;
	}
	public String getSubClassname() {
		if (this.subClassName.equals("")) {
			return "UNKNOWN";
		} else return this.subClassName;
	}
	public String getMinorClassName() {
		if (this.minorClassName.equals("")) {
			return "UNKNOWN";
		} else return this.minorClassName;
	}
	public String getProductCountryName() {
		if (this.productCountryName.equals("")) {
			return "UNKNOWN";
		} else return this.productCountryName;
	}
	public String getSkuNo() {
		if (this.skuNo.equals("")) {
			return "UNKNOWN";
		} else return this.skuNo;
	}
	public String getProductLongName() {
		if (this.productLongName.equals("")) {
			return "UNKNOWN";
		} else return this.productLongName;
	}
	public String getUPC() {
		if (this.UPC.equals("")) {
			return "UNKNOWN";
		} else return this.UPC;
	}
	public double getLitres() {
		return this.litres;
	}
	public int getBottlesPerContainer() {
		return this.bottlesPerContainer;
	}
	public double getAlcoholContent() {
		return this.alcoholContent;
	}
	public double getPrice() {
		return this.price;
	}
	public String getSweetnessCode() {
		if (this.sweetnessCode.equals("") || this.sweetnessCode.equals("NA")) {
			return "UNKNOWN";
		} else return this.sweetnessCode;
	}
	public Long getID() {
		return this.id;
	}
}