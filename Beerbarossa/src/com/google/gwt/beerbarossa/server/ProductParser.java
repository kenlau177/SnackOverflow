package com.google.gwt.beerbarossa.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.beerbarossa.client.Liquors;

public class ProductParser {

	//public static void main(String[] args) {

	//	ProductParser obj = new ProductParser();
	//	obj.run();
	//}

	//public void run() {
	@SuppressWarnings("null")
	public List<Liquors> parseCsvUrl() throws FileNotFoundException {

		BufferedReader br = null;
		String line = "";
		String separator = ",";
		List<Liquors> listOfLiquors = new ArrayList<Liquors>();

		try {
			URL link = new URL("http://pub.data.gov.bc.ca/datasets/176284/BC_Liquor_Store_Product_Price_List.csv");
			URLConnection con = link.openConnection();
			con.setConnectTimeout(10000);
			br = new BufferedReader(new InputStreamReader(link.openStream()));
			while ( (line = br.readLine()) != null) {
				String[] liquorStats = line.split(separator);
				//System.out.println(line);

				//only add it to the product list if it came from Germany
				if (liquorStats[4].toString().equals("GERMANY")) {
					// Add the string array as my constructor handles it.
					//System.out.println(liquorStats[11]);
					listOfLiquors.add(new Liquors(line.split(separator)));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
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
		return listOfLiquors;
	}
}
