package edu.dase.prl.ExcelFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.language.Soundex;

import edu.dase.prl.encryption.DataEncryptor;
import edu.dase.prl.encryption.DataEncryptorException;
import edu.dase.prl.stringComparsion.QGram;

public class ExcelReader {

	
	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, DataEncryptorException, Exception {

		String password = "password";
		Soundex soundex = new Soundex();
		int qNumber=2;
		
		PrintWriter pw = new PrintWriter("/C:/Users/Brian/Desktop/REU/new_encrypt_data.csv");
		//PrintWriter pw = new PrintWriter("/C:/Users/Brian/Desktop/REU/Pawels_encryptednew_data.csv");
		 Scanner scanner = new Scanner(new File("/C:/Users/Brian/Desktop/REU/test_data.csv"));
		//Scanner scanner = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Pawels_synthetic_records_org.csv"));
		

		 scanner.nextLine(); // throw out column headers
	        scanner.useDelimiter(",");
	        while(scanner.hasNext()){
	        	
	        	String line = scanner.nextLine();
	        	System.out.println(line);
	        	
	        	String[] tokens = line.split(",");
	        	
	        	String firstName = tokens[0];
	        	String lastName = tokens[1];
	        	
	        	System.out.println("First: " + firstName);
	        	System.out.println("Last: " + lastName);
	        	
	        	String encryptedFirst = DataEncryptor.encryptString(firstName, password);
	        	System.out.println("Encrypted first: " + encryptedFirst);
	        	
	        	String encryptedLast=DataEncryptor.encryptString(lastName, password);
	        	System.out.println("Encrypted last: " + encryptedLast);
	        	
	        	String soundexFirst = soundex.encode(firstName);
	        	System.out.println("Soundex first: " + soundexFirst);
	        	String soundexLast = soundex.encode(lastName);
 
	        	String encryptedSoundexFirst = DataEncryptor.encryptString(soundexFirst, password);
	        	String encryptedSoundexLast = DataEncryptor.encryptString(soundexLast,password);
	        	System.out.println("Encrypted soundex first: " + encryptedSoundexFirst);
	        	
	        	String[] qGramsFirst = QGram.convertToQgram(firstName, qNumber);
	        	String[] qGramsLast = QGram.convertToQgram(lastName, qNumber);
	        	String QGramsFN= String.join("  ", qGramsFirst);
	        	String QGramsLN= String.join("  ", qGramsLast);


	        	System.out.println("Q-grams first: " + Arrays.toString(qGramsFirst));
	        	
	        	String[] encryptedQGramsFirst = DataEncryptor.encryptStringArray(qGramsFirst, password);
	        	String[] encryptedQGramsLast = DataEncryptor.encryptStringArray(qGramsLast, password);
	        	
	        	String encryptedQGramsFN= String.join("  ", encryptedQGramsFirst);
	        	String encryptedQGramsLN= String.join("  ", encryptedQGramsLast);
	        	System.out.println("test: " + encryptedQGramsLN);
	        	System.out.println("Encrypted q-grams first: " + Arrays.toString(encryptedQGramsFirst));
	        	
	        	pw.print(firstName + "," + lastName + "," + encryptedFirst + "," + encryptedLast+ "," + soundexFirst + "," + soundexLast + "," + encryptedSoundexFirst + "," + 
	        			encryptedSoundexLast + "," + Integer.toString(qNumber)+"," + "QGramFN,");
	        	
	        	pw.print(QGramsFN+ ",");

	        	/*for(String qGram:qGramsFirst){
	        		pw.print(qGram+ ",");
	        	}*/pw.print("qGramLN,");
	        	
	        	pw.print(QGramsLN+ ",");
	        	/*for(String qGram:qGramsLast){
	        		pw.print(qGram+ ",");
	        	}*/
	        	pw.print("encryptedQGramFirst,");
	        	/*for(String qGram: encryptedQGramsFirst){
	        		pw.print(qGram + ",");
	        	}*/
	        	pw.print(encryptedQGramsFN+ ",");
	        	
	        	pw.print("encryptedQGramLast,");
	        	/*for(String qGram: encryptedQGramsLast){
	        		pw.print(qGram + ",");
	        
	        	}*/
	        	pw.print(encryptedQGramsLN+ ",");
	        	pw.println();
	        	
	        }
	        scanner.close();
	        pw.close();
	    }
}


