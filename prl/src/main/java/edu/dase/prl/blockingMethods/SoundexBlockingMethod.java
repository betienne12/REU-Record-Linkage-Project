package edu.dase.prl.blockingMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.language.Soundex;
import edu.dase.prl.encryption.DataEncryptor;
import edu.dase.prl.benchMark.records.BetterRecord;


public class SoundexBlockingMethod {
	
	public static HashMap<String, ArrayList<BetterRecord>> soundexblocking(String file) throws Exception{
	Soundex soundex = new Soundex();
	ArrayList<BetterRecord> records = new ArrayList<>();
	Scanner in = new Scanner(new File(file));
	while (in.hasNext()) {
		records.add(new BetterRecord(in.nextLine()));
	}
	in.close();

	// Creating the blocking index -- this one is based on Soundex value of the last name
			HashMap<String, ArrayList<BetterRecord>> blockingTable = new HashMap<>();
			for (BetterRecord r: records) {
				String soundexVal = DataEncryptor.encryptString(soundex.encode(r.lastName),"password");
				if (blockingTable.containsKey(soundexVal)) {
					blockingTable.get(soundexVal).add(r);
				} else {
					ArrayList<BetterRecord> temp = new ArrayList<>();
					temp.add(r);
					blockingTable.put(soundexVal, temp);
				}
			}
			return blockingTable;
	}

}
