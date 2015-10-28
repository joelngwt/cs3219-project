import java.io.File;
import java.util.ArrayList;

public class GUIModel {
	private static Parser parser= new Parser();
	private static TextRetrieval textRetrieval=new TextRetrieval();
	private static Stemmer stemmer=new Stemmer();
	
	/**
     * 
     * @param file          PDF file to parse into a text file
     * @param fileNumber    Index number of file (depends on number of files opened)
     * 
     */
	public static File parsePDFFiles(File file, int fileNumber) {
		
		parser.parseFile(file, fileNumber);
		
		return new File(System.getProperty("user.dir")+"\\pdfoutput" + fileNumber + ".txt");
	}
	
	/**
     * 
     * Function is called when the user presses "start processing"
	 * 
	 * @param keywords 	String of keywords (need to split them)
     * 
     */
	public static void startProcessing(String fileName) {
		Category category=new Category();
		ArrayList<String> fileportions=category.SplitFile(fileName);
		String[] fileTerms=stemmer.processFile(fileName);
		textRetrieval.AddFile(fileName, fileTerms);
		textRetrieval.AddPortions(fileportions, fileName);
	}
	
	public static String[][] search(String[] keywords)
	{
		//String[] lines=keywords.split("\\r?\\n");
		
		return textRetrieval.getWeightedResults(keywords);
			
	}
	
	public static String getCVDetails(int index)
	{
		return textRetrieval.getCVDetails(System.getProperty("user.dir")+"\\pdfoutput" + index + ".txt");
	}
}
