import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import au.com.bytecode.opencsv.*;

import java.util.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Main{

	public static void main(String[] args){

		//ending number to be changed to iterate from 0 to 115
		String url = "http://www.prosper-stats.com/TopLenders.jsp?page="+'0';

		try{

			Document doc = Jsoup.connect(url).get();

			//grabs the screen name of everything that appears on the screen
			Elements links = doc.select("td.l a[href]");

			for(Element link: links){

				String screenName = link.text();
				System.out.println(screenName);

				//for each of the screenName download csv file
				parseScreenName(screenName);
			}



		} catch (Exception e) {
			e.printStackTrace();
		}

		


	}

	public static void parseScreenName(String screenName){

		try {
			InputStream input = new URL("http://www.prosper-stats.com/SearchResults.jsp?sn="+screenName+"&showBreakdown=t&csv=t").openStream();

			CSVReader reader = new CSVReader(new InputStreamReader(input), ',', '"', 1);

			//CSVReader reader = new CSVReader(new FileReader("example_PINONEER580.csv"), ',','"', 1);
			
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {


					String listingNumber = nextLine[0];
					String prosperRating = nextLine[1];
					String term = nextLine[2];
					String amountBorrowed = nextLine[3];
					String originationDate = nextLine[4];
					String borrowerState = nextLine[5];

					System.out.println(originationDate);

				    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
			        
			        Date orginDate = formatter.parse(originationDate);

			        Calendar dayBefore = new GregorianCalendar();
			        Calendar dayAfter = new GregorianCalendar();
			        
			        dayBefore.setTime(orginDate);
			        dayAfter.setTime(orginDate);

			        dayBefore.add(Calendar.DAY_OF_MONTH, -1);
			        dayAfter.add(Calendar.DAY_OF_MONTH, 1);

			        formatter = new SimpleDateFormat("yyyy-MM-dd");
			        String dateMin = formatter.format(dayBefore.getTime());
			        String dateMax = formatter.format(dayAfter.getTime());

			        String url = "http://www.prosper-stats.com/SearchResults.jsp?showBreakdown=t&dateMin="+dateMin+
			        				"&dateMax="+dateMax+
			        				"&sn="+screenName+
			        				"&1="+prosperRating+
			        				"&2="+term+
			        				"&3M="+amountBorrowed+
			        				"&3X="+amountBorrowed+
			        				"&5="+borrowerState;


			        //System.out.println(url);


			        String amountInvested = getAmountInvested(url);
			       

					
					String[] entries = new String[3];
					//Values to be written
					entries[0] = screenName;
					entries[1] = listingNumber;
					entries[2] = amountInvested;

					writeToFile(entries);

			    }
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public static String getAmountInvested(String url){

		String amountInvested = "";

		try {
			 Document doc = Jsoup.connect(url).get();

			//grabs the amount invested
			Element link = doc.select("td.f + td + td + td").first();

			amountInvested = link.text();

			amountInvested = amountInvested.replace(",","");

		} catch (Exception e){
			e.printStackTrace();
		}

		return amountInvested;

	}



	public static void writeToFile(String[] entries){

		try {
			CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true), ',', CSVWriter.NO_QUOTE_CHARACTER);

			writer.writeNext(entries);
			writer.close();	
		} catch (Exception e){
			e.printStackTrace();
		}
		

	}




}