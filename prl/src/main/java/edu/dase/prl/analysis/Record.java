package edu.dase.prl.analysis;

import java.util.ArrayList;


public class Record {

	public int id;
	public String firstName;
	public String lastName;
	public ArrayList<String> encryptedFirstQGrams;
	public ArrayList<String> encryptedLastQGrams;
	public String encryptedFirstSoundex;
	public String encryptedLastSoundex;
	public String encryptedFirstMetaphone;
	public String encryptedLastMetaphone;
	public String encryptedFirstPrefix;
	public String encryptedLastPrefix;
	public String encryptedFirstSuffix;
	public String encryptedLastSuffix;
	public int firstNumQgrams;
	public int lastNumQgrams;
}
