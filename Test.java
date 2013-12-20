


public class Test{

	public static void main(String[] args){

		try{

			
			

			//InputStream input = new URL("http://www.prosper-stats.com/SearchResults.jsp?sn=PIONEER580&showBreakdown=t&csv=t").openStream();

	/*		BufferedReader in = new BufferedReader(new InputStreamReader(input));


			String inputLine;
			while ((inputLine = in.readLine()) != null)
			    System.out.println(inputLine);
			in.close();*/
			
			 CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true), ',', CSVWriter.NO_QUOTE_CHARACTER);
		     // feed in your array (or convert your data to an array)
		     String[] entries = "first#second#third".split("#");
		     writer.writeNext(entries);
			 writer.close();

		} catch (Exception e){
			e.printStackTrace();
		}

	}






}