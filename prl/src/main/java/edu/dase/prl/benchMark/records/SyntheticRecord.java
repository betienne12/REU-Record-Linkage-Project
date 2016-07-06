package edu.dase.prl.benchMark.records;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wcohen.ss.JaroWinkler;

import edu.dase.prl.stringComparsion.NormalizedLevenstein;

public class SyntheticRecord extends Record {

	public static final String[] RECORD_HEADER_UNENCRYPTED = 
		{"rec_id", "FirstName", "LastName", "CreditCardNumber"};
	public static final String[] RECORD_HEADER_ENCRYPTED = 
		{"rec_id", "FirstName", "LastName", "CreditCardNumber", 
		"FirstNameEncrypted", "LastNameEncrypted", 
		"FirstNameSoundex", "FirstNameSoundexEncrypted", "LastNameSoundex", "LastNameSoundexEncrypted", 
		"qGramFNSeparator", "qGramsFN", "qGramLNSeparator", "qGramsLN"};
	
	private String ccNumber;
	
	private String firstName;
	private String lastName;
	
	private String firstNameEncrypted;
	private String lastNameEncrypted;
	
	private String firstNameSoundex;
	private String lastNameSoundex;
	private String firstNameSoundexEncrypted;
	private String lastNameSoundexEncrypted;
	
	private List<String> qGramsFirstNameEncrypted;
	private List<String> qGramsLastNameEncrypted;
	private List<String> consolidatedQGrams;
	
	public SyntheticRecord(String id, String firstName, String lastName, String ccNumber) {
		
		super(massageID(id), false);
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.ccNumber = ccNumber;
	}
	
	public SyntheticRecord(String id, String firstName, String lastName, String ccNumber, 
			String firstNameEncrypted, String lastNameEncrypted, 
			String firstNameSoundex, String lastNameSoundex, String firstNameSoundexEncrypted, String lastNameSoundexEncrypted, 
			String qNumber, List<String> qGramsFirstNameEncrypted, List<String> qGramsLastNameEncrypted) {
		
		this(id, firstName, lastName, ccNumber);
		
		this.firstNameEncrypted = firstNameEncrypted;
		this.lastNameEncrypted = lastNameEncrypted;
		
		this.firstNameSoundex = firstNameSoundex;
		this.lastNameSoundex = lastNameSoundex;
		this.firstNameSoundexEncrypted = firstNameSoundexEncrypted;
		this.lastNameSoundexEncrypted = lastNameSoundexEncrypted;
		
		this.qGramsFirstNameEncrypted = qGramsFirstNameEncrypted;
		this.qGramsLastNameEncrypted = qGramsLastNameEncrypted;
		this.consolidatedQGrams = new ArrayList<String>(this.qGramsFirstNameEncrypted);
		this.consolidatedQGrams.addAll(this.qGramsLastNameEncrypted);
		
		this.isEncrypted = true;
	}
	
	private static final Pattern pattern = Pattern.compile("(?<=\\-)(.*?)(?=\\-)");
	private static String massageID(String id) {
		
		Matcher matcher = pattern.matcher(id);
		
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			System.err.println("Couldn't process the synthetic record ID: " + id);
			return id;
		}
	}
	
	public String[] getRecordHeader() {
	
		return isEncrypted ? RECORD_HEADER_ENCRYPTED : RECORD_HEADER_UNENCRYPTED;
	}
	
	public List<String> getQGramsFirstName() {
		
		return qGramsFirstNameEncrypted;
	}
	
	public List<String> getQGramsLastName() {
		
		return qGramsLastNameEncrypted;
	}
	
	public String getFirstName() {
		
		return this.firstName;
	}
	
	public String getLastName() {
		
		return this.lastName;
	}
	
	public boolean exactMatchUnecrypted(SyntheticRecord otherSyntheticRecord) {
		
		if (this.firstName.equals(otherSyntheticRecord.firstName) &&
				this.lastName.equals(otherSyntheticRecord.lastName)) {
			return true;
		}
		
		return false;
	}
	
	public boolean exactMatchEncrypted(SyntheticRecord otherSyntheticRecord) {
		
		if (this.firstNameEncrypted.equals(otherSyntheticRecord.firstNameEncrypted) &&
				this.lastNameEncrypted.equals(otherSyntheticRecord.lastNameEncrypted)) {
			return true;
		}
		
		return false;
	}
	
	JaroWinkler jaroWinkler = new JaroWinkler();
	public double getJaroWinklerScore(SyntheticRecord otherSyntheticRecord) {
		
		return jaroWinkler.score(this.firstName + " " + this.lastName, 
				otherSyntheticRecord.firstName + " " + otherSyntheticRecord.lastName);
	}
	
	NormalizedLevenstein normalizedLevenstein = new NormalizedLevenstein();
	public double getNormalizedLevensteinScore(SyntheticRecord otherSyntheticRecord) {
		
		return normalizedLevenstein.score(this.firstName + " " + this.lastName, 
				otherSyntheticRecord.firstName + " " + otherSyntheticRecord.lastName);
	}
	
	public boolean getEncryptedSoundexScore(SyntheticRecord otherSyntheticRecord) {
		
		if (this.firstNameSoundexEncrypted.equals(otherSyntheticRecord.firstNameSoundexEncrypted) &&
				this.lastNameSoundexEncrypted.equals(otherSyntheticRecord.lastNameSoundexEncrypted)) {
			return true;
		}
		return false;
	}
	
	public boolean getUnencryptedSoundexScore(SyntheticRecord otherSyntheticRecord) {
		
		if (this.firstNameSoundex.equals(otherSyntheticRecord.firstNameSoundex) &&
				this.lastNameSoundex.equals(otherSyntheticRecord.lastNameSoundex)) {
			return true;
		}
		return false;
	}
	
	public double getJaccardQGramScore(SyntheticRecord otherSyntheticRecord) {
		
		double numCommon = 0.0;
		
		for (String qGram : this.consolidatedQGrams) {
			if (otherSyntheticRecord.consolidatedQGrams.contains(qGram)) {
				numCommon++;
			}
		}

		return numCommon / (this.consolidatedQGrams.size() + otherSyntheticRecord.consolidatedQGrams.size() - numCommon);
	}
	
	public double getDiceQGramScore(SyntheticRecord otherSyntheticRecord) {
		
		double numCommon = 0.0;
		
		for (String qGram : this.consolidatedQGrams) {
			if (otherSyntheticRecord.consolidatedQGrams.contains(qGram)) {
				numCommon++;
			}
		}
		
		return (2 * numCommon) / (this.consolidatedQGrams.size() + otherSyntheticRecord.consolidatedQGrams.size());
	}

}
