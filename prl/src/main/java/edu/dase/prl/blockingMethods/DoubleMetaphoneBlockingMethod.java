package edu.dase.prl.blockingMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.language.DoubleMetaphone;
import edu.dase.prl.benchMark.records.BetterRecord;
import edu.dase.prl.encryption.DataEncryptor;

public class DoubleMetaphoneBlockingMethod {

	
	public static HashMap<String, ArrayList<BetterRecord>> metaphoneBlocking(String file) throws Exception{
		
		DoubleMetaphone doublemetaphone=new DoubleMetaphone();
		ArrayList<BetterRecord> records = new ArrayList<>();
		Scanner in = new Scanner(new File(file));
		while (in.hasNext()) {
			records.add(new BetterRecord(in.nextLine()));
		}
		in.close();

				HashMap<String, ArrayList<BetterRecord>> blockingTable = new HashMap<>();
				for (BetterRecord r: records) {
					String metaphoneVal = DataEncryptor.encryptString(doublemetaphone.encode(r.lastName),"password");
					if (blockingTable.containsKey(metaphoneVal)) {
						blockingTable.get(metaphoneVal).add(r);
					} else {
						ArrayList<BetterRecord> temp = new ArrayList<>();
						temp.add(r);
						blockingTable.put(metaphoneVal, temp);
					}
				}
			

				return blockingTable;
		}

}
