import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Utilities for reading and writing files.
 * 
 * @author Allana evans
 * @version September 8, 2014
 */

public class OpenFile {
	
	public static Scanner openToRead(String fileName){
		Scanner input = null;
		try{
			input = new Scanner (new File(fileName));
		}catch (FileNotFoundException e){
			System.err.println("ERROR: Cannot open " + fileName + " for reading.");
			System.exit(1);
		}
		return input;
	}
	
	public static PrintWriter openToWrite (String fileName){
		PrintWriter output = null;
		try{
			output = new PrintWriter(new File(fileName));
		}catch(IOException e){
			System.err.println("ERROR: Cannot open " + fileName + " for writing.");
			System.exit(1);
		}
		return output;
	}
	
}
