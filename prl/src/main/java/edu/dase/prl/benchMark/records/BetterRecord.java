package edu.dase.prl.benchMark.records;



public class BetterRecord {
	
	public String firstName;
	public String lastName;
	public String encryptedFirstName;
	public String encryptedLastName;
	public String soundexFirst;
	public String soundexLast;
	public String encryptedSoundexFirst;
	public String encryptedSoundexLast;
	
	public String encryptedQGramsFirst;
	public String encryptedQGramsLast;
	

	
	
	// other fields here
	
	public BetterRecord(String row) {
		String[] fields = row.split(",");
		firstName = fields[0];
		lastName = fields[1];
		encryptedFirstName = fields[2];
		encryptedLastName = fields[3];
		soundexFirst = fields[4];
		soundexLast = fields[5];
		encryptedSoundexFirst = fields[6];
		encryptedSoundexLast = fields[7];
		encryptedQGramsFirst=fields[14];
		encryptedQGramsLast=fields[16];


	
	}

}
